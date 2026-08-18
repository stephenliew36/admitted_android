package com.realtouchapp.admitted.main.explore.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.realtouchapp.admitted.databinding.AdapterExtracurricularsCategoryBinding
import com.realtouchapp.admitted.network.entity.Extracurricular
import com.realtouchapp.tools.ext.setTextSize
import com.realtouchapp.tools.ui.adapter.BaseRecyclerViewAdapter

/*
    作者: RobertChou
    時間: 2023/2/18 下午 07:42
    信箱: didi31139@gmail.com
    說明:
*/

class ExtracurricularsCategoryAdapter (context: Context) : BaseRecyclerViewAdapter<HashMap<String,ArrayList<Extracurricular>>, ExtracurricularsCategoryAdapter.ViewHolder>(context){
    lateinit var onItemClick: ((Extracurricular) -> Unit)
    lateinit var onGetALLClick: ((ArrayList<Extracurricular>) -> Unit)
    lateinit var postFavExtracurricular: ((Extracurricular) -> Unit)
    lateinit var deleteFavExtracurricular: ((Extracurricular) -> Unit)
    lateinit var contactMethod: ((Extracurricular) -> Unit)
    var lat =0.0
    var lon =0.0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterExtracurricularsCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val key  = dataList[position].keys.toTypedArray().get(0)
        holder.tvAdapterExtracurricularsmain.text = key
        holder.tvAdapterExtracurricularsmainAll.setOnClickListener {
                onGetALLClick(dataList[position].getValue( dataList[position].keys.toTypedArray().get(0)))
        }
        val datas = dataList[position].getValue( dataList[position].keys.toTypedArray().get(0))
        val adapter = ExtracurricularsBirefAdapter(mContext)
        adapter.onItemClick = onItemClick
        adapter.postFavExtracurricular = postFavExtracurricular
        adapter.deleteFavExtracurricular = deleteFavExtracurricular
        adapter.contactMethod = contactMethod

        adapter.lat = lat
        adapter.lon = lon
        adapter.dataList = datas
        val linearLayoutManager = LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false)


        holder.rvAdapterExtracurricularsmain.layoutManager = linearLayoutManager
        holder.rvAdapterExtracurricularsmain.adapter = adapter



    }

    inner class ViewHolder(binding: AdapterExtracurricularsCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvAdapterExtracurricularsmain: AppCompatTextView = binding.tvAdapterExtracurricularsmain
        val tvAdapterExtracurricularsmainAll: AppCompatTextView = binding.tvAdapterExtracurricularsmainAll
        val rvAdapterExtracurricularsmain: RecyclerView = binding.rvAdapterExtracurricularsmain

        init {
            tvAdapterExtracurricularsmain.setTextSize(13)
            tvAdapterExtracurricularsmainAll.setTextSize(13)
        }

    }

}
