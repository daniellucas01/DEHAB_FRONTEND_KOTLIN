package com.example.dehab.ui.wallet

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.dehab.R

import com.example.dehab.databinding.FragmentStartingBinding
import com.example.dehab.ui.CustomLoadingDialog
import kotlinx.android.synthetic.main.fragment_starting.view.*

class StartingFragment : Fragment() {
    private lateinit var _binding : FragmentStartingBinding
    private val binding get() = _binding
    private lateinit var loadingDialog: CustomLoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStartingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = CustomLoadingDialog(requireActivity())
        binding.createWalletButton.setOnClickListener() {
            //Animation
            Log.e("Daniel", "Animation Starting")
            loadingDialog.startLoadingDialog()
            //Process
        }
        binding.loadWalletButton.setOnClickListener() {
            //Animation Start
            Log.e("Daniel", "Animation Starting")
            //Process
        }
    }

    private fun createWallet() {
        //Create Wallet
        //Load Wallet
    }

    private fun LoadWallet() {
        //Load Wallet on Default Path
    }
}
