package com.realtouchapp.admitted.main.explore.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.begin.view.AdmittedApplication
import com.realtouchapp.admitted.databinding.FragmentExploregetallBinding
import com.realtouchapp.admitted.main.explore.ui.adapter.ExploreMainStatusAdapter
import com.realtouchapp.admitted.main.explore.ui.adapter.ExtracurricularsBirefAdapter
import com.realtouchapp.admitted.main.explore.viewmodel.ExtracurricularMainFragmentViewModel
import com.realtouchapp.admitted.main.roadmmap.viewmodel.MainActivityViewModel
import com.realtouchapp.admitted.network.entity.Extracurricular
import com.realtouchapp.tools.ext.setViewSizeOrignalWidth

class ExploreGetAllFragment : Fragment(), View.OnClickListener {
    companion object{
        const val BUNDLE_EXTRACURRICULAR = "Extracurricular"
    }
    private var _binding: FragmentExploregetallBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ExtracurricularMainFragmentViewModel by activityViewModels()
    lateinit var extracurriculars: ArrayList<Extracurricular>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentExploregetallBinding.inflate(inflater, container, false)
        val root: View = binding.root
        getData()
        initialViewSize()
        initialView()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    fun getData(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            extracurriculars= (arguments?.getSerializable(BUNDLE_EXTRACURRICULAR, ArrayList::class.java) as ArrayList<Extracurricular>?)!!
        } else {
            extracurriculars = arguments?.getSerializable(BUNDLE_EXTRACURRICULAR) as ArrayList<Extracurricular>
        }

    }

    fun initialViewSize(){

    }

    fun initialView(){
        binding.llExtraBack.setOnClickListener(this)
        var  gridLayoutManager = GridLayoutManager(requireContext(), 2,
            GridLayoutManager.VERTICAL,false)

        var extracurricularsBirefAdapter = ExtracurricularsBirefAdapter(requireContext())
        extracurricularsBirefAdapter.lat = (requireActivity().applicationContext as AdmittedApplication).lat
        extracurricularsBirefAdapter.lon = (requireActivity().applicationContext as AdmittedApplication).lon
        extracurricularsBirefAdapter.dataList = extracurriculars
        extracurricularsBirefAdapter.onItemClick={
            val bundle = Bundle()
            bundle.putSerializable(ExploreDetailFragment.BUNDLE_EXTRACURRICULAR,it)
            parentFragmentManager.findFragmentById(R.id.nav_host_fragment2)?.findNavController()!!.navigate(R.id.action_navigation_exploregetall_to_navigation_exploredetail, bundle)
        }
        extracurricularsBirefAdapter.postFavExtracurricular ={

        }
        extracurricularsBirefAdapter.deleteFavExtracurricular ={

        }
        extracurricularsBirefAdapter.contactMethod ={
            viewModel.getPlacePhone(it,{result->
                result?.let { result ->
                    if (result.formatted_phone_number == null){
                        Toast.makeText(requireContext(),"No phone number", Toast.LENGTH_SHORT).show()
                    }else{
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + result.formatted_phone_number))
                        startActivity(intent)
                    }
                }
            })

        }
        binding.rvGetAll.layoutManager = gridLayoutManager
        binding.rvGetAll.adapter = extracurricularsBirefAdapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.ll_extra_back -> {
                findNavController().popBackStack()
            }
        }
    }
}