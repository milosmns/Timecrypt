package co.timecrypt.android.v2.api

import com.squareup.moshi.Json

/**
 * Interpretation of the `/read` endpoint response from the Timecrypt server. A sample response is:
 * ```
 * {
 *     "text": "Here is the message",
 *     "title": "Custom title",
 *     "views": 2,
 *     "lifetime": "2016-08-15",
 *     "status_code": 0
 * }
 * ```
 */
class ReadResponse(
        val text: String?,
        val title: String?,
        val views: Int?,
        @Json(name = "lifetime") val destructDate: String?,
        @Json(name = "status_code") val statusCode: Int
) : TimecryptResponse