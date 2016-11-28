package co.timecrypt.android.v2.api

import android.content.Context
import co.timecrypt.android.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * A Timecrypt API communication controller implementation, allows the user to create, unlock and read Timecrypt messages.
 */
class TimecryptController(serverUrl: String) {

    /*
     * TODO: We could assign a special IntentFilter to listener for custom domains
     *
     *                                ==  Java  ==
     *
     * IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
     * BroadcastReceiver receiver = new ScreenReceiver();
     * context.registerReceiver(receiver, filter);
     */
    companion object {
        val TIMECRYPT_URL = "https://github.com/milosmns/Timecrypt"
        val DEFAULT_API_URL = "https://timecrypt-angrybyte.rhcloud.com/v2/"
        val DEFAULT_MESSAGE_URL = "http://timecrypt.co/?c=%s"
    }

    interface CreateCompletedListener {
        fun onCreateCompleted(id: String)
        fun onCreateFailed(message: String)
    }

    interface LockCheckListener {
        fun onLockCheckCompleted(locked: Boolean)
        fun onLockCheckFailed(message: String)
    }

    interface ReadCompleteListener {
        fun onReadComplete(text: String, title: String?, destructDate: String, views: Int)
        fun onReadFailed(message: String)
    }

    private val retrofit: Retrofit
    private val api: TimecryptRestApi
    private val requests: MutableList<Call<out TimecryptResponse>> = mutableListOf()

    init {
        retrofit = Retrofit.Builder()
                .baseUrl(serverUrl)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        api = retrofit.create(TimecryptRestApi::class.java)
    }

    /* Helpers */

    private fun resolveErrorText(context: Context, code: Int): String {
        return Utils.parseStatusCode(context, code) ?: context.getString(R.string.error_unknown)
    }

    private fun resolveErrorText(t: Throwable?): String {
        return String.format("Network internal error: '%s'", t?.message ?: "unknown")
    }

    private fun resolveErrorText(context: Context, message: String?): String {
        return context.getString(R.string.error_server, message ?: "unknown")
    }

    fun create(context: Context, message: TimecryptMessage, listener: CreateCompletedListener) {
        val createCall = api.create(Utils.convertToQueryMap(message))
        requests.add(createCall)

        createCall.enqueue(object : Callback<CreateResponse> {
            override fun onResponse(call: Call<CreateResponse>, response: Response<CreateResponse>) {
                if (!response.isSuccessful) {
                    listener.onCreateFailed(resolveErrorText(context, response.message()))
                    return
                }

                val errorCode = response.body().statusCode
                if (errorCode == 0) {
                    listener.onCreateCompleted(response.body().id!!)
                } else {
                    listener.onCreateFailed(resolveErrorText(context, errorCode))
                }
            }

            override fun onFailure(call: Call<CreateResponse>, t: Throwable?) {
                listener.onCreateFailed(resolveErrorText(t))
            }
        })
    }

    /* Controller API */

    fun lockCheck(context: Context, id: String, listener: LockCheckListener) {
        val lockedCall = api.isLocked(id)
        requests.add(lockedCall)

        lockedCall.enqueue(object : Callback<IsLockedResponse> {
            override fun onResponse(call: Call<IsLockedResponse>, response: Response<IsLockedResponse>) {
                if (!response.isSuccessful) {
                    listener.onLockCheckFailed(resolveErrorText(context, response.message()))
                    return
                }

                val errorCode = response.body().statusCode
                if (errorCode == 0) {
                    listener.onLockCheckCompleted(response.body().isLocked!!)
                } else {
                    listener.onLockCheckFailed(resolveErrorText(context, errorCode))
                }
            }

            override fun onFailure(call: Call<IsLockedResponse>, t: Throwable?) {
                listener.onLockCheckFailed(resolveErrorText(t))
            }
        })
    }

    fun read(context: Context, id: String, password: String? = null, listener: ReadCompleteListener) {
        val readCall = api.read(id, password)
        requests.add(readCall)

        readCall.enqueue(object : Callback<ReadResponse> {
            override fun onResponse(call: Call<ReadResponse>, response: Response<ReadResponse>) {
                if (!response.isSuccessful) {
                    listener.onReadFailed(resolveErrorText(context, response.message()))
                }

                val errorCode = response.body().statusCode
                if (errorCode == 0) {
                    listener.onReadComplete(response.body().text!!, response.body().title, response.body().destructDate!!, response.body().views!!)
                } else {
                    listener.onReadFailed(resolveErrorText(context, errorCode))
                }
            }

            override fun onFailure(call: Call<ReadResponse>?, t: Throwable?) {
                throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    fun stopAll() {
        requests.forEach { if (!it.isCanceled) it.cancel() }
    }

}