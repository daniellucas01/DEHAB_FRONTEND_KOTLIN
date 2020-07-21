package com.example.dehab.ui.home.wallet_card

class WalletCardItems {
    var walletLabel: String = ""
    var etherCount: Double = 0.00
    var moneyCount: Double = 0.00

    constructor(walletLabel: String,etherCount: Double, money_count: Double) {
        this.walletLabel = walletLabel
        this.etherCount = etherCount
        this.moneyCount= money_count
    }
}