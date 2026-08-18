package com.realtouchapp.admitted.main.explore.ui.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.begin.view.AdmittedApplication
import com.realtouchapp.admitted.databinding.FragmentExploredetailBinding
import com.realtouchapp.admitted.main.explore.viewmodel.ExtracurricularMainFragmentViewModel
import com.realtouchapp.admitted.network.entity.Extracurricular

class ExploreDetailFragment : Fragment(), View.OnClickListener {
    companion object{
        const val BUNDLE_EXTRACURRICULAR = "Extracurricular"
    }
    private var _binding: FragmentExploredetailBinding? = null
    private val viewModel: ExtracurricularMainFragmentViewModel by activityViewModels()
    private val binding get() = _binding!!
    lateinit var extracurricular: Extracurricular
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentExploredetailBinding.inflate(inflater, container, false)
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
            extracurricular= arguments?.getSerializable(BUNDLE_EXTRACURRICULAR, Extracurricular::class.java)!!
        } else {
            extracurricular = arguments?.getSerializable(BUNDLE_EXTRACURRICULAR) as Extracurricular
        }

    }

    fun initialViewSize(){

    }

    fun initialView(){
        binding.llExtraBack.setOnClickListener(this)
        extracurricular.name?.let { binding.tvTitle.text = it }
        extracurricular.address?.let { binding.tvAddress.text = it }
        binding.llCopy.setOnClickListener(this)
        binding.llContect.setOnClickListener(this)

        var contenxt = requireActivity().applicationContext as AdmittedApplication
        if (contenxt.lat == 0.0 && contenxt.lon == 0.0){
            binding.tvDistance.visibility = View.GONE
        }else{
            binding.tvDistance.visibility = View.VISIBLE
            binding.tvDistance.text = calculateDistanceInKilometers(contenxt.lat, contenxt.lon, extracurricular.lat.toDouble(), extracurricular.lng.toDouble()).toString() + " km"
        }
        binding.ivExtraFav.setOnClickListener(this)
        if (extracurricular.extracurricularId != null) {
            binding.ivExtraFav.setImageResource(R.drawable.vd_ic_favorite_filled)
        }else{
            binding.ivExtraFav.setImageResource(R.drawable.vd_ic_favorite_empty)
        }


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
            R.id.iv_extra_fav -> {
                if (extracurricular.extracurricularId ==null) {
                    extracurricular.extracurricularId = 1

                    binding.ivExtraFav.setImageResource(R.drawable.vd_ic_favorite_filled)
                }else{
                    binding.ivExtraFav.setImageResource(R.drawable.vd_ic_favorite_empty)
                    extracurricular.extracurricularId = null
                }
                viewModel.setFav(extracurricular)
            }
            R.id.llCopy -> {
                val textToCopy = "Subject: Volunteering at [Name of Shelter/Clinic]\n" +
                        "Dear [Name or \"Volunteer Coordinator\"],\n" +
                        "My name is [Your Name], and I’m a high school student at [Your School Name]. I’m reaching out because I’m passionate about [helping animals, giving back to the community, learning more about animal care, etc.] and would love the opportunity to volunteer at [Name of Shelter/Clinic].\n" +
                        "I believe my qualities, such as being empathetic, hardworking, and reliable, make me a good fit for supporting your team. I’m happy to help with [specific tasks, e.g., caring for animals, cleaning, organizing, or assisting with events], or anything else where assistance is needed.\n" +
                        "I am available [mention times, e.g., after school, on weekends, during school breaks] and would be excited to contribute in any way I can. Please let me know if you have any volunteer opportunities or forms I should fill out to get started.\n" +
                        "Thank you so much for your time and for the important work you do. I look forward to hearing from you!\n" +
                        "Sincerely,\n" +
                        "[Your Name]\n" +
                        "[Your Contact Information]"
                val clipData = ClipData.newPlainText("label", textToCopy)
                val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboardManager.setPrimaryClip(clipData)
            }

            R.id.llContect -> {
                viewModel.getPlacePhone(extracurricular,{result->
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
        }
    }
    fun calculateDistanceInKilometers(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadiusKm = 6371 // 地球半徑，單位：公里

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)

        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        var number = earthRadiusKm * c
        val wholePart = number.toInt()
        val decimalPart = (number % 1 * 10).toInt() / 10.0
        return wholePart + decimalPart
    }
}