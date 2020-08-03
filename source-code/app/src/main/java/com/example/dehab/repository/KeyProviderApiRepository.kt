package com.example.dehab.repository

import com.example.dehab.model.NewUserModel
import retrofit2.Response

object KeyProviderApiRepository {
    private var apiInstance = RetrofitSingleton.instance

    suspend fun registerUser(user : NewUserModel) : Response<Void>  {
        return apiInstance.createNewUser(user).await()
    }

    suspend fun loginUser (username: String, password: String) : Response<Void> {
        return apiInstance.loginUser(username, password).await()
    }

    suspend fun getUserWallet (user : NewUserModel) {

    }
}