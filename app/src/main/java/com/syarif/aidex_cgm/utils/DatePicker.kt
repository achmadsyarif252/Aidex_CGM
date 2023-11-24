package com.syarif.aidex_cgm.utils

import android.app.DatePickerDialog
import android.content.Context
import android.widget.TextView
import com.microtechmd.cgmlib.utils.DateUtils
import java.util.Calendar
import java.util.Date

object DatePicker {
    fun selectDate(
        context: Context,
        textView: TextView,
        type: Int,
        searchHistory: () -> Unit,
        onDateSelected: (startDate: Date?, endDate: Date?, errorMessage: String?) -> Unit
    ) {
        val ca = Calendar.getInstance()
        val mYear = ca[Calendar.YEAR]
        val mMonth = ca[Calendar.MONTH]
        val mDay = ca[Calendar.DAY_OF_MONTH]

        var startDate: Date?
        var endDate: Date?
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val result = year.toString() + "-" + (month + 1) + "-" + dayOfMonth
                textView.text = result
                if (type == 1) {
                    startDate = DateUtils.stringToDate(result, DateUtils.DateFormat.ONLY_DAY)
                    onDateSelected(
                        startDate,
                        null, "Please select a start time"
                    )
                } else {
                    endDate = DateUtils.stringToDate(result, DateUtils.DateFormat.ONLY_DAY)
                    onDateSelected(
                        endDate,
                        null,
                        "Please select an end time"
                    )
                    searchHistory()
                }
            },
            mYear, mMonth, mDay
        )
        datePickerDialog.show()
    }
}