package com.example.dehab

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.dehab.repository.WalletSingleton
import org.web3j.crypto.Credentials
import org.web3j.crypto.WalletUtils

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_history, R.id.navigation_transactions))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val model: MainViewModel by viewModels()
        viewModel = model
        val intent : Intent = intent
        val walletPassword = intent.getStringExtra("password")
        val walletDirectory = intent.getStringExtra("wallet_directory")
        val credentials1 = Credentials.create("f15fb2252bd1836dd7cad56089bf3f83228c4c881b031f855af47acb611a6adc")
        val credentials = WalletUtils.loadCredentials(walletPassword, walletDirectory)
        viewModel.setWalletCredentials(WalletSingleton.walletCredentials)
    }
}
