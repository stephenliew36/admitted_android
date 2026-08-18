package com.realtouchapp.admitted.member.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.databinding.AdapterRecycleviewInterestContentBinding
import com.realtouchapp.tools.ext.*
import com.realtouchapp.tools.ui.adapter.BaseRecyclerViewAdapter

class InterestContentRecyleViewAdapter(context: Context) : BaseRecyclerViewAdapter<String, InterestContentRecyleViewAdapter.ViewHolder>(context) {
    lateinit var onItemClick: ((String) -> Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterRecycleviewInterestContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.tvAdapterRecycleviewTitle.text = dataList[position]
        holder.btnAdapterRecycleviewView.text = mContext.getString(R.string.btn_adapter_recycleview_view) + dataList[position]
        holder.btnAdapterRecycleviewView.setOnClickListener {
            onItemClick?.let { onItemClick(dataList[position]) }
        }
        if (position==0){
            holder.tvAdapterRecycleviewTitle.setMarginTopRatioHWithoutBar(0f)
        }
    }

    inner class ViewHolder(binding: AdapterRecycleviewInterestContentBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvAdapterRecycleviewTitle: AppCompatTextView = binding.tvAdapterRecycleviewTitle
        val tvAdapterRecycleviewContent: AppCompatTextView = binding.tvAdapterRecycleviewContent
        val btnAdapterRecycleviewView: AppCompatButton = binding.btnAdapterRecycleviewView

        init {
            tvAdapterRecycleviewTitle.setTextSize(15)
            tvAdapterRecycleviewTitle.setMarginTopRatioHWithoutBar(.021f)
            tvAdapterRecycleviewContent.setTextSize(13)
            tvAdapterRecycleviewContent.setMarginTopRatioHWithoutBar(.014f)
            btnAdapterRecycleviewView.setTextSize(13)
            btnAdapterRecycleviewView.setMarginTopRatioHWithoutBar(.014f)
            btnAdapterRecycleviewView.setMarginBottomRatioHWithoutBar(.031f)
            btnAdapterRecycleviewView.setMarginLeftRatioW(.043f)
            btnAdapterRecycleviewView.setMarginRightRatioW(.043f)
        }
    }

}
