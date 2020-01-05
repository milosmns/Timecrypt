package com.appifyhub.timecrypt.helpers

import com.appifyhub.timecrypt.v2.api.TimecryptMessage

/**
 * A simple interface that allows attaching and detaching listeners of type [OnMessageChangedListener] on the implementing instance.
 */
interface OnMessageChangedEmitter {

    /**
     * A local list of listeners interested in knowing about changes from the [OnMessageChangedListener] group.
     */
    var listeners: MutableList<OnMessageChangedListener>
    /**
     * A local reference to the current Timecrypt message.
     */
    var message: TimecryptMessage

    /**
     * Adds a new [OnMessageChangedListener] to the [listeners] list.
     */
    fun addMessageListener(listener: OnMessageChangedListener) {
        listeners.add(listener)
    }

    /**
     * Removes the given [OnMessageChangedListener] from the [listeners] list.
     */
    fun removeMessageListener(listener: OnMessageChangedListener) {
        listeners.remove(listener)
    }

    /**
     * Notifies each of the [listeners] that an [event] happened.
     *
     * @param event What exactly happened
     */
    fun notifyListener(event: (OnMessageChangedListener) -> Unit) {
        listeners.forEach(event)
    }

    /**
     * This gets called to notify that the current Timecrypt message contains changes.
     */
    fun onMessageUpdated()

}