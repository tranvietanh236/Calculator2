package com.v1.smartv1alculatorv1.ui.history

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.v1.smartv1alculatorv1.Model.ChatAnswer
import com.v1.smartv1alculatorv1.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter2(private var chatList: List<ChatAnswer>) : RecyclerView.Adapter<HistoryAdapter2.ChatViewHolder>(){

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val llBot: ConstraintLayout = itemView.findViewById(R.id.textMessageBot)
        //private val textMessageBot: TextView = itemView.findViewById(R.id.txt_bot)
        private val textMessageUser: TextView = itemView.findViewById(R.id.textMessageUser)
        private val textTimestampBot: TextView = itemView.findViewById(R.id.textTimestampBot)
        private val textTimestampUser: TextView = itemView.findViewById(R.id.textTimestampUser)
        //private val img_chatbot: ImageView = itemView.findViewById(R.id.img_chatbot)

//        fun bind(message: ChatAnswer) {
//            llBot.visibility = View.GONE
//            textMessageUser.visibility = View.GONE
//            if (message.isBot) {
//            } else {
//                //img_chatbot.visibility = View.GONE
//                textMessageUser.text = message.answer
//                textTimestampUser.text = formatTimestamp(message.createdAt)
//                textMessageUser.visibility = View.VISIBLE
//                textTimestampUser.visibility = View.VISIBLE
//            }
//        }

        private fun formatTimestamp(timestamp: String): String {
            val time = timestamp.toLong()
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            return sdf.format(Date(time))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_message, parent, false)
        return ChatViewHolder(view)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chatSummary = chatList[position]
        holder.itemView.apply {

        }

        holder.itemView.setOnClickListener {
            Log.d("TAG123", "onBindViewHolder: ${chatSummary.answer}")
        }
    }
}