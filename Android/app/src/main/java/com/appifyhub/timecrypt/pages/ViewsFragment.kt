package com.appifyhub.timecrypt.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appifyhub.timecrypt.R
import kotlinx.android.synthetic.main.fragment_views.*
import me.angrybyte.circularslider.CircularSlider

/**
 * The fragment containing the maximum allowed views picker.
 */
class ViewsFragment : TimecryptFragment(), CircularSlider.OnSliderMovedListener {

    companion object {
        const val MIN_VIEWS = 1
        const val MAX_VIEWS = 499
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_views, container, false)
    }

    // this also gets called by super-class on fragment start
    override fun onMessageUpdated() {
        sliderViews.setPosition(convertPercentToPosition(message.views.toDouble() / MAX_VIEWS))
        sliderCountViews.text = message.views.toString()
    }

    override fun onStart() {
        super.onStart()
        sliderViews.setOnSliderMovedListener(this)
    }

    override fun onStop() {
        super.onStop()
        sliderViews.setOnSliderMovedListener(null)
    }

    override fun onSliderMoved(pos: Double) {
        val calculatedViews = ((convertAngleToPercent(pos) * (MAX_VIEWS - MIN_VIEWS)).round() + MIN_VIEWS).toInt()
        sliderCountViews.text = calculatedViews.toString()
        message.views = calculatedViews
    }

}