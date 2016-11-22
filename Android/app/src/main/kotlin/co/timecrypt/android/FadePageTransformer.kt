package co.timecrypt.android

import android.support.v4.view.ViewPager
import android.support.v4.view.ViewPager.PageTransformer
import android.view.View


/**
 * This [PageTransformer] is invoked whenever a visible/attached [ViewPager] page is scrolled. This offers an opportunity for the
 * application to apply a custom transformation to the page views using animation properties.
 *
 * As property animation is only supported as of Android 3.0 and forward, setting a PageTransformer on a ViewPager on earlier
 * platform versions will be ignored.
 */
class FadePageTransformer : ViewPager.PageTransformer {

    /**
     * Apply a property transformation to the given page.
     *
     * @param view Apply the transformation to this page
     * @param position Position of page relative to the current front-and-center position of the pager:
     *                 * ` 0` is front and center
     *                 * ` 1` is one full page position to the right
     *                 * `-1` is one page position to the left
     */
    override fun transformPage(view: View, position: Float) {
        if (position <= -1.0f || position >= 1.0f) {
            view.translationX = view.width * position
            view.alpha = 0.0f
        } else if (position == 0.0f) {
            view.translationX = view.width * position
            view.alpha = 1.0f
        } else {
            // position is between -1.0f & 0.0f OR 0.0f & 1.0f
            view.translationX = view.width * -position
            view.alpha = 1.0f - Math.abs(position)
        }
    }

}