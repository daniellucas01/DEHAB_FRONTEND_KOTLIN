package com.example.dehab.ui.transaction

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.dehab.ui.transaction.deposit.DepositFragment
import com.example.dehab.ui.transaction.transfer.TransferFragment

class TransactionViewPagerAdapter (activity: FragmentActivity): FragmentStateAdapter(activity) {

    lateinit var fragment: Fragment

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        lateinit var fragment: Fragment

        when (position) {
            0 -> {
                val depositFragment = DepositFragment()
                fragment = depositFragment
            }

            1 -> {
                val transferFragment = TransferFragment()
                fragment = transferFragment
            }
        }
        return fragment
    }
}