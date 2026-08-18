package com.realtouchapp.admitted.begin.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.ext.configureEdgeToEdge
import com.realtouchapp.admitted.databinding.ActivityInfoBinding
import com.realtouchapp.admitted.main.explore.ui.adapter.InfoImagesAdapter
import com.realtouchapp.tools.ext.setMargin
import com.realtouchapp.tools.ext.setViewSizeOrignalWH

class InfoActivity : AppCompatActivity(){
    companion object{
        fun Intent(activity: AppCompatActivity){
            val intent = Intent()
                .setClass(activity, InfoActivity::class.java)
            activity.startActivity(intent)
        }
    }
    private lateinit var binding: ActivityInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureEdgeToEdge()

        initialView()

    }
    private fun initialView(){
        binding.tvSkpi.setOnClickListener { finish() }
        binding.btnBeginStart.setOnClickListener { finish() }
        initialInfoViewPager()
    }


    private fun initialInfoViewPager(){
        var infoImages = arrayListOf<Int>(R.drawable.info1,R.drawable.info2,R.drawable.info3,R.drawable.info4,R.drawable.info5,R.drawable.info6)
        for (item in infoImages){
            var iv = ImageView(this)
            iv.setImageResource(R.drawable.vd_dot_default)
            binding.llBeginDot.addView(iv)
            iv.setViewSizeOrignalWH(8,8)
            iv.setMargin(4,4,4,4)
        }

        var adapter = InfoImagesAdapter(this)
        adapter.dataList = infoImages
        binding.vpBegin.adapter = adapter
        binding.vpBegin.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                var iv = binding.llBeginDot.getChildAt(position) as ImageView
                iv.setImageResource(R.drawable.vd_dot_enable)
                if (position < binding.llBeginDot.childCount-1){
                    var iv_after = binding.llBeginDot.getChildAt(position+1) as ImageView
                    iv_after.setImageResource(R.drawable.vd_dot_default)
                }
                if (position>0){
                    var iv_pre = binding.llBeginDot.getChildAt(position-1) as ImageView
                    iv_pre.setImageResource(R.drawable.vd_dot_default)
                }
                if (position ==5){
                    binding.btnBeginStart.visibility = View.VISIBLE
                }

                super.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })
    }
}