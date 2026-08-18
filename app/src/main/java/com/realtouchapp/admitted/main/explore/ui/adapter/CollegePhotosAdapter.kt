package com.realtouchapp.admitted.main.explore.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.databinding.AdapterCollegePhotosBinding
import com.realtouchapp.admitted.common.parity.SchoolParityRules
import com.realtouchapp.tools.ui.adapter.BaseRecyclerViewAdapter

class CollegePhotosAdapter(context: Context) : BaseRecyclerViewAdapter<List<String>, CollegePhotosAdapter.ViewHolder>(context) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterCollegePhotosBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.photoContainer.removeAllViews()
        dataList[position].forEach { rawUrl ->
            val image = AppCompatImageView(mContext).apply {
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f).also {
                    it.marginEnd = mContext.resources.getDimensionPixelSize(R.dimen.school_photo_gap)
                }
                scaleType = ImageView.ScaleType.CENTER_CROP
                contentDescription = mContext.getString(R.string.school_photo)
            }
            holder.photoContainer.addView(image)
            Glide.with(image)
                .load(SchoolParityRules.normalizeImageUrl(rawUrl))
                .placeholder(R.drawable.vd_ic_extracurricular_institution)
                .error(R.drawable.vd_ic_extracurricular_institution)
                .thumbnail(.5f)
                .into(image)
        }
    }

    inner class ViewHolder(binding: AdapterCollegePhotosBinding) : RecyclerView.ViewHolder(binding.root) {
        val root:View = binding.root
        val photoContainer: LinearLayoutCompat = binding.llAdapterCollegePhotos
        init {

        }
    }

}
