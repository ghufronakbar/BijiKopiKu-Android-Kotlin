package com.example.bijikopiku.api

import com.example.bijikopiku.model.dto.CartDTO
import com.example.bijikopiku.model.dto.CheckoutDTO
import com.example.bijikopiku.model.dto.CoffeeFilterDTO
import com.example.bijikopiku.model.dto.LoginDTO
import com.example.bijikopiku.model.dto.ProfileDTO
import com.example.bijikopiku.model.dto.RegisterDTO
import com.example.bijikopiku.model.response.ApiRes
import com.example.bijikopiku.model.response.Coffee
import com.example.bijikopiku.model.response.Order
import com.example.bijikopiku.model.response.User
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiInterface {

    // ACCOUNT
    @POST("account/login")
    fun postLogin(@Body request: LoginDTO): Call<ApiRes<User>>

    @POST("account/register")
    fun postRegister(@Body request: RegisterDTO): Call<ApiRes<User>>

    @GET("account")
    fun getProfile(): Call<ApiRes<User>>

    @PUT("account")
    fun editProfile(@Body request: ProfileDTO): Call<ApiRes<User>>

    // ORDER
    @GET("orders")
    fun getOrders(): Call<ApiRes<List<Order>>>

    @GET("orders/{id}")
    fun getDetailOrder(@Path("id") orderId: String): Call<ApiRes<Order>>

    @POST("orders")
    fun createCheckout(@Body request: CheckoutDTO): Call<ApiRes<Order>>

    @PATCH("orders/{id}")
    fun cancelOrder(@Path("id") orderId: String): Call<ApiRes<Order>>

    @Multipart
    @POST("orders/{id}/pay")
    fun postPayment(
        @Path("id") orderId: String,
        @Part picture: MultipartBody.Part
    ): Call<ApiRes<Order>>

    // COFFEE
    @GET("coffee")
    fun getCoffees(): Call<ApiRes<List<Coffee>>>

    @GET("coffee/{id}")
    fun getCoffee(@Path("id") coffeeId: String): Call<ApiRes<Coffee>>

    @POST("coffee")
    fun getCoffeeFiltered(@Body request: CoffeeFilterDTO): Call<ApiRes<List<Coffee>>>

    // CART
    @POST("cart")
    fun getCarts(@Body request: CartDTO): Call<ApiRes<List<Coffee>>>
}
