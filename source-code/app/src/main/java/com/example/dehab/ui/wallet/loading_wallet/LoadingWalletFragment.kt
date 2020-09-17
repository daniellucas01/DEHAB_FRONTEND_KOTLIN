package com.example.dehab.ui.wallet.loading_wallet

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.dehab.Constants
import com.example.dehab.MainActivity
import com.example.dehab.R
import com.example.dehab.databinding.LoadingWalletFragmentBinding
import com.example.dehab.model.ErrorResponseModel
import com.example.dehab.model.UserModel
import com.example.dehab.repository.KeyProviderApiRepository
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.web3j.crypto.CipherException
import org.web3j.crypto.Credentials
import org.web3j.crypto.WalletUtils
import retrofit2.Response


class LoadingWalletFragment : Fragment() {

    companion object {
        fun newInstance() = LoadingWalletFragment()
    }

    private val args: LoadingWalletFragmentArgs by navArgs()
    private lateinit var viewModel: LoadingWalletViewModel
    private lateinit var _binding : LoadingWalletFragmentBinding
    private val binding get() = _binding
    private var apiRepository = KeyProviderApiRepository
    private lateinit var walletDirectory : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LoadingWalletFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        walletDirectory = args.expectedWalletDirectory
        binding.newUserButton.setOnClickListener() {
            val action = LoadingWalletFragmentDirections.CreateNewUserDirection()
            view.findNavController().navigate(action)
        }
        binding.loginButton.setOnClickListener() {
            loadWallet(
                binding.loginUsernameTextbox.text.toString(),
                binding.loginPasswordTextbox.text.toString(),
                walletDirectory
            )
        }
    }

    private fun loadWallet (username : String, password: String ,walletDirectory: String) {

        if (!formValidation()) {
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            //Authenticate User using API
            val userAuth = apiRepository.loginUser(
                username,
                password
            )
            // Then try to decrypt wallet
            withContext(Dispatchers.Main) {
                if (!userAuth.isSuccessful) {
                    handlingResponseCodes(userAuth)
                }

                if (userAuth.isSuccessful) {
                    val userId = userAuth.body()!!.userId
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    intent.putExtra("wallet_directory",walletDirectory)
                    intent.putExtra("userId", userId.toString())
                    startActivity(intent)
                }
            }
        }
    }


    private fun handlingResponseCodes (response: Response<UserModel>) {
        val gson = Gson()
        val type = object : TypeToken<ErrorResponseModel>() {}.type
        val errorResponse: ErrorResponseModel = gson.fromJson(response.errorBody()!!.charStream(), type)
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(response.code().toString() + " " + resources.getString(R.string.error_label))
            .setMessage(errorResponse.message)
            .setPositiveButton(resources.getString(R.string.ok_label)) { dialog, which ->

            }
            .show()
    }

    @SuppressLint("SetTextI18n")
//    private suspend fun decryptWallet(walletDirectory : String) : Boolean {
//        //ToDo : Wallet Password Validation
//        //ToDo : Load Wallet
//        //ToDo : Make it asynchronous
//        try {
//            val password = binding.loginWalletPasswordTextbox.text.toString()
//            val credentials : Credentials = WalletUtils.loadCredentials(password, walletDirectory)
//            withContext(Dispatchers.Main) {
//                binding.privateAddressLabel.text = credentials.ecKeyPair.privateKey.toString(16)
//            }
//            return true
//        }
//        catch (error: CipherException) {
//            withContext(Dispatchers.Main) {
//                MaterialAlertDialogBuilder(requireContext())
//                    .setTitle(resources.getString(R.string.invalid_wallet_password_title))
//                    .setMessage(resources.getString(R.string.invalid_wallet_password))
//                    .setPositiveButton(resources.getString(R.string.ok_label)) { dialog, which ->
//
//                    }
//                    .show()
//            }
//            return false
//        }
//
//
//    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoadingWalletViewModel::class.java)

    }

    private fun formValidation() : Boolean {
        var validation = true
        //Content
        val username = binding.loginUsernameTextbox.text.toString()
        val password = binding.loginPasswordTextbox.text.toString()
        //Outline
        val usernameOutline = binding.loginUsernameTextboxOutline
        val passwordOutline = binding.loginPasswordTextboxOutline
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
        return validation
    }

}
