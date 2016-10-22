package co.timecrypt.android.v2.api

import com.squareup.moshi.Json

/**
 * Interpretation of the `/create` endpoint response from the Timecrypt server. A sample response is:
 * ```
 * {
 *     "id": "f78a89",
 *     "status_code": 0
 * }
 * ```
 */
class CreateResponse(
        val id: String?,
        @Json(name = "status_code") val statusCode: Int
)