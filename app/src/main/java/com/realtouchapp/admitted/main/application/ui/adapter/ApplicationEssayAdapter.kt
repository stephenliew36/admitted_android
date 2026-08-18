package com.realtouchapp.admitted.main.application.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.databinding.AdapterEssayCheckBinding
import com.realtouchapp.admitted.ext.closeSoftIntPut
import com.realtouchapp.admitted.member.view.adapter.SpinnerArrayAdapter
import com.realtouchapp.admitted.network.entity.ApplicationSchool
import com.realtouchapp.admitted.network.entity.Essay
import com.realtouchapp.admitted.network.entity.TargetSchool
import com.realtouchapp.tools.ext.setTextSize
import com.realtouchapp.tools.ext.setViewSizeOrignalWH
import com.realtouchapp.tools.ui.adapter.BaseRecyclerViewAdapter
import com.realtouchapp.tools.utils.UserInterfaceTool
import java.util.ArrayList

/*
    作者: RobertChou
    時間: 2023/2/18 下午 07:42
    信箱: didi31139@gmail.com
    說明:
*/
class ApplicationEssayAdapter(context: Context) : BaseRecyclerViewAdapter<Essay, ApplicationEssayAdapter.ViewHolder>(context){
    lateinit var onItemClick: ((ArrayList<Essay>) -> Unit)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterEssayCheckBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (dataList[position].isChecked ==1){
            holder.cbAdapterRecycleviewEssay.isChecked = true
            holder.cbAdapterRecycleviewEssay.text = "Complete"
        }else{
            holder.cbAdapterRecycleviewEssay.isChecked = false
            holder.cbAdapterRecycleviewEssay.text = ""
        }

        dataList[position].tittle?.let { holder.tvAdapterApplicationbriefName.text = "Prompt ${position+1} - "+it }
        holder.cbAdapterRecycleviewEssay.setOnCheckedChangeListener{cb, isChecked ->
            if (isChecked){
                dataList[position].isChecked =1
                cb.text = "Complete"
            }else{
                dataList[position].isChecked =0
                cb.text = ""
            }
            onItemClick(dataList)
        }

    }
    inner class ViewHolder(binding: AdapterEssayCheckBinding) : RecyclerView.ViewHolder(binding.root) {
        val cbAdapterRecycleviewEssay: AppCompatCheckBox = binding.cbAdapterRecycleviewEssay
        val tvAdapterApplicationbriefName: AppCompatTextView = binding.tvAdapterRecycleviewEssay

        init {
            tvAdapterApplicationbriefName.setTextSize(15)
        }

    }

}
