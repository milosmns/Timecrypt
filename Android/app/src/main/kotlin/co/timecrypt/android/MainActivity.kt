package co.timecrypt.android

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import co.timecrypt.android.api.ApiObjectHelper
import co.timecrypt.android.api.CreateResponse
import co.timecrypt.android.api.TimecryptMessage
import co.timecrypt.android.api.TimecryptRestApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity(), Callback<CreateResponse> {

    val TAG = MainActivity::class.java.simpleName;

    val mRetrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://timecrypt-angrybyte.rhcloud.com/v2/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    val mTimecryptApi: TimecryptRestApi = mRetrofit.create(TimecryptRestApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        test()
    }

    fun test() {
        val message = TimecryptMessage("Just testing")
        val call = mTimecryptApi.create(ApiObjectHelper.convertToQueryMap(message))
        call.enqueue(this)
    }

    /* Callbacks */

    override fun onFailure(call: Call<CreateResponse>?, t: Throwable?) {
        Toast.makeText(this, "Failed to process create request: ${t.toString()}", Toast.LENGTH_LONG).show()
    }

    override fun onResponse(call: Call<CreateResponse>?, response: Response<CreateResponse>?) {
        if (response?.code() == 200) {
            val answer = response?.body()
            Toast.makeText(this, "Answer is: ID=${answer?.id}, StatusCode=${answer?.statusCode}", Toast.LENGTH_LONG).show()
            return
        }

        Toast.makeText(this, "Failed, request status: ${response?.code()}", Toast.LENGTH_LONG).show()
        Log.e(TAG, "Create response: ${response?.raw()?.toString()}")
    }

}
