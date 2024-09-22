package com.v1.smartv1alculatorv1.ui.history.activity

import android.content.DialogInterface
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.calculator.customformula.utils.GlobalFunction
import com.v1.smartv1alculatorv1.Database.ChatRepository
import com.v1.smartv1alculatorv1.R
import com.v1.smartv1alculatorv1.base.BaseActivity
import com.v1.smartv1alculatorv1.base.BaseViewModel
import com.v1.smartv1alculatorv1.databinding.ActivityHistoryNewBinding
import com.v1.smartv1alculatorv1.dialog.deleteDialog
import com.v1.smartv1alculatorv1.ui.history.HisScanFragment
import com.v1.smartv1alculatorv1.ui.history.HisTutorFragment

class HistoryActivityNew : BaseActivity<ActivityHistoryNewBinding, BaseViewModel>() {
    private lateinit var chatRepository: ChatRepository
    override fun createBinding(): ActivityHistoryNewBinding {
        return ActivityHistoryNewBinding.inflate(layoutInflater)
    }

    override fun setViewModel(): BaseViewModel {
        return BaseViewModel()
    }

    override fun initView() {
        super.initView()
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        chatRepository = ChatRepository(this)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_his_fragment, HisScanFragment(), "FRAGMENT_A")
            .commit()

        binding.hisScan.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container_his_fragment, HisScanFragment(), "FRAGMENT_A")
                .commit()
            binding.hisScan.setBackgroundResource(R.drawable.bg_border_8_history_on)
            binding.hisScan.setTextColor(ContextCompat.getColor(this, R.color.txt_his))
            binding.hisTutor.setBackgroundResource(R.drawable.bg_border_8_history)
            binding.hisTutor.setTextColor(ContextCompat.getColor(this, R.color.black))
        }
        binding.hisTutor.setOnClickListener {
            binding.hisTutor.setBackgroundResource(R.drawable.bg_border_8_history_on)
            binding.hisTutor.setTextColor(ContextCompat.getColor(this, R.color.txt_his))
            binding.hisScan.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.hisScan.setBackgroundResource(R.drawable.bg_border_8_history)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container_his_fragment, HisTutorFragment(), "FRAGMENT_B")
                .commit()


        }

        binding.icBackHis.setOnClickListener {
            finish()
        }

        binding.icDelete.setOnClickListener {
            val dialog = deleteDialog(this@HistoryActivityNew)
            dialog.init(this@HistoryActivityNew, object : deleteDialog.OnClickDialogPaListener{
                override fun cancel() {
                    dialog.dismiss()
                }

                override fun ok() {
                    val fragmentA = supportFragmentManager.findFragmentByTag("FRAGMENT_A") as? HisScanFragment
                    val fragmentB = supportFragmentManager.findFragmentByTag("FRAGMENT_B") as? HisTutorFragment

                    if (fragmentA != null && fragmentA.isVisible) {
                        chatRepository.deleteChatScan()
                        fragmentA.loadChatHistoryNew()
                    } else if (fragmentB != null && fragmentB.isVisible) {
                        chatRepository.deleteChatHis()
                        fragmentB.loadChatHistory()
                    }
                    dialog.dismiss()
                }

            })
            dialog.show()
        }
    }
}