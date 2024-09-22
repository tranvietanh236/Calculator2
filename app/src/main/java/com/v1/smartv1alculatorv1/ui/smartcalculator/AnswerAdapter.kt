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
        val indexes = txtFilter.indexOfAny(charArrayOf(':', '\n'))

        // Tạo số bước, thêm 1 vì position bắt đầu từ 0
        val stepNumber = position + 1

        if (indexes != -1) {
            // Tách thành hai phần
            val part1 = txtFilter.substring(0, indexes + 1).trim() // Phần từ đầu đến dấu phẩy hoặc dấu hai chấm
            val part2 = txtFilter.substring(indexes + 1).trim() // Phần từ sau dấu phẩy hoặc dấu hai chấm

            // Hiển thị kết quả lên TextView
            holder.tvStepAnswar.text = "Step $stepNumber: $part1"
            holder.tvDescriptionAnswar.text = part2
            holder.tvDescriptionAnswar.visibility = View.VISIBLE
        } else {

            holder.tvStepAnswar.text = "Step $stepNumber: ${stepsAnswerList[position]}"
            holder.tvDescriptionAnswar.visibility = View.GONE
        }
    }




}