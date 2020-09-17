package com.example.dehab.ui.wallet

import android.Manifest
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import com.example.dehab.R

import com.example.dehab.databinding.FragmentStartingBinding
import com.example.dehab.ui.CustomLoadingDialog
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_starting.view.*
import org.web3j.crypto.WalletUtils
import java.io.File

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

            //ToDo : Check if the wallet file exist

            //ToDo : If not exist go to create page

//            val action = StartingFragmentDirections.walletCreationDirection()
//            view.findNavController().navigate(action)

            //Process
        }
        binding.loadWalletButton.setOnClickListener() {
            //Animation Start
            val folderName = "Eth_Wallet"
            val walletFolderDirectory = File(Environment.getExternalStorageDirectory(), "/$folderName")
            permissionsBuilder(Manifest.permission.WRITE_EXTERNAL_STORAGE).build().send { result ->
                if (result.allGranted()) {
                    prepareLoadWallet(walletFolderDirectory, view)
                }
            }
        }
    }

    private fun prepareLoadWallet(walletDirectory: File, view: View) {
        var name = ""
        var mWalletDirectory = File(walletDirectory.toString())
        val contents : Array<File>? = walletDirectory.listFiles()
        if (contents == null) {
            //The directory is not really a directory
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.error_label))
                .setMessage(resources.getString(R.string.no_wallet_file_label))
                .setPositiveButton(resources.getString(R.string.ok_label)) { dialog, which ->

                }
                .show()
            return
        }
        for (f in walletDirectory.listFiles()) {
            if (f.isFile) {
                name = f.name
            }
        }
        if (name != "") {
            mWalletDirectory = File(walletDirectory, "/$name")
            // ToDO : Pass the wallet directory
            val expectedDirectory = mWalletDirectory.toString()
//            val action = StartingFragmentDirections.loadingWalletDirection(expectedDirectory)
//            view.findNavController().navigate(action)
        }
        else {
            //File is empty or no wallet file
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.error_label))
                .setMessage(resources.getString(R.string.no_wallet_file_label))
                .setPositiveButton(resources.getString(R.string.ok_label)) { dialog, which ->
//                    val action = StartingFragmentDirections.walletCreationDirection()
//                    view.findNavController().navigate(action)
                }
                .show()
        }
    }
}
