package com.example.dehab.model

import com.google.gson.annotations.SerializedName

data class NewUserModel(
    val username: String,
    val password: String,
    @SerializedName("wallet_address")
    val walletAddress: Int
)