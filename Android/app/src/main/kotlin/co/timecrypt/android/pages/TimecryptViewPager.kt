package co.timecrypt.android.pages

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent


/**
 * This variant of the [ViewPager] allows dynamic controlling of touch events to enable or disable swiping through the pages.
 */
class TimecryptViewPager : ViewPager {

    var swipeEnabled = true

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (swipeEnabled) super.onInterceptTouchEvent(event) else false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (swipeEnabled) super.onTouchEvent(event) else false
    }

}