package com.example.dehab.ui.transaction.transfer

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.dehab.Constants

import com.example.dehab.R
import com.example.dehab.databinding.FragmentTransferBinding

class TransferFragment : Fragment() {

    companion object {
        fun newInstance() =
            TransferFragment()
    }
    private lateinit var viewModel: TransferViewModel
    private lateinit var _binding: FragmentTransferBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTransferBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TransferViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val transferUnit = arrayOf<String>("Ether","Gwei", "Wei")
        val adapter = ArrayAdapter<Any>(requireContext(), R.layout.drop_down_menu_item, transferUnit)

        binding.transferUnitDropdown.setAdapter(adapter)
        binding.transferButton.setOnClickListener() {
            transferCrypto()
        }
    }

    private fun transferCrypto()  {
        if (!transferValidation()) {
            return
        }

        Log.e(Constants.AUTHOR_NAME, "Yay you can transfer")
    }

    private fun transferValidation() : Boolean {
        var validation = true
        //Content
        val transferAddress = binding.transferAddressValue.text.toString()
        val amount = binding.transferAmountTextboxValue.text
        //Outline
        val transferOutline = binding.transferAddressOutline
        val amountOutline = binding.transferAmountTextboxOutline
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

        return validation
    }
}
