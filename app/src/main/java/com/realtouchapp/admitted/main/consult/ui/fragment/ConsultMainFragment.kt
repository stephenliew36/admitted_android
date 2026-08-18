package com.realtouchapp.admitted.main.consult.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.databinding.FragmentConsultmainBinding
import com.realtouchapp.admitted.main.application.ui.adapter.ApplicationMainStatusAdapter
import com.realtouchapp.admitted.main.roadmmap.ui.activity.MainActivity
import com.realtouchapp.admitted.main.roadmmap.viewmodel.MainActivityViewModel
import com.realtouchapp.tools.ext.setTextSize
import com.realtouchapp.tools.ext.setViewSizeOrignalWH
import com.realtouchapp.tools.ext.setViewSizeOrignalWidth
import com.realtouchapp.tools.ext.setViewSizeWidthByRatio

class ConsultMainFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentConsultmainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainActivityViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentConsultmainBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initialViewSize()
        initialView()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUi()
    }

    fun subscribeUi() {

    }

    fun initialViewSize(){
        binding.ivConsultmain.setViewSizeOrignalWH(225,225)
        binding.tvConsultmainTitle.setTextSize(22)
        binding.tvConsultmainHint.setTextSize(15)
        binding.btnConsultmainRoadmap.setTextSize(13)
    }

    fun initialView(){
        binding.btnConsultmainRoadmap.setOnClickListener {
            (requireActivity() as MainActivity).setSelecetTab(0)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onClick(view: View) {
        when (view.id) {
        }
    }
}