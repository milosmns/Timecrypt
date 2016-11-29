package co.timecrypt.android.helpers

import co.timecrypt.android.v2.api.TimecryptMessage

/**
 * A simple interface that allows attaching and detaching listeners of type [OnMessageChangedListener] on the implementing instance.
 */
interface OnMessageChangedEmitter {

    var listeners: MutableList<OnMessageChangedListener>
    var message: TimecryptMessage

    fun addMessageListener(listener: OnMessageChangedListener) {
        listeners.add(listener)
    }

    fun removeMessageListener(listener: OnMessageChangedListener) {
        listeners.remove(listener)
    }

    fun notifyListener(event: (OnMessageChangedListener) -> Unit) {
        listeners.forEach(event)
    }

    fun onMessageUpdated()

}