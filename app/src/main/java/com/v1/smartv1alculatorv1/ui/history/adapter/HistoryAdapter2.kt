package com.v1.smartv1alculatorv1.ui.history.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.v1.smartv1alculatorv1.Model.ChatAnswer
import com.v1.smartv1alculatorv1.R

class HistoryAdapter2(private var chatList: MutableList<ChatAnswer>) : RecyclerView.Adapter<HistoryAdapter2.ChatViewHolder>(){

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var textMessageUser: TextView = itemView.findViewById(R.id.history_item_calculation)
        private var textTimestampBot: TextView = itemView.findViewById(R.id.history_time)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return ChatViewHolder(view)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {

    }
}