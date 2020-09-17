package com.example.dehab.network

import com.example.dehab.model.*
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface KeyProviderApiInterface {
//    @GET("user")
//    fun getAllUser() : Deferred <Response<List<UserModel>>>

    @GET("user/authenticate/{username}/{password}")
    fun loginUser(
        @Path("username") username: String,
        @Path("password") password: String
    ) : Deferred <Response<UserModel>>

    @POST("user")
    fun createNewUser(@Body newUser : NewUserModel) : Deferred<Response<Void>>

    @POST("user/sign")
    fun signTransaction(@Body transaction : TransactionSignModel) : Deferred<Response<Void>>

    @POST("user/wallet/update")
    fun updateUserWalletWithId(@Body data : UpdateUserWalletDataModel) : Deferred<Response<Void>>

    @GET("user/wallet/{userId}")
    fun getUserWalletById(@Path("userId") userId: Int) : Deferred<Response<UserWalletByIdModel>>



}