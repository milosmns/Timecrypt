package com.appifyhub.timecrypt.pages

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.appifyhub.timecrypt.activities.CreateMessageActivity
import com.appifyhub.timecrypt.helpers.OnMessageChangedEmitter
import com.appifyhub.timecrypt.helpers.OnMessageChangedListener
import com.appifyhub.timecrypt.v2.api.TimecryptMessage
import kotlin.reflect.full.primaryConstructor

/**
 * The pager adapter being used in the [CreateMessageActivity]. Does caching internally but also uses the cache from [FragmentManager].
 */
class SwipeAdapter(
        listener: OnMessageChangedListener,
        message: TimecryptMessage,
        private val manager: FragmentManager,
        override var listeners: MutableList<OnMessageChangedListener> = mutableListOf()
) : FragmentPagerAdapter(manager), OnMessageChangedListener, OnMessageChangedEmitter {

    companion object {
        val PAGES = listOf(
                TextFragment::class,
                ViewsFragment::class,
                DestructDateFragment::class,
                DeliveryFragment::class
        )
    }

    private val fragmentCache: MutableMap<String, TimecryptFragment> = mutableMapOf()
    private var messageListener: OnMessageChangedListener?

    private var _message: TimecryptMessage = TimecryptMessage("")
    override var message: TimecryptMessage
        get() = _message
        set(value) {
            _message = value
            // notify only if listeners are attached (no reason to update otherwise)
            if (listeners.size > 0) {
                onMessageUpdated()
            }
        }

    init {
        _message = message
        messageListener = listener
        addMessageListener(listener)
    }

    /**
     * Makes sure that a fragment exists at the given [position]. Uses the [fragmentCache] to store them afterwards.
     * @param position Which position to look at - refer to [PAGES]
     */
    private fun ensureFragment(position: Int): TimecryptFragment {
        val name = PAGES[position].simpleName!!
        val found = manager.findFragmentByTag(name) ?: fragmentCache[name]
        if (found == null) {
            val fragment = PAGES[position].primaryConstructor?.call() ?: throw IllegalStateException()
            fragment.message = this.message
            fragmentCache[name] = fragment
            fragment.addMessageListener(this)
            return fragment
        }
        return found as TimecryptFragment
    }

    override fun getItem(position: Int): TimecryptFragment {
        return ensureFragment(position)
    }

    override fun getCount(): Int {
        return PAGES.size
    }

    /**
     * Clears the internal cache.
     */
    fun cleanup() {
        for ((_, fragment) in fragmentCache) {
            fragment.removeMessageListener(this)
        }

        try {
            val transaction = manager.beginTransaction()
            PAGES.forEach {
                val fragment = manager.findFragmentByTag(it.simpleName)
                fragment?.let { transaction.remove(it) }
            }
            transaction.commitAllowingStateLoss()
        } catch (ignored: Throwable) {
        }

        fragmentCache.clear()

        messageListener?.let { removeMessageListener(it) }
    }

    /* Message emitter and listener */

    override fun onTextInvalidated(empty: Boolean) {
        notifyListener { it.onTextInvalidated(empty) }
    }

    override fun onMessageUpdated() {
        for ((_, fragment) in fragmentCache) {
            fragment.message = message
        }
    }

}