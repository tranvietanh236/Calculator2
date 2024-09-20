package com.v1.smartv1alculatorv1.ui.Unit_converter


import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.v1.smartv1alculatorv1.Model.ConverterModel
import com.v1.smartv1alculatorv1.R
import com.v1.smartv1alculatorv1.base.BaseActivity
import com.v1.smartv1alculatorv1.databinding.ActivityConverterBinding
import com.v1.smartv1alculatorv1.ui.Unit_converter.Adapter.ConverterAdapter
import com.v1.smartv1alculatorv1.ui.Unit_converter.Fragment.LengthFragment


class ConverterActivity : BaseActivity<ActivityConverterBinding, ConverterViewModel>() {
    private var imageAdapter: ConverterAdapter? = null
    override fun createBinding(): ActivityConverterBinding {
        return ActivityConverterBinding.inflate(layoutInflater)
    }

    override fun setViewModel(): ConverterViewModel {
        return ConverterViewModel()
    }

    override fun initView() {
        super.initView()
        window.statusBarColor = ContextCompat.getColor(this, R.color.ic_converter_color)
        val indexUnit = intent.getIntExtra("unit", 0)
        Log.d("TAG123", "index: $indexUnit")
        imageAdapter?.upgradeIndex(indexUnit)

        binding.icBackConverter.setOnClickListener {
            finish()
        }
        val fragment = LengthFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()


        val imageUrls = ArrayList<ConverterModel>()
        imageUrls.add(ConverterModel(R.drawable.ic_unit_length))
        imageUrls.add(ConverterModel(R.drawable.ic_unit_volume))
        imageUrls.add(ConverterModel(R.drawable.ic_unit_area))
        imageUrls.add(ConverterModel(R.drawable.ic_unit_mass))
        imageUrls.add(ConverterModel(R.drawable.ic_unit_time))
        imageUrls.add(ConverterModel(R.drawable.ic_unit_speed))
        imageUrls.add(ConverterModel(R.drawable.ic_unit_temp))
        imageUrls.add(ConverterModel(R.drawable.ic_unit_density))
        imageUrls.add(ConverterModel(R.drawable.ic_unit_energy))

        imageAdapter = ConverterAdapter(imageUrls, supportFragmentManager)

        binding.recycleview.adapter = imageAdapter
        binding.recycleview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        if (indexUnit != -1) {
            imageAdapter?.upgradeIndex(indexUnit)
            binding.recycleview.scrollToPosition(indexUnit)
            imageAdapter?.switchFragment(indexUnit)
        }
    }
}