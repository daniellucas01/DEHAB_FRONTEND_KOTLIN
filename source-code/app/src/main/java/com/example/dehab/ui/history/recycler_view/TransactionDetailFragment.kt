package com.example.dehab.ui.history.recycler_view

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.dehab.Constants
import com.example.dehab.R
import com.example.dehab.databinding.FragmentTransactionDetailBinding
import com.example.dehab.model.ErrorResponseModel
import com.example.dehab.model.TransactionSignModel
import com.example.dehab.repository.KeyProviderApiRepository
import com.example.dehab.repository.WalletSingleton
import com.example.dehab.sol_contract_wrapper.MultiSignatureWallet
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.web3j.utils.Convert
import retrofit2.Response
import java.lang.RuntimeException
import java.math.BigInteger

class TransactionDetailFragment : Fragment() {

    private lateinit var viewModel: TransactionDetailViewModel
    private lateinit var _binding: FragmentTransactionDetailBinding
    private val binding get() = _binding
    private var apiRepository = KeyProviderApiRepository
    private val args: TransactionDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTransactionDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TransactionDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val transactionNumber = args.transactionNumber
        val contractAddress = args.contract

        CoroutineScope(Dispatchers.IO).launch {
            try{
                val walletContract = MultiSignatureWallet.load(
                    contractAddress,
                    WalletSingleton.web3,
                    WalletSingleton.walletCredentials,
                    WalletSingleton.gasPrice,
                    WalletSingleton.gasLimit
                )
                val transaction = walletContract.getPendingTransactionInformation(transactionNumber.toBigInteger()).send()
                withContext(Dispatchers.Main) {
                    if (transaction != null) {
                        Log.e(Constants.AUTHOR_NAME, transaction.toString())
                        binding.transactionSenderValue.text = transaction.value1.toString()
                        binding.transactionRecipientValue.text = transaction.value2.toString()
                        binding.transactionAmountValue.text = convertWeiToEther(transaction.value3)
                        binding.transactionSignatureValue.text = transaction.value4.toString()
                    }
                }
            }
            catch (e : RuntimeException) {
                withContext(Dispatchers.Main) {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(resources.getString(R.string.loading_transaction_error_title))
                        .setMessage(resources.getString(R.string.loading_transaction_error_message))
                        .setPositiveButton(resources.getString(R.string.ok_label)) { dialog, which ->
                        }
                        .show()
                }
            }

        }

        //Buttons
        binding.signPaymentButton.setOnClickListener() {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val walletContract = MultiSignatureWallet.load(
                        contractAddress,
                        WalletSingleton.web3,
                        WalletSingleton.walletCredentials,
                        WalletSingleton.gasPrice,
                        WalletSingleton.gasLimit
                    )
                    val transaction = walletContract.signTransaction(transactionNumber.toBigInteger()).send()
                    withContext(Main){
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle(resources.getString(R.string.signature_success_title))
                            .setMessage(resources.getString(R.string.signature_success_message))
                            .setPositiveButton(resources.getString(R.string.ok_label)) { dialog, which ->
                            }
                            .show()
                    }
                }
                catch (e : RuntimeException){
                    withContext(Main){
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle(resources.getString(R.string.signature_error_title))
                            .setMessage(resources.getString(R.string.signature_error_message))
                            .setPositiveButton(resources.getString(R.string.ok_label)) { dialog, which ->
                            }
                            .show()
                    }
                }
            }
        }

        binding.deleteTransactionButton.setOnClickListener(){
            deleteTransactionConfirmation(contractAddress, transactionNumber, view)
        }

        binding.requestSignatureButton.setOnClickListener(){
            requestSignatureFromAPI(contractAddress, transactionNumber, view)
        }
    }

    private fun deleteTransactionConfirmation(address : String, number : Int, view : View) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.delete_transaction_confirmation_title))
            .setMessage(resources.getString(R.string.delete_transaction_confirmation_message))
            .setPositiveButton(resources.getString(R.string.ok_label)) { dialog, which ->
                attemptingToDeleteTransaction(address, number, view)
            }
            .setNegativeButton(resources.getString(R.string.cancel_label)) { dialog, which ->
                //Do nothing
            }
            .show()
    }

    private fun attemptingToDeleteTransaction(address : String, number : Int, view : View) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val walletContract = MultiSignatureWallet.load(
                    address,
                    WalletSingleton.web3,
                    WalletSingleton.walletCredentials,
                    WalletSingleton.gasPrice,
                    WalletSingleton.gasLimit
                )
                val transaction = walletContract.deleteTransaction(number.toBigInteger()).send()
                withContext(Main){
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(resources.getString(R.string.delete_transaction_finish_title))
                        .setMessage(resources.getString(R.string.delete_transaction_finish_message))
                        .setPositiveButton(resources.getString(R.string.ok_label)) { dialog, which ->
                            view.findNavController().navigateUp()
                        }
                        .show()
                }
            }
            catch (e : RuntimeException){
                withContext(Main){
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(resources.getString(R.string.delete_transaction_error_title))
                        .setMessage(resources.getString(R.string.delete_transaction_error_message))
                        .setPositiveButton(resources.getString(R.string.ok_label)) { dialog, which ->
                            //Do nothing
                            Log.e(Constants.AUTHOR_NAME, e.toString())
                        }
                        .show()
                }
            }
        }
    }

    private fun requestSignatureFromAPI(address : String, number : Int, view : View) {
        CoroutineScope(Dispatchers.IO).launch {
            val sign = apiRepository.signTransaction(
                TransactionSignModel(
                    address,
                    number
                )
            )
            withContext(Main) {
                if (sign.isSuccessful) {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(resources.getString(R.string.request_signature_success_title))
                        .setMessage(resources.getString(R.string.request_signature_success_message))
                        .setPositiveButton(resources.getString(R.string.ok_label)) { dialog, which ->
                        }
                        .show()
                }
                else {
                    handlingResponseCodes(sign)
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

    private fun convertWeiToEther(number : BigInteger) : String {
        val etherAmount = Convert.fromWei(number.toString(), Convert.Unit.ETHER)
        return String.format(
            "%.2f",
            etherAmount) + " ETH"
    }
}