package com.realtouchapp.admitted.main.explore.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.realtouchapp.admitted.databinding.AdapterTargetschoolCategoryBinding
import com.realtouchapp.admitted.network.entity.TargetSchool
import com.realtouchapp.tools.ext.setTextSize
import com.realtouchapp.tools.ui.adapter.BaseRecyclerViewAdapter
import java.util.ArrayList

/*
    作者: RobertChou
    時間: 2023/2/18 下午 07:42
    信箱: didi31139@gmail.com
    說明:
*/
class TargetSchoolCategoryAdapter(context: Context,val  isFreeVersion: Boolean) : BaseRecyclerViewAdapter<HashMap<String,ArrayList<TargetSchool>>, TargetSchoolCategoryAdapter.ViewHolder>(context){
    lateinit var onItemClick: ((TargetSchool) -> Unit)
    lateinit var onViewAllItemClick: ((ArrayList<TargetSchool>) -> Unit)
    lateinit var isInUserTargetSchool: ((Int) -> Boolean)
    lateinit var isInApplicationSchool: ((Int) -> Boolean)
    lateinit var postUserTargetSchool: ((Int) -> Unit)
    lateinit var postApplicationSchool: ((Int) -> Unit)
    lateinit var deleteUserTargetSchool: ((Int) -> Unit)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterTargetschoolCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        var key  = dataList[position].keys.toTypedArray().get(0)
        holder.tvAdapterTargetschool.text = key
        holder.tvAdapterTargetschoolAll.setOnClickListener {
            onViewAllItemClick(dataList[position].getValue(key))


        }
        var datas = dataList[position].getValue(key)
        var adapter = TargetSchoolBirefAdapter(mContext, isFreeVersion)
        adapter.onItemClick = onItemClick
        adapter.isInUserTargetSchool = isInUserTargetSchool
        adapter.isInApplicationSchool = isInApplicationSchool
        adapter.postUserTargetSchool = postUserTargetSchool
        adapter.postApplicationSchool = postApplicationSchool
        adapter.deleteUserTargetSchool = deleteUserTargetSchool
        adapter.dataList = datas
        var linearLayoutManager = LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false)

        holder.rvAdapterTargetschool.layoutManager = linearLayoutManager
        holder.rvAdapterTargetschool.adapter = adapter



    }

    inner class ViewHolder(binding: AdapterTargetschoolCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvAdapterTargetschool: AppCompatTextView = binding.tvAdapterTargetschool
        val tvAdapterTargetschoolAll: AppCompatTextView = binding.tvAdapterTargetschoolAll
        val rvAdapterTargetschool: RecyclerView = binding.rvAdapterTargetschool

        init {
            tvAdapterTargetschool.setTextSize(17)
            tvAdapterTargetschoolAll.setTextSize(13)
        }

    }

}
