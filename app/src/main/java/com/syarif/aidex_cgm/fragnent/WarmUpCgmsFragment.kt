package com.syarif.aidex_cgm.fragnent

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.microtechmd.cgmlib.CgmManager
import com.microtechmd.cgmlib.entity.CgmStatusEntity
import com.syarif.aidex_cgm.R

class WarmUpCgmsFragment : Fragment() {
    var txtRemainTime: TextView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.frag_glucose_warmup, null)
        txtRemainTime = view.findViewById<TextView>(R.id.txt_remain_time)

        // Diperbarui dengan waktu terbaru
        val cgmStatus: CgmStatusEntity? = CgmManager.getInstance().cgmStatus
        if (cgmStatus != null && txtRemainTime != null) {
            txtRemainTime!!.text = "" + cgmStatus.warmUpRemainTime
        }
        CgmManager.getInstance()
            .addCgmStatusChangeListener { broadcast ->
                txtRemainTime?.text = "" + broadcast.warmUpRemainTime
            }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val cgmStatus:CgmStatusEntity? = CgmManager.getInstance().cgmStatus
        if (cgmStatus != null && txtRemainTime != null) {
            txtRemainTime!!.text = "" + cgmStatus.warmUpRemainTime
        }
    }
}

