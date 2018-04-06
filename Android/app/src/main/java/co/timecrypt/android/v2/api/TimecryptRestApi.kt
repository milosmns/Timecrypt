package co.timecrypt.android.v2.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.QueryMap

/**
 * Allows RESTful access to the Timecrypt-capable server.
 */
interface TimecryptRestApi {

    /**
     * Stores a new Timecrypt message on the server.
     *
     * @param values Which values to send (use [Utils] to convert a [TimecryptMessage] to a map)
     * @return A [Call] interface allowing you to execute the request asynchronously or synchronously
     */
    @POST("create")
    fun create(@QueryMap values: Map<String, String>): Call<CreateResponse>

    /**
     * Checks the server to see if message is locked with a password or not.
     *
     * @param id Timecrypt message ID, usually obtained using the [create] API call
     * @return A [Call] interface allowing you to execute the request asynchronously or synchronously
     */
    @GET("is-locked")
    fun isLocked(@Query("id") id: String): Call<IsLockedResponse>

    /**
     * Fetches and reads the Timecrypt message from the server.
     *
     * @param id Timecrypt message ID, usually obtained using the [create] API call
     * @param password An optional parameter, used to decrypt the message if it [isLocked]. Pass `null` to ignore
     * @return A [Call] interface allowing you to execute the request asynchronously or synchronously
     */
    @GET("read")
    fun read(@Query("id") id: String, @Query("password") password: String? = null): Call<ReadResponse>

}