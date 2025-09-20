package com.amk.follower.model.net

import com.amk.follower.model.data.AddOrderServiceResponse
import com.amk.follower.model.data.OrderStatusResponse
import com.amk.follower.model.data.ServiceItemsResponse
import com.amk.follower.util.API_KEY
import com.amk.follower.util.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
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

    @GET("api/v2/")
    suspend fun getOrderStatus(
        @Query("key") key: String = API_KEY,
        @Query("action") action: String = "status",
        @Query("orders") orders: String
    ): Map<String, OrderStatusResponse>

}

fun createApiService(): ApiService {
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit.create(ApiService::class.java)
}