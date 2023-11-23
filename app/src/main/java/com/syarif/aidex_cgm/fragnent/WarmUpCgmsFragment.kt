package com.syarif.aidex_cgm.fragnent

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.microtechmd.cgmlib.CgmManager
import com.microtechmd.cgmlib.entity.CgmStatusEntity
import com.syarif.aidex_cgm.databinding.FragGlucoseWarmupBinding

class WarmUpCgmsFragment : Fragment() {
    private lateinit var binding: FragGlucoseWarmupBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragGlucoseWarmupBinding.inflate(layoutInflater)

        // Diperbarui dengan waktu terbaru
        val cgmStatus: CgmStatusEntity? = CgmManager.getInstance().cgmStatus
        if (cgmStatus != null && binding.txtRemainTime != null) {
            binding.txtRemainTime.text = "" + cgmStatus.warmUpRemainTime
        }
        CgmManager.getInstance()
            .addCgmStatusChangeListener { broadcast ->
                binding.txtRemainTime.text = "" + broadcast.warmUpRemainTime
            }
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val cgmStatus: CgmStatusEntity? = CgmManager.getInstance().cgmStatus
        if (cgmStatus != null && binding.txtRemainTime != null) {
            binding.txtRemainTime.text = "" + cgmStatus.warmUpRemainTime
        }
    }
}

