package co.timecrypt.android.helpers

import android.support.v4.view.ViewPager.OnPageChangeListener

/**
 * Implements all methods from the [OnPageChangeListener] interface.
 */
open class PageChangeListenerAdapter : OnPageChangeListener {
    override fun onPageSelected(position: Int) {}
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
    override fun onPageScrollStateChanged(state: Int) {}
}