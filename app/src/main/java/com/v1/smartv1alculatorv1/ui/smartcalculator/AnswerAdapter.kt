package com.v1.smartv1alculatorv1.ui.smartcalculator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.v1.smartv1alculatorv1.R

class AnswerAdapter(
    private val stepsAnswerList: List<String>
) :
    RecyclerView.Adapter<AnswerAdapter.StepAnswerViewHolder>() {


    class StepAnswerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvStepAnswar: TextView = itemView.findViewById(R.id.tvStepAnswar)
        val tvDescriptionAnswar: TextView = itemView.findViewById(R.id.tvDescriptionAnswar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepAnswerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_answer, parent, false)
        return StepAnswerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return stepsAnswerList.size
    }

    override fun onBindViewHolder(holder: StepAnswerViewHolder, position: Int) {
        val txtFilter = stepsAnswerList[position]
        val indexes = txtFilter.indexOfAny(charArrayOf(',', ':'))
        if (indexes != -1) {
            // Tách thành hai phần
            val part1 = txtFilter.substring(0, indexes + 1)
                .trim() // Phần từ đầu đến dấu phẩy hoặc dấu hai chấm
            val part2 =
                txtFilter.substring(indexes + 1).trim() // Phần từ sau dấu phẩy hoặc dấu hai chấm

            val index = position + 1
            // Hiển thị kết quả lên TextView
            if (position == stepsAnswerList.size - 1) {
                holder.tvStepAnswar.text = "Result:"
            } else {
                holder.tvStepAnswar.text = "Step $index:$part1"
            }

            holder.tvDescriptionAnswar.text = part2
        } else {
            // Nếu không tìm thấy dấu phẩy hoặc dấu hai chấm
            holder.tvStepAnswar.text = stepsAnswerList[position]
            holder.tvDescriptionAnswar.visibility = View.GONE
        }
    }




}