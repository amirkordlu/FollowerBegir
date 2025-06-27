package com.amk.followerbegir.model.net

import com.amk.followerbegir.model.data.AddOrderServiceResponse
import com.amk.followerbegir.model.data.ServiceItemsResponse
import com.amk.followerbegir.util.API_KEY
import com.amk.followerbegir.util.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @GET("api/v2/")
    suspend fun getAllItems(
        @Query("key") key: String = API_KEY,
        @Query("action") action: String = "services"
    ): List<ServiceItemsResponse>

    @GET("api/v2/")
    suspend fun addOrderService(
        @Query("key") key: String = API_KEY,
        @Query("action") action: String = "add",
        @Query("service") service: Int,
        @Query("link") link: String,
        @Query("quantity") quantity: Int,
        @Query("is_test") isTest: Int
    ): AddOrderServiceResponse

}

fun createApiService(): ApiService {
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit.create(ApiService::class.java)
}