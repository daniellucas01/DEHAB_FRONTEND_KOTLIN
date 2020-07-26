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
import com.example.dehab.Constants
import com.example.dehab.R
import com.example.dehab.databinding.FragmentWalletCreationBinding
import com.example.dehab.model.NewUserModel
import com.example.dehab.repository.KeyProviderApiRepository
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.web3j.crypto.WalletUtils
import java.io.File

class WalletCreationFragment : Fragment() {
    private lateinit var _binding : FragmentWalletCreationBinding
    private val binding get() = _binding
    private var apiRepository = KeyProviderApiRepository

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
        binding.createWalletButton.setOnClickListener() {
            permissionsBuilder(Manifest.permission.WRITE_EXTERNAL_STORAGE).build().send { result ->
                if (result.allGranted()) {
                    val walletDirectory = File(Environment.getExternalStorageDirectory(), "/${Constants.folderName}")
                    createFolder(walletDirectory)
                    userSignUp(
                        binding.usernameTextbox.text.toString(),
                        binding.passwordTextBox.text.toString(),
                        binding.walletPasswordTextbox.text.toString(),
                        walletDirectory,
                        view
                    )
//                    try {
//                        val ethereumWallet = generateWallet(walletDirectory, walletPassword, view)
//                        walletDirectory = File(walletDirectory, "/$ethereumWallet")
//                        val action = WalletCreationFragmentDirections.fromCreationToLoadingDirection(walletDirectory.toString())
//                        view.findNavController().navigate(action)
//                    }
//                    catch (error : Exception) {
//                        MaterialAlertDialogBuilder(requireContext())
//                            .setTitle(resources.getString(R.string.error_label))
//                            .setMessage(error.toString())
//                            .setPositiveButton(resources.getString(R.string.ok_label)) { dialog, which ->
//
//                            }
//                            .show()
//                    }
                }
            }
        }
    }

    private fun userSignUp (username: String, password: String, walletPassword : String, walletDirectory: File, view: View) {

        if (!formValidation()) {
            return
        }
        else {
            CoroutineScope(IO).launch {
                val register = apiRepository.registerUser(
                    NewUserModel(
                        username,
                        password,
                        1
                    )
                )
                withContext(Main) {
                    if (register) {
                        Log.d("Daniel", "Register succesfull")
                    }
                    else {
                        Log.d("Daniel", "Register failure")
                    }
                }
            }
        }
//        try {
//            val ethereumWallet = generateWallet(walletDirectory, walletPassword, view)
//            val newWalletDirectory = File(walletDirectory, "/$ethereumWallet")
//            val action = WalletCreationFragmentDirections.fromCreationToLoadingDirection(newWalletDirectory.toString())
//            view.findNavController().navigate(action)
//        }
//        catch (error : Exception) {
//            MaterialAlertDialogBuilder(requireContext())
//                .setTitle(resources.getString(R.string.error_label))
//                .setMessage(error.toString())
//                .setPositiveButton(resources.getString(R.string.ok_label)) { dialog, which ->
//
//                }
//                .show()
//        }
    }

    private fun formValidation() : Boolean {
        var validation = true
        //Content
        val username = binding.usernameTextbox.text.toString()
        val password = binding.passwordTextBox.text.toString()
        val walletPassword = binding.walletPasswordTextbox.text.toString()
        //Outline
        val usernameOutline = binding.usernameTextboxOutline
        val passwordOutline = binding.passwordTextBoxOutline
        val walletPasswordOutline = binding.walletPasswordTextboxOutline
        // If username is empty
        if(username.isEmpty()) {
            usernameOutline.error = "Please fill in your username"
            validation = false
        }
        else {
            usernameOutline.error = null
        }
        // If password is empty
        if(password.isEmpty()) {
            passwordOutline.error = "Please fill in your password"
            validation = false
        }

        else {
            passwordOutline.error = null
        }
        // If wallet is empty
        if(walletPassword.isEmpty()) {
            walletPasswordOutline.error = "Please fill in your wallet password"
            validation = false
        }
        else {
            walletPasswordOutline.error = null
        }



        return validation
    }

    private fun generateWallet(walletDirectory: File , password : String, view: View) : String {
        var name = ""
        val contents : Array<File>? = walletDirectory.listFiles()
        if (contents == null) {
            //The directory is not really a directory
            MaterialAlertDialogBuilder(requireContext())
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
            MaterialAlertDialogBuilder(requireContext())
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
