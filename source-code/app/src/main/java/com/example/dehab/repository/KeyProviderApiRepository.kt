package com.example.dehab.repository

import com.example.dehab.model.NewUserModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

object KeyProviderApiRepository {
    private var apiInstance = RetrofitSingleton.instance

    suspend fun registerUser(user : NewUserModel) : Boolean  {
        val webResponse = apiInstance.createNewUser(user).await()
        return webResponse.isSuccessful
//        CoroutineScope(IO).launch {
//            val webResponse = apiInstance.createNewUser(user).await()
//            withContext(Main) {
//                return@withContext webResponse.isSuccessful
//            }
//        }
    }
}