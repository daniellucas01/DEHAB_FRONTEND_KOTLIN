package com.example.dehab.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dehab.MainViewModel
import com.example.dehab.databinding.FragmentHomeBinding
import com.example.dehab.Constants
import com.example.dehab.ui.home.wallet_card.WalletCardAdapter
import com.example.dehab.ui.home.wallet_card.WalletCardItems
import org.web3j.crypto.Credentials
import java.math.BigDecimal


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: MainViewModel
    private lateinit var walletArrayList: ArrayList<WalletCardItems>
    private lateinit var _binding : FragmentHomeBinding
    private val binding get() = _binding
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var walletCredentials : Credentials


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        walletArrayList = ArrayList()
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.walletData.observe(requireActivity(), Observer {
            Log.e(Constants.AUTHOR_NAME, "Observing" )
            walletArrayList = ArrayList()
            walletArrayList.add(
                (WalletCardItems(
                    Constants.PERSONAL_WALLET,
                    it.ether_count.toDouble(),
                    convertEtherToDollar(it.ether_count))
                        )
            )
            binding.walletRecyclerView.adapter = WalletCardAdapter(walletArrayList, requireActivity())
        })
        gridLayoutManager = GridLayoutManager(requireContext(), 1, LinearLayoutManager.VERTICAL, false)
        binding.walletRecyclerView.layoutManager = gridLayoutManager
        binding.walletRecyclerView.setHasFixedSize(true)
        binding.walletRecyclerView.adapter = WalletCardAdapter(walletArrayList, requireActivity())
    }

    private fun convertEtherToDollar (etherCount: BigDecimal) : Double {
        return (etherCount.toDouble()) * 238.88
    }
}
