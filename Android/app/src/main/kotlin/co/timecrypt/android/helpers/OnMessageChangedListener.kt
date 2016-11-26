package co.timecrypt.android.helpers

/**
 * A simple listener for Timecrypt message changes.
 */
interface OnMessageChangedListener {

    /**
     * Notifies the listener instance that the text being typed was invalidated (either empty or not empty).
     * @param empty Whether the new text is empty or not
     */
    fun onTextInvalidated(empty: Boolean)

}