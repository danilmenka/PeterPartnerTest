package com.expl.peterpartnertest.screen.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.expl.peterpartnertest.R
import com.expl.peterpartnertest.model.TransactionHistory
import com.expl.peterpartnertest.utilits.APP_ACTIVITY
import com.squareup.picasso.Picasso
import kotlin.math.abs

class HistoryAdapter :RecyclerView.Adapter<HistoryAdapter.MainHolder>() {
    private var transactionHistoryList = emptyList<TransactionHistory>()
    private var currentCurrencySymbol = emptyList<String>()

    class MainHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.history_name_text)
        val dateTextView: TextView = view.findViewById(R.id.history_date_text)
        val amountTextView: TextView = view.findViewById(R.id.history_balance_text)
        val currentAmountTextView: TextView = view.findViewById(R.id.history_current_balance_text)
        val symbolTextView: TextView = view.findViewById(R.id.history_symbol)
        val titleImageView: ImageView = view.findViewById(R.id.image_history)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return MainHolder(view)
    }

    override fun getItemCount(): Int = transactionHistoryList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        holder.titleTextView.text = transactionHistoryList[position].title
        holder.dateTextView.text = transactionHistoryList[position].date
        holder.amountTextView.text = "$ ${abs(transactionHistoryList[position].amount)}"
        holder.currentAmountTextView.text = abs(transactionHistoryList[position].currentAmount).toString()
        holder.symbolTextView.text = currentCurrencySymbol[position]

        Picasso.with(APP_ACTIVITY)
            .load(transactionHistoryList[position].icon_url)
            .placeholder(R.drawable.ic_person)
            .error(R.drawable.ic_person)
            .into(holder.titleImageView)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(transactionHistoryList: List<TransactionHistory>,currentCurrencySymbol:List<String>) {
        this.transactionHistoryList = transactionHistoryList
        this.currentCurrencySymbol = currentCurrencySymbol
        notifyDataSetChanged()
    }
}