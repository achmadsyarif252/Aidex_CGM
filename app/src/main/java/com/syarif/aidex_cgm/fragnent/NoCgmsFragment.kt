package com.syarif.aidex_cgm.fragnent

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.syarif.aidex_cgm.PairActivity
import com.syarif.aidex_cgm.databinding.FragCmgsEmptyBinding


class NoCgmsFragment : Fragment() {
    private lateinit var binding :FragCmgsEmptyBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragCmgsEmptyBinding.inflate(layoutInflater)
        binding.btnPair.setOnClickListener {
            startActivity(
                Intent(context, PairActivity::class.java)
            )
        }
        return binding.root
    }
}

