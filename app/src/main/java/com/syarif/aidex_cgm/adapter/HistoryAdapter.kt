package com.syarif.aidex_cgm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.microtechmd.cgmlib.entity.GlucoseEntity
import com.syarif.aidex_cgm.R


class HistoryAdapter(var context: Context, var cgmHistory: List<GlucoseEntity>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(LayoutInflater.from(context).inflate(R.layout.item_history, null))
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = cgmHistory[position]
        holder.txtEventIndex.text = "设备" + history.deviceSn
        holder.txtDeviceTime.text = history.deviceTime
        holder.txtGlucose.text = if (history.glucose != null) history.glucose.toString() else ""
    }

    override fun getItemCount(): Int {
        return cgmHistory.size
    }

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtEventIndex: TextView
        var txtDeviceTime: TextView
        var txtGlucose: TextView

        init {
            txtGlucose = itemView.findViewById<TextView>(R.id.txt_glucose)
            txtDeviceTime = itemView.findViewById<TextView>(R.id.txt_deviceTime)
            txtEventIndex = itemView.findViewById<TextView>(R.id.txt_eventindex)
        }
    }
}

