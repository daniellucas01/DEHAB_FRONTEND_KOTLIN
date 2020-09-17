package com.example.dehab.repository

import com.example.dehab.model.*
import retrofit2.Response

object KeyProviderApiRepository {
    private var apiInstance = RetrofitSingleton.instance

    suspend fun registerUser(user : NewUserModel) : Response<Void>  {
        return apiInstance.createNewUser(user).await()
    }

    suspend fun loginUser (username: String, password: String) : Response<UserModel> {
        return apiInstance.loginUser(username, password).await()
    }

    suspend fun getUserWalletById (userId : Int): Response<UserWalletByIdModel> {
        return apiInstance.getUserWalletById(userId).await()
    }

    suspend fun updateUserWalletById (data : UpdateUserWalletDataModel): Response<Void> {
        return apiInstance.updateUserWalletWithId(data).await()
    }

    suspend fun signTransaction(transaction : TransactionSignModel): Response<Void> {
        return apiInstance.signTransaction(transaction).await()
    }
}