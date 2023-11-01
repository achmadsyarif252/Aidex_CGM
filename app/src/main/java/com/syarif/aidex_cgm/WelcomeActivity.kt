package com.syarif.aidex_cgm

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.microtechmd.cgmlib.CgmManager
import com.microtechmd.cgmlib.LogUtils
import com.microtechmd.cgmlib.inter.Callback
import com.syarif.aidex_cgm.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPermission.setOnClickListener { requestPermission() }
        binding.btnInit.setOnClickListener { init(true) }

        binding.btnNextError.setEnabled(false)
        binding.btnNext.setEnabled(false)
        binding.btnNext.setOnClickListener {
            startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
            finish()
        }
        binding.btnNextError.setOnClickListener {
            startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
            finish()
        }

//        init(true);
    }

    //Meminta izin Bluetooth
    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= 31) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_SCAN
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.BLUETOOTH_ADVERTISE,
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ), 2
                )
                binding.txtMessage.text = getString(R.string.content_permission_request)
            } else {
                binding.txtMessage.text = getString(R.string.content_permission_have)
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ), 2
                )
                binding.txtMessage.text = getString(R.string.content_permission_request)
            } else {
                binding.txtMessage.text = getString(R.string.content_permission_have)
            }
        }
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), 2
            )
            binding.txtMessage.text = getString(R.string.content_permission_request)
        } else {
//            txtMessage.setText(getString(R.string.content_permission_have));
        }
    }

    /**
     * SDK init
     */
    private fun init(isAutoNext: Boolean) {
        val testAppSecret = "weitai1234"
        CgmManager.getInstance().init(this, testAppSecret, object : Callback {
            override fun onFailure(code: Int) {
                LogUtils.e("onFailure :$code")
                val errorMsg = ErrorUtils.getErrorMsg(this@WelcomeActivity, code)
                binding.txtMessage.text = errorMsg
                binding.btnNextError.setEnabled(true)
            }

            override fun onSuccess() {
                binding.btnNextError.setEnabled(false)
                binding.txtMessage.text = getText(R.string.content_init_succ)
                runOnUiThread { binding.btnNext.setEnabled(true) }
                if (isAutoNext) {
                    startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
                    finish()
                }
            }
        })
    }
}