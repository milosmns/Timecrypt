package co.timecrypt.android.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import co.timecrypt.android.R
import co.timecrypt.android.helpers.OnMessageChangedListener
import co.timecrypt.android.helpers.PageChangeListenerAdapter
import co.timecrypt.android.helpers.TextWatcherAdapter
import co.timecrypt.android.pages.SwipeAdapter
import co.timecrypt.android.v2.api.TimecryptMessage
import kotlinx.android.synthetic.main.activity_message.*


/**
 * The activity responsible for creating Timecrypt messages.
 */
class MessageActivity : AppCompatActivity(), View.OnClickListener, OnMessageChangedListener {

    private companion object {
        val TIMECRYPT_URL = "https://github.com/milosmns/Timecrypt"
    }

    // val TAG: String = MainActivity::class.java.simpleName
    //
    // val mRetrofit: Retrofit = Retrofit.Builder()
    //         .baseUrl("https://timecrypt-angrybyte.rhcloud.com/v2/")
    //         .addConverterFactory(MoshiConverterFactory.create())
    //         .build()
    // val mTimecryptApi: TimecryptRestApi = mRetrofit.create(TimecryptRestApi::class.java)

    private var message: TimecryptMessage? = null
    private var swipeAdapter: SwipeAdapter? = null
    private var lastSelected: Int = 0
    private var tabs: List<ImageView> = emptyList()
    private var titles: List<Int> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        tabs = listOf(tabText, tabViews, tabDestructDate, tabDelivery)
        titles = listOf(R.string.title_edit_hint, R.string.title_views, R.string.title_destruct_date, R.string.title_delivery)

        message = TimecryptMessage("")
        swipeAdapter = SwipeAdapter(this, supportFragmentManager, message!!)

        viewPager.adapter = swipeAdapter
        viewPager.addOnPageChangeListener(pageChangeListener)
        viewPager.offscreenPageLimit = tabs.size - 1
        viewPager.swipeEnabled = false

        tabs.forEachIndexed {
            i, view ->
            view.setOnClickListener {
                viewPager.setCurrentItem(i, true)
            }
        }

        titleEdit.addTextChangedListener(titleChangeListener)
        titleLogo.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(TIMECRYPT_URL)))
        }

        buttonCreate.setOnClickListener(this)
        switchTabSelection(0)
    }

    private val pageChangeListener = object : PageChangeListenerAdapter() {
        override fun onPageScrollStateChanged(state: Int) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                if (viewPager.currentItem != lastSelected) {
                    // update title and hide keyboard
                    if (viewPager.currentItem == 0) {
                        titleEdit.isEnabled = true
                        titleEdit.setText(message?.title)
                        titleEdit.addTextChangedListener(titleChangeListener)
                    } else {
                        currentFocus?.let {
                            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            inputManager.hideSoftInputFromWindow(it.windowToken, 0)
                        }
                        titleEdit.removeTextChangedListener(titleChangeListener)
                        titleEdit.isEnabled = false
                        titleEdit.setText(titles[viewPager.currentItem])
                    }
                    // update tab selection
                    switchTabSelection(viewPager.currentItem)
                }
            }
        }
    }

    private val titleChangeListener = object : TextWatcherAdapter() {
        override fun afterTextChanged(text: Editable) {
            message?.title = text.toString()
        }
    }

    private fun switchTabSelection(current: Int) {
        tabs[lastSelected].setBackgroundResource(R.drawable.icon_background)
        tabs[current].setBackgroundResource(R.drawable.icon_background_active)
        tabs[lastSelected].isSelected = false
        tabs[current].isSelected = true
        lastSelected = current
    }

    override fun onTextInvalidated(empty: Boolean) {
        titleEdit.visibility = if (empty) View.GONE else View.VISIBLE
        buttonCreate.visibility = if (empty) View.GONE else View.VISIBLE
        tabsContainer.visibility = if (empty) View.GONE else View.VISIBLE
        viewPager.swipeEnabled = !empty
    }

    override fun onClick(view: View) {
        when (view.id) {
            buttonCreate.id -> Toast.makeText(this, "Message is ${swipeAdapter?.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        titleEdit.removeTextChangedListener(titleChangeListener)
        viewPager.removeOnPageChangeListener(pageChangeListener)
        swipeAdapter?.cleanup()
    }

}
