package com.realtouchapp.admitted.member.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.databinding.AdapterIntellectualcuriosityInfoBinding
import com.realtouchapp.admitted.databinding.AdapterRecycleviewInterestContentBinding
import com.realtouchapp.admitted.databinding.AdapterSchoolEditBinding
import com.realtouchapp.admitted.ext.testAction
import com.realtouchapp.admitted.network.entity.IntellectualCuriosity
import com.realtouchapp.cplanner.main.begin.ui.adaptercallback.SimpleItemTouchHelperCallback
import com.realtouchapp.tools.ext.*
import com.realtouchapp.tools.ui.adapter.BaseRecyclerViewAdapter
import java.util.*

class IntellectualCuriosityInfoAdapter(context: Context) : BaseRecyclerViewAdapter<IntellectualCuriosity, IntellectualCuriosityInfoAdapter.ViewHolder>(context){
    lateinit var onItemClick: ((Int) -> Unit)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterIntellectualcuriosityInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.tvAdapterRecycleviewTitle.text = ""
        dataList[position].title?.let {title->
            holder.tvAdapterRecycleviewTitle.text = title
            dataList[position].name?.let {name->
                holder.tvAdapterRecycleviewTitle.text = title +", " + name
            }
        }
        holder.tvAdapterRecycleviewRole.text = ""
        dataList[position].role?.let {
            if (it == 1){
                holder.tvAdapterRecycleviewRole.text = "．\t"+ mContext.getString(R.string.tv_bsdf_addciactivity_role_leader)
            }else if (it == 2){
                holder.tvAdapterRecycleviewRole.text = "．\t"+ mContext.getString(R.string.tv_bsdf_addciactivity_role_member)
            }

        }
        holder.tvAdapterRecycleviewType.text = ""
        dataList[position].type?.let {
            var arr = mContext.resources.getStringArray(R.array.array_icactivity_type)
                holder.tvAdapterRecycleviewType.text = "．\t"+ arr.get(it.toInt())
        }
        holder.tvAdapterRecycleviewHours.text = ""
        holder.tvAdapterRecycleviewPerhour.text = ""
        holder.tvAdapterRecycleviewPerhour.text = ""
        dataList[position].hoursPerWeek?.let {hours->
            dataList[position].weekPerYear?.let { weeks ->
                if (hours == null ||weeks ==null){
                    holder.tvAdapterRecycleviewHours.text =""
                    holder.tvAdapterRecycleviewPerhour.text =""
                    holder.tvAdapterRecycleviewPerhour.visibility= View.GONE
                }else{
                    holder.tvAdapterRecycleviewHours.text = buildString {
                        append(((hours) * (weeks.toInt())).toString())
                        append(holder.tvAdapterRecycleviewHours.context.getString(R.string.tv_bsdf_addciactivity_hours_unit))
                    }

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

                    "．\t$perHour".also { holder.tvAdapterRecycleviewPerhour.text = it }
                }

            }
        }
        holder.tvAdapterRecycleviewGrade.text = ""
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
        holder.tvAdapterRecycleviewDesc.text = ""
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

    inner class ViewHolder(binding: AdapterIntellectualcuriosityInfoBinding) : RecyclerView.ViewHolder(binding.root) {
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
