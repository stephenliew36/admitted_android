package com.realtouchapp.admitted.main.application.ui.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.RecyclerView
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.databinding.AdapterApplicaitonBriefBinding
import com.realtouchapp.admitted.ext.closeSoftIntPut
import com.realtouchapp.admitted.member.view.adapter.SpinnerArrayAdapter
import com.realtouchapp.admitted.network.entity.ApplicationSchool
import com.realtouchapp.admitted.network.entity.CountryCode
import com.realtouchapp.admitted.network.entity.TargetSchool
import com.realtouchapp.tools.ext.gone
import com.realtouchapp.tools.ext.setTextSize
import com.realtouchapp.tools.ext.setViewSizeOrignalWH
import com.realtouchapp.tools.ext.visible
import com.realtouchapp.tools.ui.adapter.BaseRecyclerViewAdapter
import com.realtouchapp.tools.utils.UserInterfaceTool

/*
    作者: RobertChou
    時間: 2023/2/18 下午 07:42
    信箱: didi31139@gmail.com
    說明:
*/
class ApplicationBirefAdapter(
    context: Context,
    private var isStudent: Boolean = true
): BaseRecyclerViewAdapter<TargetSchool, ApplicationBirefAdapter.ViewHolder>(context) {

    lateinit var onItemClick: ((TargetSchool) -> Unit)
    lateinit var onItemEdit: ((ApplicationSchool) -> Unit)

    fun setStudent(isStudent: Boolean) {
        this.isStudent = isStudent
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterApplicaitonBriefBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.clAdapterApplicationbrief.setViewSizeOrignalWH(343, if (isStudent) 191 else 132)
        holder.clAdapterApplicationbrief.setOnClickListener {
            onItemClick(dataList[position])
        }
        dataList[position].schoolName?.let { holder.tvAdapterApplicationbriefName.text = it }
        var city =""
        dataList[position].city?.let {
            city = it+", "
        }
        dataList[position].country?.let {
            when(it){
                CountryCode.USA.code->city = city+"USA．"
                CountryCode.Canada.code->city = city+"CA．"
                CountryCode.UK.code->city = city+"UK．"
            }
        }
        holder.tvAdapterApplicationbriefCity.text = city
        var content =""
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
        holder.tvAdapterApplicationbriefInfo.text = content
        dataList[position].scoreAlgorithm?.let { holder.tvAdapterApplicationbriefChanceValue.text = it +"%"}

        dataList[position].regularDecisionApplicationDeadline?.let {
            holder.tvAdapterApplicationbriefRegulat.text = "Regular Application "+it
        }

        var tmppArrayList=ArrayList<String>()
        tmppArrayList.add("")
        tmppArrayList.addAll(mContext.resources.getStringArray(R.array.array_applystatus))
        val spinnerArrayAdapter= SpinnerArrayAdapter(mContext,tmppArrayList.toList())
        holder.sp_adapter_applicationbrief_applying.adapter = spinnerArrayAdapter
        var schoolID = dataList[position].schoolId
        holder.sp_adapter_applicationbrief_applying.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                    if (!tmppArrayList[pos].equals("")){
                        configureApplyingStatus(holder, pos-1, isStudent)

                        onItemEdit(ApplicationSchool(schoolID,pos-1))
                    }
                    closeSoftIntPut(holder.tvAdapterApplicationbriefApply)

                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    closeSoftIntPut(holder.tvAdapterApplicationbriefApply)
                }
            }
        holder.sp_adapter_applicationbrief_applying.dropDownVerticalOffset = UserInterfaceTool().getScreenHeightWithoutBarByRatio(holder.tvAdapterApplicationbriefApply.context,0.068f)
        //設定初始值不為0 才能觸發按相同位置
        holder.sp_adapter_applicationbrief_applying.setSelection(-1,false)
        dataList[position].applyingStatus?.let {
            configureApplyingStatus(holder, it, isStudent)
        }
    }

    private fun configureApplyingStatus(holder: ViewHolder, status: Int, isStudent: Boolean) {
        if (isStudent) {
            holder.groupAdapterApplicationbriefSpinners.visible()
            holder.tvAdapterApplicationbriefApplying.gone()
        } else {
            holder.groupAdapterApplicationbriefSpinners.gone()
            holder.tvAdapterApplicationbriefApplying.visible()
        }

        when (status) {
            0 -> {
                holder.tvAdapterApplicationbriefApply.text = "Applying"
                holder.tvAdapterApplicationbriefApply.setTextColor(Color.parseColor("#3F5D95"))
                holder.ivAdapterApplicationbriefApplying.setBackgroundResource(R.drawable.bg_corner5_f2f4f6)

                holder.tvAdapterApplicationbriefApplying.text = "APPLYING"
                holder.tvAdapterApplicationbriefApplying.setTextColor(Color.parseColor("#3F5D95"))
                holder.tvAdapterApplicationbriefApplying.setBackgroundResource(R.drawable.bg_stroke_9eaec5)
            }
            1 -> {
                holder.tvAdapterApplicationbriefApply.text = "Applied"
                holder.tvAdapterApplicationbriefApply.setTextColor(Color.parseColor("#3F5D95"))
                holder.ivAdapterApplicationbriefApplying.setBackgroundResource(R.drawable.bg_corner5_f2f4f6)

                holder.tvAdapterApplicationbriefApplying.text = "APPLIED"
                holder.tvAdapterApplicationbriefApplying.setTextColor(Color.parseColor("#3F5D95"))
                holder.tvAdapterApplicationbriefApplying.setBackgroundResource(R.drawable.bg_stroke_9eaec5)
            }
            2 -> {
                holder.tvAdapterApplicationbriefApply.text = "Waitlisted"
                holder.tvAdapterApplicationbriefApply.setTextColor(Color.parseColor("#4F5257"))
                holder.ivAdapterApplicationbriefApplying.setBackgroundResource(R.drawable.bg_corner5_f3f0dd)

                holder.tvAdapterApplicationbriefApplying.text = "WAITLISTED"
                holder.tvAdapterApplicationbriefApplying.setTextColor(Color.parseColor("#4F5257"))
                holder.tvAdapterApplicationbriefApplying.setBackgroundResource(R.drawable.bg_corner5_fdeab2)
            }
            3 -> {
                holder.tvAdapterApplicationbriefApply.text = "Accepted"
                holder.tvAdapterApplicationbriefApply.setTextColor(Color.parseColor("#4F5257"))
                holder.ivAdapterApplicationbriefApplying.setBackgroundResource(R.drawable.bg_corner5_cde3cf)

                holder.tvAdapterApplicationbriefApplying.text = "ACCEPTED"
                holder.tvAdapterApplicationbriefApplying.setTextColor(Color.parseColor("#FFFFFF"))
                holder.tvAdapterApplicationbriefApplying.setBackgroundResource(R.drawable.bg_corner5_679a6c)
            }
            4 -> {
                holder.tvAdapterApplicationbriefApply.text = "Rejected"
                holder.tvAdapterApplicationbriefApply.setTextColor(Color.parseColor("#4F5257"))
                holder.ivAdapterApplicationbriefApplying.setBackgroundResource(R.drawable.bg_corner5_dddddd)

                holder.tvAdapterApplicationbriefApplying.text = "REJECTED"
                holder.tvAdapterApplicationbriefApplying.setTextColor(Color.parseColor("#4F5257"))
                holder.tvAdapterApplicationbriefApplying.setBackgroundResource(R.drawable.bg_corner5_d6dbe3)
            }
            5 -> {
                holder.tvAdapterApplicationbriefApply.text = "Enrolled"
                holder.tvAdapterApplicationbriefApply.setTextColor(Color.parseColor("#4F5257"))
                holder.ivAdapterApplicationbriefApplying.setBackgroundResource(R.drawable.bg_corner5_d2dced)

                holder.tvAdapterApplicationbriefApplying.text = "ENROLLED"
                holder.tvAdapterApplicationbriefApplying.setTextColor(Color.parseColor("#FFFFFF"))
                holder.tvAdapterApplicationbriefApplying.setBackgroundResource(R.drawable.bg_corner5_3f5d95)
            }
            else -> {
                holder.tvAdapterApplicationbriefApply.text = "Applying"
                holder.tvAdapterApplicationbriefApply.setTextColor(Color.parseColor("#3F5D95"))
                holder.ivAdapterApplicationbriefApplying.setBackgroundResource(R.drawable.bg_corner5_f2f4f6)

                holder.tvAdapterApplicationbriefApplying.text = "APPLYING"
                holder.tvAdapterApplicationbriefApplying.setTextColor(Color.parseColor("#3F5D95"))
                holder.tvAdapterApplicationbriefApplying.setBackgroundResource(R.drawable.bg_stroke_9eaec5)
            }
        }
    }

    inner class ViewHolder(binding: AdapterApplicaitonBriefBinding) : RecyclerView.ViewHolder(binding.root) {
        val clAdapterApplicationbrief: ConstraintLayout = binding.clAdapterApplicationbrief
        val sp_adapter_applicationbrief_applying: AppCompatSpinner = binding.spAdapterApplicationbriefApplying
        val tvAdapterApplicationbriefName: AppCompatTextView = binding.tvAdapterApplicationbriefName
        val tvAdapterApplicationbriefCity: AppCompatTextView = binding.tvAdapterApplicationbriefCity
        val tvAdapterApplicationbriefInfo: AppCompatTextView = binding.tvAdapterApplicationbriefInfo
        val tvAdapterApplicationbriefChanceValue: AppCompatTextView = binding.tvAdapterApplicationbriefChanceValue
        val tvAdapterApplicationbriefChance: AppCompatTextView = binding.tvAdapterApplicationbriefChance
        val tvAdapterApplicationbriefRegulat: AppCompatTextView = binding.tvAdapterApplicationbriefRegulat
        val tvAdapterApplicationbriefApply: AppCompatTextView = binding.tvAdapterApplicationbriefApply
        val ivAdapterApplicationbriefArrow: AppCompatImageView = binding.ivAdapterApplicationbriefArrow
        val ivAdapterApplicationbriefApplying: AppCompatImageView = binding.ivAdapterApplicationbriefApplying
        val ivAdapterApplicationbriefArrowDown: AppCompatImageView = binding.ivAdapterApplicationbriefArrowDown

        val tvAdapterApplicationbriefApplying: AppCompatTextView = binding.tvAdapterApplicationbriefApplying
        val groupAdapterApplicationbriefSpinners: Group = binding.groupAdapterApplicationbriefSpinners

        init {
            clAdapterApplicationbrief.setViewSizeOrignalWH(343,191)
//            binding.root.setMarginTopOrignal(16)
            tvAdapterApplicationbriefName.setTextSize(15)
            tvAdapterApplicationbriefCity.setTextSize(13)
            tvAdapterApplicationbriefInfo.setTextSize(13)
            tvAdapterApplicationbriefChanceValue.setTextSize(22)
            tvAdapterApplicationbriefChance.setTextSize(10)
            tvAdapterApplicationbriefApply.setTextSize(13)
            tvAdapterApplicationbriefRegulat.setTextSize(10)
            tvAdapterApplicationbriefApplying.setTextSize(10)

            ivAdapterApplicationbriefArrow.setViewSizeOrignalWH(24,24)
            ivAdapterApplicationbriefArrowDown.setViewSizeOrignalWH(20,20)
            ivAdapterApplicationbriefApplying.setViewSizeOrignalWH(112,48)

//            tvAdapterRecycleviewType.setTextSize(13)
//            tvAdapterRecycleviewRank.setTextSize(13)
//            tvAdapterRecycleviewGrade.setTextSize(13)
//            tvAdapterRecycleviewRecognition.setTextSize(13)
        }

    }

}
