package com.v1.smartv1alculatorv1.ui.language

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.v1.smartv1alculatorv1.Model.LanguageModel
import com.v1.smartv1alculatorv1.R
import com.v1.smartv1alculatorv1.databinding.ItemLanguagescreenBinding

class LanguageAdapter(
    private val context: Context,
    private var list: List<LanguageModel>,
    private val listener: (LanguageModel) -> Unit
) : RecyclerView.Adapter<LanguageAdapter.ViewHolder>() {

    private var selectedPosition = list.indexOfFirst { it.active }

    inner class ViewHolder(val binding: ItemLanguagescreenBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LanguageModel, isSelected: Boolean) {
            binding.txtCountry.text = item.languageName
            binding.imgFrag.setImageResource(item.image)

            if (isSelected) {
                binding.root.setBackgroundResource(R.drawable.custom_item_language_selected)
                binding.txtCountry.setTextColor(context.getColor(R.color.white))
            } else {
                binding.root.setBackgroundResource(R.drawable.custom_item_language)
                binding.txtCountry.setTextColor(context.getColor(R.color.black))
            }

            binding.root.setOnClickListener {
                val previousPosition = selectedPosition
                selectedPosition = adapterPosition

                if (previousPosition != -1) {
                    list[previousPosition].active = false
                    notifyItemChanged(previousPosition)
                }

                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)

                listener(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemLanguagescreenBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        // Cập nhật selectedPosition ban đầu nếu item.active = true
        val isSelected = position == selectedPosition || item.active
        holder.bind(item, isSelected)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateData(newList: List<LanguageModel>) {
        list = newList
        selectedPosition = list.indexOfFirst { it.active }
        notifyDataSetChanged()
    }

    fun setSelectedLanguage(selectedLanguage: LanguageModel) {
        val previousPosition = selectedPosition
        selectedPosition = list.indexOfFirst { it == selectedLanguage }
        if (previousPosition != -1) {
            notifyItemChanged(previousPosition)
        }
        if (selectedPosition != -1) {
            notifyItemChanged(selectedPosition)
        }
    }
}