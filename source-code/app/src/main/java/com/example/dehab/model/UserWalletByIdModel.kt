package com.example.dehab.model


import com.google.gson.annotations.SerializedName

data class UserWalletByIdModel(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("wallet_address")
    val walletAddress: String
)