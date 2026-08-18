package com.realtouchapp.admitted.member.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.databinding.AdapterTestInfoBinding
import com.realtouchapp.admitted.network.entity.Test
import com.realtouchapp.tools.ext.*
import com.realtouchapp.tools.ui.adapter.BaseRecyclerViewAdapter
import kotlin.collections.ArrayList

class TestInfoAdapter(context: Context) : BaseRecyclerViewAdapter<ArrayList<Test>, TestInfoAdapter.ViewHolder>(context){
    lateinit var onItemClick: ((Test) -> Unit)
    lateinit var onItemDelete: ((Test) -> Unit)
    lateinit var isOpenShiftRemove: ((Boolean) -> Unit)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterTestInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        var datas = dataList[position]

        datas.get(0)?.let {
            holder.tvAdapterRecycleviewTitle.text = it.title()
        }

        val highestTest = Test.selectedHighestMostRecent(datas) ?: Test()

        holder.tvAdapterRecycleviewTitle.setText(highestTest.title())
        holder.tvAdapterRecycleviewScore.text = highestTest.displayScore()

        val linearLayoutManager = LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false)
        linearLayoutManager.setInitialPrefetchItemCount(datas.size);
        holder.rvAdapterRecycleview.layoutManager = linearLayoutManager
        val testInfoAdapter = TestDetailInfoAdapter(mContext)
        testInfoAdapter.onItemClick = onItemClick
        testInfoAdapter.highlightedTest = highestTest
        val sorted = ArrayList(Test.sortedChronologically(datas))
        testInfoAdapter.setData(sorted)
        holder.rvAdapterRecycleview.adapter = testInfoAdapter
        holder.rvAdapterRecycleview.addSwipeLeftDelete(sorted) {
            onItemDelete(sorted[it])
        }

        holder.itemView.setOnClickListener {
            if (holder.llAdapterRecycleviewIc.visibility == View.VISIBLE){
                isOpenShiftRemove(true)
                holder.llAdapterRecycleviewIc.visibility = View.GONE
                holder.ivAdapterRecycleviewArrow.setImageResource(R.drawable.vd_ic_arrow_down_black)
            }else{
                isOpenShiftRemove(false)
                holder.llAdapterRecycleviewIc.visibility = View.VISIBLE
                holder.ivAdapterRecycleviewArrow.setImageResource(R.drawable.vd_ic_arrow_top)
            }
        }

    }
    inner class ViewHolder(binding: AdapterTestInfoBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvAdapterRecycleviewTitle: AppCompatTextView = binding.tvAdapterRecycleviewTitle
        val rvAdapterRecycleview: RecyclerView = binding.rvAdapterRecycleview
        val tvAdapterRecycleviewScore: AppCompatTextView = binding.tvAdapterRecycleviewScore
        val ivAdapterRecycleviewArrow: AppCompatImageView = binding.ivAdapterRecycleviewArrow
        val llAdapterRecycleviewIc: LinearLayoutCompat = binding.llAdapterRecycleviewIc

        init {
            binding.root.setMarginTopOrignal(16)
            tvAdapterRecycleviewTitle.setTextSize(13)
            tvAdapterRecycleviewScore.setTextSize(13)

            ivAdapterRecycleviewArrow.setViewSizeOrignalWH(24,24)
        }

    }

}
