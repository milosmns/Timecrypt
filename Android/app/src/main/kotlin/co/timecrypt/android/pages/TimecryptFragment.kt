package co.timecrypt.android.pages

import android.support.v4.app.Fragment
import co.timecrypt.android.v2.api.TimecryptMessage

/**
 * A fragment variant that allows easy storage of the living [TimecryptMessage] instance.
 */
open class TimecryptFragment() : Fragment() {

    /**
     * Formats the current double value to a `digits`-length decimal format.
     * @param digits How many digits
     * @return The formatted value
     */
    fun Double.format(digits: Int) = String.format("%.${digits}f", this)

    /**
     * Converts the input from the slider to the `[0, 1)` range.
     * @param input The input from the slider
     * @return The calculated ranged output value
     */
    protected fun convertAngleToPercent(input: Double): Double {
        return (3f / 4f - input) % 1
    }

    /**
     * This is a direct inverse of the [convertAngleToPercent] method.
     */
    protected fun convertPercentToAngle(percent: Double): Double {
        return (7f / 4f - percent) % 1 - 1f
    }

    var message: TimecryptMessage? = null

}