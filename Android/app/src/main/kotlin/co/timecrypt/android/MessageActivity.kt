package co.timecrypt.android

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MessageActivity : AppCompatActivity() {

    // val TAG: String = MainActivity::class.java.simpleName
    //
    // val mRetrofit: Retrofit = Retrofit.Builder()
    //         .baseUrl("https://timecrypt-angrybyte.rhcloud.com/v2/")
    //         .addConverterFactory(MoshiConverterFactory.create())
    //         .build()
    // val mTimecryptApi: TimecryptRestApi = mRetrofit.create(TimecryptRestApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}
