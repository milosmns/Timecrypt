package co.timecrypt.android.v2.api

import android.content.Context
import co.timecrypt.android.R
import org.threeten.bp.DateTimeUtils
import org.threeten.bp.LocalDate
import java.text.SimpleDateFormat
import java.util.*

/**
 * Helps convert object types to easily use the API.
 */
object Utils {

    /**
     * A compiled list of all possible server errors.
     */
    private val SERVER_ERRORS = hashMapOf(
            Pair(-1, R.string.error_internal),
            Pair(-2, R.string.error_missing_id),
            Pair(-3, R.string.error_invalid_id),
            Pair(-4, R.string.error_missing_text),
            Pair(-5, R.string.error_title_bounds),
            Pair(-6, R.string.error_text_bounds),
            Pair(-7, R.string.error_views_bounds),
            Pair(-8, R.string.error_password_unusable),
            Pair(-9, R.string.error_destruct_date_bounds),
            Pair(-10, R.string.error_email_to_invalid),
            Pair(-11, R.string.error_email_from_invalid)
    )

    /**
     * Converts a Timecrypt message to a simple mapped structure for URL queries.
     * @param message Which message to convert
     * @return A compiled map of key-value pairs that corresponds to the given messages
     */
    fun convertToQueryMap(message: TimecryptMessage): Map<String, String> {
        val map = HashMap<String, String>()
        map.put("text", message.text)
        map.put("views", message.views.toString())
        message.destructDate?.let {
            map.put("lifetime", SimpleDateFormat("yyyy-MM-dd", Locale.US).format(it))
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
     * @return The date instance representing Tomorrow
     */
    fun getTomorrow(resetTimeToMidnight: Boolean): Date {
        try {
            val localDate = LocalDate.now()
            if (resetTimeToMidnight) {
                localDate.atStartOfDay()
            }
            localDate.plusDays(1)
            return DateTimeUtils.toSqlDate(localDate)
        } catch (e: Throwable) {
            println("Not using AndroidThreeTen")

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
    }

    /**
     * Tries to parse the given String into a [Date] object.
     * @param date String representation of a date
     * @return A parsed Date object
     */
    fun parseDate(date: String?): Date? {
        return try {
            return SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date)
        } catch (ex: Exception) {
            null
        }
    }

    /**
     * Gets the text message associated to the given status code, or `null` if status is `0` or invalid.
     * @param context Which [Context] to use to fetch the text
     * @param code Status code to parse
     * @return A string value for display, or `null` for invalid codes
     * @see [SERVER_ERRORS]
     */
    fun parseStatusCode(context: Context, code: Int): String? {
        SERVER_ERRORS[code]?.let {
            return context.getString(R.string.error_template, it.toString())
        }
        return null
    }

}