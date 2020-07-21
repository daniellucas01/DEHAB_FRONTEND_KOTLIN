package com.example.dehab.ui.wallet

import android.Manifest
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.dehab.R
import com.example.dehab.databinding.FragmentWalletCreationBinding
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.web3j.crypto.WalletUtils
import java.io.File

class WalletCreationFragment : Fragment() {
    private lateinit var _binding : FragmentWalletCreationBinding
    private val binding get() = _binding
    companion object {
        fun newInstance() = WalletCreationFragment()
    }

    private lateinit var viewModel: WalletCreationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWalletCreationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val folderName = "Eth_Wallet"
        binding.createWalletButton.setOnClickListener() {
            val walletPassword = binding.walletPasswordTextbox.text.toString()
            permissionsBuilder(Manifest.permission.WRITE_EXTERNAL_STORAGE).build().send { result ->

                if (result.allGranted()) {
                    var walletDirectory = File(Environment.getExternalStorageDirectory(), "/$folderName")
                    createFolder(walletDirectory)
                    try {
                        val ethereumWallet = generateWallet(walletDirectory, walletPassword, view)
                        walletDirectory = File(walletDirectory, "/$ethereumWallet")
                        val action = WalletCreationFragmentDirections.fromCreationToLoadingDirection(walletDirectory.toString())
                        view.findNavController().navigate(action)
                    }
                    catch (error : Exception) {
                        MaterialAlertDialogBuilder(context)
                            .setTitle(resources.getString(R.string.error_label))
                            .setMessage(error.toString())
                            .setPositiveButton(resources.getString(R.string.ok_label)) { dialog, which ->

                            }
                            .show()
                    }
                }
            }
        }
    }

    private fun generateWallet(walletDirectory: File , password : String, view: View) : String {
        var name = ""
        val contents : Array<File>? = walletDirectory.listFiles()
        if (contents == null) {
            //The directory is not really a directory
            MaterialAlertDialogBuilder(context)
                .setTitle(resources.getString(R.string.error_label))
                .setMessage(resources.getString(R.string.invalid_directory))
                .setPositiveButton(resources.getString(R.string.ok_label)) { dialog, which ->

                }
                .show()
        }
        for (f in walletDirectory.listFiles()) {
            if (f.isFile) {
                name = f.name
            }
        }
        if (name != "") {
            MaterialAlertDialogBuilder(context)
                .setTitle(resources.getString(R.string.wallet_exists_title))
                .setMessage(resources.getString(R.string.existing_wallet_message))
                .setPositiveButton(resources.getString(R.string.ok_label)) { dialog, which ->

                }
                .show()
            return name
        }
        else {
            val mWalletDirectory = WalletUtils.generateLightNewWalletFile(password, walletDirectory)
            return mWalletDirectory
        }
    }

    private fun createFolder(folder: File) {
        if (!folder.exists()) {
            Log.d("Daniel", "Folder doesn't exist, creating folder")
            //Create folder if it does not exists
            folder.mkdir()
        } else {
            Log.d("Daniel", "Folder exist")
        }
    }
}
