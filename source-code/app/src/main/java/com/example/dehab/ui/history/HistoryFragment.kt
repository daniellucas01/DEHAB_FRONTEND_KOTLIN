package com.example.dehab.ui.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dehab.Constants
import com.example.dehab.databinding.FragmentHistoryBinding
import com.example.dehab.repository.WalletSingleton
import com.example.dehab.sol_contract_wrapper.MultiSignatureWallet
import com.example.dehab.ui.history.recycler_view.TransactionItem
import com.example.dehab.ui.history.recycler_view.TransactionListAdapter
import com.example.dehab.ui.home.wallet_card.WalletCardItems
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryFragment : Fragment() {

    private lateinit var dashboardViewModel: HistoryViewModel
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var _binding : FragmentHistoryBinding
    private val binding get() = _binding
    private var arrayList: ArrayList<TransactionItem> = ArrayList()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gridLayoutManager = GridLayoutManager(requireContext(), 1, LinearLayoutManager.VERTICAL, false)
        binding.transactionListRecyclerView.layoutManager = gridLayoutManager
        binding.transactionListRecyclerView.adapter = TransactionListAdapter(arrayList, requireActivity())
        CoroutineScope(Dispatchers.IO).launch {
            val walletContract = MultiSignatureWallet.load(
                "0x3399289ce3c2197f5b16736ab238703e0ea80d9b",
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
                binding.transactionListRecyclerView.adapter = TransactionListAdapter(arrayList, requireActivity())
            }
        }
    }
}
