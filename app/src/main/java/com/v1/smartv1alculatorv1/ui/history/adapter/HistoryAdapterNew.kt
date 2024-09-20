package com.v1.smartv1alculatorv1.ui.history.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.calculator.customformula.utils.GlobalFunction
import com.v1.smartv1alculatorv1.Model.HistoryModel
import com.v1.smartv1alculatorv1.databinding.ItemHistoryBinding
import com.v1.smartv1alculatorv1.inteface.OnItemHisClickListener
import com.v1.smartv1alculatorv1.ui.history.activity.DetailHistoryActivity
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.log

class HistoryAdapterNew(private var list: List<HistoryModel>,
                        val context: Context,
                        private val listener: OnItemHisClickListener) :
    RecyclerView.Adapter<HistoryAdapterNew.HistoryViewHolder>() {


    @RequiresApi(Build.VERSION_CODES.O)
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    class HistoryViewHolder(val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val data = list[position]
        holder.binding.historyItemCalculation.text = data.answer
        val today = LocalDate.now()
        val itemDate = convertTimestampToLocalDate(data.createdAt)
        val daysDifference = ChronoUnit.DAYS.between(itemDate, today)

        holder.binding.root.setOnClickListener {
            Toast.makeText(context, "Item $position clicked", Toast.LENGTH_SHORT).show()
            GlobalFunction.startActivity(context, DetailHistoryActivity::class.java)
        }

        holder.binding.historyTime.visibility = View.VISIBLE
        when {
            daysDifference == 0L -> {
                holder.binding.historyTime.text = "Today"
            }

            daysDifference == 1L -> {
                holder.binding.historyTime.text = "Yesterday"
            }

            else -> {
                holder.binding.historyTime.text = itemDate.format(formatter)
            }
        }

        if (position > 0) {
            val previousItemDate = convertTimestampToLocalDate(list[position - 1].createdAt)
            if (itemDate == previousItemDate) {
                holder.binding.historyTime.visibility = View.GONE
            }
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertTimestampToLocalDate(timestamp: String): LocalDate {
        val timestampLong = timestamp.toLong()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Instant.ofEpochMilli(timestampLong).atZone(ZoneId.systemDefault()).toLocalDate()
        } else {
            Instant.ofEpochMilli(timestampLong).atZone(ZoneId.systemDefault()).toLocalDate()
        }
    }
}
