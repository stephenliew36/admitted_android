package com.realtouchapp.admitted.common.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.databinding.FragmentImagezoomBinding

/*
*/
class ImageZoomFragment : Fragment() {
    companion object{
        const val BUNDLE_IMAGEURL = "imageurl"
        const val BUNDLE_IMAGESOURCEID = "imagesourceid"
//        const val TAG_BACK = "ImageZoomFragmentBACK"


//        fun showCourseImage(fragmentManager: FragmentManager,imageSoucreId :Int){
//            //因為不想要返回被刷新，直接使用beginTransaction，不用nagivation
//            var bundle = Bundle()
//            bundle.putSerializable(BUNDLE_IMAGESOURCEID,imageSoucreId)
//            fragmentManager.beginTransaction()
////                .setCustomAnimations(R.anim.right_in,R.anim.left_out,R.anim.left_in,R.anim.right_out)
//                .replace(R.id.nav_host_fragment, ImageZoomFragment().apply {
//                    arguments = bundle
//                })
//                .addToBackStack(TAG_BACK)
//                .commit()
//        }

    }
    private var _binding: FragmentImagezoomBinding? = null

    private val binding get() = _binding!!
    private var imageUrl =""
    private var imageSourceID = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentImagezoomBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        initialView()
    }

    fun getData(){
        imageUrl= arguments?.getString(BUNDLE_IMAGEURL,"")!!
        imageSourceID= arguments?.getInt(BUNDLE_IMAGESOURCEID,0)!!

    }

    private fun initialView() {
        binding.llImagezoomfragmentBack.setOnClickListener {
            findNavController().popBackStack()
//            parentFragmentManager.popBackStack(TAG_BACK,FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
        if (imageSourceID !=0){
            binding.ivImagezoomfragment.setImageResource(imageSourceID)
        }
        if (imageUrl.isNotEmpty()){
            Glide.with(this)
                .load( imageUrl)
                .into(binding.ivImagezoomfragment)
        }

//        requireView().isFocusableInTouchMode = true
//        requireView().requestFocus()
//        requireView().setOnKeyListener { _, keyCode, event ->
//            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
//                // 处理返回键事件
//                parentFragmentManager.popBackStack(TAG_BACK,FragmentManager.POP_BACK_STACK_INCLUSIVE)
//                true
//            } else {
//                false
//            }
//        }

    }

    override fun onDestroyView() {
        requireView().setOnKeyListener(null)
        super.onDestroyView()
        _binding = null
    }






}