package com.v1.smartv1alculatorv1.ui.intro

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.v1.smartv1alculatorv1.Model.IntroModel
import com.v1.smartv1alculatorv1.R
import com.v1.smartv1alculatorv1.databinding.ItemPageBinding

class IntroAdapter (val context: Context, var list: List<IntroModel>) : RecyclerView.Adapter<IntroAdapter.IntroViewHolder>()  {

    inner class IntroViewHolder(val binding: ItemPageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: IntroModel, context: Context) {
            Glide.with(context).load(data.image).into(binding.imgIntro01)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntroViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_page, parent, false)
        return IntroViewHolder(ItemPageBinding.bind(view))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: IntroViewHolder, position: Int) {
        holder.bind(list[position], context)
    }

}