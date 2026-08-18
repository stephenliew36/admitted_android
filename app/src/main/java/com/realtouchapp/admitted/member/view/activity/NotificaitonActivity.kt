package com.realtouchapp.admitted.member.view.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.realtouchapp.admitted.databinding.ActivityNotificationBinding
import com.realtouchapp.admitted.ext.configureEdgeToEdge
import com.realtouchapp.admitted.ext.deCryption
import com.realtouchapp.admitted.ext.showSnackBar
import com.realtouchapp.admitted.member.view.activity.model.NotificaitonActivityViewModel
import com.realtouchapp.admitted.member.view.adapter.NotificationAdapter
import com.realtouchapp.admitted.network.api.AdmittedApi
import com.realtouchapp.admitted.network.entity.FCMessage
import com.realtouchapp.admitted.network.result.base.NetworkResult
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NotificaitonActivity:AppCompatActivity(){

    companion object{

        fun Intent(activity: AppCompatActivity){
            val intent = android.content.Intent()
                .setClass(activity, NotificaitonActivity::class.java)
            activity.startActivity(intent)
        }
    }

    private lateinit var binding:ActivityNotificationBinding
    private val viewModel by viewModels<NotificaitonActivityViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureEdgeToEdge()

        subscriveUI()
        initialViewSize()
        initialView()

        viewModel.getNotificiations()

    }

    fun initialViewSize(){

    }

    fun initialView(){
        binding.llActivityNotificationBack.setOnClickListener {
            finish()
        }
        
    }
    fun subscriveUI(){

        viewModel.response.observe(this) { result ->
            when (result.status) {
                NetworkResult.Status.SUCCESS -> {
                    //成功
                    when (result.action) {
                        NetworkResult.Action.GET_NOTIFICATION,NetworkResult.Action.CACHE_NOTIFICATION -> {
                            result.data?.let {
                                it.data?.let { it ->
                                    var notificiations = it as ArrayList<FCMessage>
                                    if (notificiations.size>0){
                                        var adapter =NotificationAdapter(this)
                                        adapter.dataList = notificiations
                                        binding.rvActivityNotification.adapter = adapter
                                        binding.rvActivityNotification.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

                                    }

                                }

                            }
                        }
                        NetworkResult.Action.PUT_USERBASIC -> {
                        }
                        else -> {


                        }
                    }
                }
                NetworkResult.Status.ERROR -> {
                    //失敗
                    if (result.statusCode == AdmittedApi.StatusCode.ERROR_OTTHER.code) {
                        //本地端失敗(網路未開啟等等)
                        result.message?.let {
                            showSnackBar(binding.root, it)
                        }
                    } else {
                        //失敗
                        result.error!!.message?.let { message ->
                            showSnackBar(binding.root, deCryption(message).msgDesc)
                        }
                    }
                }
                
                NetworkResult.Status.LOADING -> {
                    //載入中 可顯示載入動畫
                }
            }

        }
    }

}