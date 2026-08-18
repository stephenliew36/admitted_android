package com.realtouchapp.admitted.member.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import com.realtouchapp.admitted.R
import com.realtouchapp.tools.ext.setViewSizeByRatioWH

/*
    作者: RobertChou
    時間: 2023/1/2 下午 03:41
    信箱: didi31139@gmail.com
    說明:
*/
class SpinnerArrayAdapter(context: Context, colors: List<String>):BaseAdapter() {
    private val mContext: Context = context
    private val datas: List<String> = colors
    var sp_width =0.915f
    override fun getCount(): Int {
        return datas.size
    }

    override fun getItem(position: Int): Any {
        return datas[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        if (position==0){
            //第一個是空的要隱藏
            val view = LayoutInflater.from(mContext)
                .inflate(R.layout.adapter_spinner_string, parent, false)
            view.visibility = View.GONE
            return view
        }
        //設定初始值不為0 才能觸發按相同位置
        if (position == datas.size){
            return  LayoutInflater.from(mContext).inflate(R.layout.adapter_spinner_string, parent, false)
        }
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.adapter_spinner_string, parent, false)
        val tv_adapter_spinner_content = view.findViewById<AppCompatTextView>(R.id.tv_adapter_spinner_content)
        val iv_adapter_spinner_content = view.findViewById<ImageView>(R.id.iv_adapter_spinner_content)
        view.setViewSizeByRatioWH(sp_width,0.062f)
        tv_adapter_spinner_content.setViewSizeByRatioWH(sp_width,0.062f)
        tv_adapter_spinner_content.text = datas[position]
        if (position==1){
            iv_adapter_spinner_content.visibility = View.INVISIBLE
        }else{
            iv_adapter_spinner_content.visibility = View.VISIBLE
        }

        return view
    }
}

