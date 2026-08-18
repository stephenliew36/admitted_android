package com.realtouchapp.admitted.member.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.databinding.AdapterRecycleviewInterestContentBinding
import com.realtouchapp.admitted.databinding.AdapterSchoolEditBinding
import com.realtouchapp.admitted.network.entity.CountryCode
import com.realtouchapp.admitted.network.entity.TargetSchool
import com.realtouchapp.cplanner.main.begin.ui.adaptercallback.SimpleItemTouchHelperCallback
import com.realtouchapp.tools.ext.*
import com.realtouchapp.tools.ui.adapter.BaseRecyclerViewAdapter
import java.util.*

class SchoolEditRecyleViewAdapter(context: Context) : BaseRecyclerViewAdapter<TargetSchool, SchoolEditRecyleViewAdapter.ViewHolder>(context),SimpleItemTouchHelperCallback.ItemTouchHelperAdapter  {
    lateinit var onItemClick: ((Int) -> Unit)
    var isEditing = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterSchoolEditBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (isEditing){
            holder.itemView.setOnClickListener {
                   holder.cbAdapterRecycleviewSchool.isChecked = !holder.cbAdapterRecycleviewSchool.isChecked
                dataList[position].isSelected = holder.cbAdapterRecycleviewSchool.isChecked
            }
            holder.cbAdapterRecycleviewSchool.visibility = View.VISIBLE
            holder.ivAdapterRecycleviewArrow.visibility = View.GONE
        }else{
            holder.itemView.setOnClickListener {
                onItemClick?.let { it -> it(position) }
            }
            holder.cbAdapterRecycleviewSchool.visibility = View.GONE
            holder.ivAdapterRecycleviewArrow.visibility = View.VISIBLE
        }
        holder.tvAdapterRecycleviewSchoolTitle.text = dataList[position].schoolName
        var content =""
        dataList[position].city?.let {
            content = it+", "
        }
        dataList[position].country?.let {
            when(it){
                CountryCode.USA.code->content = content+"USA．"
                CountryCode.Canada.code->content = content+"CA．"
                CountryCode.UK.code->content = content+"UK．"
            }
        }
        dataList[position].isPublic?.let {
            if (it == 0){
                //私立
                content = content+"Private．"
            }else{
                content = content+"Public．"
            }
        }
        dataList[position].tuition?.let {

            content = content+"$"+it
        }

        holder.tvAdapterRecycleviewContent.text = content
        holder.tvAdapterRecycleviewPercent.text = dataList[position].scoreAlgorithm+"%"

        if (position==0){
            holder.itemView.setMarginTopRatioHWithoutBar(0f)
            holder.ivAdapterRecycleviewSchoolDivider.visibility= View.GONE
        }
    }

    inner class ViewHolder(binding: AdapterSchoolEditBinding) : RecyclerView.ViewHolder(binding.root) {
        val ivAdapterRecycleviewSchoolDivider: AppCompatImageView = binding.ivAdapterRecycleviewSchoolDivider
        val cbAdapterRecycleviewSchool: AppCompatCheckBox = binding.cbAdapterRecycleviewSchool
        val tvAdapterRecycleviewSchoolTitle: AppCompatTextView = binding.tvAdapterRecycleviewSchoolTitle
        val tvAdapterRecycleviewContent: AppCompatTextView = binding.tvAdapterRecycleviewContent
        val tvAdapterRecycleviewPercent: AppCompatTextView = binding.tvAdapterRecycleviewPercent
        val ivAdapterRecycleviewArrow: AppCompatImageView = binding.ivAdapterRecycleviewArrow

        init {

            itemView.setViewSizeOrignalWH(342,60)
            cbAdapterRecycleviewSchool.setViewSizeOrignalWH(24,24)

            tvAdapterRecycleviewSchoolTitle.setTextSize(13)

//            tvAdapterRecycleviewContent.setMarginTopOrignal(4)
            tvAdapterRecycleviewContent.setTextSize(13)

            tvAdapterRecycleviewPercent.setTextSize(15)
            tvAdapterRecycleviewPercent.setMarginRightOrignal(5)

            ivAdapterRecycleviewArrow.setViewSizeOrignalWH(24,24)
            ivAdapterRecycleviewArrow.setMarginRightOrignal(5)

        }

    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {


        return true
    }

    override fun onItemDismiss(position: Int) {
        dataList.removeAt(position)
        notifyItemRemoved(position)
    }
}
