package co.timecrypt.android

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import co.timecrypt.android.helpers.OnMessageChangedEmitter
import co.timecrypt.android.helpers.OnMessageChangedListener
import co.timecrypt.android.pages.*
import co.timecrypt.android.v2.api.TimecryptMessage
import kotlin.reflect.primaryConstructor

/**
 * The pager adapter being used in the [MessageActivity].
 * @param manager The [FragmentManager] being used by the current activity
 */
class SwipeAdapter(
        override var listeners: MutableList<OnMessageChangedListener>?,
        val manager: FragmentManager,
        val message: TimecryptMessage
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
    private var messageListener: OnMessageChangedListener? = null

    constructor(listener: OnMessageChangedListener, manager: FragmentManager, message: TimecryptMessage) : this(mutableListOf(), manager, message) {
        this.messageListener = listener
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
            fragmentCache.put(name, fragment)
            fragment.addMessageListener(this)
            return fragment
        }
        return found as TimecryptFragment
    }

    /**
     * Clears the internal cache.
     */
    fun cleanup() {
        for ((name, fragment) in fragmentCache) {
            fragment.removeMessageListener(this)
            fragment.message = null
        }
        fragmentCache.clear()

        messageListener?.let { removeMessageListener(it) }
    }

    override fun getItem(position: Int): TimecryptFragment {
        return ensureFragment(position)
    }

    override fun getCount(): Int {
        return PAGES.size
    }

    /* Message changed listener */

    override fun onTextInvalidated(empty: Boolean) {
        notifyListener { it.onTextInvalidated(empty) }
    }

}