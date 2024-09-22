package com.v1.smartv1alculatorv1.ui.setting

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.v1.smartv1alculatorv1.R
import com.v1.smartv1alculatorv1.untils.tap


class RatingDialog @SuppressLint("NonConstantResourceId") constructor(context2: Context) :
    Dialog(context2, R.style.BaseDialog) {
    private var onPress: OnPress? = null
    private val tvTitle: TextView
    private val tvContent: TextView
    private val tvCancel: ImageView

    private val rootStar1: ImageView
    private val rootStar2: ImageView
    private val rootStar3: ImageView
    private val rootStar4: ImageView
    private val rootStar5: ImageView

    private val editFeedback: EditText? = null
    private val context: Context
    private val btnLater: TextView

    private val btnRate: TextView
    private val rootBgButton: ViewGroup? = null
    private var rate = 5

    init {

        this.context = context2
        setContentView(R.layout.rating_dialog)
        val attributes = window!!.attributes
//        attributes.width = WindowManager.LayoutParams.MATCH_PARENT
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT
        attributes.width = (context.resources.displayMetrics.widthPixels * 0.9).toInt()
        window!!.attributes = attributes
        window!!.setSoftInputMode(16)

        tvTitle = findViewById<TextView>(R.id.tv_title)
        tvContent = findViewById<TextView>(R.id.tv_content)

        btnLater = findViewById<TextView>(R.id.btn_cancel)
        btnRate = findViewById<TextView>(R.id.btn_submit)
        tvCancel = findViewById<ImageView>(R.id.img_close)

        rootStar1 = findViewById<ImageView>(R.id.rootRate1)
        rootStar2 = findViewById<ImageView>(R.id.rootRate2)
        rootStar3 = findViewById<ImageView>(R.id.rootRate3)
        rootStar4 = findViewById<ImageView>(R.id.rootRate4)
        rootStar5 = findViewById<ImageView>(R.id.rootRate5)

        onclick()
        changeRating()

        rootStar1.tap {
            rate = 1
            setStar(1)
        }

        rootStar2.tap {
            rate = 2
            setStar(2)
        }

        rootStar3.tap {
            rate = 3
            setStar(3)
        }

        rootStar4.tap {
            rate = 4
            setStar(4)
        }

        rootStar5.tap {
            rate = 5
            setStar(5)
        }

    }

    interface OnPress {
        fun send(s: Int)

        fun rating(s: Int)

        fun cancel()

        fun later()

        fun gotIt()
    }

    fun init(context: Context?, onPress: OnPress?) {
        this.onPress = onPress
    }

    fun changeRating() {
//        rtb.onRatingBarChangeListener =
//            OnRatingBarChangeListener { ratingBar: RatingBar?, rating: Float, fromUser: Boolean ->
//                val getRating = rtb.rating.toString()
//                s = when (getRating) {
//                    "1.0" -> 1
//                    "2.0" -> 2
//                    "3.0" -> 3
//                    "4.0" -> 4
//                    "5.0" -> 5
//                    else -> 0
//                }
//            }
    }

    val newName: String
        get() = editFeedback!!.text.toString()



    fun onclick() {
        btnRate.tap { view: View? ->
//            Log.d("TAG23", "onclick: ${rtb.rating}")
            if (rate <= 4.0) {
                onPress!!.send(rate)
            } else {
                onPress!!.rating(rate)
            }
//
//
//            btnGotIt.visibility = View.VISIBLE
//            rtb.visibility = View.GONE

        }

        btnLater.tap {
                view: View? -> onPress!!.later()
        }

        tvCancel.tap {
                view: View? -> onPress!!.cancel()
        }
    }

    fun setStar(value: Int) {
        val rates = listOf(
            rootStar1,
            rootStar2,
            rootStar3,
            rootStar4,
            rootStar5
        )

        for (i in rates.indices) {
            if (i < value) {
                rates[i].setBackgroundResource(R.drawable.ic_rate_seclect)
            } else {
                rates[i].setBackgroundResource(R.drawable.ic_rate_unseclect)
            }
        }
    }
}