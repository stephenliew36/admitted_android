package com.realtouchapp.admitted.member.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.databinding.AdapterRecycleviewTargetschoolAddkBinding
import com.realtouchapp.admitted.network.entity.TargetSchool
import com.realtouchapp.tools.ext.setMargin
import com.realtouchapp.tools.ext.setTextSize
import com.realtouchapp.tools.ext.setViewSize
import com.realtouchapp.tools.ui.adapter.BaseRecyclerViewAdapter
import com.realtouchapp.tools.utils.UserInterfaceTool

class AddTargetSchoolRecyleViewAdapter(context: Context) : BaseRecyclerViewAdapter<TargetSchool, AddTargetSchoolRecyleViewAdapter.ViewHolder>(context) {
    //暫時保存勾到的
    var selectetMap = HashMap<String,String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterRecycleviewTargetschoolAddkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.tvAdapterRecycleviewTitle.text = dataList[position].schoolName
        if (selectetMap.containsKey(dataList[position].schoolId.toString())){
            dataList[position].isSelected =true

        }
        if (dataList[position].isSelected){
            holder.ivAdapterRecycleview.setImageResource(R.drawable.vd_ic_checkmark)
            holder.ivAdapterRecycleview.setTag(R.drawable.selector_cb_addschool_checked)
        }else{
            holder.ivAdapterRecycleview.setImageResource(R.drawable.vd_ic_cross)
            holder.ivAdapterRecycleview.setTag(R.drawable.selector_addschool_unchecked)
        }

        holder.root.setOnClickListener {
            if (holder.ivAdapterRecycleview.getTag() == R.drawable.selector_addschool_unchecked){
                holder.ivAdapterRecycleview.setImageResource(R.drawable.vd_ic_checkmark)
                holder.ivAdapterRecycleview.setTag(R.drawable.selector_cb_addschool_checked)
                dataList[position].isSelected = true
                selectetMap.put(dataList[position].schoolId.toString(),dataList[position].schoolName?:"")
            }else{
                holder.ivAdapterRecycleview.setImageResource(R.drawable.vd_ic_cross)
                holder.ivAdapterRecycleview.setTag(R.drawable.selector_addschool_unchecked)
                dataList[position].isSelected = false
                selectetMap.remove(dataList[position].schoolId.toString())
            }
        }
    }

    inner class ViewHolder(binding: AdapterRecycleviewTargetschoolAddkBinding) : RecyclerView.ViewHolder(binding.root) {
        val root:View = binding.root
        val tvAdapterRecycleviewTitle: AppCompatTextView = binding.tvAdapterRecycleviewTitle
        val ivAdapterRecycleview: AppCompatImageView = binding.ivAdapterRecycleview

        init {
//            binding.root.setViewSizeByRatioWH(.89f,.052f)
            var margin = UserInterfaceTool().getScreenHeightByRatio(binding.root.context ,0.0155f)
            binding.tvAdapterRecycleviewTitle.setMargin(0,margin,0,margin)
            binding.tvAdapterRecycleviewTitle.setTextSize(15)

            val iv_size = UserInterfaceTool().getScreenHeightWithoutBarByRatio(binding.root.context,.018f);
            ivAdapterRecycleview.setViewSize(iv_size,iv_size)

            ivAdapterRecycleview.setTag(R.drawable.selector_addschool_unchecked);
            ivAdapterRecycleview.setImageResource(R.drawable.vd_ic_cross)

        }
    }

}
