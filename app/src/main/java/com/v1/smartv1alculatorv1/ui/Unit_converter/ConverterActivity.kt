package com.v1.smartv1alculatorv1.ui.Unit_converter


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

        binding.icBackConverter.setOnClickListener {
            finish()
        }
        val fragment = LengthFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()


        val imageUrls = ArrayList<ConverterModel>()
        imageUrls.add(ConverterModel(R.drawable.ic_test_length))
        imageUrls.add(ConverterModel(R.drawable.ic_test_volume))
        imageUrls.add(ConverterModel(R.drawable.ic_test_area))
        imageUrls.add(ConverterModel(R.drawable.ic_test_mass))
        imageUrls.add(ConverterModel(R.drawable.ic_test_time))
        imageUrls.add(ConverterModel(R.drawable.ic_test_speed))
        imageUrls.add(ConverterModel(R.drawable.ic_test_temp))
        imageUrls.add(ConverterModel(R.drawable.ic_test_density))
        imageUrls.add(ConverterModel(R.drawable.ic_test_energy))

        imageAdapter = ConverterAdapter(imageUrls, supportFragmentManager)

        binding.recycleview.adapter = imageAdapter
        binding.recycleview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


    }
}