package com.example.dehab.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.dehab.R
import com.example.dehab.databinding.FragmentTransactionBinding
import com.google.android.material.tabs.TabLayoutMediator

class TransactionFragment : Fragment() {

    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var transcationViewPageAdapter: TransactionViewPagerAdapter
    private var transactionTabLayoutTitle = arrayListOf<String>()
    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
//        transactionViewModel =
//                ViewModelProviders.of(this).get(TransactionViewModel::class.java)
        _binding = FragmentTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        transactionTabLayoutTitle.add("DEPOSIT")
        transactionTabLayoutTitle.add("TRANSFER")

        transcationViewPageAdapter = TransactionViewPagerAdapter(requireActivity())

        binding.transactionViewPager.adapter = transcationViewPageAdapter
        TabLayoutMediator(binding.transactionTabLayout, binding.transactionViewPager) { tab, position ->
            tab.text = transactionTabLayoutTitle[position]
        }.attach()
    }


}
