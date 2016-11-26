package co.timecrypt.android.helpers

/**
 * A simple interface that allows attaching and detaching listeners of type [OnMessageChangedListener] on the implementing instance.
 */
interface OnMessageChangedEmitter {

    var listeners: MutableList<OnMessageChangedListener>?

    fun addMessageListener(listener: OnMessageChangedListener) {
        listeners?.add(listener)
    }

    fun removeMessageListener(listener: OnMessageChangedListener) {
        listeners?.remove(listener)
    }

    fun notifyListener(event: (OnMessageChangedListener) -> Unit) {
        listeners?.forEach(event)
    }

}