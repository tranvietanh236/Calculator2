package com.v1.smartv1alculatorv1.ui.history.activity

import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.content.ContextCompat
import com.v1.smartv1alculatorv1.R
import com.v1.smartv1alculatorv1.base.BaseActivity
import com.v1.smartv1alculatorv1.base.BaseViewModel
import com.v1.smartv1alculatorv1.databinding.ActivityDetailScanBinding
import com.v1.smartv1alculatorv1.ui.scan_to_slove.StepsBottomSheet

class DetailScanActivity : BaseActivity<ActivityDetailScanBinding, BaseViewModel>() {
    override fun createBinding(): ActivityDetailScanBinding {
        return ActivityDetailScanBinding.inflate(layoutInflater)
    }

    override fun setViewModel(): BaseViewModel {
        return BaseViewModel()
    }

    override fun initView() {
        super.initView()
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        val byteArray = intent.getByteArrayExtra("imageBytes")
        if (byteArray != null) {
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            binding.imgCropHis.setImageBitmap(bitmap)
        }
        val resuilt = intent.getStringExtra("resuilt")
        Log.d("TAG123", "resuilt: $resuilt")
        val steps = intent.getStringExtra("steps")

        // Tìm chỉ số của cả ba chuỗi
        val stepByStepIndex =
            steps?.indexOf("Step-by-Step Solution:")
        val solutionStepsIndex =
            steps?.indexOf("Solution Steps:")
        val solutionProcessIndex =
            steps?.indexOf("Solution Process:")
        val recapIndex =
            steps?.indexOf("**Recap:**") // Tìm chỉ số của "**Recap:**"

        // Lấy chỉ số bắt đầu của phần cần lấy
        val startIndex = when {
            stepByStepIndex != -1 && solutionStepsIndex != -1 && solutionProcessIndex != -1 ->
                minOf(
                    stepByStepIndex!!,
                    solutionStepsIndex!!,
                    solutionProcessIndex!!
                )

            stepByStepIndex != -1 && solutionProcessIndex != -1 ->
                minOf(stepByStepIndex!!, solutionProcessIndex!!)

            solutionStepsIndex != -1 && solutionProcessIndex != -1 ->
                minOf(solutionStepsIndex!!, solutionProcessIndex!!)

            stepByStepIndex != -1 ->
                stepByStepIndex

            solutionStepsIndex != -1 ->
                solutionStepsIndex

            solutionProcessIndex != -1 ->
                solutionProcessIndex

            else ->
                -1
        }

        if (startIndex != -1 && recapIndex != -1) {
            val stepsSection =
                steps.substring(startIndex!!, recapIndex!!)
                    .trim() // Lấy nội dung từ startIndex đến recapIndex
            Log.d("TAG123", "stepsSection: $stepsSection")

            val regex = Regex(
                """\d+\.\s(.+?)(?=\d+\.\s|$)""",
                RegexOption.DOT_MATCHES_ALL
            )
            val stepsList = regex.findAll(stepsSection)
                .map { it.groupValues[1].trim() }.toMutableList()

            stepsList.add(resuilt!!)
            if (stepsList.isNotEmpty()) {
                // Hiển thị BottomSheet với danh sách bước
                val bottomSheet = StepsBottomSheet(stepsList)
                bottomSheet.show(
                    supportFragmentManager,
                    bottomSheet.tag
                )

            }
        }

        binding.icBackDetailCanHis.setOnClickListener { finish() }
    }
}