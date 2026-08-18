package com.realtouchapp.admitted.main.application.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.realtouchapp.admitted.databinding.AdapterApplicationDateBinding
import com.realtouchapp.tools.ext.setTextSize
import com.realtouchapp.tools.ui.adapter.BaseRecyclerViewAdapter

/*
    作者: RobertChou
    時間: 2023/2/18 下午 07:42
    信箱: didi31139@gmail.com
    說明:
*/
class ApplicationDateAdapter(context: Context) : BaseRecyclerViewAdapter<ApplicationDateAdapter.DateItem, ApplicationDateAdapter.ViewHolder>(context){
    lateinit var onItemClick: ((DateItem) -> Unit)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterApplicationDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        dataList[position].title?.let { holder.tvAdapterDateTtile.text = it }
        dataList[position].date?.let {
            if (it.split("/").size ==3){
                val splitResult = it.split("/")
                if (splitResult[0].toInt()>2000){
                    holder.tvAdapterDate.text = month(splitResult[1],splitResult[2])
                }else if (splitResult[2].toInt()>2000){
                    holder.tvAdapterDate.text = month(splitResult[0],splitResult[1])
                }

            }else if (it.split(".").size ==2){
                val splitResult = it.split(".")
                holder.tvAdapterDate.text = splitResult[0].trim()+"\n"+splitResult[1]
            }else if(it.equals("N/A")){
                holder.tvAdapterDate.text = it
            }else{
                holder.tvAdapterDate.text = it
            }
        }
        holder.rlAdapterDateAdd.setOnClickListener {
            onItemClick(dataList[position])

        }

    }
    inner class ViewHolder(binding: AdapterApplicationDateBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvAdapterDateTtile: AppCompatTextView = binding.tvAdapterDateTtile
        val tvAdapterDate: AppCompatTextView = binding.tvAdapterDate
        val tvAdapterDateAdd: AppCompatTextView = binding.tvAdapterDateAdd
        val rlAdapterDateAdd: RelativeLayout = binding.rlAdapterDateAdd

        init {
            tvAdapterDateTtile.setTextSize(10)
            tvAdapterDate.setTextSize(22)
            tvAdapterDateAdd.setTextSize(10)
        }

    }
    fun month(month:String,date:String):String{
        var dateString = date.toInt().toString() +"\n"
        when(month.toInt()){
            1->{dateString = dateString + "Jan"}
            2->{dateString = dateString + "Feb"}
            3->{dateString = dateString + "Mar"}
            4->{dateString = dateString + "Apr"}
            5->{dateString = dateString + "May"}
            6->{dateString = dateString + "Jun"}
            7->{dateString = dateString + "Jul"}
            8->{dateString = dateString + "Aug"}
            9->{dateString = dateString + "Sep"}
            10->{dateString = dateString + "Oct"}
            11->{dateString = dateString + "Nov"}
            12->{dateString = dateString + "Dec"}
        }
        return dateString
    }
    data class DateItem(var title:String,var date:String?){

    }
}











