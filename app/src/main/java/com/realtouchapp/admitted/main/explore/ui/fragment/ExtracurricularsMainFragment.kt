package com.realtouchapp.admitted.main.explore.ui.fragment

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.begin.view.AdmittedApplication
import com.realtouchapp.admitted.databinding.FragmentExtracurricularsmainBinding
import com.realtouchapp.admitted.ext.testPrint
import com.realtouchapp.admitted.main.explore.ui.adapter.ExtracurricularsBirefAdapter
import com.realtouchapp.admitted.main.explore.ui.adapter.ExtracurricularsCategoryAdapter
import com.realtouchapp.admitted.main.explore.ui.adapter.TargetSchoolCategoryAdapter
import com.realtouchapp.admitted.main.explore.ui.bsdialogfragment.ExtraFilterBSDailogFragment
import com.realtouchapp.admitted.main.explore.viewmodel.ExtracurricularMainFragmentViewModel
import com.realtouchapp.admitted.network.entity.ExtraFilter
import com.realtouchapp.admitted.network.entity.Extracurricular
import com.realtouchapp.admitted.network.entity.GooglePlaceNearby
import com.realtouchapp.tools.ext.setTextSize
import kotlinx.coroutines.*
import kotlin.collections.ArrayList

class ExtracurricularsMainFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentExtracurricularsmainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ExtracurricularMainFragmentViewModel by activityViewModels()
    private var extraFilter = ExtraFilter()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentExtracurricularsmainBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initialViewSize()
        initialView()
        getLocation()
        viewModel.getExtraGooglePlace("",extraFilter,{
            displayAdapter(it)
        })
        return root
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            // Fragment 已經顯示在畫面上

            //顯示toast
//            Toast.makeText(requireContext(), "顯示toast", Toast.LENGTH_SHORT).show()
        } else {
            // Fragment 被隱藏
//            Toast.makeText(requireContext(), "顯示toast", Toast.LENGTH_SHORT).show()
        }
    }
    fun showLocationDialog() {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setMessage("Please allow location permission to search for nearby institutions.")
            .setPositiveButton("ACCEPT", DialogInterface.OnClickListener { _, _ ->
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_PERMISSION
                )
            })
            .setNegativeButton("DENY", null)
            .create()

        alertDialog.show()
    }
    override fun onResume() {
        super.onResume()
        if (!isHidden) {
//            Toast.makeText(requireContext(), "顯示toast", Toast.LENGTH_SHORT).show()
//            getLocation()
            // Fragment 已經顯示在畫面上
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUi()
    }

    fun subscribeUi() {

    }

    private fun getLocation(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        // 檢查是否已經取得位置權限
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // 如果已經取得位置權限，則繼續取得位置
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        val latitude = location.latitude
                        val longitude = location.longitude

                        (requireActivity().application as AdmittedApplication).lat = latitude
                        (requireActivity().application as AdmittedApplication).lon = longitude
                        // 在這裡處理取得的經緯度資料
                        testPrint("位置","經度：$latitude, 緯度：$longitude")
                        viewModel.checkLocation(GooglePlaceNearby.Location(latitude,longitude))
                        viewModel.getExtraGooglePlace(binding.etExtracurricularsmain.text.toString().trim(),extraFilter,{
                            displayAdapter(it)
                        })
                        //收集資料
                    } else {
                        Toast.makeText(requireContext(), "Unable to get location information", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(requireContext(), "取得位置發生錯誤：${exception.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            // 否則，請求使用者授予位置權限
            showLocationDialog()
        }

    }
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 如果使用者授予位置權限，則繼續取得位置
                getLocation()
            } else {
                Toast.makeText(requireContext(), "Requires location permission to get near information", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
    }
    
    fun initialViewSize(){
        binding.etExtracurricularsmain.setTextSize(13)


    }
    private fun displayAdapter(datas :ArrayList<HashMap<String,ArrayList<Extracurricular>>>){
        if (extraFilter.isFilter() == false && searchText.equals("")){
            //根據分類顯示

            binding.rvFragmentExtracurricularsmain.layoutManager = linearLayoutManager
            extracurricularsCategoryAdapter.lat = (requireActivity().applicationContext as AdmittedApplication).lat
            extracurricularsCategoryAdapter.lon = (requireActivity().applicationContext as AdmittedApplication).lon
            binding.rvFragmentExtracurricularsmain.adapter = extracurricularsCategoryAdapter
            extracurricularsCategoryAdapter.dataList = datas
            extracurricularsCategoryAdapter.notifyDataSetChanged()
        }else{
            extracurricularsBirefAdapter.lat = (requireActivity().applicationContext as AdmittedApplication).lat
            extracurricularsBirefAdapter.lon = (requireActivity().applicationContext as AdmittedApplication).lon
            binding.rvFragmentExtracurricularsmain.adapter = extracurricularsBirefAdapter
            binding.rvFragmentExtracurricularsmain.layoutManager = gridLayoutManager
            if (datas.size>0){
                extracurricularsBirefAdapter.dataList = datas[0].getValue( datas[0].keys.toTypedArray().get(0))
            }else{
                extracurricularsBirefAdapter.dataList = ArrayList<Extracurricular>()
            }
            extracurricularsBirefAdapter.notifyDataSetChanged()

            //根據搜尋顯示


        }

    }
    lateinit var extracurricularsCategoryAdapter : ExtracurricularsCategoryAdapter
    lateinit var extracurricularsBirefAdapter : ExtracurricularsBirefAdapter
    lateinit var gridLayoutManager : GridLayoutManager
    lateinit var linearLayoutManager: LinearLayoutManager
    private var searchText = ""
    private var searchJob: Job? = null
    fun initialView(){

        binding.etExtracurricularsmain.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchText = s.toString().trim()
//                mainViewModel.getCategoryTagetSchools(s.toString(), targetSchooFilter)
            }

            override fun afterTextChanged(s: Editable?) {
                searchJob?.cancel()
                // 重置計時器
                searchJob = CoroutineScope(Dispatchers.Main).launch {
                    delay(200) // 延遲500毫秒
                    if (searchText == binding.etExtracurricularsmain.text.toString()) {
                        // 如果輸入框內容沒有更改，則發送 API 請求
                        viewModel.getExtraGooglePlace(searchText.trim(),extraFilter,{
                            displayAdapter(it)
                        })
                    }
                }

            }
        })

        gridLayoutManager = GridLayoutManager(requireContext(), 2,GridLayoutManager.VERTICAL,false)
        linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        extracurricularsCategoryAdapter = ExtracurricularsCategoryAdapter(requireContext())
        extracurricularsCategoryAdapter.lat = (requireActivity().applicationContext as AdmittedApplication).lat
        extracurricularsCategoryAdapter.lon = (requireActivity().applicationContext as AdmittedApplication).lon
        extracurricularsCategoryAdapter.dataList = ArrayList<HashMap<String,ArrayList<Extracurricular>>>()
        extracurricularsCategoryAdapter.onItemClick={
            val bundle = Bundle()
            bundle.putSerializable(ExploreDetailFragment.BUNDLE_EXTRACURRICULAR,it)
            parentFragmentManager.findFragmentById(R.id.nav_host_fragment2)?.findNavController()!!.navigate(R.id.action_navigation_explore_main_to_navigation_exploredetail, bundle)

        }
        extracurricularsCategoryAdapter.onGetALLClick ={
            val bundle = Bundle()
            bundle.putSerializable(ExploreDetailFragment.BUNDLE_EXTRACURRICULAR,it)
            parentFragmentManager.findFragmentById(R.id.nav_host_fragment2)?.findNavController()!!.navigate(R.id.action_navigation_explore_main_to_navigation_exploregetall, bundle)

        }
        extracurricularsCategoryAdapter.postFavExtracurricular ={
            viewModel.setFav(it)
        }
        extracurricularsCategoryAdapter.deleteFavExtracurricular ={
            viewModel.setFav(it)
        }
        extracurricularsCategoryAdapter.contactMethod ={
            viewModel.getPlacePhone(it,{result->
                result?.let { result ->
                    if (result.formatted_phone_number == null){
                        Toast.makeText(requireContext(),"No phone number",Toast.LENGTH_SHORT).show()
                    }else{
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + result.formatted_phone_number))
                        startActivity(intent)
                    }
                }
            })
        }

        binding.rvFragmentExtracurricularsmain.layoutManager = linearLayoutManager
        binding.rvFragmentExtracurricularsmain.adapter = extracurricularsCategoryAdapter




        extracurricularsBirefAdapter = ExtracurricularsBirefAdapter(requireContext())
        extracurricularsBirefAdapter.lat = (requireActivity().applicationContext as AdmittedApplication).lat
        extracurricularsBirefAdapter.lon = (requireActivity().applicationContext as AdmittedApplication).lon
        extracurricularsBirefAdapter.onItemClick={
            val bundle = Bundle()
            bundle.putSerializable(ExploreDetailFragment.BUNDLE_EXTRACURRICULAR,it)
            parentFragmentManager.findFragmentById(R.id.nav_host_fragment2)?.findNavController()!!.navigate(R.id.action_navigation_explore_main_to_navigation_exploredetail, bundle)
        }
        extracurricularsBirefAdapter.postFavExtracurricular ={
            viewModel.setFav(it)
        }
        extracurricularsBirefAdapter.deleteFavExtracurricular ={
            viewModel.setFav(it)
        }
        extracurricularsBirefAdapter.contactMethod ={
            viewModel.getPlacePhone(it,{result->
                result?.let { result ->
                    if (result.formatted_phone_number == null){
                        Toast.makeText(requireContext(),"No phone number",Toast.LENGTH_SHORT).show()
                    }else{
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + result.formatted_phone_number))
                        startActivity(intent)
                    }
                }
            })
        }

        binding.ivExtracurricularsmainFav.setOnClickListener(this)
        binding.ivExtracurricularsmainFilter.setOnClickListener(this)
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
            R.id.iv_extracurricularsmain_fav -> {
                if (extraFilter.isSaved){
                    extraFilter.isSaved = false
                    binding.ivExtracurricularsmainFav.setImageResource(R.drawable.vd_ic_favorite_empty)
                }else{
                    extraFilter.isSaved = true
                    binding.ivExtracurricularsmainFav.setImageResource(R.drawable.vd_ic_favorite_filled)
                }
                viewModel.getExtraGooglePlace(binding.etExtracurricularsmain.text.toString().trim(),extraFilter,{
                    displayAdapter(it)
                })
            }
            R.id.iv_extracurricularsmain_filter->{
                activity?.let {
                    val extraFilterBSDailogFragment = ExtraFilterBSDailogFragment()
                    extraFilterBSDailogFragment.extraFilter = extraFilter
                    extraFilterBSDailogFragment.onDone={
                        extraFilter = it.copy()
                        viewModel.getExtraGooglePlace(binding.etExtracurricularsmain.text.toString().trim(),extraFilter,{
                            displayAdapter(it)
                        })

//                        mainViewModel.getCategoryTagetSchools(binding.etTargetschool.text.toString(), targetSchooFilter,isFavFilter)
                        testPrint("onDone","collegesFilterBSDailogFragment")
                    }
                    extraFilterBSDailogFragment.showOnce(it.supportFragmentManager)
                }
            }
        }
    }
}