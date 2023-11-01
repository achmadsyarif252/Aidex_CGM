package com.syarif.aidex_cgm.fragnent

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.syarif.aidex_cgm.PairActivity
import com.syarif.aidex_cgm.R


class NoCgmsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.frag_cmgs_empty, null)
        view.findViewById<View>(R.id.btn_pair).setOnClickListener {
            startActivity(
                Intent(context, PairActivity::class.java)
            )
        }
        return view
    }
}

