package com.example.dehab.ui.wallet.loading_wallet

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.example.dehab.MainActivity
import com.example.dehab.R
import com.example.dehab.databinding.LoadingWalletFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.web3j.crypto.CipherException
import org.web3j.crypto.Credentials
import org.web3j.crypto.WalletUtils


class LoadingWalletFragment : Fragment() {

    companion object {
        fun newInstance() = LoadingWalletFragment()
    }

    private val args: LoadingWalletFragmentArgs by navArgs()
    private lateinit var viewModel: LoadingWalletViewModel
    private lateinit var _binding : LoadingWalletFragmentBinding
    private val binding get() = _binding
    private lateinit var walletDirectory : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LoadingWalletFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        walletDirectory = args.expectedWalletDirectory
        binding.walletDirectoryLabel.text = walletDirectory
        binding.decryptWalletButton.setOnClickListener() {
            decryptWallet(walletDirectory)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun decryptWallet(walletDirectory : String) {
        //ToDo : Wallet Password Validation
        //ToDo : Load Wallet
        //ToDo : Make it asynchronous
        try {
            val password = binding.loginWalletPasswordTextbox.text.toString()
            val credentials : Credentials = WalletUtils.loadCredentials(password, walletDirectory)
            binding.privateAddressLabel.text = credentials.ecKeyPair.privateKey.toString(16)
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.putExtra("wallet_directory",walletDirectory)
            intent.putExtra("password", password)

            startActivity(intent)
        }
        catch (error: CipherException) {
            MaterialAlertDialogBuilder(context)
                .setTitle(resources.getString(R.string.invalid_wallet_password_title))
                .setMessage(resources.getString(R.string.invalid_wallet_password))
                .setPositiveButton(resources.getString(R.string.ok_label)) { dialog, which ->

                }
                .show()
        }


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoadingWalletViewModel::class.java)

    }

}
