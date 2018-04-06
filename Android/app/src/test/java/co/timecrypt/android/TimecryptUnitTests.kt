package co.timecrypt.android

import co.timecrypt.android.v2.api.TimecryptMessage
import co.timecrypt.android.v2.api.TimecryptRestApi
import co.timecrypt.android.v2.api.Utils
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import org.junit.Assert.*
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.text.SimpleDateFormat
import java.util.*

/**
 * These tests will execute on the development machine (host).
 */
class TimecryptUnitTests {

    @Suppress("PrivatePropertyName")
    private val TAG: String = TimecryptUnitTests::class.simpleName!!

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
    private fun makeMoshi(): Moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    private fun makeRetrofit(): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(makeMoshi()))
                .baseUrl("https://timecrypt.co/api/v2/")
                .build()
    }

    private fun makeTimecryptApi(): TimecryptRestApi {
        return makeRetrofit().create(TimecryptRestApi::class.java)
    }

    private fun createTextOnlyMessage(): Pair<TimecryptMessage, String> {
        val text = "$TAG#${(Math.random() * 1000 + 30).toInt()}"
        val message = TimecryptMessage(text)
        val result = makeTimecryptApi().create(Utils.convertToQueryMap(message)).execute()
        assertTrue(result.isSuccessful)
        return Pair(message, result.body()?.id as String)
    }

    private fun createTextAndViewsMessage(views: Int): Pair<TimecryptMessage, String> {
        val text = "$TAG#${(Math.random() * 1000 + 30).toInt()}"
        val message = TimecryptMessage(text, views)
        val result = makeTimecryptApi().create(Utils.convertToQueryMap(message)).execute()
        assertTrue(result.isSuccessful)
        return Pair(message, result.body()?.id as String)
    }

    private fun createAllFieldsMessage(message: TimecryptMessage): String {
        val result = makeTimecryptApi().create(Utils.convertToQueryMap(message)).execute()
        assertTrue(result.isSuccessful)
        return result.body()?.id as String
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
        val tomorrow = Utils.getTomorrow(true)
        var message = TimecryptMessage("Full message", 3, tomorrow, "dyk24648@xzsok.com", "dyk24644@xzsok.com", "This thing", "pass1234")
        var map = Utils.convertToQueryMap(message)
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
        map = Utils.convertToQueryMap(message)
        assertEquals("Text only", map["text"])
        assertEquals("1", map["views"])
        assertEquals(2, map.size)

        // test constructor #2
        message = TimecryptMessage("Text with views", 2)
        map = Utils.convertToQueryMap(message)
        assertEquals("Text with views", map["text"])
        assertEquals("2", map["views"])
        assertEquals(2, map.size)
    }
    // </editor-fold>

    // <editor-fold desc="Create API Tests">
    @Test
    fun testCreateApi_textOnly() {
        val text = "$TAG#${(Math.random() * 1000 + 30).toInt()}"
        val message = TimecryptMessage(text)
        val call = makeTimecryptApi().create(Utils.convertToQueryMap(message))
        assertNotNull(call)

        // check HTTP
        val result = call.execute()
        assertTrue(result.isSuccessful)

        // check response body
        val response = result.body()
        assertNotNull(response)
        assertEquals(0, response?.statusCode)
        assertNotNull(response?.id)
        val length = response?.id?.length as Int
        assertTrue(length > 0)
    }

    @Test
    fun testCreateApi_textAndViews() {
        val text = "$TAG#${(Math.random() * 1000 + 30).toInt()}"
        val message = TimecryptMessage(text, 3)
        val call = makeTimecryptApi().create(Utils.convertToQueryMap(message))
        assertNotNull(call)

        // check HTTP
        val result = call.execute()
        assertTrue(result.isSuccessful)

        // check response body
        val response = result.body()
        assertNotNull(response)
        assertEquals(0, response?.statusCode)
        assertNotNull(response?.id)
        val length = response?.id?.length as Int
        assertTrue(length > 0)
    }

    @Test
    fun testCreateApi_allFields() {
        val text = "$TAG#${(Math.random() * 1000 + 30).toInt()}"
        val message = TimecryptMessage(text, 3, Utils.getTomorrow(true),
                "dyk24648@xzsok.com", "dyk24644@xzsok.com", "This thing", "pass1234")
        val call = makeTimecryptApi().create(Utils.convertToQueryMap(message))
        assertNotNull(call)

        // check HTTP
        val result = call.execute()
        assertTrue(result.isSuccessful)

        // check response body
        val response = result.body()
        assertNotNull(response)
        assertEquals(0, response?.statusCode)
        assertNotNull(response?.id)
        val length = response?.id?.length as Int
        assertTrue(length > 0)
    }
    // </editor-fold>

    // <editor-fold desc="Lock check API Tests">
    @Test
    fun testLockCheckApi_invalidMessage() {
        val call = makeTimecryptApi().isLocked("invalid-ID")
        assertNotNull(call)

        // check HTTP
        val result = call.execute()
        assertTrue(result.isSuccessful)

        // check response body
        val response = result.body()
        assertNotNull(response)
        assertEquals(-3, response?.statusCode)
        assertNull(response?.isLocked)
    }

    @Test
    fun testLockCheckApi_lockedMessage() {
        // create a locked message first...
        val text = "$TAG#${(Math.random() * 1000 + 30).toInt()}"
        val message = TimecryptMessage(text, 3, Utils.getTomorrow(true),
                "dyk24648@xzsok.com", "dyk24644@xzsok.com", "This thing", "pass1234")
        val createCall = makeTimecryptApi().create(Utils.convertToQueryMap(message))
        assertNotNull(createCall)

        // check create HTTP
        val createResult = createCall.execute()
        assertTrue(createResult.isSuccessful)

        // check create response body
        val createResponse = createResult.body()
        assertNotNull(createResponse)
        assertEquals(0, createResponse?.statusCode)
        assertNotNull(createResponse?.id)
        val length = createResponse?.id?.length as Int
        assertTrue(length > 0)

        // now to check if created message is locked...
        val lockCheckCall = makeTimecryptApi().isLocked(createResponse.id!!)
        assertNotNull(lockCheckCall)

        // check is-locked HTTP
        val lockCheckResult = lockCheckCall.execute()
        assertTrue(lockCheckResult.isSuccessful)

        // check is-locked response body
        val lockCheckResponse = lockCheckResult.body()
        assertNotNull(lockCheckResponse)
        assertEquals(0, lockCheckResponse?.statusCode)
        assertNotNull(lockCheckResponse?.isLocked)
        assertTrue(lockCheckResponse?.isLocked!!)
    }

    @Test
    fun testLockCheckApi_unlockedMessage() {
        // create a locked message first...
        val text = "$TAG#${(Math.random() * 1000 + 30).toInt()}"
        val message = TimecryptMessage(text, 3, Utils.getTomorrow(true),
                "dyk24648@xzsok.com", "dyk24644@xzsok.com", "This thing", null)
        val createCall = makeTimecryptApi().create(Utils.convertToQueryMap(message))
        assertNotNull(createCall)

        // check create HTTP
        val createResult = createCall.execute()
        assertTrue(createResult.isSuccessful)

        // check create response body
        val createResponse = createResult.body()
        assertNotNull(createResponse)
        assertEquals(0, createResponse?.statusCode)
        assertNotNull(createResponse?.id)
        val length = createResponse?.id?.length as Int
        assertTrue(length > 0)

        // now to check if created message is locked...
        val lockCheckCall = makeTimecryptApi().isLocked(createResponse.id!!)
        assertNotNull(lockCheckCall)

        // check is-locked HTTP
        val lockCheckResult = lockCheckCall.execute()
        assertTrue(lockCheckResult.isSuccessful)

        // check is-locked response body
        val lockCheckResponse = lockCheckResult.body()
        assertNotNull(lockCheckResponse)
        assertEquals(0, lockCheckResponse?.statusCode)
        assertNotNull(lockCheckResponse?.isLocked)
        assertFalse(lockCheckResponse?.isLocked!!)
    }
    // </editor-fold>

    // <editor-fold desc="Read API Tests">
    @Test
    fun testReadApi_invalidMessage() {
        val call = makeTimecryptApi().read("invalid-ID")
        assertNotNull(call)

        // check HTTP
        val result = call.execute()
        assertTrue(result.isSuccessful)

        // check response body
        val response = result.body()
        assertNotNull(response)
        assertEquals(-3, response?.statusCode)
        assertNull(response?.text)
        assertNull(response?.title)
        assertNull(response?.views)
        assertNull(Utils.parseDate(response?.destructDate))
    }

    @Test
    fun testReadApi_textOnly() {
        // create a message first...
        val created = createTextOnlyMessage()

        // now to check if created message is properly saved...
        val readCall = makeTimecryptApi().read(created.second)
        assertNotNull(readCall)

        // check read HTTP
        val readResult = readCall.execute()
        assertTrue(readResult.isSuccessful)

        // check read response body
        val readResponse = readResult.body()
        assertNotNull(readResponse)
        assertEquals(0, readResponse?.statusCode)

        assertEquals(0, readResponse?.views)
        assertEquals(Utils.getTomorrow(true), Utils.parseDate(readResponse?.destructDate))
        assertEquals(created.first.text, readResponse?.text)
        assertNull(readResponse?.title)
    }

    @Test
    fun testReadApi_textAndViews() {
        // create a message first...
        val views = Random().nextInt(20)
        val created = createTextAndViewsMessage(views)

        // now to check if created message is properly saved...
        val readCall = makeTimecryptApi().read(created.second)
        assertNotNull(readCall)

        // check read HTTP
        val readResult = readCall.execute()
        assertTrue(readResult.isSuccessful)

        // check read response body
        val readResponse = readResult.body()
        assertNotNull(readResponse)
        assertEquals(0, readResponse?.statusCode)

        assertEquals(views - 1, readResponse?.views)
        assertEquals(Utils.getTomorrow(true), Utils.parseDate(readResponse?.destructDate))
        assertEquals(created.first.text, readResponse?.text)
        assertNull(readResponse?.title)
    }

    @Test
    fun testReadApi_allFieldsUnlocked() {
        // create a message first...
        val views = Random().nextInt(20)
        val message = TimecryptMessage("$TAG: Testing all fields with $views views", views, Utils.getTomorrow(true),
                "dyk24644@xzsok.com", "dyk24644@xzsok.com", "All fields title", null)
        val createId = createAllFieldsMessage(message)

        // now to check if created message is properly saved...
        val readCall = makeTimecryptApi().read(createId)
        assertNotNull(readCall)

        // check read HTTP
        val readResult = readCall.execute()
        assertTrue(readResult.isSuccessful)

        // check read response body
        val readResponse = readResult.body()
        assertNotNull(readResponse)
        assertEquals(0, readResponse?.statusCode)

        assertEquals(views - 1, readResponse?.views)
        assertEquals(Utils.getTomorrow(true), Utils.parseDate(readResponse?.destructDate))
        assertEquals(message.text, readResponse?.text)
        assertEquals(message.title, readResponse?.title)
    }

    @Test
    fun testReadApi_allFieldsLocked() {
        // create a message first...
        val views = Random().nextInt(20)
        val message = TimecryptMessage("$TAG: Testing all fields with $views views", views, Utils.getTomorrow(true),
                "dyk24644@xzsok.com", "dyk24644@xzsok.com", "All fields title", "myPass1234")
        val createId = createAllFieldsMessage(message)

        // now to check if created message is properly saved...
        val readCall = makeTimecryptApi().read(createId, message.password)
        assertNotNull(readCall)

        // check read HTTP
        val readResult = readCall.execute()
        assertTrue(readResult.isSuccessful)

        // check read response body
        val readResponse = readResult.body()
        assertNotNull(readResponse)
        assertEquals(0, readResponse?.statusCode)

        assertEquals(views - 1, readResponse?.views)
        assertEquals(Utils.getTomorrow(true), Utils.parseDate(readResponse?.destructDate))
        assertEquals(message.text, readResponse?.text)
        assertEquals(message.title, readResponse?.title)
    }
    // </editor-fold>

}
