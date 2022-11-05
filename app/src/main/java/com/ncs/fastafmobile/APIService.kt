package com.ncs.fastafmobile

import okhttp3.RequestBody
import okhttp3.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface APIService {


    @GET("/api/users/{token}")
    suspend fun getTokenValidationResponseCode(@Path("token") token: String): Response

    @POST("/api/v1/create")
    suspend fun postFastAFUserDetails(@Body requestBody: RequestBody): Response


}