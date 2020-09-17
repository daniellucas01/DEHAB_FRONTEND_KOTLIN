package com.example.dehab.ui.transaction.deposit

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.dehab.Constants
import com.example.dehab.MainViewModel

import com.example.dehab.R
import com.example.dehab.databinding.FragmentDepositBinding
import com.example.dehab.model.ErrorResponseModel
import com.example.dehab.model.UserWalletByIdModel
import com.example.dehab.repository.KeyProviderApiRepository
import com.example.dehab.ui.transaction.TransactionFragmentDirections
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import retrofit2.Response

class DepositFragment() : Fragment() {

    private lateinit var mainViewModel : MainViewModel
    private lateinit var _binding: FragmentDepositBinding
    private val binding get() = _binding
    private lateinit var contractAddress : String
    private var apiRepository = KeyProviderApiRepository
    private val inputType = "deposit"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainViewModel = ViewModelProvider(requireActivity()).get(com.example.dehab.MainViewModel::class.java)
        _binding = FragmentDepositBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val depositUnit = arrayOf<String>("Ether","Gwei", "Wei")
        val adapter = ArrayAdapter<Any>(requireContext(), R.layout.drop_down_menu_item, depositUnit)
        mainViewModel.userId.observe(requireActivity(), Observer {
            setupContractAddress(it)
        })
        binding.depositUnitDropdown.setAdapter(adapter)
        binding.depositButton.setOnClickListener() {
            depositCrypto(inputType,view, contractAddress)
        }
    }

    private fun depositCrypto(transactionType : String,view : View, contract: String){
        if (!depositValidation()) {
            return
        }

        if (!validateAddress(contract)) {
            return
        }

        val amount = binding.depositAmountTextboxValue.text.toString()
        val unit = binding.depositUnitDropdown.text.toString()
        val action = TransactionFragmentDirections.transactionConfirmationDirection(transactionType, amount, unit, "", contract)
        view.findNavController().navigate(action)
    }

    private fun setupContractAddress(userId: Int){
        CoroutineScope(Dispatchers.IO).launch {
            contractAddress = async {getContractAddress(userId)}.await()
        }
    }

    private suspend fun getContractAddress(userId : Int) : String {
        val apiCall = apiRepository.getUserWalletById(
            userId
        )
        if (apiCall.isSuccessful) {
            Log.e(Constants.AUTHOR_NAME, "Wallet Address :" + apiCall.body()?.walletAddress.toString())
            return apiCall.body()?.walletAddress.toString()
        }
        else {
            withContext(Dispatchers.Main){
                handlingResponseCodes(apiCall)
            }
            return 0.toString()
        }
    }

    private fun handlingResponseCodes (response: Response<UserWalletByIdModel>) {
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

    private fun validateAddress(contract : String) : Boolean {
        if (contract == "1" || contract == "0") {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.error_label))
                .setMessage("Online Wallet not detected or initialized please try again later once you have good network coverage or have deployed the contract")
                .setPositiveButton(resources.getString(R.string.ok_label)) { dialog, which ->

                }
                .show()
            return false
        }
        return true
    }

    private fun depositValidation() : Boolean {
        var validation = true
        //Content
        val amount = binding.depositAmountTextboxValue.text
        val unit = binding.depositUnitDropdown.text.toString()
        //Outline
        val amountOutline = binding.depositAmountTextboxOutline
        val unitOutline = binding.depositUnitDropdownOutline
        // If password is empty
        if(amount.toString().isEmpty()) {
            amountOutline.error = "Please enter amount"
            validation = false
        }

        else {
            if (amount.toString().toDouble() == 0.0) {
                amountOutline.error = "Amount must not be ZERO"
                validation = false
            }
            else {
                amountOutline.error = null
            }
        }

        if (unit.isEmpty()){
            unitOutline.error = "Unit?"
            validation = false
        }

        else {
            unitOutline.error = null
        }

        return validation
    }
}
