package com.v1.smartv1alculatorv1.ui.history.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.v1.smartv1alculatorv1.Model.ScanModel
import com.v1.smartv1alculatorv1.databinding.ItemScanListBinding
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
class ScanAdapter(
    private var list: List<ScanModel>,
    val context: Context
) : RecyclerView.Adapter<ScanAdapter.HistoryScanViewHolder>() {
    class HistoryScanViewHolder(val binding: ItemScanListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryScanViewHolder {
        val binding =
            ItemScanListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryScanViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun onBindViewHolder(holder: HistoryScanViewHolder, position: Int) {
        val data = list[position]
        val bitmap = byteArrayToBitmap(data.imageBytes)
        holder.binding.imgScan.setImageBitmap(bitmap)

        val today = LocalDate.now()
        val itemDate = convertTimestampToLocalDate(data.createdAt)
        val daysDifference = ChronoUnit.DAYS.between(itemDate, today)

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

    fun byteArrayToBitmap(byteArray: ByteArray?): Bitmap? {
        return byteArray?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
    }

    fun convertTimestampToLocalDate(timestamp: String): LocalDate {
        val timestampLong = timestamp.toLong()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Instant.ofEpochMilli(timestampLong).atZone(ZoneId.systemDefault()).toLocalDate()
        } else {
            Instant.ofEpochMilli(timestampLong).atZone(ZoneId.systemDefault()).toLocalDate()
        }
    }
}