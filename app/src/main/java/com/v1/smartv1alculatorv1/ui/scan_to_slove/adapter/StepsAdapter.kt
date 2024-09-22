package com.v1.smartv1alculatorv1.ui.scan_to_slove.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.v1.smartv1alculatorv1.R

class StepsAdapter(private val stepsList: List<String>) :
    RecyclerView.Adapter<StepsAdapter.StepViewHolder>() {

    class StepViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvStep: TextView = itemView.findViewById(R.id.tvStep)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.step_item, parent, false)
        return StepViewHolder(view)
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        val txtFilter = stepsList[position]
        val indexes = txtFilter.indexOfAny(charArrayOf(',', ':'))
        if (indexes != -1) {
            // Tách thành hai phần
            val part1 = txtFilter.substring(0, indexes + 1)
                .trim() // Phần từ đầu đến dấu phẩy hoặc dấu hai chấm
            val part2 =
                txtFilter.substring(indexes + 1).trim() // Phần từ sau dấu phẩy hoặc dấu hai chấm

            // Hiển thị kết quả lên TextView
            if (position == stepsList.size - 1) {
                holder.tvStep.text = "Result:"
            } else {
                holder.tvStep.text = "Step $part1"
            }

            holder.tvDescription.text = part2
        } else {
            // Nếu không tìm thấy dấu phẩy hoặc dấu hai chấm
            holder.tvStep.text = stepsList[position]
            holder.tvDescription.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return stepsList.size
    }
}
