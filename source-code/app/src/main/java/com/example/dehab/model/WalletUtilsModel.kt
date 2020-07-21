package com.example.dehab.model

import android.inputmethodservice.Keyboard
import org.web3j.abi.datatypes.Address

data class WalletUtilsModel(
    var file_location: String = "",
    var address: Address,
    var walletPrivateKey : String = "",
    var walletPassword : String = ""
)