package com.example.dehab

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.dehab.repository.WalletSingleton
import com.example.dehab.ui.wallet.WalletActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
        val userId = intent.getStringExtra("userId")
        viewModel.setUserId(userId.toInt())
        viewModel.setWalletCredentials(WalletSingleton.walletCredentials)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.log_out_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.log_out_button -> {
                MaterialAlertDialogBuilder(this)
                    .setTitle(resources.getString(R.string.log_dialog_title))
                    .setMessage(resources.getString(R.string.log_dialog_message))
                    .setPositiveButton(resources.getString(R.string.ok_label)) { dialog, which ->
                        val intent = Intent(this, WalletActivity::class.java)
                        startActivity(intent)
                    }
                    .setNegativeButton(resources.getString(R.string.cancel_label)){ dialog, which ->

                    }
                    .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
