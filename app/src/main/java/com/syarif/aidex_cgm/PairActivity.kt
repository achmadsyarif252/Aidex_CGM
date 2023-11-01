package com.syarif.aidex_cgm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.microtechmd.cgmlib.CgmManager
import com.microtechmd.cgmlib.inter.Callback
import com.syarif.aidex_cgm.databinding.ActivityPair2Binding
import com.syarif.aidex_cgm.view.CEditText

class PairActivity : AppCompatActivity() {
    var txtView: CEditText? = null
    private lateinit var binding: ActivityPair2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPair2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.setEnabled(false)
        txtView?.setOnFinishListener(object : CEditText.OnFinishListener {
            override fun onFinish(msg: String?) {}
        })
        binding.btnPair.setOnClickListener {
            val sn: String = txtView?.getText().toString()
            val userId = "00000000000000000000000000839539"
            binding.txtMessage.text =
                getString(R.string.content_pair_device) + "SN ：" + sn + ",userid" + userId
            CgmManager.getInstance().pair(userId,
                sn, object : Callback {
                    override fun onFailure(code: Int) {
                        binding.txtMessage.text = ErrorUtils.getErrorMsg(this@PairActivity, code)
                    }

                    override fun onSuccess() {
                        runOnUiThread {
                            with(binding.txtMessage) {
                                text = getString(R.string.content_pair_succ) + "，SN :" + sn
                                text = getText(R.string.content_next_page)
                            }
                            binding.btnNext.setEnabled(true)
                        }
                    }
                })
        }
        binding.btnNext.setOnClickListener { finish() }
    }
}