package com.example.dehab.ui.transaction.transfer

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
import com.example.dehab.databinding.FragmentTransferBinding
import com.example.dehab.model.ErrorResponseModel
import com.example.dehab.model.UserWalletByIdModel
import com.example.dehab.repository.KeyProviderApiRepository
import com.example.dehab.repository.WalletSingleton
import com.example.dehab.sol_contract_wrapper.MultiSignatureWallet
import com.example.dehab.ui.home.wallet_card.WalletCardAdapter
import com.example.dehab.ui.home.wallet_card.WalletCardItems
import com.example.dehab.ui.transaction.TransactionFragmentDirections
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_transfer.view.*
import kotlinx.coroutines.*
import org.web3j.utils.Convert
import retrofit2.Response

class TransferFragment : Fragment() {

    private lateinit var mainViewModel : MainViewModel
    private lateinit var _binding: FragmentTransferBinding
    private lateinit var contractAddress : String
    private var apiRepository = KeyProviderApiRepository
    private val binding get() = _binding
    private val inputType = "transfer"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainViewModel = ViewModelProvider(requireActivity()).get(com.example.dehab.MainViewModel::class.java)
        _binding = FragmentTransferBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val transferUnit = arrayOf<String>("Ether","Gwei", "Wei")
        val adapter = ArrayAdapter<Any>(requireContext(), R.layout.drop_down_menu_item, transferUnit)
        mainViewModel.userId.observe(requireActivity(), Observer {
            setupContractAddress(it)
        })
        binding.transferUnitDropdown.setAdapter(adapter)
        binding.transferButton.setOnClickListener() {
            transferCrypto(inputType,view,contractAddress)
        }
    }

    private fun setupContractAddress(userId: Int){
        CoroutineScope(Dispatchers.IO).launch {
            contractAddress = async {getContractAddress(userId)}.await()
        }
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

    private fun transferCrypto(transactionType : String,view : View , contract : String )  {

        if (!transferValidation()) {
            return
        }

        if (!validateAddress(contract)) {
            return
        }

        val amount = binding.transferAmountTextboxValue.text.toString()
        val unit = binding.transferUnitDropdown.text.toString()
        val address = binding.transferAddressValue.text.toString()
        //validate contract address
        val action = TransactionFragmentDirections.transactionConfirmationDirection(transactionType, amount, unit, address, contract)
        view.findNavController().navigate(action)
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

    private fun transferValidation() : Boolean {
        var validation = true
        //Content
        val transferAddress = binding.transferAddressValue.text.toString()
        val amount = binding.transferAmountTextboxValue.text
        val unit = binding.transferUnitDropdown.text.toString()
        //Outline
        val transferOutline = binding.transferAddressOutline
        val amountOutline = binding.transferAmountTextboxOutline
        val unitOutline = binding.transferUnitDropdownOutline
        // If transfer address is empty
        if(transferAddress.isEmpty()) {
            transferOutline.error = "Please fill in the address"
            validation = false
        }
        else {
            // If transfer address is not 40 characters
            if (transferAddress.length < 40) {
                transferOutline.error = "Address is at least 40 characters"
                validation = false
            }
            else {
                transferOutline.error = null
            }
        }
        // If amount is empty
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
