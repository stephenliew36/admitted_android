package com.realtouchapp.admitted.main.explore.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.databinding.AdapterTargetschoolBriefBinding
import com.realtouchapp.admitted.common.parity.SchoolParityRules
import com.realtouchapp.admitted.network.entity.TargetSchool
import com.realtouchapp.tools.ext.setTextSize
import com.realtouchapp.tools.ext.setViewSizeOrignalWH
import com.realtouchapp.tools.ui.adapter.BaseRecyclerViewAdapter

/*
    作者: RobertChou
    時間: 2023/2/18 下午 07:42
    信箱: didi31139@gmail.com
    說明:
*/
class TargetSchoolBirefAdapter(context: Context, val isFreeVersion: Boolean) : BaseRecyclerViewAdapter<TargetSchool, TargetSchoolBirefAdapter.ViewHolder>(context){
    lateinit var onItemClick: ((TargetSchool) -> Unit)
    lateinit var isInUserTargetSchool: ((Int) -> Boolean)
    lateinit var isInApplicationSchool: ((Int) -> Boolean)
    lateinit var postUserTargetSchool: ((Int) -> Unit)
    lateinit var postApplicationSchool: ((Int) -> Unit)
    lateinit var deleteUserTargetSchool: ((Int) -> Unit)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterTargetschoolBriefBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (isFreeVersion){
            holder.tvAdapterTargetschoolType.text = "\tUnlock difficulty"
            holder.tvAdapterTargetschoolType.setCompoundDrawablesWithIntrinsicBounds(R.drawable.vd_lock,0,0,0)
        }else if (TargetSchool.isFarReachSchool(dataList[position])){
            // Far Reach 名單學校固定顯示 FAR REACH（與 web 版/iOS 版相同）
            holder.tvAdapterTargetschoolType.text = "FAR REACH"
        }else{
            holder.tvAdapterTargetschoolType.text = dataList[position].difficulty
        }

        holder.tvAdapterTargetschoolName.text = dataList[position].schoolName
        holder.tvAdapterTargetschoolCity.text = dataList[position].city
        val photoUrl = SchoolParityRules.displayPhotoUrls(dataList[position].photos).firstOrNull()
        Glide.with(holder.ivAdapterTargetschoolPhoto)
            .load(photoUrl)
            .placeholder(R.drawable.vd_ic_extracurricular_institution)
            .error(R.drawable.vd_ic_extracurricular_institution)
            .thumbnail(.5f)
            .into(holder.ivAdapterTargetschoolPhoto)
        if (isInUserTargetSchool(dataList[position].schoolId!!)){
            holder.ivAdapterTargetschoolFav.setImageResource(R.drawable.vd_ic_favorite_filled)
        }else{
            holder.ivAdapterTargetschoolFav.setImageResource(R.drawable.vd_ic_favorite_empty)
        }

        holder.ivAdapterTargetschoolFav.setOnClickListener {
            if (isInUserTargetSchool(dataList[position].schoolId!!)){
                deleteUserTargetSchool(dataList[position].schoolId!!)
            }else{
                postUserTargetSchool(dataList[position].schoolId!!)
            }
        }

        holder.cvAdapterSchool.setOnClickListener{
            onItemClick(dataList[position])

        }


        if (isInApplicationSchool(dataList[position].schoolId!!)){
            holder.tvAdapterTargetschoolAdd.text = "Added"
            holder.tvAdapterTargetschoolAdd.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null)
        }else{
            holder.tvAdapterTargetschoolAdd.text = "Add to Applications"
            holder.tvAdapterTargetschoolAdd.setCompoundDrawablesWithIntrinsicBounds(null,null,mContext.getDrawable(R.drawable.vd_ic_add),null)
        }

        holder.llAdapterTargetschoolContact.setOnClickListener {
            if (!isInApplicationSchool(dataList[position].schoolId!!)){
                postApplicationSchool(dataList[position].schoolId!!)
            }
        }
    }

    inner class ViewHolder(binding: AdapterTargetschoolBriefBinding) : RecyclerView.ViewHolder(binding.root) {
        val cvAdapterSchool: CardView = binding.cvAdapterSchool
        val clAdapterSchool: ConstraintLayout = binding.clAdapterSchool
        val llAdapterTargetschoolContact: LinearLayoutCompat = binding.llAdapterTargetschoolContact
        val tvAdapterTargetschoolName: AppCompatTextView = binding.tvAdapterTargetschoolName
        val tvAdapterTargetschoolType: AppCompatTextView = binding.tvAdapterTargetschoolType
        val tvAdapterTargetschoolCity: AppCompatTextView = binding.tvAdapterTargetschoolCity
        val tvAdapterTargetschoolAdd: AppCompatTextView = binding.tvAdapterTargetschoolAdd
        val ivAdapterTargetschoolFav: AppCompatImageView = binding.ivAdapterTargetschoolFav
        val ivAdapterTargetschoolDivider: AppCompatImageView = binding.ivAdapterTargetschoolDivider
        val ivAdapterTargetschoolPhoto: AppCompatImageView = binding.ivAdapterTargetschoolPhoto

        init {
            cvAdapterSchool.setViewSizeOrignalWH(156,285)
            clAdapterSchool.setViewSizeOrignalWH(156,285)

            tvAdapterTargetschoolType.setTextSize(10)
            tvAdapterTargetschoolName.setTextSize(13)
            tvAdapterTargetschoolCity.setTextSize(10)
            tvAdapterTargetschoolAdd.setTextSize(10)

            ivAdapterTargetschoolPhoto.setViewSizeOrignalWH(156,116)
            ivAdapterTargetschoolFav.setViewSizeOrignalWH(24,24)
            ivAdapterTargetschoolDivider.setViewSizeOrignalWH(136,1)
            llAdapterTargetschoolContact.setViewSizeOrignalWH(150,35)

        }

    }

}
