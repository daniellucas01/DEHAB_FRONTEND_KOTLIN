package com.example.dehab.model


import com.google.gson.annotations.SerializedName

data class TransactionSignModel(
    @SerializedName("contract_address")
    val contractAddress: String,
    val transactionId: Int
)