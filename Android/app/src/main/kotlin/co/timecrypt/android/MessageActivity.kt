package co.timecrypt.android

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_message.*
import java.util.*

class MessageActivity : AppCompatActivity(), View.OnClickListener {

    // val TAG: String = MainActivity::class.java.simpleName
    //
    // val mRetrofit: Retrofit = Retrofit.Builder()
    //         .baseUrl("https://timecrypt-angrybyte.rhcloud.com/v2/")
    //         .addConverterFactory(MoshiConverterFactory.create())
    //         .build()
    // val mTimecryptApi: TimecryptRestApi = mRetrofit.create(TimecryptRestApi::class.java)

    fun <T> Set<T>.random(): T {
        return elementAt(Random().nextInt(size))
    }

    fun View.toggleVisibility() {
        visibility = if (visibility == View.VISIBLE) View.GONE else View.VISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        setOf(titleLogo, tabText, tabViews, tabDestructDate, tabDelivery).forEach { it.setOnClickListener(this) }
    }

    override fun onClick(view: View) {
        // MM not real code, just testing :)
        when (view.id) {
            R.id.titleLogo -> titleEdit.toggleVisibility()
            R.id.tabText -> setOf(tabViews, tabDestructDate, tabDelivery).random().toggleVisibility()
            R.id.tabViews -> setOf(tabText, tabDestructDate, tabDelivery).random().toggleVisibility()
            R.id.tabDestructDate -> setOf(tabText, tabViews, tabDelivery).random().toggleVisibility()
            R.id.tabDelivery -> setOf(tabText, tabViews, tabDestructDate).random().toggleVisibility()
        }
    }

}
