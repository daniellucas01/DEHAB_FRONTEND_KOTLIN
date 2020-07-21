package com.example.dehab.ui.home.wallet_card

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.dehab.R
import org.w3c.dom.Text

class WalletCardAdapter (private var arrayList: ArrayList<WalletCardItems>, private var mActivity: FragmentActivity) :
    RecyclerView.Adapter<WalletCardAdapter.CardItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardItemHolder {
        val cardItemHolder = LayoutInflater.from(parent.context).inflate(R.layout.wallet_card_layout, parent, false)
        return CardItemHolder(cardItemHolder)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: CardItemHolder, position: Int) {
        val walletCardItems : WalletCardItems = arrayList[position]
        // center buttons
        // data distribution
        val etherCountPlaceholder = mActivity.getString(R.string.ether_count_placeholder)
        val moneyCountPlaceholder = mActivity.getString(R.string.money_count_placeholder)
        holder.walletLabel.text = walletCardItems.walletLabel
        holder.etherCount.text = String.format(etherCountPlaceholder, walletCardItems.etherCount.toString())
        holder.realMoney.text =String.format(moneyCountPlaceholder, walletCardItems.moneyCount.toString())
        // on click listener
    }

    class CardItemHolder(cardItemView: View) : RecyclerView.ViewHolder(cardItemView) {
        var walletLabel : TextView = cardItemView.findViewById(R.id.wallet_label)
        var etherCount: TextView = cardItemView.findViewById(R.id.ether_count_label)
        var realMoney: TextView = cardItemView.findViewById(R.id.real_money_label)
    }
}