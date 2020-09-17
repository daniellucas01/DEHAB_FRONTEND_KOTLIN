package com.example.dehab.ui.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dehab.Constants
import com.example.dehab.MainViewModel
import com.example.dehab.R
import com.example.dehab.databinding.FragmentHistoryBinding
import com.example.dehab.model.ErrorResponseModel
import com.example.dehab.model.UserWalletByIdModel
import com.example.dehab.repository.KeyProviderApiRepository
import com.example.dehab.repository.WalletSingleton
import com.example.dehab.sol_contract_wrapper.MultiSignatureWallet
import com.example.dehab.ui.history.recycler_view.TransactionItem
import com.example.dehab.ui.history.recycler_view.TransactionListAdapter
import com.example.dehab.ui.home.wallet_card.WalletCardItems
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import retrofit2.Response

class HistoryFragment : Fragment() {

    private lateinit var mainViewModel : MainViewModel
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var contractAddress : String
    private var apiRepository = KeyProviderApiRepository
    private lateinit var _binding : FragmentHistoryBinding
    private var controller : LayoutAnimationController? = null
    private val binding get() = _binding
    private var arrayList: ArrayList<TransactionItem> = ArrayList()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mainViewModel = ViewModelProvider(requireActivity()).get(com.example.dehab.MainViewModel::class.java)
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.userId.observe(requireActivity(), Observer {
            setupContractAddress(it)
        })
        gridLayoutManager = GridLayoutManager(requireContext(), 1, LinearLayoutManager.VERTICAL, false)
        //Animation
        controller = AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.card_layout_slide_from_right)
        binding.transactionListRecyclerView.layoutManager = gridLayoutManager
        binding.transactionListRecyclerView.adapter = TransactionListAdapter(arrayList, "0")
        binding.transactionListRecyclerView.layoutAnimation = controller
        binding.transactionListRecyclerView.scheduleLayoutAnimation()
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

    private fun setupContractAddress(userId: Int){
        CoroutineScope(Dispatchers.IO).launch {
            contractAddress = async {getContractAddress(userId)}.await()
            if (validateAddress(contractAddress)) {
                initPendingTransactionList(contractAddress)
            }

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

    private suspend fun validateAddress(contract : String) : Boolean {
        if (contract == "1" || contract == "0") {
            withContext(Dispatchers.Main) {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(resources.getString(R.string.error_label))
                    .setMessage("Online Wallet not detected or initialized please try again later once you have good network coverage or have deployed the contract")
                    .setPositiveButton(resources.getString(R.string.ok_label)) { dialog, which ->

                    }
                    .show()
            }
            return false
        }
        return true
    }

    private fun initPendingTransactionList(contractAddress : String) {

        CoroutineScope(Dispatchers.IO).launch {
            val walletContract = MultiSignatureWallet.load(
                contractAddress,
                WalletSingleton.web3,
                WalletSingleton.walletCredentials,
                WalletSingleton.gasPrice,
                WalletSingleton.gasLimit
            )
            val transaction = walletContract.pendingTransactions.send()
            Log.e(Constants.AUTHOR_NAME, transaction.toString())
            withContext(Dispatchers.Main) {
                arrayList = ArrayList()
                var loopNum = 0
                var mTransactionNumber = 0
                while (loopNum < transaction.size) {
                    mTransactionNumber = transaction[loopNum].toString().toInt()
                    arrayList.add(
                        (TransactionItem(mTransactionNumber))
                    )
                    loopNum++
                }
                binding.transactionListRecyclerView.adapter = TransactionListAdapter(arrayList, contractAddress)
                binding.transactionListRecyclerView.scheduleLayoutAnimation()
            }
        }
    }
}
