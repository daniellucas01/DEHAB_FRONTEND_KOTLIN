package com.example.dehab.ui.transaction.transfer

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.findNavController
import com.example.dehab.Constants

import com.example.dehab.R
import com.example.dehab.databinding.FragmentTransferBinding
import com.example.dehab.ui.transaction.TransactionFragmentDirections
import kotlinx.android.synthetic.main.fragment_transfer.view.*

class TransferFragment : Fragment() {

    companion object {
        fun newInstance() =
            TransferFragment()
    }
    private lateinit var viewModel: TransferViewModel
    private lateinit var _binding: FragmentTransferBinding
    private val binding get() = _binding
    private val inputType = "transfer"

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
            transferCrypto(inputType,view)
        }
    }

    private fun transferCrypto(transactionType : String,view : View)  {
//        if (!transferValidation()) {
//            return
//        }
        val amount = binding.transferAmountTextboxValue.text.toString()
        val unit = binding.transferUnitDropdown.text.toString()
        val address = binding.transferAddressValue.text.toString()
        val action = TransactionFragmentDirections.transactionConfirmationDirection(transactionType, amount, unit, address)
        view.findNavController().navigate(action)
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
