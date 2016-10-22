package co.timecrypt.android.v2.api

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.QueryMap

/**
 * Allows RESTful access to the Timecrypt-capable server.
 */
interface TimecryptRestApi {

    /**
     * Stores a new Timecrypt message on the server.
     * @param values Which values to send (use [ApiObjectHelper] to convert a [TimecryptMessage] to a map)
     */
    @POST("create")
    fun create(@QueryMap values: Map<String, String>): Call<CreateResponse>

}