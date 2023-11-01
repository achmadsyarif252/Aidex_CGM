package com.syarif.aidex_cgm.fragnent

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.microtechmd.cgmlib.CgmManager
import com.microtechmd.cgmlib.constants.GlucoseTrend
import com.microtechmd.cgmlib.constants.SensorStatus
import com.microtechmd.cgmlib.entity.CgmStatusEntity
import com.syarif.aidex_cgm.R
import com.syarif.aidex_cgm.utils.TimeUtils


class GlucosePanelFragment : Fragment() {
    var txtGlucoseTime: TextView? = null
    var txtGlucose: TextView? = null
    var txtStatus: TextView? = null
    var txtRemainTime: TextView? = null
    var txtGlucoseTrends: TextView? = null
    var countDownTimer: CountDownTimer? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.frag_glucose_panel, null)
        txtRemainTime = view.findViewById<TextView>(R.id.txt_remain_time)
        txtGlucoseTime = view.findViewById<TextView>(R.id.txt_time)
        txtStatus = view.findViewById<TextView>(R.id.txt_state)
        txtGlucose = view.findViewById<TextView>(R.id.txt_glucose)
        txtGlucoseTrends = view.findViewById<TextView>(R.id.txt_trend)
        val cgmStatus:CgmStatusEntity? = CgmManager.getInstance().cgmStatus
        setGlucoseInfo(cgmStatus)
        CgmManager.getInstance()
            .addCgmStatusChangeListener { broadcast -> setGlucoseInfo(broadcast) }
        return view
    }

    fun setGlucoseInfo(broadcast: CgmStatusEntity?) {
        if (broadcast == null || context == null || !isAdded) return
        txtGlucose!!.text = broadcast.glucose.toString()
        txtGlucoseTime?.text = TimeUtils.format(broadcast.glucoseTime)
        if (broadcast.state == SensorStatus.SENSOR_STATUS_NORMAL) {
            txtStatus!!.text = getString(R.string.sensor_status_normal)
        } else if (broadcast.state == SensorStatus.SENSOR_STATUS_STABLE) {
            txtStatus!!.text = getString(R.string.sensor_status_stable)
        } else if (broadcast.state == SensorStatus.SENSOR_STATUS_ERROR) {
            txtStatus!!.text = getString(R.string.sensor_status_error)
        } else if (broadcast.state == SensorStatus.SENSOR_STATUS_REPLACE) {
            txtStatus!!.text = getString(R.string.sensor_status_replace)
        } else if (broadcast.state == SensorStatus.SENSOR_STATUS_EXPIRATION) {
            txtStatus!!.text = getString(R.string.sensor_status_expiration)
        }
        if (broadcast.glucoseTread == GlucoseTrend.GLUCOSE_TREND_UNKOWN.toFloat()) {
            txtGlucoseTrends!!.text = getString(R.string.trend_0)
        } else if (broadcast.glucoseTread == GlucoseTrend.GLUCOSE_TREND_FAST_UP.toFloat()) {
            txtGlucoseTrends!!.text = getString(R.string.trend_1)
        } else if (broadcast.glucoseTread == GlucoseTrend.GLUCOSE_TREND_UP.toFloat()) {
            txtGlucoseTrends!!.text = getString(R.string.trend_2)
        } else if (broadcast.glucoseTread == GlucoseTrend.GLUCOSE_TREND_SLOW_UP.toFloat()) {
            txtGlucoseTrends!!.text = getString(R.string.trend_3)
        } else if (broadcast.glucoseTread == GlucoseTrend.GLUCOSE_TREND_STEADY.toFloat()) {
            txtGlucoseTrends!!.text = getString(R.string.trend_4)
        } else if (broadcast.glucoseTread == GlucoseTrend.GLUCOSE_TREND_SLOW_DOWN.toFloat()) {
            txtGlucoseTrends!!.text = getString(R.string.trend_5)
        } else if (broadcast.glucoseTread == GlucoseTrend.GLUCOSE_TREND_DOWN.toFloat()) {
            txtGlucoseTrends!!.text = getString(R.string.trend_6)
        } else if (broadcast.glucoseTread == GlucoseTrend.GLUCOSE_TREND_FAST_DOWN.toFloat()) {
            txtGlucoseTrends!!.text = getString(R.string.trend_7)
        }
        txtRemainTime!!.text = String.format(getString(R.string.remain_hour), broadcast.remainHour)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        countDownTimer = object : CountDownTimer(Long.MAX_VALUE, (60 * 1000).toLong()) {
            override fun onTick(l: Long) {
                //更新下最新时间
                val cgmStatus:CgmStatusEntity? = CgmManager.getInstance().cgmStatus
                if (cgmStatus != null && cgmStatus.state == SensorStatus.SENSOR_STATUS_NORMAL && txtGlucoseTime != null) {
                    txtGlucoseTime?.text = TimeUtils.format(cgmStatus.glucoseTime)
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

    override fun onResume() {
        super.onResume()
    }
}
