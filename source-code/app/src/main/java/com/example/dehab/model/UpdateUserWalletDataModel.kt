package com.example.dehab.model


import com.google.gson.annotations.SerializedName

data class UpdateUserWalletDataModel(
    val userId: Int,
    val userWalletAddress: String
)