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
import com.realtouchapp.admitted.databinding.AdapterHonorsInfoBinding
import com.realtouchapp.admitted.network.entity.Honors
import com.realtouchapp.tools.ext.*
import com.realtouchapp.tools.ui.adapter.BaseRecyclerViewAdapter
import java.util.*

class HonorsInfoAdapter(context: Context) : BaseRecyclerViewAdapter<Honors, HonorsInfoAdapter.ViewHolder>(context){
    lateinit var onItemClick: ((Int) -> Unit)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterHonorsInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        dataList[position].title?.let {
            holder.tvAdapterRecycleviewTitle.text = it
        }

        dataList[position].type?.let {
            holder.tvAdapterRecycleviewRank.visibility = View.GONE
            if (it==1){
                holder.tvAdapterRecycleviewType.text = "．\t"+ mContext.getString(R.string.et_bsdf_addhonors_type_competion)
                dataList[position].rank?.let {rank->
                    holder.tvAdapterRecycleviewRank.visibility = View.VISIBLE
                    holder.tvAdapterRecycleviewRank.text =
                        "．\t" + mContext.resources.getStringArray(R.array.array_honors_rank)
                            .get(rank - 1)
                }

            }else if (it == 2){
                holder.tvAdapterRecycleviewType.text = "．\t"+ mContext.getString(R.string.et_bsdf_addhonors_type_certificate)


            }else if (it==3){
                holder.tvAdapterRecycleviewType.text = "．\t"+ mContext.getString(R.string.et_bsdf_addhonors_type_research)
                dataList[position].researchType?.let {researchType->
                    holder.tvAdapterRecycleviewRank.visibility = View.VISIBLE
                    holder.tvAdapterRecycleviewRank.text = "．\t"+ mContext.resources.getStringArray(R.array.array_honors_research_type).get(researchType-1)
                }

            } else {

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

        dataList[position].level?.let {
            if (it>3){
                return
            }
            holder.tvAdapterRecycleviewRecognition.text =
                "．\t" + mContext.resources.getStringArray(R.array.array_honors_recognition)
                    .get(it - 1)
        }

        holder.tvAdapterRecycleviewTitle.setOnClickListener {
            if (holder.llAdapterRecycleviewIc.visibility == View.VISIBLE){
                holder.llAdapterRecycleviewIc.visibility = View.GONE
                holder.ivAdapterRecycleviewArrow.setImageResource(R.drawable.vd_ic_arrow_down_black)
            }else{
                holder.llAdapterRecycleviewIc.visibility = View.VISIBLE
                holder.ivAdapterRecycleviewArrow.setImageResource(R.drawable.vd_ic_arrow_top)
            }
        }
        holder.clAdapterRecycleview.setOnClickListener {
            if (holder.llAdapterRecycleviewIc.visibility == View.VISIBLE){
                holder.llAdapterRecycleviewIc.visibility = View.GONE
                holder.ivAdapterRecycleviewArrow.setImageResource(R.drawable.vd_ic_arrow_down_black)
            }else{
                holder.llAdapterRecycleviewIc.visibility = View.VISIBLE
                holder.ivAdapterRecycleviewArrow.setImageResource(R.drawable.vd_ic_arrow_top)
            }
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
            onItemClick?.let {
                it(position) }
        }


    }

    inner class ViewHolder(binding: AdapterHonorsInfoBinding) : RecyclerView.ViewHolder(binding.root) {
        val clAdapterRecycleview: View = binding.clAdapterRecycleview
        val tvAdapterRecycleviewTitle: AppCompatTextView = binding.tvAdapterRecycleviewTitle
        val ivAdapterRecycleviewArrow: AppCompatImageView = binding.ivAdapterRecycleviewArrow
        val llAdapterRecycleviewIc: LinearLayoutCompat = binding.llAdapterRecycleviewIc
        val tvAdapterRecycleviewType: AppCompatTextView = binding.tvAdapterRecycleviewType
        val tvAdapterRecycleviewRank: AppCompatTextView = binding.tvAdapterRecycleviewRank
        val tvAdapterRecycleviewGrade: AppCompatTextView = binding.tvAdapterRecycleviewGrade
        val tvAdapterRecycleviewRecognition: AppCompatTextView = binding.tvAdapterRecycleviewRecognition

        init {
            binding.root.setMarginTopOrignal(16)
            tvAdapterRecycleviewTitle.setTextSize(13)

            ivAdapterRecycleviewArrow.setViewSizeOrignalWH(24,24)
            tvAdapterRecycleviewType.setTextSize(13)
            tvAdapterRecycleviewRank.setTextSize(13)
            tvAdapterRecycleviewGrade.setTextSize(13)
            tvAdapterRecycleviewRecognition.setTextSize(13)
        }

    }

}
