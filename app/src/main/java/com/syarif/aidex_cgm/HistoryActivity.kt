package com.syarif.aidex_cgm

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.microtechmd.cgmlib.CgmManager
import com.microtechmd.cgmlib.LogUtils
import com.microtechmd.cgmlib.entity.GlucoseEntity
import com.microtechmd.cgmlib.utils.DateUtils
import com.syarif.aidex_cgm.adapter.HistoryAdapter
import java.util.Calendar
import java.util.Date

class HistoryActivity : AppCompatActivity() {
    var mListHistory: RecyclerView? = null
    var listGlucose: List<GlucoseEntity>? = null
    var listStartUp: List<GlucoseEntity>? = null
    var listError: List<GlucoseEntity>? = null
    var listExpired: List<GlucoseEntity>? = null
    var listCalibration: List<GlucoseEntity>? = null
    private var tabLayout: TabLayout? = null
    private var startDate: Date? = null
    private var endDate: Date? = null
    private var etDeviceSn: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        tabLayout = findViewById(R.id.tabLayout)
        mListHistory = findViewById(R.id.list_history)
        etDeviceSn = findViewById(R.id.et_sn)
        mListHistory?.setLayoutManager(
            LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            )
        )
        findViewById<View>(R.id.iv_back).setOnClickListener { finish() }
        findViewById<View>(R.id.txt_start).setOnClickListener { view ->
            selectDate(
                view as TextView,
                1
            )
        }
        findViewById<View>(R.id.txt_end).setOnClickListener { view ->
            selectDate(
                view as TextView,
                2
            )
        }
        findViewById<View>(R.id.btn_search).setOnClickListener(View.OnClickListener {
            if (startDate == null) {
                Toast.makeText(
                    this@HistoryActivity,
                    getString(R.string.content_select_beginDate),
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            if (endDate == null) {
                Toast.makeText(
                    this@HistoryActivity,
                    getString(R.string.content_select_endDate),
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            searchHistory()
        })
    }

    private fun searchHistory() {
        val deviceSn = etDeviceSn!!.getText().toString()
        LogUtils.d("startDate :$startDate, endDate :$endDate")
        val history = CgmManager.getInstance().getHistory(deviceSn, startDate, endDate)
        listGlucose = history.listGlucose
        listStartUp = history.listStartUp
        listError = history.listError
        listExpired = history.listExpired
        listCalibration = history.listCalibration
        LogUtils.d("cgmHistory size: " + listGlucose?.size)
        //        mListHistory.setAdapter(new HistoryAdapter(this, listGlucose));
        val tabId = tabLayout!!.selectedTabPosition
        when (tabId) {
            0 -> {
                mListHistory!!.setAdapter(HistoryAdapter(
                    this@HistoryActivity,
                    listGlucose ?: listOf()
                ))
            }
            1 -> {
                mListHistory!!.setAdapter(HistoryAdapter(this@HistoryActivity, listStartUp ?: listOf()))
            }
            2 -> {
                mListHistory!!.setAdapter(HistoryAdapter(this@HistoryActivity, listError ?: listOf()))
            }
            3 -> {
                mListHistory!!.setAdapter(HistoryAdapter(this@HistoryActivity, listExpired ?: listOf()))
            }
            4 -> {
                mListHistory!!.setAdapter(
                    HistoryAdapter(
                        this@HistoryActivity,
                        listCalibration ?: listOf()
                    )
                )
            }
        }
        tabLayout!!.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                LogUtils.d("tab positon :" + tab.position)
                val tabId = tab.position
                if (tabId == 0) {
                    mListHistory!!.setAdapter(
                        HistoryAdapter(
                            this@HistoryActivity,
                            listGlucose ?: listOf()
                        )
                    )
                } else if (tabId == 1) {
                    mListHistory!!.setAdapter(
                        HistoryAdapter(
                            this@HistoryActivity,
                            listStartUp ?: listOf()
                        )
                    )
                } else if (tabId == 2) {
                    mListHistory!!.setAdapter(
                        HistoryAdapter(
                            this@HistoryActivity,
                            listError ?: listOf()
                        )
                    )
                } else if (tabId == 3) {
                    mListHistory!!.setAdapter(
                        HistoryAdapter(
                            this@HistoryActivity,
                            listExpired ?: listOf()
                        )
                    )
                } else if (tabId == 4) {
                    mListHistory!!.setAdapter(
                        HistoryAdapter(
                            this@HistoryActivity,
                            listCalibration ?: listOf()
                        )
                    )
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
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