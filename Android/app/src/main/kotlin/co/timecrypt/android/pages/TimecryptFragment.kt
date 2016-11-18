package co.timecrypt.android.pages

import android.support.v4.app.Fragment
import co.timecrypt.android.v2.api.TimecryptMessage

/**
 * A fragment variant that allows easy storage of the living [TimecryptMessage] instance.
 */
open class TimecryptFragment() : Fragment() {

    /**
     * Converts the angle radian range (0Pi - 2Pi) to human friendly range (0% - 100%).
     */
    protected fun convertAngleToPercent(angle: Double): Double {
        // reverse angle direction (radians go CCW) and calculate the PI/4 offset
        return (5 / 4 - angle / (2 * Math.PI)) % 1
    }

    /**
     * This is a direct inverse of [convertAngleToPercent].
     */
    protected fun convertPercentToAngle(percent: Double): Double {
        // just an inverted function from above
        return -(Math.PI * (4 * percent - 5) / 2) % (2 * Math.PI)
    }

    var message: TimecryptMessage? = null

}