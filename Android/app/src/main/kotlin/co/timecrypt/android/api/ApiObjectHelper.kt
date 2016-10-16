package co.timecrypt.android.api

import android.text.format.DateFormat
import java.util.*

/**
 * Helps convert object types to easily use the API.
 */
object ApiObjectHelper {

    /**
     * Converts a Timecrypt message to a simple mapped structure for URL queries.
     * @param message Which message to convert
     */
    fun convertToQueryMap(message: TimecryptMessage): Map<String, String> {
        val map = HashMap<String, String>()
        map.put("text", message.text)
        map.put("views", message.views.toString())
        message.destructDate?.let {
            map.put("lifetime", DateFormat.format("yyyy-MM-dd", it).toString())
        }
        message.emailTo?.let {
            map.put("email_to", it)
        }
        message.emailFrom?.let {
            map.put("email_from", it)
        }
        message.title?.let {
            map.put("title", it)
        }
        message.password?.let {
            map.put("password", it)
        }
        return map
    }

    /**
     * Returns the tomorrow's date object.
     * @param resetTimeToMidnight Set this to `true` to reset the clock to midnight for today
     */
    fun getTomorrow(resetTimeToMidnight: Boolean): Date {
        val tomorrowCalendar = Calendar.getInstance()
        tomorrowCalendar.timeInMillis = System.currentTimeMillis()
        if (resetTimeToMidnight) {
            tomorrowCalendar.set(Calendar.HOUR_OF_DAY, 0)
            tomorrowCalendar.set(Calendar.MINUTE, 0)
            tomorrowCalendar.set(Calendar.SECOND, 0)
            tomorrowCalendar.set(Calendar.MILLISECOND, 0)
        }
        tomorrowCalendar.add(Calendar.DATE, 1)
        return Date(tomorrowCalendar.time.time)
    }

    /**
     * Adds the specified number of days to the given date.
     * @param date Original date
     * @param howMany How many days to add to the original date
     * @param resetTimeToMidnight Set this to `true` to reset the clock to midnight on the original date
     */
    fun addDays(date: Date, howMany: Int, resetTimeToMidnight: Boolean): Date {
        val resultCalendar = Calendar.getInstance()
        resultCalendar.timeInMillis = date.time
        if (resetTimeToMidnight) {
            resultCalendar.set(Calendar.HOUR_OF_DAY, 0)
            resultCalendar.set(Calendar.MINUTE, 0)
            resultCalendar.set(Calendar.SECOND, 0)
            resultCalendar.set(Calendar.MILLISECOND, 0)
        }
        resultCalendar.add(Calendar.DATE, 1)
        return Date(resultCalendar.time.time)
    }

}