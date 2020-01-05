package com.appifyhub.timecrypt.v2.api

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
class IsLockedResponse(@Json(name = "locked") var isLocked: Boolean?, @Json(name = "status_code") var statusCode: Int) : TimecryptResponse