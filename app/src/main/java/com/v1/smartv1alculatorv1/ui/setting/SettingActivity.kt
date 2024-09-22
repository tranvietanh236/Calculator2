package com.v1.smartv1alculatorv1.ui.setting

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.net.Uri
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.android.gms.tasks.Task
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.v1.smartv1alculatorv1.R
import com.v1.smartv1alculatorv1.base.BaseActivity
import com.v1.smartv1alculatorv1.base.BaseViewModel
import com.v1.smartv1alculatorv1.databinding.ActivitySettingBinding
import com.v1.smartv1alculatorv1.ui.home.HomeActivity
import com.v1.smartv1alculatorv1.untils.SharePrefUtils
import com.v1.smartv1alculatorv1.untils.tap

class SettingActivity : BaseActivity<ActivitySettingBinding, BaseViewModel>() {

    private var check = false

    override fun createBinding() = ActivitySettingBinding.inflate(layoutInflater)

    override fun setViewModel() = BaseViewModel()

    override fun initView() {
        super.initView()

        binding.icBack.tap {
            finish()
        }

        binding.clLanguage.tap {
            val intent = Intent(this, LanguageSettingActivity::class.java)
            startActivity(intent)
        }

        binding.clShare.setOnClickListener {
            if (!check) {
                share()
            }
        }

        binding.clRating.setOnClickListener {
            if (!check) {
                showRateDialog()
            }
        }

        binding.clPolicy.setOnClickListener {
            check = true
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://cloud.dify.ai/app/f929f7d9-c062-4bb8-a16b-91fe39fea1ea/develop"))
            startActivity(intent)
        }

        if (SharePrefUtils.isRated(this@SettingActivity)) {
            binding.clRating.visibility = View.GONE
        } else {
            binding.clRating.visibility = View.VISIBLE
        }
    }

    private fun share() {
        check = true
        val intentShare = Intent(Intent.ACTION_SEND)
        intentShare.type = "text/plain"
        intentShare.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
        intentShare.putExtra(
            Intent.EXTRA_TEXT,
            "${getString(R.string.app_name)}\nhttps://play.google.com/store/apps/details?id=${this.packageName}"
        )
        startActivity(Intent.createChooser(intentShare, "Share"))
    }

    private fun showRateDialog() {
        check = true

        val cornerRadius = 24f.toDp()
        val radii = floatArrayOf(
            cornerRadius,
            cornerRadius,
            cornerRadius,
            cornerRadius,
            cornerRadius,
            cornerRadius,
            cornerRadius,
            cornerRadius
        )
        val shape = RoundRectShape(radii, null, null)
        val shapeDrawable = ShapeDrawable(shape)
        shapeDrawable.paint.color = Color.WHITE
        val ratingDialog = RatingDialog(this@SettingActivity)
        ratingDialog.window!!.setBackgroundDrawable(shapeDrawable)
        ratingDialog.setCanceledOnTouchOutside(false)
        ratingDialog.init(this@SettingActivity, object : RatingDialog.OnPress {
            override fun send(s: Int) {
//                Toast.makeText(
//                    this@SettingActivity,
//                    getString(R.string.thank_you),
//                    Toast.LENGTH_SHORT
//                ).show()
                binding.clRating.visibility = View.GONE
                SharePrefUtils.forceRated(this@SettingActivity)
                ratingDialog.dismiss()
            }

            override fun rating(s: Int) {
                onRateAppNew()
                binding.clRating.visibility = View.GONE
                SharePrefUtils.forceRated(this@SettingActivity)
                ratingDialog.dismiss()
            }

            override fun cancel() {
                ratingDialog.dismiss()
            }

            override fun later() {
                ratingDialog.dismiss()
            }

            override fun gotIt() {
                ratingDialog.dismiss()
            }
        })

        ratingDialog.show()
        ratingDialog.setOnDismissListener {
            check = false
        }
    }


    private fun rateAppOnStoreNew() {
        val packageName = baseContext.packageName
        val uri: Uri = Uri.parse("market://details?id=$packageName")
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        goToMarket.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=$packageName")
                )
            )
        }
    }


    private fun onRateAppNew() {
        val manager: ReviewManager?
        var reviewInfo: ReviewInfo?
        manager = ReviewManagerFactory.create(this)
        val request: Task<ReviewInfo> = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                SharePrefUtils.forceRated(this)
                reviewInfo = task.result
                val flow: Task<Void> =
                    manager.launchReviewFlow(this, reviewInfo!!)
                flow.addOnSuccessListener {
                    showThankYouDialog()
                    rateAppOnStoreNew()
                }
            }
        }
    }

    private fun showThankYouDialog() {
        check = true

        val cornerRadius = 24f.toDp()
        val radii = floatArrayOf(
            cornerRadius,
            cornerRadius,
            cornerRadius,
            cornerRadius,
            cornerRadius,
            cornerRadius,
            cornerRadius,
            cornerRadius
        )
        val shape = RoundRectShape(radii, null, null)
        val shapeDrawable = ShapeDrawable(shape)
        shapeDrawable.paint.color = Color.WHITE
        val thankDialog = ThankDialog(this@SettingActivity)
        thankDialog.window!!.setBackgroundDrawable(shapeDrawable)
        thankDialog.setCanceledOnTouchOutside(false)
        thankDialog.init(this@SettingActivity, object : ThankDialog.OnPress {
            override fun send(s: Int) {
                Toast.makeText(
                    this@SettingActivity,
                    getString(R.string.thank_you),
                    Toast.LENGTH_SHORT
                ).show()
                binding.clRating.visibility = View.GONE
                SharePrefUtils.forceRated(this@SettingActivity)
                thankDialog.dismiss()
            }

            override fun rating(s: Int) {
                onRateAppNew()
                binding.clRating.visibility = View.GONE
                SharePrefUtils.forceRated(this@SettingActivity)
                thankDialog.dismiss()
            }

            override fun cancel() {
                thankDialog.dismiss()
            }

            override fun later() {
                thankDialog.dismiss()
            }

            override fun gotIt() {
                thankDialog.dismiss()
            }
        })

        thankDialog.show()
        thankDialog.setOnDismissListener {
            check = false
        }
    }



    override fun onResume() {
        super.onResume()
        check = false
    }
    fun Float.toDp(): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics
        )
    }
}