package com.v1.smartv1alculatorv1.ui.smartcalculator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.v1.smartv1alculatorv1.Model.SmartModel
import com.v1.smartv1alculatorv1.R

class HistoryAdapter(private val historyList: List<SmartModel>) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.history_item_text) // ID của TextView trong item layout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_historysmart, parent, false)
        return HistoryViewHolder(view)

    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = historyList[position]
        holder.textView.text = item.calculation
    }

    override fun getItemCount(): Int {
        return historyList.size
    }
}