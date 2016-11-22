package co.timecrypt.android.pages

import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.text.Editable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import co.timecrypt.android.R
import co.timecrypt.android.TextWatcherAdapter
import kotlinx.android.synthetic.main.fragment_text.*

/**
 * The fragment containing the message input.
 */
class TextFragment : TimecryptFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_text, container, false)
    }

    override fun onStart() {
        super.onStart()
        messageInputField.addTextChangedListener(messageTextWatcher)
        messageInputField.setText(message?.text)
    }

    override fun onStop() {
        super.onStop()
        messageInputField.removeTextChangedListener(messageTextWatcher)
    }

    /* Text Watcher API */

    private fun EditText.shiftGravity(text: Editable) {
        if (text.isEmpty() && gravity != Gravity.CENTER_HORIZONTAL or Gravity.TOP) {
            gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
        } else if (!text.isEmpty() && gravity != GravityCompat.START or Gravity.TOP) {
            gravity = GravityCompat.START or Gravity.TOP
        }
    }

    private val messageTextWatcher = object : TextWatcherAdapter() {
        override fun afterTextChanged(text: Editable) {
            messageInputField.shiftGravity(text)
        }
    }

}