package co.timecrypt.android.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.timecrypt.android.R
import kotlinx.android.synthetic.main.fragment_views.*
import me.angrybyte.circularslider.CircularSlider

/**
 * The fragment containing the maximum allowed views picker.
 */
class ViewsFragment : TimecryptFragment(mutableListOf()), CircularSlider.OnSliderMovedListener {

    companion object {
        val MIN_VIEWS = 1
        val MAX_VIEWS = 499
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_views, container, false)
    }

    override fun onStart() {
        super.onStart()
        sliderViews.setOnSliderMovedListener(this)
        sliderViews.setPosition(convertPercentToPosition(message.views.toDouble() / MAX_VIEWS))
        sliderViews.invalidate()
        sliderCountViews.text = message.views.toString()
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