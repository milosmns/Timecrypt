package co.timecrypt.android.pages

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.timecrypt.android.R
import co.timecrypt.android.helpers.TextWatcherAdapter
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
        messageInputField.setText(message.text)
    }

    override fun onStop() {
        super.onStop()
        messageInputField.removeTextChangedListener(messageTextWatcher)
    }

    private val messageTextWatcher = object : TextWatcherAdapter() {
        override fun afterTextChanged(text: Editable) {
            messageInputField.shiftGravity(text)
            message.text = text.toString().trim()
            notifyListener { it.onTextInvalidated(message.text.isEmpty()) }
        }
    }

}