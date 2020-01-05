package com.appifyhub.timecrypt.v2.api

import android.content.Context
import com.appifyhub.timecrypt.R
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
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
        const val TIMECRYPT_URL = "https://github.com/milosmns/Timecrypt"
        const val DEFAULT_API_URL = "https://api.timecrypt.appifyhub.com/v2/"
        const val DEFAULT_MESSAGE_URL = "https://timecrypt.appifyhub.com/?c=%s"
    }

    /**
     * Used as a utility interface inside the [create] method to notify the listener of _created_ and _create failed_ remote events.
     */
    interface CreateCompletedListener {
        fun onCreateCompleted(id: String)
        fun onCreateFailed(message: String)
    }

    /**
     * Used as a utility interface inside the [lockCheck] method to notify the listener of _lock checked_ and _lock check failed_
     * remote events.
     */
    interface LockCheckListener {
        fun onLockCheckCompleted(locked: Boolean)
        fun onLockCheckFailed(message: String)
    }

    /**
     * Used as a utility interface inside the [read] method to notify the listener of _read successful_ and _read failed_ remote events.
     */
    interface ReadCompleteListener {
        fun onReadComplete(text: String, title: String?, destructDate: String, views: Int)
        fun onReadFailed(message: String)
    }

    private val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()!!
    private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(serverUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    private val api: TimecryptRestApi = retrofit.create(TimecryptRestApi::class.java)
    private val requests: MutableList<Call<out TimecryptResponse>> = mutableListOf()

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

    /* Business logic, Controller API */

    /**
     * Creates a new Timecrypt message using the remote API.
     *
     * @param context Which context to use to read error strings from resources
     * @param message Which message to create
     * @param listener A callback to call when this operation completes
     */
    fun create(context: Context, message: TimecryptMessage, listener: CreateCompletedListener) {
        val createCall = api.create(Utils.convertToQueryMap(message))
        requests.add(createCall)

        createCall.enqueue(object : Callback<CreateResponse> {
            override fun onResponse(call: Call<CreateResponse>, response: Response<CreateResponse>) {
                if (!response.isSuccessful) {
                    listener.onCreateFailed(resolveErrorText(context, response.message()))
                    return
                }

                val errorCode = response.body()?.statusCode ?: 400
                if (errorCode == 0) {
                    listener.onCreateCompleted(response.body()?.id!!)
                } else {
                    listener.onCreateFailed(resolveErrorText(context, errorCode))
                }
            }

            override fun onFailure(call: Call<CreateResponse>, t: Throwable?) {
                listener.onCreateFailed(resolveErrorText(t))
            }
        })
    }

    /**
     * Using the remote API, checks whether the given ID is associated with a Timecrypt message
     * and whether that message is locked using a password or not.
     *
     * @param context Which context to use to read error strings from resources
     * @param id The message identifier, unique
     * @param listener A callback to call when this operation completes
     */
    fun lockCheck(context: Context, id: String, listener: LockCheckListener) {
        val lockedCall = api.isLocked(id)
        requests.add(lockedCall)

        lockedCall.enqueue(object : Callback<IsLockedResponse> {
            override fun onResponse(call: Call<IsLockedResponse>, response: Response<IsLockedResponse>) {
                if (!response.isSuccessful) {
                    listener.onLockCheckFailed(resolveErrorText(context, response.message()))
                    return
                }

                val errorCode = response.body()?.statusCode ?: 400
                if (errorCode == 0) {
                    listener.onLockCheckCompleted(response.body()?.isLocked!!)
                } else {
                    listener.onLockCheckFailed(resolveErrorText(context, errorCode))
                }
            }

            override fun onFailure(call: Call<IsLockedResponse>, t: Throwable?) {
                listener.onLockCheckFailed(resolveErrorText(t))
            }
        })
    }

    /**
     * Tries tho fetch the Timecrypt message content for the given ID using the remote API. Using a wrong password will not
     * fail this operation, but will return garbled content of this message and reduce the remaining view count by one.
     *
     * @param context Which context to use to read error strings from resources
     * @param id The message identifier, unique
     * @param password A password to unlock the message, or `null` to unlock using the default password
     * @param listener A callback to call when this operation completes
     */
    fun read(context: Context, id: String, password: String? = null, listener: ReadCompleteListener) {
        val readCall = api.read(id, password)
        requests.add(readCall)

        readCall.enqueue(object : Callback<ReadResponse> {
            override fun onResponse(call: Call<ReadResponse>, response: Response<ReadResponse>) {
                if (!response.isSuccessful) {
                    listener.onReadFailed(resolveErrorText(context, response.message()))
                }

                val errorCode = response.body()?.statusCode ?: 400
                if (errorCode == 0) {
                    listener.onReadComplete(response.body()?.text!!, response.body()?.title, response.body()?.destructDate!!, response.body()?.views!!)
                } else {
                    listener.onReadFailed(resolveErrorText(context, errorCode))
                }
            }

            override fun onFailure(call: Call<ReadResponse>?, t: Throwable?) {
                throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    /**
     * Stops all active and pending operations that this controller is using.
     */
    fun stopAll() {
        requests.forEach { if (!it.isCanceled) it.cancel() }
    }

}