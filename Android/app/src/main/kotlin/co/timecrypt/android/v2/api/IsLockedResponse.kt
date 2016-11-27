package co.timecrypt.android.v2.api

import com.squareup.moshi.Json

/**
 * Interpretation of the `/is-locked` endpoint response from the Timecrypt server. A sample response is:
 * ```
 * {
 *     "locked": true,
 *     "status_code": 0
 * }
 * ```
 */
class IsLockedResponse(
        @Json(name = "locked") val isLocked: Boolean?,
        @Json(name = "status_code") val statusCode: Int
) : TimecryptResponse