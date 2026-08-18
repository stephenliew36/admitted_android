package com.realtouchapp.admitted.member.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.graphics.Color
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.realtouchapp.admitted.databinding.AdapterTestInfoDetailBinding
import com.realtouchapp.admitted.network.entity.Test
import com.realtouchapp.tools.ext.*
import com.realtouchapp.tools.ui.adapter.BaseRecyclerViewAdapter

class TestDetailInfoAdapter(context: Context) : BaseRecyclerViewAdapter<Test, TestDetailInfoAdapter.ViewHolder>(context){
    lateinit var onItemClick: ((Test) -> Unit)
    var highlightedTest: Test? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterTestInfoDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        var data = dataList[position]

        data?.let {
            var title="‧ ${data.month},${data.year}"
            holder.tvAdapterRecycleviewTitle.text =title
            holder.tvAdapterRecycleviewScore.text = data.displayScore()
            holder.root.setBackgroundColor(if (data == highlightedTest) Color.parseColor("#FFF3CD") else Color.TRANSPARENT)

        }
        holder.itemView.setOnClickListener {
            onItemClick(data)
        }

    }
    inner class ViewHolder(binding: AdapterTestInfoDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        val root = binding.root
        val tvAdapterRecycleviewTitle: AppCompatTextView = binding.tvAdapterRecycleviewTitle
        val tvAdapterRecycleviewScore: AppCompatTextView = binding.tvAdapterRecycleviewScore

        init {
            binding.root.setMarginTopOrignal(16)
            tvAdapterRecycleviewTitle.setTextSize(13)
            tvAdapterRecycleviewScore.setTextSize(13)

        }

    }

}
