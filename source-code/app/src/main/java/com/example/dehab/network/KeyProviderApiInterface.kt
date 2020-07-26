package com.example.dehab.network

import com.example.dehab.model.NewUserModel
import com.example.dehab.model.UserModel
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface KeyProviderApiInterface {
    @GET("user")
    fun getAllUser() : Deferred<Response<List<UserModel>>>

    @POST("user")
    fun createNewUser(@Body newUser : NewUserModel) : Deferred<Response<Void>>
}