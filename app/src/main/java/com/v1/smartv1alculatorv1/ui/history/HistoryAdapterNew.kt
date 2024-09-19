package com.v1.smartv1alculatorv1.ui.history

import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.v1.smartv1alculatorv1.Model.ChatAnswer
import com.v1.smartv1alculatorv1.R

class HistoryAdapterNew(
    var chatList: MutableList<ChatAnswer>
) : RecyclerView.Adapter<HistoryAdapterNew.HisViewHolder>() {

    class HisViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val time: TextView = itemView.findViewById(R.id.history_time)
        private val calculation: TextView = itemView.findViewById(R.id.history_item_calculation)


        fun bind(message: ChatAnswer, chatList: MutableList<ChatAnswer>, position: Int) {
            if (message.isBot) {
                calculation.text = message.answer
                if (message.createdAt.isNullOrEmpty()) {
                    time.visibility = View.GONE
                } else {
                    val currentDate = message.createdAt.toLong()
                    val prevDate = if (position > 0) chatList[position - 1].createdAt?.toLong() else null
                    val nextDate = if (position + 1 < chatList.size) chatList[position + 1].createdAt?.toLong() else null

                    time.text = DateUtils.getRelativeTimeSpanString(
                        currentDate,
                        System.currentTimeMillis(),
                        DateUtils.DAY_IN_MILLIS,
                        DateUtils.FORMAT_ABBREV_RELATIVE
                    )

                    // Check if the former result has the same date -> hide the date
                    val prevDateText = prevDate?.let {
                        DateUtils.getRelativeTimeSpanString(
                            it,
                            System.currentTimeMillis(),
                            DateUtils.DAY_IN_MILLIS,
                            DateUtils.FORMAT_ABBREV_RELATIVE
                        )
                    }
                    time.visibility = if (prevDateText == time.text) View.GONE else View.VISIBLE

                    // Check if the next result has the same date -> hide the separator
                    val nextDateText = nextDate?.let {
                        DateUtils.getRelativeTimeSpanString(
                            it,
                            System.currentTimeMillis(),
                            DateUtils.DAY_IN_MILLIS,
                            DateUtils.FORMAT_ABBREV_RELATIVE
                        )
                    }
                    //separator.visibility = if (nextDateText == time.text) View.GONE else View.VISIBLE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HisViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return HisViewHolder(view)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun onBindViewHolder(holder: HisViewHolder, position: Int) {
        val chatSummary = chatList[position]
        holder.bind(chatSummary, chatList, position)

        holder.itemView.setOnClickListener {
            Log.d("TAG123", "onBindViewHolder: $position")
        }
    }

}
