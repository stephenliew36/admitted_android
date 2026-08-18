package com.realtouchapp.admitted.member.view.adapter

import android.view.View

/*
    作者: RobertChou
    時間: 2023/1/3 下午 09:25
    信箱: didi31139@gmail.com
    說明:
*/
interface RecyclerItemClickListenner {
    fun onRecyclerViewItemClick(view: View, position: Int);
}