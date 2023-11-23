package com.syarif.aidex_cgm.fragnent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.syarif.aidex_cgm.databinding.FragNewSensorBinding


class NewSensorFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragNewSensorBinding.inflate(layoutInflater)
        return binding.root
    }
}

