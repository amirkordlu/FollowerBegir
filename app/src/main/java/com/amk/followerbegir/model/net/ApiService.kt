package com.amk.followerbegir.model.net

import com.amk.followerbegir.model.data.ServiceItemsResponse
import com.amk.followerbegir.util.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("api/v2/")
    suspend fun getAllItems(
        @Query("key") key: String = "ziF9gL7Tz_yngnfDUIoZcf7YoXqXMSd7",
        @Query("action") action: String = "services"
    ): List<ServiceItemsResponse>

}

fun createApiService(): ApiService {
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit.create(ApiService::class.java)
}