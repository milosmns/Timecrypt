package com.appifyhub.timecrypt.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.text.util.LinkifyCompat
import android.support.v7.app.AppCompatActivity
import android.text.util.Linkify
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.appifyhub.timecrypt.R
import com.appifyhub.timecrypt.v2.api.TimecryptController
import kotlinx.android.synthetic.main.activity_read_message.*

/**
 * An activity that handles displaying (and unlocking) for one Timecrypt message.
 */
class ReadMessageActivity : AppCompatActivity(), View.OnClickListener {

    @Suppress("PrivatePropertyName")
    private val TAG = ReadMessageActivity::class.simpleName!!

    companion object {
        const val KEY_MESSAGE_ID = "MESSAGE_ID"
    }

    private var controller: TimecryptController? = null
    private var messageId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // check preconditions, must find a message ID
        val extras = intent.extras
        val uri = intent.data
        if (extras == null && uri == null) {
            Log.e(TAG, "No URL data provided")
            finish()
            return
        }
        messageId = extras?.getString(KEY_MESSAGE_ID, null) ?: parseMessageUrl(uri)
        if (messageId == null) {
            Log.e(TAG, "No message ID provided")
            finish()
            return
        }

        setContentView(R.layout.activity_read_message)

        // activity is ready, create business logic components
        controller = TimecryptController(TimecryptController.Companion.DEFAULT_API_URL)
        controller?.lockCheck(this, messageId!!, lockCheckListener)

        // setup initial Views
        progressOverlay.visibility = View.VISIBLE
        listOf(buttonCancel, buttonCreateNew, buttonUnlock).forEach {
            it.setOnClickListener(this)
        }
    }

    /**
     * Tries to find the message ID by parsing the given [Uri].
     *
     * @param url An Intent [Uri] that represents the message link
     */
    private fun parseMessageUrl(url: Uri?): String? {
        try {
            if (url == null) return null
            val query = url.query ?: return null
            query.split("&").forEach {
                val param = it.split("=")
                if (param[0].trim() == "c" || param[0].trim() == "id") {
                    return param[1].trim()
                }
            }
            return null
        } catch (t: Throwable) {
            return null
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            buttonCancel.id -> {
                stopReading()
                finish()
            }
            buttonCreateNew.id -> {
                startActivity(Intent(this, CreateMessageActivity::class.java))
                finish()
            }
            buttonUnlock.id -> {
                currentFocus?.let {
                    val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputManager.hideSoftInputFromWindow(it.windowToken, 0)
                }
                progressOverlay.visibility = View.VISIBLE
                readMessagePassword.visibility = View.GONE
                buttonUnlock.visibility = View.GONE
                controller?.read(this, messageId!!, readMessagePassword.text.toString(), readCompleteListener)
                readMessagePassword.setText("")
                readInformation.text = ""
            }
        }
    }

    /**
     * Used to listen for changes from the [TimecryptController.lockCheck] operation.
     */
    private val lockCheckListener = object : TimecryptController.LockCheckListener {
        override fun onLockCheckCompleted(locked: Boolean) {
            if (locked) {
                progressOverlay.visibility = View.GONE
                readMessagePassword.visibility = View.VISIBLE
                buttonUnlock.visibility = View.VISIBLE
                readInformation.text = getString(R.string.read_locked)
            } else {
                controller?.read(this@ReadMessageActivity, messageId!!, null, readCompleteListener)
            }
        }

        override fun onLockCheckFailed(message: String) {
            Log.e(TAG, message)
            progressOverlay.visibility = View.GONE
            Toast.makeText(this@ReadMessageActivity, message, Toast.LENGTH_LONG).show()
            finish()
        }
    }

    /**
     * Used to listen for changes from the [TimecryptController.read] operation.
     */
    private val readCompleteListener = object : TimecryptController.ReadCompleteListener {
        override fun onReadComplete(text: String, title: String?, destructDate: String, views: Int) {
            progressOverlay.visibility = View.GONE
            buttonCreateNew.visibility = View.VISIBLE

            val viewsCount = resources.getQuantityString(R.plurals.views_left, views, views)
            readInformation.text = getString(R.string.read_info, viewsCount, destructDate)

            // append title on top if present
            var finalText = text
            title?.let {
                finalText = "- $it -\n\n$finalText"
            }
            readMessageText.text = finalText
            LinkifyCompat.addLinks(readMessageText, Linkify.WEB_URLS)
            readMessageText.visibility = View.VISIBLE
        }

        override fun onReadFailed(message: String) {
            Log.e(TAG, message)
            progressOverlay.visibility = View.GONE
            Toast.makeText(this@ReadMessageActivity, message, Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun stopReading() {
        controller?.stopAll()
        progressOverlay.visibility = View.GONE
    }

    override fun onStop() {
        super.onStop()
        stopReading()
    }

}
