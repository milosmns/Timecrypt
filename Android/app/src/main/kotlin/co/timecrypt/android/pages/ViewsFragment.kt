package co.timecrypt.android.pages

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.timecrypt.android.R
import kotlinx.android.synthetic.main.fragment_views.*
import me.angrybyte.circularslider.CircularSlider

/**
 * The fragment containing the maximum allowed views picker
 */
class ViewsFragment : TimecryptFragment(), CircularSlider.OnSliderMovedListener {

    companion object {
        val MIN_VIEWS = 1
        val MAX_VIEWS = 499
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_views, container, false)
    }

    val h = Handler()
    fun getRunnable(): Runnable {
        return Runnable() {
            val position = convertPercentToPosition(i)
            val angle = Math.PI /*start_angle*/ + position * 2.0 * Math.PI
            Log.d("DEBUG", "Input $i, position $position, angle $angle")

            sliderViews.setPosition(position)
            sliderViews.invalidate()
            sliderCountViews.text = (i * MAX_VIEWS).round().toString()

            i += 0.125

            if (i <= 1.0) {
                h.postDelayed(getRunnable(), 1000)
            }
        }
    }

    var i = 0.0
    fun startDemoTimer() {
        h.postDelayed(getRunnable(), 1000)
    }

    override fun onStart() {
        super.onStart()
        sliderViews.setOnSliderMovedListener(this)
        message?.let {
            // val forcedAngle = convertPercentToPosition((message!!.views / MAX_VIEWS).toDouble())
            startDemoTimer()
            sliderViews.setPosition(convertPercentToPosition(0.0))
            sliderViews.invalidate()
            onSliderMoved(convertPercentToPosition(0.0))
        }
    }

    override fun onStop() {
        super.onStop()
        sliderViews.setOnSliderMovedListener(null)
    }

    override fun onSliderMoved(pos: Double) {
        val calculatedViews = (Math.round(convertAngleToPercent(pos) * (MAX_VIEWS - MIN_VIEWS)) + MIN_VIEWS).toInt()
        sliderCountViews.text = calculatedViews.toString()
        message?.views = calculatedViews
    }

}