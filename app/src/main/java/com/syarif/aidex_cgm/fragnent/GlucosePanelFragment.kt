package com.syarif.aidex_cgm.fragnent

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.microtechmd.cgmlib.CgmManager
import com.microtechmd.cgmlib.constants.GlucoseTrend
import com.microtechmd.cgmlib.constants.SensorStatus
import com.microtechmd.cgmlib.entity.CgmStatusEntity
import com.syarif.aidex_cgm.R
import com.syarif.aidex_cgm.databinding.FragGlucosePanelBinding
import com.syarif.aidex_cgm.utils.TimeUtils


class GlucosePanelFragment : Fragment() {
    private lateinit var binding: FragGlucosePanelBinding
    private var countDownTimer: CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragGlucosePanelBinding.inflate(layoutInflater)
        val cgmStatus: CgmStatusEntity? = CgmManager.getInstance().cgmStatus
        setGlucoseInfo(cgmStatus)
        CgmManager.getInstance()
            .addCgmStatusChangeListener { broadcast -> setGlucoseInfo(broadcast) }
        return binding.root
    }

    private fun setGlucoseInfo(broadcast: CgmStatusEntity?) {
        if (broadcast == null || context == null || !isAdded) return
        binding.txtGlucose.text = broadcast.glucose.toString()
        binding.txtTime.text = TimeUtils.format(broadcast.glucoseTime)
        when (broadcast.state) {
            SensorStatus.SENSOR_STATUS_NORMAL -> {
                binding.txtState.text = getString(R.string.sensor_status_normal)
            }
            SensorStatus.SENSOR_STATUS_STABLE -> {
                binding.txtState.text = getString(R.string.sensor_status_stable)
            }
            SensorStatus.SENSOR_STATUS_ERROR -> {
                binding.txtState.text = getString(R.string.sensor_status_error)
            }
            SensorStatus.SENSOR_STATUS_REPLACE -> {
                binding.txtState.text = getString(R.string.sensor_status_replace)
            }
            SensorStatus.SENSOR_STATUS_EXPIRATION -> {
                binding.txtState.text = getString(R.string.sensor_status_expiration)
            }
        }
        when (broadcast.glucoseTread) {
            GlucoseTrend.GLUCOSE_TREND_UNKOWN.toFloat() -> {
                binding.txtGlucose.text = getString(R.string.trend_0)
            }
            GlucoseTrend.GLUCOSE_TREND_FAST_UP.toFloat() -> {
                binding.txtGlucose.text = getString(R.string.trend_1)
            }
            GlucoseTrend.GLUCOSE_TREND_UP.toFloat() -> {
                binding.txtGlucose.text = getString(R.string.trend_2)
            }
            GlucoseTrend.GLUCOSE_TREND_SLOW_UP.toFloat() -> {
                binding.txtGlucose.text = getString(R.string.trend_3)
            }
            GlucoseTrend.GLUCOSE_TREND_STEADY.toFloat() -> {
                binding.txtGlucose.text = getString(R.string.trend_4)
            }
            GlucoseTrend.GLUCOSE_TREND_SLOW_DOWN.toFloat() -> {
                binding.txtGlucose.text = getString(R.string.trend_5)
            }
            GlucoseTrend.GLUCOSE_TREND_DOWN.toFloat() -> {
                binding.txtGlucose.text = getString(R.string.trend_6)
            }
            GlucoseTrend.GLUCOSE_TREND_FAST_DOWN.toFloat() -> {
                binding.txtGlucose.text = getString(R.string.trend_7)
            }
        }
        binding.txtRemainTime.text =
            String.format(getString(R.string.remain_hour), broadcast.remainHour)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        countDownTimer = object : CountDownTimer(Long.MAX_VALUE, (60 * 1000).toLong()) {
            override fun onTick(l: Long) {
                //Diperbarui dengan waktu terbaru
                val cgmStatus: CgmStatusEntity? = CgmManager.getInstance().cgmStatus
                if (cgmStatus != null && cgmStatus.state == SensorStatus.SENSOR_STATUS_NORMAL && binding.txtTime != null) {
                    binding.txtTime.text = TimeUtils.format(cgmStatus.glucoseTime)
                }
            }

            override fun onFinish() {}
        }
        (countDownTimer as CountDownTimer).start()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (countDownTimer != null) {
            countDownTimer!!.cancel()
            countDownTimer = null
        }
    }
}
