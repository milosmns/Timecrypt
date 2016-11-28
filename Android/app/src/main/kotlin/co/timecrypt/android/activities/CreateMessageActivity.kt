package co.timecrypt.android.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import co.timecrypt.android.R
import co.timecrypt.android.helpers.OnMessageChangedListener
import co.timecrypt.android.helpers.PageChangeListenerAdapter
import co.timecrypt.android.helpers.TextWatcherAdapter
import co.timecrypt.android.pages.SwipeAdapter
import co.timecrypt.android.v2.api.TimecryptController
import co.timecrypt.android.v2.api.TimecryptMessage
import kotlinx.android.synthetic.main.activity_create_message.*


/**
 * The activity responsible for letting users create Timecrypt messages.
 */
class CreateMessageActivity : AppCompatActivity(), View.OnClickListener, OnMessageChangedListener {

    private val TAG = CreateMessageActivity::class.simpleName!!

    private var message: TimecryptMessage? = null
    private var swipeAdapter: SwipeAdapter? = null
    private var lastSelected: Int = 0
    private var tabs: List<ImageView> = emptyList()
    private var titles: List<Int> = emptyList()
    private var controller: TimecryptController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_message)

        controller = TimecryptController(TimecryptController.Companion.DEFAULT_API_URL)

        tabs = listOf(tabText, tabViews, tabDestructDate, tabDelivery)
        titles = listOf(R.string.title_edit_hint, R.string.title_views, R.string.title_destruct_date, R.string.title_delivery)

        message = createMessage(intent)
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

        listOf(titleLogo, buttonCreate, buttonCancel).forEach { it.setOnClickListener(this) }
        switchTabSelection(0)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        message = createMessage(intent)
        swipeAdapter?.cleanup()
        swipeAdapter?.notifyDataSetChanged()
    }

    private fun createMessage(intent: Intent?): TimecryptMessage {
        if (Intent.ACTION_SEND == intent?.action && intent?.type?.startsWith("text/", true) == true) {
            // this was a share action from outside
            return TimecryptMessage(intent?.getStringExtra(Intent.EXTRA_TEXT) ?: "")
        }
        return TimecryptMessage("")
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

    override fun onClick(view: View) {
        // log? "Message is ${swipeAdapter?.message}"
        when (view.id) {
            buttonCreate.id -> {
                progressOverlay.visibility = View.VISIBLE
                controller?.create(this, message!!, createOperationListener)
            }
            buttonCancel.id -> stopCreating()
            titleLogo.id -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(TimecryptController.Companion.TIMECRYPT_URL)))
        }
    }

    private val createOperationListener = object : TimecryptController.CreateCompletedListener {
        override fun onCreateCompleted(id: String) {
            progressOverlay.visibility = View.GONE
            // send info to the link viewer activity
            val messageInfo = Bundle(3)
            val fullUrl = String.format(TimecryptController.Companion.DEFAULT_MESSAGE_URL, id)
            messageInfo.putString(LinkDisplayActivity.KEY_URL, fullUrl)
            messageInfo.putString(LinkDisplayActivity.KEY_DATE, message!!.destructDate!!.toString())
            messageInfo.putInt(LinkDisplayActivity.KEY_VIEWS, message!!.views)
            val intent = Intent(this@CreateMessageActivity, LinkDisplayActivity::class.java)
            intent.putExtras(messageInfo)
            startActivity(intent)
            finish()
        }

        override fun onCreateFailed(message: String) {
            Log.e(TAG, message)
            Toast.makeText(this@CreateMessageActivity, R.string.message_not_created, Toast.LENGTH_LONG).show()
            progressOverlay.visibility = View.GONE
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

    override fun onStop() {
        super.onStop()
        stopCreating()
    }

    private fun stopCreating() {
        controller?.stopAll()
        progressOverlay.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        controller = null
        titleEdit.removeTextChangedListener(titleChangeListener)
        viewPager.removeOnPageChangeListener(pageChangeListener)
        swipeAdapter?.cleanup()
    }

}
