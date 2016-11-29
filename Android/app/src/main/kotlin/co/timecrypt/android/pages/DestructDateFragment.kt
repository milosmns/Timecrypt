package co.timecrypt.android.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.timecrypt.android.R
import kotlinx.android.synthetic.main.fragment_destruct_date.*
import me.angrybyte.circularslider.CircularSlider
import org.threeten.bp.DateTimeUtils
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import java.util.*

/**
 * The fragment containing the destruct date picker.
 */
class DestructDateFragment : TimecryptFragment(), CircularSlider.OnSliderMovedListener {

    private companion object {
        val DATES = listOf(
                1 to R.plurals.plural_day,
                2 to R.plurals.plural_day,
                3 to R.plurals.plural_day,
                4 to R.plurals.plural_day,
                5 to R.plurals.plural_day,
                6 to R.plurals.plural_day,
                1 to R.plurals.plural_week,
                2 to R.plurals.plural_week,
                3 to R.plurals.plural_week,
                4 to R.plurals.plural_week,
                1 to R.plurals.plural_month,
                2 to R.plurals.plural_month,
                3 to R.plurals.plural_month,
                4 to R.plurals.plural_month,
                5 to R.plurals.plural_month,
                6 to R.plurals.plural_month,
                9 to R.plurals.plural_month,
                11 to R.plurals.plural_month,
                1 to R.plurals.plural_year,
                2 to R.plurals.plural_year,
                3 to R.plurals.plural_year
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_destruct_date, container, false)
    }

    // this also gets called by super-class on fragment start
    override fun onMessageUpdated() {
        message.destructDate = convertToDate(DATES[message.extra_DestructOptionPicked])
        val pickedRange = DATES[message.extra_DestructOptionPicked]
        sliderCountDestruct.text = pickedRange.first.toString()
        sliderLabelDestruct.text = resources.getQuantityString(pickedRange.second, pickedRange.first)
        sliderDestruct.setPosition(convertPercentToPosition(message.extra_DestructOptionPicked.toDouble() / DATES.size))
    }

    override fun onStart() {
        super.onStart()
        sliderDestruct.setOnSliderMovedListener(this)
    }

    override fun onStop() {
        super.onStop()
        sliderDestruct.setOnSliderMovedListener(null)
    }

    private fun convertToDate(mapPick: Pair<Int, Int>): Date {
        val localTime: LocalDateTime
        when (mapPick.second) {
            R.plurals.plural_day -> localTime = LocalDate.now().atStartOfDay().plusDays(mapPick.first.toLong())
            R.plurals.plural_week -> localTime = LocalDate.now().atStartOfDay().plusWeeks(mapPick.first.toLong())
            R.plurals.plural_month -> localTime = LocalDate.now().atStartOfDay().plusMonths(mapPick.first.toLong())
            R.plurals.plural_year -> localTime = LocalDate.now().atStartOfDay().plusYears(mapPick.first.toLong())
            else -> throw IllegalStateException("What's this case? $mapPick")
        }
        return DateTimeUtils.toSqlDate(localTime.toLocalDate())
    }

    override fun onSliderMoved(pos: Double) {
        val pickedIndex = (convertAngleToPercent(pos) * (DATES.size - 1)).round().toInt()
        val calculatedPick = DATES[pickedIndex]
        sliderCountDestruct.text = calculatedPick.first.toString()
        sliderLabelDestruct.text = resources.getQuantityString(calculatedPick.second, calculatedPick.first)
        message.extra_DestructOptionPicked = pickedIndex
        message.destructDate = convertToDate(calculatedPick)
    }

}