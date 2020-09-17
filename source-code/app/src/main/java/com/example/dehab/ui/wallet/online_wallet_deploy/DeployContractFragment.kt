package com.example.dehab.ui.wallet.online_wallet_deploy

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.dehab.Constants
import com.example.dehab.MainViewModel
import com.example.dehab.R
import com.example.dehab.databinding.FragmentDeployContractBinding
import com.example.dehab.model.ErrorResponseModel
import com.example.dehab.model.UpdateUserWalletDataModel
import com.example.dehab.repository.KeyProviderApiRepository
import com.example.dehab.repository.WalletSingleton
import com.example.dehab.sol_contract_wrapper.MultiSignatureWallet
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.web3j.crypto.CipherException
import org.web3j.crypto.Credentials
import retrofit2.Response
import java.io.IOException
import java.lang.RuntimeException

class DeployContractFragment : Fragment() {

    private lateinit var mainViewModel : MainViewModel
    private lateinit var _binding : FragmentDeployContractBinding
    private val binding get() = _binding
    private var apiRepository = KeyProviderApiRepository
    private lateinit var walletAddress: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        _binding = FragmentDeployContractBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.deployContractButton.setOnClickListener() {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.deploy_contract_authentication_title))
                .setMessage(resources.getString(R.string.deploy_contract_authentication_message))
                .setPositiveButton(resources.getString(R.string.ok_label)) { dialog, which ->
                    mainViewModel.userId.observe(requireActivity(), Observer {
                        deployContract(it)
                    })
                }
                .setNegativeButton(resources.getString(R.string.cancel_label)) { dialog, which ->
                    //Do Nothing
                }
                .show()
        }
    }

    private fun deployContract(userId : Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val address1 = "0x7919e12AfBae378f21659A4e2914a2B493211Fa3"
                val address2 = "0x62e5B50731d5F212A1847a694305A82Bb4135a9c"
                val contract = MultiSignatureWallet.deploy(
                    WalletSingleton.web3,
                    WalletSingleton.walletCredentials,
                    WalletSingleton.gasPrice,
                    WalletSingleton.gasLimit,
                    address1,
                    address2
                ).send()
                Log.e(Constants.AUTHOR_NAME, contract.contractAddress)
                walletAddress = contract.contractAddress
                val apiCall = apiRepository.updateUserWalletById(
                    UpdateUserWalletDataModel(
                        userId,
                        walletAddress
                    )
                )
                withContext(Dispatchers.Main){
                    if (apiCall.isSuccessful) {
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle(resources.getString(R.string.deploy_contract_success_title))
                            .setMessage(resources.getString(R.string.deploy_contract_success_message))
                            .setPositiveButton(resources.getString(R.string.ok_label)) { dialog, which ->
                            }
                            .show()
                    }
                    else {
                        handlingResponseCodes(apiCall)
                    }
                }
            }
            catch (e : RuntimeException){
                withContext(Dispatchers.Main){
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(resources.getString(R.string.deploy_contract_failure_title))
                        .setMessage(resources.getString(R.string.deploy_contract_failure_message))
                        .setPositiveButton(resources.getString(R.string.ok_label)) { dialog, which ->
                        }
                        .show()
                }
            }
        }
    }

    private fun handlingResponseCodes (response: Response<Void>) {
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
}