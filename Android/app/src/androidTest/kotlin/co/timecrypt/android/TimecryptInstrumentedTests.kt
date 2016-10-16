package co.timecrypt.android

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * These tests will execute on an Android device.
 */
@RunWith(AndroidJUnit4::class)
class TimecryptInstrumentedTests() {

    @Test
    @Throws(Exception::class)
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("co.timecrypt.android", appContext.packageName)
    }

}
