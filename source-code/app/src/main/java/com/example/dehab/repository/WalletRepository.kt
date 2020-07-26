package com.example.dehab.repository

import androidx.lifecycle.LiveData
import com.example.dehab.model.WalletDataModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import org.web3j.crypto.Credentials
import org.web3j.crypto.ECKeyPair
import org.web3j.crypto.WalletUtils
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.utils.Convert
import java.math.BigInteger

object WalletRepository {
    var job : CompletableJob? = null

    fun getWalletEther (credentials : Credentials) : LiveData<WalletDataModel> {
        job = Job()
        return object : LiveData<WalletDataModel>() {
            override fun onActive() {
                super.onActive()
                job?.let {theJob ->
                    CoroutineScope(IO + theJob).launch {
                        val web3 = WalletSingleton.web3
                        val address = credentials.address
                        val balance = web3.ethGetBalance(address, DefaultBlockParameterName.LATEST).sendAsync().get()
                        withContext(Main) {
                            val wei = balance.balance.toBigDecimal()
                            value = WalletDataModel(
                                ether_count = Convert.fromWei(wei, Convert.Unit.ETHER)
                            )
                            theJob.complete()
                        }
                    }
                }
            }
        }
    }

    fun loadWalletCredentials (password : String, directory: String) : Credentials {
        return WalletUtils.loadCredentials(password, directory)
    }

    fun getWalletAddress(credentials: Credentials) : String {
        return credentials.address
    }

    fun getWalletECKeyPair(credentials: Credentials) : ECKeyPair {
        return credentials.ecKeyPair
    }

    fun getWalletPrivateKey(credentials: Credentials) : BigInteger {
        return credentials.ecKeyPair.privateKey
    }

    fun getWalletPublicKey(credentials: Credentials) : BigInteger {
        return credentials.ecKeyPair.privateKey
    }
}