package com.realtouchapp.admitted.main.roadmmap.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.common.ui.fragment.ImageZoomFragment
import com.realtouchapp.admitted.databinding.FragmentCoursetrackchartBinding
import com.realtouchapp.admitted.member.view.adapter.CourseTrackAdapter
import com.realtouchapp.admitted.network.entity.AreaOfInterest
import com.realtouchapp.tools.ext.setTextSize
import com.realtouchapp.tools.ext.setViewSizeOrignalWH

class CourseTrackChartFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentCoursetrackchartBinding? = null
    private val binding get() = _binding!!
    var title = ""
    companion object{
        const val BUNLDE_TITLE ="BUNLDE_TITLE"
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoursetrackchartBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    fun getData(){
        arguments?.let {
            title = it.getString(BUNLDE_TITLE)!!
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            getData()
            initialViewSize()
            initialView()
    }


    fun initialViewSize(){
        binding.ivCousetrackchartfragmentBack.setViewSizeOrignalWH(24, 24)
        binding.tvCousetrackchartfragmentBack.setTextSize(13)
        binding.tvCousetrackchartfragmentTitle.setTextSize(15)
    }

    fun initialView(){
        binding.tvCousetrackchartfragmentTitle.text = getString(R.string.btn_adapter_recycleview) +  title
        var courseTrackAdapter = CourseTrackAdapter(requireContext())
        courseTrackAdapter.setData(AreaOfInterest.getCourseTrack(requireContext(),title))
        courseTrackAdapter.onItemClick = {
            var bundle = Bundle()
            bundle.putSerializable(ImageZoomFragment.BUNDLE_IMAGESOURCEID,it)
            findNavController().navigate(R.id.action_navigation_coursetrackchart_to_navigation_courseimage, bundle)
        }

        var linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        binding.rvCousetrackchartfragment.adapter = courseTrackAdapter
        binding.rvCousetrackchartfragment.layoutManager = linearLayoutManager
        binding.llCousetrackchartfragmentBack.setOnClickListener(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.ll_cousetrackchartfragment_back -> {
                findNavController().popBackStack()
            }
        }
    }
}