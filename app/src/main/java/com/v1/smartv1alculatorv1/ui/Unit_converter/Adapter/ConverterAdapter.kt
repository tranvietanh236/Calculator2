package com.v1.smartv1alculatorv1.ui.Unit_converter.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.v1.smartv1alculatorv1.Model.ConverterModel
import com.v1.smartv1alculatorv1.R
import com.v1.smartv1alculatorv1.ui.Unit_converter.Fragment.AreaFragment
import com.v1.smartv1alculatorv1.ui.Unit_converter.Fragment.DensityFragment
import com.v1.smartv1alculatorv1.ui.Unit_converter.Fragment.EnergyFragment
import com.v1.smartv1alculatorv1.ui.Unit_converter.Fragment.LengthFragment
import com.v1.smartv1alculatorv1.ui.Unit_converter.Fragment.MassFragment
import com.v1.smartv1alculatorv1.ui.Unit_converter.Fragment.SpeedFragment
import com.v1.smartv1alculatorv1.ui.Unit_converter.Fragment.TempFragment
import com.v1.smartv1alculatorv1.ui.Unit_converter.Fragment.TimeFragment
import com.v1.smartv1alculatorv1.ui.Unit_converter.Fragment.VolumeFragment

class ConverterAdapter(
    private val list: ArrayList<ConverterModel>,
    private val fragmentManager: FragmentManager
) : RecyclerView.Adapter<ConverterAdapter.ViewHolder>() {
    private var selectedPosition: Int = 0

    //RecyclerView.NO_POSITION
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_converter)

        init {
            itemView.setOnClickListener {
                notifyItemChanged(selectedPosition)
                selectedPosition = adapterPosition
                notifyItemChanged(selectedPosition)

                val fragment = when (adapterPosition) {
                    0 -> LengthFragment() // Chuyển đến Fragment1 khi click vào item đầu tiên
                    1 -> VolumeFragment() // Chuyển đến Fragment2 khi click vào item thứ hai
                    2 -> AreaFragment() // Chuyển đến Fragment3 khi click vào item thứ ba 0 -> Fragment1() // Chuyển đến Fragment1 khi click vào item đầu tiên
                    3 -> MassFragment() // Chuyển đến Fragment2 khi click vào item thứ hai
                    4 -> TimeFragment() // Chuyển đến Fragment3 khi click vào item thứ ba 0 -> Fragment1() // Chuyển đến Fragment1 khi click vào item đầu tiên
                    5 -> SpeedFragment() // Chuyển đến Fragment2 khi click vào item thứ hai
                    6 -> TempFragment() // Chuyển đến Fragment3 khi click vào item thứ ba 0 -> Fragment1() // Chuyển đến Fragment1 khi click vào item đầu tiên
                    7 -> DensityFragment() // Chuyển đến Fragment2 khi click vào item thứ hai
                    8 -> EnergyFragment() // Chuyển đến Fragment3 khi click vào item thứ ba
                    else -> LengthFragment() // Mặc định chuyển đến Fragment1 nếu vị trí không khớp
                }

                // Replace the current fragment with the new one
                fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null) // Thêm vào back stack để quay lại được
                    .commit()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_converter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            imageView.setImageResource(list[position].image)
            if (position == selectedPosition) {
                imageView.setColorFilter(itemView.context.getColor(R.color.ic_converter_color))
                itemView.setBackgroundResource(R.drawable.bg_border_8_item_converter_on)
            } else {
                itemView.setBackgroundResource(R.drawable.bg_border_8_item_converter_off)
                imageView.setColorFilter(itemView.context.getColor(R.color.white))
            }
        }
    }

    fun upgradeIndex(index: Int) {
        notifyItemChanged(selectedPosition) // Cập nhật item trước đó
        selectedPosition = index
        notifyItemChanged(selectedPosition) // Cập nhật item mới
    }

    fun switchFragment(position: Int) {
        val fragment = when (position) {
            0 -> LengthFragment()
            1 -> VolumeFragment()
            2 -> AreaFragment()
            3 -> MassFragment()
            4 -> TimeFragment()
            5 -> SpeedFragment()
            6 -> TempFragment()
            7 -> DensityFragment()
            8 -> EnergyFragment()
            else -> LengthFragment()
        }

        // Replace fragment tương ứng với index
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

}