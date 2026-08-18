package com.realtouchapp.admitted.main.application.ui.fragment

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
import com.realtouchapp.admitted.databinding.FragmentApplicationmainBinding
import com.realtouchapp.admitted.main.application.ui.adapter.ApplicationMainStatusAdapter
import com.realtouchapp.admitted.main.roadmmap.viewmodel.MainActivityViewModel
import com.realtouchapp.tools.ext.setViewSizeOrignalWidth
import com.realtouchapp.tools.ext.setViewSizeWidthByRatio

class ApplicationMainFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentApplicationmainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainActivityViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentApplicationmainBinding.inflate(inflater, container, false)
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
        binding.tlApplicationmain.setViewSizeOrignalWidth(280)
    }

    fun initialView(){
        binding.tlApplicationmain.removeAllTabs()
        var pageTItle = resources.getStringArray(R.array.array_application_tab)

        binding.tlApplicationmain.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        val adapter = ApplicationMainStatusAdapter(requireActivity(), 2)
        binding.vpApplicationmain.adapter = adapter
        binding.vpApplicationmain.isUserInputEnabled =false
        TabLayoutMediator(
            binding.tlApplicationmain, binding.vpApplicationmain
        ) { tab, position -> tab.text = pageTItle.get(position) }.attach()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.ll_honorsmainfragment_back -> {
                findNavController().popBackStack()
            }
            R.id.rl_honorsmainfragment_add -> {
                activity?.supportFragmentManager?.let {
                }

            }
        }
    }
}