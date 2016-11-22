package co.timecrypt.android

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import co.timecrypt.android.v2.api.TimecryptMessage
import kotlinx.android.synthetic.main.activity_message.*

/**
 * The activity responsible for creating Timecrypt messages.
 */
class MessageActivity : AppCompatActivity(), View.OnClickListener {

    // val TAG: String = MainActivity::class.java.simpleName
    //
    // val mRetrofit: Retrofit = Retrofit.Builder()
    //         .baseUrl("https://timecrypt-angrybyte.rhcloud.com/v2/")
    //         .addConverterFactory(MoshiConverterFactory.create())
    //         .build()
    // val mTimecryptApi: TimecryptRestApi = mRetrofit.create(TimecryptRestApi::class.java)

    private var message: TimecryptMessage? = null
    private var swipeAdapter: SwipeAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        message = TimecryptMessage("")
        swipeAdapter = SwipeAdapter(supportFragmentManager, message!!)
        viewPager.adapter = swipeAdapter
        viewPager.setPageTransformer(true, FadePageTransformer())

        buttonCreate.setOnClickListener(this)
        listOf(tabText, tabViews, tabDestructDate, tabDelivery).forEachIndexed {
            i, view ->
            view.setOnClickListener {
                viewPager.setCurrentItem(i, true)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        swipeAdapter?.cleanup()
    }

    override fun onClick(view: View) {
        when (view.id) {
            buttonCreate.id -> Toast.makeText(this, "Message is ${swipeAdapter?.message}", Toast.LENGTH_LONG).show()
        }
    }

}
