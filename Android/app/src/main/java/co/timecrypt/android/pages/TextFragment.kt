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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_text, container, false)
    }

    // this also gets called by super-class on fragment start
    override fun onMessageUpdated() {
        messageInputField.setText(message.text)
    }

    override fun onStart() {
        super.onStart()
        messageInputField.addTextChangedListener(messageTextWatcher)
        updateMessageUi(messageInputField.text)
    }

    override fun onStop() {
        super.onStop()
        messageInputField.removeTextChangedListener(messageTextWatcher)
    }

    private fun updateMessageUi(text: Editable) {
        message.text = text.toString().trim()
        val changed = messageInputField.shiftGravity(message.text)
        if (changed) notifyListener { it.onTextInvalidated(message.text.isEmpty()) }
    }

    private val messageTextWatcher = object : TextWatcherAdapter() {
        override fun afterTextChanged(text: Editable) {
            updateMessageUi(text);
        }
    }

}