package com.realtouchapp.admitted.member.view.adapter

import android.content.Context
import android.database.DatabaseUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.realtouchapp.admitted.databinding.AdapterNotificationBinding
import com.realtouchapp.admitted.network.entity.FCMessage
import com.realtouchapp.baselibrary.utils.DateUtils
import com.realtouchapp.tools.ext.setTextSize
import com.realtouchapp.tools.ui.adapter.BaseRecyclerViewAdapter

/*
    作者: RobertChou
    時間: 2023/5/21 下午 07:41
    信箱: didi31139@gmail.com
    說明:
*/
class NotificationAdapter(context: Context) : BaseRecyclerViewAdapter<FCMessage, NotificationAdapter.ViewHolder>(context){
    lateinit var onItemClick: ((FCMessage) -> Unit)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        dataList[position].title?.let { holder.tvAdapterNotificationTitile.text = it }
        dataList[position].content?.let { holder.tvAdapterNotificationContent.text = it }

        dataList[position].createTimestamp?.let {
            holder.tvAdapterNotificationTime.text = DateUtils.convertTimeToString(it.toLong()*1000,"yyyy MMM dd,HH:mma")
        }


    }
    inner class ViewHolder(binding: AdapterNotificationBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvAdapterNotificationTitile: AppCompatTextView = binding.tvAdapterNotificationTitile
        val tvAdapterNotificationContent: AppCompatTextView = binding.tvAdapterNotificationContent
        val tvAdapterNotificationTime: AppCompatTextView = binding.tvAdapterNotificationTime

        init {
            tvAdapterNotificationTitile.setTextSize(13)
            tvAdapterNotificationContent.setTextSize(13)
            tvAdapterNotificationTime.setTextSize(10)
        }

    }
}
