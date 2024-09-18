package com.v1.smartv1alculatorv1.ui.Unit_converter

import android.content.Intent
import com.v1.smartv1alculatorv1.base.BaseActivity
import com.v1.smartv1alculatorv1.base.BaseViewModel
import com.v1.smartv1alculatorv1.databinding.ActivityHomeUnitConverterBinding

class HomeUnitConverterActivity : BaseActivity<ActivityHomeUnitConverterBinding, BaseViewModel>() {
    override fun createBinding(): ActivityHomeUnitConverterBinding {
        return ActivityHomeUnitConverterBinding.inflate(layoutInflater)
    }

    override fun setViewModel(): BaseViewModel {
        return BaseViewModel()
    }

    override fun initView() {
        super.initView()
        binding.itemUnitVolume.setOnClickListener {
            val intent = Intent(this, ConverterActivity::class.java)
            intent.putExtra("unit", 1)
            startActivity(intent)
        }
        binding.itemUnitLength.setOnClickListener {
            val intent = Intent(this, ConverterActivity::class.java)
            intent.putExtra("unit", 0)
            startActivity(intent)
        }
        binding.itemUnitArea.setOnClickListener {
            val intent = Intent(this, ConverterActivity::class.java)
            intent.putExtra("unit", 2)
            startActivity(intent)
        }
        binding.itemUnitMass.setOnClickListener {
            val intent = Intent(this, ConverterActivity::class.java)
            intent.putExtra("unit", 3)
            startActivity(intent)
        }
        binding.itemUnitTime.setOnClickListener {
            val intent = Intent(this, ConverterActivity::class.java)
            intent.putExtra("unit", 4)
            startActivity(intent)
        }
        binding.itemUnitSpeed.setOnClickListener {
            val intent = Intent(this, ConverterActivity::class.java)
            intent.putExtra("unit", 5)
            startActivity(intent)
        }
        binding.itemUnitTemp.setOnClickListener {
            val intent = Intent(this, ConverterActivity::class.java)
            intent.putExtra("unit", 6)
            startActivity(intent)
        }
        binding.itemUnitDensity.setOnClickListener {
            val intent = Intent(this, ConverterActivity::class.java)
            intent.putExtra("unit", 7)
            startActivity(intent)
        }
        binding.itemUnitEnergy.setOnClickListener {
            val intent = Intent(this, ConverterActivity::class.java)
            intent.putExtra("unit", 8)
            startActivity(intent)
        }

    }
}