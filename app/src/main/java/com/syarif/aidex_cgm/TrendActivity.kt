package com.syarif.aidex_cgm

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.microtechmd.cgmlib.CgmManager
import com.microtechmd.cgmlib.LogUtils
import com.microtechmd.cgmlib.utils.DateUtils
import com.syarif.aidex_cgm.databinding.ActivityTrendBinding
import java.util.Calendar
import java.util.Date

class TrendsActivity : AppCompatActivity() {
    var startDate: Date? = null
    var endDate: Date? = null
    private lateinit var binding: ActivityTrendBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrendBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ivBack.setOnClickListener { finish() }
        binding.txtStart.setOnClickListener { view ->
            selectDate(
                view as TextView,
                1
            )
        }
        binding.txtEnd.setOnClickListener { view ->
            selectDate(
                view as TextView,
                2
            )
        }
        binding.btnSearch.setOnClickListener(View.OnClickListener {
            if (startDate == null) {
                Toast.makeText(
                    this@TrendsActivity,
                    getString(R.string.content_select_beginDate),
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            if (endDate == null) {
                Toast.makeText(
                    this@TrendsActivity,
                    getString(R.string.content_select_endDate),
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            searchHistory()
        })
    }

    private fun searchHistory() {
        LogUtils.d("startDate :$startDate, endDate :$endDate")
        val glucoseTrends = CgmManager.getInstance().getGlucoseTrends(startDate, endDate, 3.3f, 10f)
        with(binding.txtMessage) {
            text = "hemoglobin terglikasi：" + glucoseTrends.hba1c
            text = "Glukosa darah harian rata-rata：" + glucoseTrends.mbg
            text = "risiko hipoglikemia：" + glucoseTrends.lbgi
            text =
                "Pemetaan AGP garis kuantil 10 persen：" + glucoseTrends.dailyPrctile10.contentToString()
            text = "Pemetaan AGP 25% garis kuantil：" + glucoseTrends.dailyPrctile25
            text =
                "Pemetaan AGP 50 persen garis kuantil：" + glucoseTrends.dailyPrctile50
            text = "Pemetaan AGP 75% garis kuantil：" + glucoseTrends.dailyPrctile75
            text =
                "Kuantil 90 persen dari pemetaan AGP：" + glucoseTrends.dailyPrctile90
            text =
                "Persentase waktu di atas target glukosa darah：" + glucoseTrends.hpt + "%"
            text =
                "Persentase waktu pada target glukosa darah：" + glucoseTrends.mpt + "%"
            text =
                "Persentase waktu di bawah target glukosa darah:" + glucoseTrends.lpt + "%"
        }

    }

    private fun selectDate(textView: TextView, type: Int) {
        val ca = Calendar.getInstance()
        val mYear = ca[Calendar.YEAR]
        val mMonth = ca[Calendar.MONTH]
        val mDay = ca[Calendar.DAY_OF_MONTH]
        val datePickerDialog = DatePickerDialog(
            this,
            OnDateSetListener { view, year, month, dayOfMonth ->
                val result = year.toString() + "-" + (month + 1) + "-" + dayOfMonth
                textView.text = result
                if (type == 1) {
                    startDate = DateUtils.stringToDate(result, DateUtils.DateFormat.ONLY_DAY)
                } else {
                    endDate = DateUtils.stringToDate(result, DateUtils.DateFormat.ONLY_DAY)
                }
                if (startDate == null) {
                    //                            Toast.makeText(HistoryActivity.this,"请选择开始时间",Toast.LENGTH_SHORT).show();
                    return@OnDateSetListener
                }
                if (endDate == null) {
                    //                            Toast.makeText(HistoryActivity.this,"请选择结束时间",Toast.LENGTH_SHORT).show();
                    return@OnDateSetListener
                }
                searchHistory()
            },
            mYear, mMonth, mDay
        )
        datePickerDialog.show()
    }
}