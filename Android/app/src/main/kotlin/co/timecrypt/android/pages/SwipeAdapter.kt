package co.timecrypt.android.pages

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import co.timecrypt.android.activities.CreateMessageActivity
import co.timecrypt.android.helpers.OnMessageChangedEmitter
import co.timecrypt.android.helpers.OnMessageChangedListener
import co.timecrypt.android.v2.api.TimecryptMessage
import kotlin.reflect.primaryConstructor

/**
 * The pager adapter being used in the [CreateMessageActivity].
 * @param manager The [FragmentManager] being used by the current activity
 */
class SwipeAdapter(
        override var listeners: MutableList<OnMessageChangedListener> = mutableListOf(),
        override var message: TimecryptMessage,
        val manager: FragmentManager,
        listener: OnMessageChangedListener
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

    init {
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