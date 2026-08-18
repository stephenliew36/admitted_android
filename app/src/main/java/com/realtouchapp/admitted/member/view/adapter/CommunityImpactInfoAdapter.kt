package com.realtouchapp.admitted.member.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.databinding.AdapterCommunityimpactInfoBinding
import com.realtouchapp.admitted.network.entity.CommunityImpact
import com.realtouchapp.admitted.network.entity.IntellectualCuriosity
import com.realtouchapp.tools.ext.*
import com.realtouchapp.tools.ui.adapter.BaseRecyclerViewAdapter
import java.util.*
import kotlin.collections.ArrayList

class CommunityImpactInfoAdapter(context: Context) : BaseRecyclerViewAdapter<CommunityImpact, CommunityImpactInfoAdapter.ViewHolder>(context){
    lateinit var onItemClick: ((Int) -> Unit)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterCommunityimpactInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        dataList[position].title?.let {
            holder.tvAdapterRecycleviewTitle.text = it
        }
        dataList[position].role?.let {
            if (it==1){
                holder.tvAdapterRecycleviewRole.text = "．\t"+ mContext.getString(R.string.tv_bsdf_addciactivity_role_leader)
            }else if (it==2){
                holder.tvAdapterRecycleviewRole.text = "．\t"+ mContext.getString(R.string.tv_bsdf_addciactivity_role_member)
            }

        }
        dataList[position].type?.let {
            if (it==1){
                holder.tvAdapterRecycleviewType.text = "．\t"+ mContext.getString(R.string.tv_bsdf_addciactivity_type_international)
            }else if (it==2){
                holder.tvAdapterRecycleviewType.text = "．\t"+ mContext.getString(R.string.tv_bsdf_addciactivity_type_notinternational)
            }
        }
        dataList[position].hoursPerWeek?.let {hours->
            dataList[position].weekPerYear?.let { weeks ->
                if (hours == 0||weeks ==0){
                    holder.tvAdapterRecycleviewHours.text =""
                    holder.tvAdapterRecycleviewPerhour.text =""
                    holder.tvAdapterRecycleviewPerhour.visibility= View.GONE
                }else{
                    holder.tvAdapterRecycleviewHours.text = ((hours.toInt())*(weeks.toInt())).toString()+ holder.tvAdapterRecycleviewHours.context.getString(R.string.tv_bsdf_addciactivity_hours_unit)

                    val perHour :StringBuilder = StringBuilder()
                    if (hours ==1){
                        perHour.append(mContext.getString(R.string.tv_bsdf_addciactivity_perhour))
                    }else{
                        perHour.append(mContext.getString(R.string.tv_bsdf_addciactivity_perhours,"$hours" ))
                    }
                    if (weeks == 1) {
                        perHour.append(mContext.getString(R.string.tv_bsdf_addciactivity_perweek))
                    }else{
                        perHour.append(mContext.getString(R.string.tv_bsdf_addciactivity_perweeks,"$weeks"))
                    }

                    holder.tvAdapterRecycleviewPerhour.text = "．\t"+ perHour.toString()
                }

            }
        }
        dataList[position].grade?.let {
            if (it.isNullOrEmpty()||!it.contains("|")){
                return
            }
            var array = it.split("|")
            var result = "．\t"
            var list = ArrayList<String>()
            list.addAll(array)
            list.removeAt(list.lastIndex)
            list.removeAt(0)
            for (item in list){
                if (list.last() == item){
                    result = result +"Grade "+item
                }else{
                    result = result +"Grade "+item+","
                }
            }
            holder.tvAdapterRecycleviewGrade.text = result
        }
        dataList[position].description?.let {
            holder.tvAdapterRecycleviewDesc.text = it
        }
        holder.itemView.setOnClickListener {
            if (holder.llAdapterRecycleviewIc.visibility == View.VISIBLE){
                holder.llAdapterRecycleviewIc.visibility = View.GONE
                holder.ivAdapterRecycleviewArrow.setImageResource(R.drawable.vd_ic_arrow_down_black)
            }else{
                holder.llAdapterRecycleviewIc.visibility = View.VISIBLE
                holder.ivAdapterRecycleviewArrow.setImageResource(R.drawable.vd_ic_arrow_top)
            }
        }
        holder.llAdapterRecycleviewIc.setOnClickListener{
            onItemClick?.let { it(position) }
        }

    }

    inner class ViewHolder(binding: AdapterCommunityimpactInfoBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvAdapterRecycleviewTitle: AppCompatTextView = binding.tvAdapterRecycleviewTitle
        val tvAdapterRecycleviewHours: AppCompatTextView = binding.tvAdapterRecycleviewHours
        val ivAdapterRecycleviewArrow: AppCompatImageView = binding.ivAdapterRecycleviewArrow
        val llAdapterRecycleviewIc: LinearLayoutCompat = binding.llAdapterRecycleviewIc
        val tvAdapterRecycleviewRole: AppCompatTextView = binding.tvAdapterRecycleviewRole
        val tvAdapterRecycleviewType: AppCompatTextView = binding.tvAdapterRecycleviewType
        val tvAdapterRecycleviewPerhour: AppCompatTextView = binding.tvAdapterRecycleviewPerhour
        val tvAdapterRecycleviewGrade: AppCompatTextView = binding.tvAdapterRecycleviewGrade
        val tvAdapterRecycleviewDesc: AppCompatTextView = binding.tvAdapterRecycleviewDesc

        init {
            binding.root.setMarginTopOrignal(16)
            tvAdapterRecycleviewTitle.setTextSize(13)
            tvAdapterRecycleviewHours.setTextSize(13)

            ivAdapterRecycleviewArrow.setViewSizeOrignalWH(24,24)
            tvAdapterRecycleviewRole.setTextSize(13)
            tvAdapterRecycleviewType.setTextSize(13)
            tvAdapterRecycleviewPerhour.setTextSize(13)
            tvAdapterRecycleviewGrade.setTextSize(13)
            tvAdapterRecycleviewDesc.setTextSize(13)

        }

    }

}
