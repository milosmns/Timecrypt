package co.timecrypt.android.helpers

import android.text.Editable
import android.text.TextWatcher

/**
 * Implements all methods from the [TextWatcher] interface.
 */
open class TextWatcherAdapter : TextWatcher {
    override fun afterTextChanged(text: Editable) {}
    override fun beforeTextChanged(text: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {}
}