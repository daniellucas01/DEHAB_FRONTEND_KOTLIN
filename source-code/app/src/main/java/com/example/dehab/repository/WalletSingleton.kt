package com.example.dehab.repository

import com.example.dehab.Constants
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.tx.Contract
import org.web3j.tx.gas.ContractGasProvider
import org.web3j.tx.gas.DefaultGasProvider
import org.web3j.utils.Convert
import java.math.BigInteger

object WalletSingleton {
    private const val BASE_URL = "HTTP://192.168.0.109:7545"

    val web3 : Web3j by lazy {
        Web3j.build(HttpService(BASE_URL))
    }

    val gasLimit: BigInteger = Contract.GAS_LIMIT
    val gasPrice: BigInteger = Convert.toWei("70", Convert.Unit.GWEI).toBigInteger();
    private val privateKey = "bfc3a8b685b3fbc29f30e3ee316c1790096da8b87a41fbeb44f39a862275cf12"
    val walletCredentials = Credentials.create(privateKey)
}