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

    interface CreateCompletedListener {
        fun onCreateCompleted(id: String)
        fun onCreateFailed(message: String)
    }

    val retrofit: Retrofit
    val api: TimecryptRestApi
    val requests: MutableList<Call<out TimecryptResponse>> = mutableListOf()

    init {
        retrofit = Retrofit.Builder()
                .baseUrl(serverUrl)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        api = retrofit.create(TimecryptRestApi::class.java)
    }

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

            override fun onResponse(call: Call<CreateResponse>?, response: Response<CreateResponse>) {
                if (!response.isSuccessful) {
                    listener.onCreateFailed(resolveErrorText(context, response.message()))
                    return;
                }

                val errorCode = response.body().statusCode
                if (errorCode == 0) {
                    listener.onCreateCompleted(response.body()?.id!!)
                } else {
                    listener.onCreateFailed(resolveErrorText(context, errorCode))
                }
            }

            override fun onFailure(call: Call<CreateResponse>?, t: Throwable?) {
                listener.onCreateFailed(resolveErrorText(t))
            }

        })
    }

    fun stopAll() {
        requests.forEach { if (!it.isCanceled) it.cancel() }
    }

}