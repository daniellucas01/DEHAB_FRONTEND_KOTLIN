package com.example.dehab.ui.history.recycler_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.dehab.R
import com.example.dehab.ui.history.HistoryFragmentDirections
import kotlinx.android.synthetic.main.custom_transaction_card.view.*
import kotlinx.android.synthetic.main.fragment_deposit.view.*
import org.w3c.dom.Text

class TransactionListAdapter (private var arrayList: ArrayList<TransactionItem>, private var mactivity: FragmentActivity) :
    RecyclerView.Adapter<TransactionListAdapter.CardItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardItemHolder {
        val transactionTemplate = LayoutInflater.from(parent.context).inflate(R.layout.custom_transaction_card, parent, false)
        return CardItemHolder(transactionTemplate)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: CardItemHolder, position: Int) {
        //TODO("Not yet finish implement transaction on click")
        val transactionItem = arrayList.get(position) 

        holder.transctionNumber.text = "Transaction # ${transactionItem.transactionNumber?.plus(1)}"
        holder.itemView.setOnClickListener() {view ->
            val transactionDetailButton : TransactionItem = arrayList[position]
            val transactionNumber = transactionDetailButton.transactionNumber
            if (transactionNumber != null) {
                val action = HistoryFragmentDirections.viewTransactionDetail(transactionNumber)
                view.findNavController().navigate(action)
            }
        }
    }

    class CardItemHolder(cardItemView: View) : RecyclerView.ViewHolder(cardItemView) {
        var transctionNumber: TextView = cardItemView.findViewById(R.id.transaction_number_label)
    }

}