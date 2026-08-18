package com.realtouchapp.admitted.main.explore.ui.fragment

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
import com.realtouchapp.admitted.databinding.FragmentExploremainBinding
import com.realtouchapp.admitted.main.explore.ui.adapter.ExploreMainStatusAdapter
import com.realtouchapp.admitted.main.roadmmap.viewmodel.MainActivityViewModel
import com.realtouchapp.tools.ext.setViewSizeOrignalWidth

class ExploreMainFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentExploremainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainActivityViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentExploremainBinding.inflate(inflater, container, false)
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
        binding.tlExploremaint.setViewSizeOrignalWidth(280)
    }

    fun initialView(){
        binding.tlExploremaint.removeAllTabs()
        val pageTItle = resources.getStringArray(R.array.array_explore_tab)

        binding.tlExploremaint.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        val adapter = ExploreMainStatusAdapter(requireActivity(), 2)
        binding.vpExploremain.adapter = adapter
        binding.vpExploremain.isUserInputEnabled =false
        TabLayoutMediator(
            binding.tlExploremaint, binding.vpExploremain
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