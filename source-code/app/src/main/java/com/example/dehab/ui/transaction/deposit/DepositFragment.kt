package com.example.dehab.ui.transaction.deposit

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
import com.example.dehab.databinding.FragmentDepositBinding
import com.example.dehab.ui.transaction.TransactionFragmentDirections

class DepositFragment() : Fragment() {

    private lateinit var viewModel: DepositViewModel
    private lateinit var _binding: FragmentDepositBinding
    private val binding get() = _binding
    private val inputType = "deposit"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDepositBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DepositViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val depositUnit = arrayOf<String>("Ether","Gwei", "Wei")
        val adapter = ArrayAdapter<Any>(requireContext(), R.layout.drop_down_menu_item, depositUnit)

        binding.depositUnitDropdown.setAdapter(adapter)
        binding.depositButton.setOnClickListener() {
            depositCrypto(inputType,view)
        }
    }

    private fun depositCrypto(transactionType : String,view : View){
//        if (!depositValidation()) {
//            return
//        }
        val amount = binding.depositAmountTextboxValue.text.toString()
        val unit = binding.depositUnitDropdown.text.toString()
        val action = TransactionFragmentDirections.transactionConfirmationDirection(transactionType, amount, unit, "")
        view.findNavController().navigate(action)
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

    // TODO: Form validation
    // TODO: After form form validation, confirmation fragment init
}
