package com.realtouchapp.admitted.main.explore.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.databinding.AdapterExtracurricularsBriefBinding
import com.realtouchapp.admitted.ext.testPrint
import com.realtouchapp.admitted.ext.testToast
import com.realtouchapp.admitted.network.entity.Extracurricular
import com.realtouchapp.admitted.network.entity.ExtracurricularInstitutionType
import com.realtouchapp.admitted.network.entity.GooglePlaceDetail
import com.realtouchapp.admitted.network.entity.GooglePlaceNearby
import com.realtouchapp.tools.ext.setMarginTopOrignal
import com.realtouchapp.tools.ext.setTextSize
import com.realtouchapp.tools.ext.setViewSizeOrignalWH
import com.realtouchapp.tools.ui.adapter.BaseRecyclerViewAdapter
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.ArrayList

/*
    作者: RobertChou
    時間: 2023/2/18 下午 07:42
    信箱: didi31139@gmail.com
    說明:
*/
class ExtracurricularsBirefAdapter(context: Context) : BaseRecyclerViewAdapter<Extracurricular, ExtracurricularsBirefAdapter.ViewHolder>(context){
    lateinit var onItemClick: ((Extracurricular) -> Unit)
    lateinit var postFavExtracurricular: ((Extracurricular) -> Unit)
    lateinit var deleteFavExtracurricular: ((Extracurricular) -> Unit)
    lateinit var contactMethod: ((Extracurricular) -> Unit)
    var lat =0.0
    var lon =0.0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterExtracurricularsBriefBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
            holder.tvAdapterExtracurricularName.text = dataList[position].name

        if (lat == 0.0 && lon == 0.0){
            holder.tvAdapterExtracurricularDistance.visibility = View.INVISIBLE
        }else{
            holder.tvAdapterExtracurricularDistance.visibility = View.VISIBLE
            holder.tvAdapterExtracurricularDistance.text = "${calculateDistanceInKilometers(lat,lon,dataList[position].lat.toDouble(),dataList[position].lng.toDouble()).toInt()} km"
        }
        dataList[position].address?.let {
            holder.tvAdapterExtracurricularType.text = it
        }
        when(dataList[position].institutionType){
            ExtracurricularInstitutionType.HOSPITAL.type->{
                holder.ivAdapterExtracurricularPhoto.setImageResource(R.drawable.img_hospital)
            }
            ExtracurricularInstitutionType.ORPHANAGE.type->{
                holder.ivAdapterExtracurricularPhoto.setImageResource(R.drawable.img_orphanages)
            }
            ExtracurricularInstitutionType.NURSING_HOME.type-> holder.ivAdapterExtracurricularPhoto.setImageResource(R.drawable.img_nursing)
            ExtracurricularInstitutionType.LIBRARY.type-> holder.ivAdapterExtracurricularPhoto.setImageResource(R.drawable.img_library)
            ExtracurricularInstitutionType.MUSEUM_GALLERY.type-> holder.ivAdapterExtracurricularPhoto.setImageResource(R.drawable.img_museum)
            ExtracurricularInstitutionType.ANIMAL_SHELTER.type-> holder.ivAdapterExtracurricularPhoto.setImageResource(R.drawable.img_anilma)
        }
        if (dataList[position].extracurricularId ==null) {
            holder.ivAdapterExtracurricularFav.setImageResource(R.drawable.vd_ic_favorite_empty)
        }else{
            holder.ivAdapterExtracurricularFav.setImageResource(R.drawable.vd_ic_favorite_filled)
        }
        holder.ivAdapterExtracurricularFav.setOnClickListener {
            if (dataList[position].extracurricularId ==null) {
                dataList[position].extracurricularId = 1
                postFavExtracurricular(dataList[position])
                holder.ivAdapterExtracurricularFav.setImageResource(R.drawable.vd_ic_favorite_filled)
            }else{
                holder.ivAdapterExtracurricularFav.setImageResource(R.drawable.vd_ic_favorite_empty)
                dataList[position].extracurricularId = null
                postFavExtracurricular(dataList[position])
            }
        }

        holder.cvAdapterExtracurricular.setOnClickListener{
            onItemClick(dataList[position])
        }


        holder.llAdapterExtracurricularContact.setOnClickListener{
            contactMethod(dataList[position])
        }

    }

    inner class ViewHolder(binding: AdapterExtracurricularsBriefBinding) : RecyclerView.ViewHolder(binding.root) {
        val cvAdapterExtracurricular: CardView = binding.cvAdapterExtracurricular
        val clAdapterExtracurricular: ConstraintLayout = binding.clAdapterExtracurricular
        val llAdapterExtracurricularContact: LinearLayoutCompat = binding.llAdapterExtracurricularContact
        val tvAdapterExtracurricularName: AppCompatTextView = binding.tvAdapterExtracurricularName
        val tvAdapterExtracurricularType: AppCompatTextView = binding.tvAdapterExtracurricularType
        val tvAdapterExtracurricularDistance: AppCompatTextView = binding.tvAdapterExtracurricularDistance
        val tvAdapterExtracurricularContact: AppCompatTextView = binding.tvAdapterExtracurricularContact
        val ivAdapterExtracurricularFav: AppCompatImageView = binding.ivAdapterExtracurricularFav
        val ivAdapterExtracurricularDivider: AppCompatImageView = binding.ivAdapterExtracurricularDivider
        val ivAdapterExtracurricularPhoto: AppCompatImageView = binding.ivAdapterExtracurricularPhoto

        init {
//            binding.root.setViewSizeOrignalWH(166,282)
            cvAdapterExtracurricular.setViewSizeOrignalWH(166,285)
            clAdapterExtracurricular.setViewSizeOrignalWH(166,285)
//            binding.root.setMarginTopOrignal(16)
            tvAdapterExtracurricularName.setTextSize(13)
            tvAdapterExtracurricularType.setTextSize(10)
            tvAdapterExtracurricularDistance.setTextSize(10)
            tvAdapterExtracurricularContact.setTextSize(13)

            ivAdapterExtracurricularPhoto.setViewSizeOrignalWH(166,116)
            ivAdapterExtracurricularFav.setViewSizeOrignalWH(24,24)
            ivAdapterExtracurricularDivider.setViewSizeOrignalWH(136,1)
            llAdapterExtracurricularContact.setViewSizeOrignalWH(166,35)

//            tvAdapterRecycleviewType.setTextSize(13)
//            tvAdapterRecycleviewRank.setTextSize(13)
//            tvAdapterRecycleviewGrade.setTextSize(13)
//            tvAdapterRecycleviewRecognition.setTextSize(13)
        }

    }
    fun calculateDistanceInKilometers(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadiusKm = 6371 // 地球半徑，單位：公里

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)

        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        var number = earthRadiusKm * c
        val wholePart = number.toInt()
        val decimalPart = (number % 1 * 10).toInt() / 10.0
        return wholePart + decimalPart
    }


}
