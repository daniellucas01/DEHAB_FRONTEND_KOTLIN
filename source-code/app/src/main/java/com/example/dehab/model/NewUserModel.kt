package com.example.dehab.model

import com.google.gson.annotations.SerializedName

data class NewUserModel(
    val password: String,
    val username: String,
    @SerializedName("wallet_address")
    val walletAddress: Int
)