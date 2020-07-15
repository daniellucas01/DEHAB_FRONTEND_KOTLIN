package com.example.dehab.ui.wallet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.dehab.R

class WalletActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)
        if(savedInstanceState == null) {
            val f1 : StartingFragment = StartingFragment()
            val fragmentTransaction : FragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.wallet_fragment_placeholder , f1)
            fragmentTransaction.commit()
        }

    }
}