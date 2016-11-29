package co.timecrypt.android.pages

import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.view.Gravity
import android.view.View
import android.widget.EditText
import co.timecrypt.android.helpers.OnMessageChangedEmitter
import co.timecrypt.android.helpers.OnMessageChangedListener
import co.timecrypt.android.v2.api.TimecryptMessage

/**
 * A fragment variant that allows easy storage of the living [TimecryptMessage] instance.
 */
abstract class TimecryptFragment(
        override var listeners: MutableList<OnMessageChangedListener> = mutableListOf()
) : Fragment(), OnMessageChangedEmitter {

    var _message: TimecryptMessage = TimecryptMessage("")
    override var message: TimecryptMessage
        get() = _message
        set(value) {
            _message = value
            // notify only if listeners are attached (no reason to update otherwise)
            if (listeners.size > 0) {
                onMessageUpdated()
            }
        }

    override fun onStart() {
        super.onStart()
        onMessageUpdated()
    }

    /**
     * Rounds the double number
     * @return The rounded [Long] value
     */
    fun Double.round() = Math.round(this)

    /**
     * Rounds the float number
     * @return The rounded [Int] value
     */
    @Suppress("unused")
    fun Float.round() = Math.round(this)

    /**
     * Formats the current double value to a `digits`-length decimal format.
     * @param digits How many digits
     * @return The formatted value
     */
    @Suppress("unused")
    fun Double.format(digits: Int) = String.format("%.${digits}f", this)

    /**
     * Converts the input from the slider to the `[0, 1)` range shifted by 1/4 of the circle and reversed direction.
     * @param input The input from the slider
     * @return The calculated ranged output value
     */
    protected fun convertAngleToPercent(input: Double): Double = (3f / 4f - input) % 1

    /**
     * This is a direct inverse of the [convertAngleToPercent] method.
     */
    protected fun convertPercentToPosition(percent: Double): Double = 1f - ((1f / 4f + percent) % 1)

    /**
     * Shifts the gravity of an [EditText] depending on the given [text] parameter.
     * Empty text shifts to [Gravity.CENTER], non-empty text shifts to [Gravity.START].
     * @param text Which string to analyze
     * @return `True` if gravity was shifted, `false` if not
     */
    protected fun EditText.shiftGravity(text: String): Boolean {
        if (text.isEmpty() && gravity != Gravity.CENTER_HORIZONTAL or Gravity.TOP) {
            gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
            return true
        } else if (!text.isEmpty() && gravity != GravityCompat.START or Gravity.TOP) {
            gravity = GravityCompat.START or Gravity.TOP
            return true
        }
        return false
    }

    /**
     * Toggles the visibility of a View.
     */
    protected fun View.toggleVisibility() = if (visibility == View.VISIBLE) visibility = View.GONE else visibility = View.VISIBLE

}