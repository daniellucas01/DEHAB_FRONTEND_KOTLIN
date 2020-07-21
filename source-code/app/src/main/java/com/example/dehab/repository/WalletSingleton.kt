package com.example.dehab.repository

import android.net.Credentials
import android.util.Log
import com.example.dehab.ui.Constants
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService

object WalletSingleton {
    private const val BASE_URL = "https://rinkeby.infura.io/v3/1c87174ef80345fb92acbd2da20133e5"

    val web3 : Web3j by lazy {
        Web3j.build(HttpService(BASE_URL))
    }



}