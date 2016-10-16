package co.timecrypt.android

import co.timecrypt.android.api.ApiObjectHelper
import co.timecrypt.android.api.TimecryptMessage
import co.timecrypt.android.api.TimecryptRestApi
import org.junit.Assert.*
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.text.SimpleDateFormat
import java.util.*

/**
 * These tests will execute on the development machine (host).
 */
class TimecryptUnitTests() {

    val TAG: String = TimecryptUnitTests::class.java.simpleName

    // <editor-fold desc="JVM Tests">
    @Test
    fun jvm_aluAdd() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun jvm_aluSubtract() {
        assertEquals(1, 4 - 3)
    }

    @Test
    fun jvm_aluMultiply() {
        assertEquals(6, 3 * 2)
    }

    @Test
    fun jvm_aluDivide() {
        assertEquals(6, 12 / 2)
    }
    // </editor-fold>

    // <editor-fold desc="Helpers">
    private fun makeRetrofit(): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl("https://timecrypt-angrybyte.rhcloud.com/v2/")
                .build()
    }

    private fun makeTimecryptApi(): TimecryptRestApi {
        return makeRetrofit().create(TimecryptRestApi::class.java)
    }
    // </editor-fold>

    // <editor-fold desc="Component construction tests">
    @Test
    fun constructRetrofit() {
        val retrofit = makeRetrofit()
        assertNotNull(retrofit)
    }

    @Test
    fun constructTimecryptApi() {
        val api = makeTimecryptApi()
        assertNotNull(api)
    }

    @Test
    fun constructQueryMap() {
        // test constructor #0
        val tomorrow = ApiObjectHelper.getTomorrow(true)
        var message = TimecryptMessage("Full message", 3, tomorrow, "dyk24648@xzsok.com", "dyk24644@xzsok.com", "This thing", "pass1234")
        var map = ApiObjectHelper.convertToQueryMap(message)
        val dateText = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(tomorrow)
        assertEquals("Full message", map["text"])
        assertEquals("3", map["views"])
        assertEquals(dateText, map["lifetime"])
        assertEquals("dyk24648@xzsok.com", map["email_to"])
        assertEquals("dyk24644@xzsok.com", map["email_from"])
        assertEquals("This thing", map["title"])
        assertEquals("pass1234", map["password"])
        assertEquals(7, map.size)

        // test constructor #1
        message = TimecryptMessage("Text only")
        map = ApiObjectHelper.convertToQueryMap(message)
        assertEquals("Text only", map["text"])
        assertEquals("1", map["views"])
        assertEquals(2, map.size)

        // test constructor #2
        message = TimecryptMessage("Text with views", 2)
        map = ApiObjectHelper.convertToQueryMap(message)
        assertEquals("Text with views", map["text"])
        assertEquals("2", map["views"])
        assertEquals(2, map.size)
    }
    // </editor-fold>

    // <editor-fold desc="Create API Tests">
    @Test
    fun testCreateApi_textOnly() {
        val message = TimecryptMessage("Testing from $TAG, text only")
        val call = makeTimecryptApi().create(ApiObjectHelper.convertToQueryMap(message))
        assertNotNull(call)

        val result = call.execute()
        assertTrue(result.isSuccessful)
        assertEquals(200, result.code())

        val response = result.body()
        assertNotNull(response)
        assertNotNull(response.id)
        assertEquals(0, response.statusCode.toInt())
    }

    @Test
    fun testCreateApi_textAndViews() {
        val message = TimecryptMessage("Testing from $TAG, text and views", 2)
        val call = makeTimecryptApi().create(ApiObjectHelper.convertToQueryMap(message))
        assertNotNull(call)

        val result = call.execute()
        assertTrue(result.isSuccessful)
        assertEquals(200, result.code())

        val response = result.body()
        assertNotNull(response)
        assertNotNull(response.id)
        assertEquals(0, response.statusCode.toInt())
    }

    @Test
    fun testCreateApi_allFields() {
        val message = TimecryptMessage("Testing from $TAG, all fields", 3, ApiObjectHelper.getTomorrow(true),
                "dyk24648@xzsok.com", "dyk24644@xzsok.com", "This thing", "pass1234")
        val call = makeTimecryptApi().create(ApiObjectHelper.convertToQueryMap(message))
        assertNotNull(call)

        val result = call.execute()
        assertTrue(result.isSuccessful)
        assertEquals(200, result.code())

        val response = result.body()
        assertNotNull(response)
        assertNotNull(response.id)
        assertEquals(0, response.statusCode.toInt())
    }
    // </editor-fold>

}
