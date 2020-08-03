package com.example.dehab

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.dehab.model.WalletDataModel
import com.example.dehab.repository.WalletRepository
import org.web3j.crypto.Credentials
import java.math.BigInteger

class MainViewModel() : ViewModel() {
    private val _walletCredentials : MutableLiveData <Credentials> = MutableLiveData()


    val walletData : LiveData<WalletDataModel> = Transformations.switchMap(_walletCredentials) {walletCredentials ->
        WalletRepository.getWalletEther(walletCredentials)
    }

    val privateKey : LiveData<BigInteger> = Transformations.switchMap(_walletCredentials) {walletCredentials ->
        WalletRepository.getPrivateKey(walletCredentials)
    }

    fun setWalletCredentials (credentials : Credentials) {
        if (_walletCredentials.value == credentials) {
            return
        }
        _walletCredentials.value = credentials
    }

    fun getWalletCredentials () : Credentials? {
        if (_walletCredentials.value == null) {
            return null
        }
        return _walletCredentials.value;
    }
}