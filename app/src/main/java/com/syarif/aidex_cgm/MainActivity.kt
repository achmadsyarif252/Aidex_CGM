package com.syarif.aidex_cgm

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.ParcelUuid
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.microtechmd.cgmlib.CgmManager
import com.microtechmd.cgmlib.LogUtils
import com.microtechmd.cgmlib.constants.Constants
import com.microtechmd.cgmlib.constants.SensorStatus
import com.microtechmd.cgmlib.db.DbManager
import com.microtechmd.cgmlib.entity.CgmEntity
import com.microtechmd.cgmlib.entity.CgmStatusEntity
import com.microtechmd.cgmlib.inter.Callback
import com.microtechmd.cgmlib.inter.NewSensorCallBack
import com.syarif.aidex_cgm.databinding.ActivityMain2Binding
import com.syarif.aidex_cgm.fragnent.GlucosePanelFragment
import com.syarif.aidex_cgm.fragnent.NewSensorFragment
import com.syarif.aidex_cgm.fragnent.NoCgmsFragment
import com.syarif.aidex_cgm.fragnent.WarmUpCgmsFragment
import java.util.Date

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var glucosePanelFragment: GlucosePanelFragment? = null
    private var warmUpCgmsFragment: WarmUpCgmsFragment? = null
    private var newSensorFragment: NewSensorFragment? = null
    private var noCgmsFragment: NoCgmsFragment? = null
    private var mFragmentManager: FragmentManager? = null
    private lateinit var binding: ActivityMain2Binding
    private var isTask = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        mFragmentManager = supportFragmentManager
        glucosePanelFragment = GlucosePanelFragment()
        warmUpCgmsFragment = WarmUpCgmsFragment()
        newSensorFragment = NewSensorFragment()
        noCgmsFragment = NoCgmsFragment()

        binding.btnPairInfo.setOnClickListener(this)
        binding.btnTask.setOnClickListener(this)

        binding.btnTrends.setOnClickListener(this)
        binding.btnCalibrate.setOnClickListener(this)
        binding.btnSensorNew.setOnClickListener(this)
        binding.btnSensorOld.setOnClickListener(this)
        binding.btnUnpair.setOnClickListener(this)
        binding.btnGetInfo.setOnClickListener(this)
        binding.btnHistory.setOnClickListener(this)
        binding.btnForceDelete.setOnClickListener(this)
        binding.btnDateRange.setOnClickListener(this)
        binding.btnSensorOta.setOnClickListener(this)

        initCgm()
        CgmManager.getInstance().addHistoriesChangeListener {
            binding.txtMessage.text = getString(R.string.content_history_change)
            val endDate = Date()
            val beginDate = Date(endDate.time - 10 * 24 * 60 * 60 * 1000)
            CgmManager.getInstance().getHistory(null, beginDate, endDate)
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_date_range -> {
                val data: List<String> = DbManager.getInstance().getGlucoseDateRange()
                if (data.size > 1) {
                    binding.txtMessage.text = getString(R.string.content_history_begin) + data[0]
                    binding.txtMessage.text = getString(R.string.content_history_end) + data[1]
                } else {
                    binding.txtMessage.text = getString(R.string.content_history_empty)
                }
            }

            R.id.btn_pair_info -> {
                val entity: CgmEntity? = CgmManager.getInstance().cgmInfo
                if (entity != null) {
                    binding.txtMessage.text =
                        getString(R.string.content_pair_info) + entity.toString()
                } else {
                    binding.txtMessage.text = getString(R.string.content_pair_info) + "null"
                }
            }

            R.id.btn_task -> {
                isTask = !isTask
                CgmManager.getInstance().setEnabledTask(isTask)
                binding.btnTask.text =
                    if (isTask) getString(R.string.content_close_task) else getString(R.string.content_open_task)
                binding.txtMessage.text =
                    if (isTask) getString(R.string.content_close_task_) else getString(
                        R.string.content_open_task_
                    )
            }

            R.id.btn_calibrate, R.id.btn_sensor_new, R.id.btn_sensor_old, R.id.btn_unpair, R.id.btn_forceDelete, R.id.btn_sensor_ota -> showNormalDialog(
                view.id
            )

            R.id.btn_getInfo -> {
                //Dapatkan status pemancar saat ini
                val cgmStatus: CgmStatusEntity? = CgmManager.getInstance().cgmStatus
                if (cgmStatus != null) {
                    binding.txtMessage.text = getString(R.string.content_cgm_status) + cgmStatus
                } else {
                    binding.txtMessage.text = getString(R.string.content_cgm_status) + " null"
                }
            }

            R.id.btn_trends -> startActivity(Intent(this, TrendsActivity::class.java))
            R.id.btn_history -> startActivity(Intent(this, HistoryActivity::class.java))
            else -> {}
        }
    }

    private fun showNormalDialog(viewId: Int) {
        val normalDialog = AlertDialog.Builder(this@MainActivity)
        normalDialog.setMessage(getString(R.string.content_perform_action))
        normalDialog.setPositiveButton(getString(R.string.content_ok),
            DialogInterface.OnClickListener { dialog, which ->
                when (viewId) {
                    R.id.btn_calibrate -> {
                        if (binding.etCalibrationGlucose.getText().toString().isEmpty()) {
                            binding.txtMessage.text = getString(R.string.content_glucose_empty)
                            return@OnClickListener
                        }
                        binding.txtMessage.text = getString(R.string.content_calibrate_begin)
                        val glucose = binding.etCalibrationGlucose.getText().toString().toFloat()
                        CgmManager.getInstance().calibration(glucose, Date(), object : Callback {
                            override fun onFailure(code: Int) {
                                val errorMsg: String =
                                    ErrorUtils.getErrorMsg(this@MainActivity, code)
                                binding.txtMessage.text = errorMsg
                            }

                            override fun onSuccess() {
                                binding.txtMessage.text = getString(R.string.content_calibrate_succ)
                            }
                        })
                    }

                    R.id.btn_sensor_new -> {
                        binding.txtMessage.text = getString(R.string.content_sensor_new_begin)
                        CgmManager.getInstance().newSensor(true, Date(), object : Callback {
                            override fun onFailure(code: Int) {
                                val errorMsg: String =
                                    ErrorUtils.getErrorMsg(this@MainActivity, code)
                                binding.txtMessage.text = errorMsg
                            }

                            override fun onSuccess() {
                                binding.txtMessage.text =
                                    getString(R.string.content_sensor_new_succ)
                            }
                        })
                    }

                    R.id.btn_sensor_old -> {
                        binding.txtMessage.text = getString(R.string.content_sensor_old_begin)
                        CgmManager.getInstance().newSensor(false, Date(), object : Callback {
                            override fun onFailure(code: Int) {
                                val errorMsg: String =
                                    ErrorUtils.getErrorMsg(this@MainActivity, code)
                                binding.txtMessage.text = errorMsg
                            }

                            override fun onSuccess() {
                                binding.txtMessage.text =
                                    getString(R.string.content_sensor_old_succ)
                            }
                        })
                    }

                    R.id.btn_unpair -> {
                        binding.txtMessage.text = getString(R.string.content_unpair_begin)
                        CgmManager.getInstance().unPair(object : Callback {
                            override fun onFailure(code: Int) {
                                val errorMsg: String =
                                    ErrorUtils.getErrorMsg(this@MainActivity, code)
                                binding.txtMessage.text = errorMsg
                            }

                            override fun onSuccess() {
                                binding.txtMessage.text = getString(R.string.content_unpair_succ)
                                finish()
                                startActivity(
                                    Intent(
                                        this@MainActivity,
                                        MainActivity::class.java
                                    )
                                )
                            }
                        })
                    }

                    R.id.btn_sensor_ota -> {
                        binding.txtMessage.text = getString(R.string.content_ota_sensor)
                        CgmManager.getInstance()
                            .newSensor(true, Date(), true, object : NewSensorCallBack {
                                override fun onOtaStateChange(state: Int, isOtaSucc: Boolean) {
                                    LogUtils.error("[Demo]OTA State:$state")
                                    if (state == Constants.STATE_DFU_BEGIN) {
                                        binding.txtMessage.text =
                                            "Memulai transfer file OTA DFU yang sedang berlangsung"
                                    } else if (state == Constants.STATE_DFU_COMPLETE) {
                                        binding.txtMessage.text =
                                            "Transfer file OTA selesai, pemasangan dimulai"
                                    } else if (state == Constants.STATE_PAIR_SUCC) {
                                        binding.txtMessage.text =
                                            "Peningkatan OTA" + (if (isOtaSucc) "keberhasilan" else "gagal (misalnya percobaan)") + "，Pemasangan berhasil. Konfirmasi sensor baru...."
                                    } else if (state == Constants.STATE_SENSOR_SUBMIT_SUCC) {
                                        binding.txtMessage.text =
                                            "Peningkatan OTA" + (if (isOtaSucc) "keberhasilan" else "gagal (misalnya percobaan)") + "，Sensor baru dikonfirmasi berhasil"
                                    }
                                }

                                override fun onFailure(code: Int) {
                                    LogUtils.error("[Demo]OTA onFailure:$code")
                                    binding.txtMessage.text =
                                        ErrorUtils.getErrorMsg(this@MainActivity, code)
                                }

                                override fun onSuccess() {
                                    LogUtils.error("[Demo]OTA onSuccess")
                                    binding.txtMessage.text =
                                        getString(R.string.content_sensor_new_succ)
                                }
                            })
                    }

                    R.id.btn_forceDelete -> {
                        binding.txtMessage.text = getString(R.string.content_delete_succ)
                        CgmManager.getInstance().forceDelete()
                        finish()
                        startActivity(Intent(this@MainActivity, MainActivity::class.java))
                    }

                    else -> {}
                }
                dialog.dismiss()
            })
        normalDialog.setNegativeButton(
            getString(R.string.content_cancel)
        ) { dialog, _ -> dialog.dismiss() }
        // mendemonstrasikan
        normalDialog.show()
    }

    override fun onResume() {
        super.onResume()
        if (CgmManager.getInstance().cgmInfo == null
            && mFragmentManager != null
        ) {
            replaceFragment(NO_TRANSMITTER)
        }
    }

    @SuppressLint("MissingPermission")
    fun initCgm() {
        if (BluetoothAdapter.getDefaultAdapter().isEnabled) {
            //Pemindaian berkemampuan Bluetooth
            BluetoothAdapter.getDefaultAdapter().bluetoothLeScanner.startScan(
                buildScanFilters(),
                buildScanSettings(),
                scanCallback
            )
        } else {
            BluetoothAdapter.getDefaultAdapter().enable()
        }

        //Dapatkan nomor SN pemancar yang dipasangkan, jika tidak nol, berarti SDK telah dipasangkan dan dapat dioperasikan secara langsung. Jika nol, diperlukan pemasangan.        //For paired CGM information method
        //If return value is null,  it is not paired. For the first time, users must pair CGM via Bluetooth,  and the SDK will save the pairing information.
        val entity: CgmEntity? = CgmManager.getInstance().cgmInfo
        if (entity != null) {
            binding.txtMessage.text = entity.toString()
            replaceFragment(GLUCOSE_PANEL)
        } else {
            binding.txtMessage.text = "null"
        }

        // Menambahkan pendengar perubahan status CGM
        CgmManager.getInstance().addCgmStatusChangeListener { broadcast ->
            binding.txtMessage.text = getString(R.string.content_cgm_change) + broadcast
            if (broadcast.state === SensorStatus.SENSOR_SRTTUS_NEW) {
                replaceFragment(NEW_SENSOR)
                binding.llSensorNew.visibility = View.VISIBLE
                binding.llCalibration.visibility = View.GONE
            } else if (broadcast.state === SensorStatus.SENSOR_SRTTUS_WARMUP) {
                replaceFragment(WARMING_UP)
                binding.llSensorNew.visibility = View.GONE
                binding.llCalibration.visibility = View.GONE

            } else {
                replaceFragment(GLUCOSE_PANEL)
                binding.llSensorNew.visibility = View.GONE
                binding.llCalibration.visibility = View.VISIBLE
            }
        }
    }

    private fun replaceFragment(tagFrg: String) {
        val fragment: Fragment? = when (tagFrg) {
            NEW_SENSOR -> {
                newSensorFragment
            }

            WARMING_UP -> {
                warmUpCgmsFragment
            }

            NO_TRANSMITTER -> {
                noCgmsFragment
            }

            else -> {
                glucosePanelFragment
            }
        }
        fragment?.let {
            mFragmentManager!!.beginTransaction()
                .replace(R.id.frag_cgms_status, it).commitAllowingStateLoss()
        }
    }

    // scan callback
    private val scanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            //Transfer the scanned broadcast to the processing data
            CgmManager.getInstance().setScanData(result)
        }
    }

    fun buildScanFilters(): List<ScanFilter> {
        val scanFilterList: MutableList<ScanFilter> = ArrayList()
        val scanFilterBuilder = ScanFilter.Builder()
        val parcelUuidMask = ParcelUuid.fromString("0000F000-0000-1000-8000-00805F9B34FB")
        val parcelUuid = ParcelUuid.fromString("0000F001-0000-1000-8000-00805F9B34FB")
        scanFilterBuilder.setServiceUuid(parcelUuid, parcelUuidMask)
        scanFilterList.add(scanFilterBuilder.build())
        return scanFilterList
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun buildScanSettings(): ScanSettings {
        val scanSettingBuilder = ScanSettings.Builder()
        scanSettingBuilder.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        scanSettingBuilder.setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE)
        if (BluetoothAdapter.getDefaultAdapter().isOffloadedScanBatchingSupported) {
            scanSettingBuilder.setReportDelay(0L)
        }
        scanSettingBuilder.setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
        return scanSettingBuilder.build()
    }

    companion object {
        const val NO_TRANSMITTER = "noTransmitter"
        const val GLUCOSE_PANEL = "glucosePanel"
        const val WARMING_UP = "warmingUp"
        const val NEW_SENSOR = "newSensor"
    }
}