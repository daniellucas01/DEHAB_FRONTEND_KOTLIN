package com.example.dehab.model


import com.google.gson.annotations.SerializedName

data class UserModel(
    val password: String,
    @SerializedName("user_id")
    val userId: Int,
    val username: String,
    @SerializedName("wallet_id")
    val walletId: Int
)