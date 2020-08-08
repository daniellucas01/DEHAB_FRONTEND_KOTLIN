package com.example.dehab.ui.transaction.confirmation

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
import com.example.dehab.databinding.FragmentTransactionConfirmationBinding
import com.example.dehab.repository.WalletSingleton
import com.example.dehab.sol_contract_wrapper.MultiSignatureWallet
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.web3j.utils.Convert
import java.math.BigDecimal
import java.math.BigInteger

class TransactionConfirmationFragment : Fragment() {

    private lateinit var viewModel: TransactionConfirmationViewModel
    private lateinit var _binding: FragmentTransactionConfirmationBinding
    private val binding get() = _binding
    private val args: TransactionConfirmationFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTransactionConfirmationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val inputType = args.transactionType
        val amount = args.amount
        val unit = args.unit
        var weiAmount : BigInteger = BigInteger.ZERO
        if (unit != "Wei") {
            weiAmount = getWeiAmount(amount, unit).toBigInteger()
        }
        else {
            weiAmount = amount.toBigInteger()
        }
        val address = args.address
        setViewContent(inputType, amount, unit, address, weiAmount.toString())
        binding.confirmPaymentButton.setOnClickListener() {
            performTransaction(inputType, view, address, weiAmount)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TransactionConfirmationViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun setViewContent(type : String, amount: String, unit : String, address : String, weiAmount: String) {
        val transactionType = binding.transactionTypeValue
        val sender = binding.senderValue
        val receipient = binding.receipientValue
        val cryptoAmount = binding.cryptoAmountValue
        val realMoneyAmount = binding.realCurrencyValue
        if (type == "deposit") {
            transactionType.text = "Deposit"
            sender.text = "Offline Wallet"
            receipient.text = "Online Wallet"
            if (amount != "") {
                cryptoAmount.text = amount + " " + unit // Amount + Unit
                realMoneyAmount.text = fromWeiToUSD(weiAmount) // Convert amount and unit to USD
            }
        }
        else if (type == "transfer"){
            transactionType.text = "Transfer"
            sender.text = "Online Wallet"
            receipient.text = address
            if (amount != "") {
                cryptoAmount.text = amount + " " + unit // Amount + Unit
                realMoneyAmount.text = fromWeiToUSD(weiAmount) // Convert amount and unit to USD
            }
        }
        else {
            transactionType.text = ""
            sender.text = ""
            receipient.text = ""
            cryptoAmount.text = "" // Amount + Unit
            realMoneyAmount.text = "" // Convert wei amount and unit to USD using function
        }
    }

    private fun getWeiAmount(amount : String, unit: String) : BigDecimal {
        var mWeiAmount : BigDecimal = BigDecimal.ZERO
        when(unit){
            "Ether"     -> mWeiAmount = Convert.toWei(amount, Convert.Unit.ETHER)
            "Gwei"      -> mWeiAmount = Convert.toWei(amount, Convert.Unit.GWEI)
        }
        return mWeiAmount
    }

    private fun performTransaction(type : String, view: View, address: String, weiAmount: BigInteger) {
        when(type){
            "deposit"   -> initateDeposit(view, weiAmount)
            "transfer"  -> initiateTransfer(view, address, weiAmount)
            else        -> transactionError(view)
        }
    }

    private fun initateDeposit(view: View, weiAmount: BigInteger) {
        Log.e(Constants.AUTHOR_NAME, "Initiating deposit")
        CoroutineScope(IO).launch {
            MultiSignatureWallet.load(
                "0x3399289ce3c2197f5b16736ab238703e0ea80d9b",
                WalletSingleton.web3,
                WalletSingleton.walletCredentials,
                WalletSingleton.gasPrice,
                WalletSingleton.gasLimit
            ).deposit(weiAmount).send()
            withContext(Dispatchers.Main) {
                finishTransaction(view)
            }
        }
    }

    private fun initiateTransfer(view: View, address: String, weiAmount: BigInteger) {
        Log.e(Constants.AUTHOR_NAME, "Initiating transfer")
        CoroutineScope(IO).launch {
            MultiSignatureWallet.load(
                "0x3399289ce3c2197f5b16736ab238703e0ea80d9b",
                WalletSingleton.web3,
                WalletSingleton.walletCredentials,
                WalletSingleton.gasPrice,
                WalletSingleton.gasLimit
            ).transferTo(weiAmount, address).send()
            withContext(Dispatchers.Main) {
                finishTransaction(view)
            }
        }
    }

    private fun finishTransaction(view: View) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.transaction_finish_title))
            .setMessage(resources.getString(R.string.transaction_finish_message))
            .setPositiveButton(resources.getString(R.string.ok_label)) { dialog, which ->
                view.findNavController().navigateUp()
            }
            .show()
    }

    private fun transactionError(view: View) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.confirmation_transaction_error_title))
            .setMessage(resources.getString(R.string.confirmation_transaction_error_message))
            .setPositiveButton(resources.getString(R.string.ok_label)) { dialog, which ->
                val action = TransactionConfirmationFragmentDirections.finishingTransactionDirection()
                view.findNavController().navigate(action)
            }
            .show()
    }

    private fun fromWeiToEther(amount : String) : Double  {
        return Convert.fromWei(amount, Convert.Unit.ETHER).toDouble()
    }

    private fun fromWeiToUSD(weiAmount: String) : String {
        if (fromWeiToEther(weiAmount) < 0.0001) {
            return "$" + String.format(
                "%.6f",
                (fromWeiToEther(weiAmount)*238.88)
            )
        }
        else {
            return "$" + String.format(
                "%.2f",
                (fromWeiToEther(weiAmount)*238.88)
            )
        }
    }

}
