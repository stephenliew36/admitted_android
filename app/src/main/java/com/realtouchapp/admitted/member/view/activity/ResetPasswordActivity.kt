package com.realtouchapp.admitted.member.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.databinding.ActivityResetpasswordBinding
import com.realtouchapp.admitted.ext.closeSoftIntPut
import com.realtouchapp.admitted.ext.configureEdgeToEdge
import com.realtouchapp.admitted.ext.deCryption
import com.realtouchapp.admitted.ext.showSnackBar
import com.realtouchapp.admitted.member.view.activity.model.ResetPasswordActivityViewModel
import com.realtouchapp.admitted.network.api.AdmittedApi
import com.realtouchapp.admitted.network.request.PasswordForgotRequest
import com.realtouchapp.admitted.network.result.base.NetworkResult
import com.realtouchapp.tools.utils.ModularFunctionUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordActivity : AppCompatActivity() , View.OnClickListener {

    companion object{
        fun Intent(activity: AppCompatActivity){
            val intent = Intent()
                .setClass(activity, ResetPasswordActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }
    }
    private lateinit var binding: ActivityResetpasswordBinding
    private val viewModel by viewModels<ResetPasswordActivityViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetpasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureEdgeToEdge()

        initialView()
        addTextWatcher()
        setClick()
        subscribeUi()
    }

    private fun initialView(){

    }
    private fun addTextWatcher(){

        binding.etResetpasswordEmail.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                checkText()
            }

        })

    }

    private fun checkText(){
        if (binding.etResetpasswordEmail.text.isNullOrEmpty()){
            enableResetPassword(false)
        }else if (ModularFunctionUtils.isValidMail(binding.etResetpasswordEmail.text.toString())){
            enableResetPassword(true)
        }else{
            enableResetPassword(false)
        }

    }
    private fun setClick(){
        binding.ivResetpasswordBack.setOnClickListener(this)
    }

    private fun subscribeUi() {
        viewModel.response.observe(this, Observer { result ->
            when (result.status) {
                NetworkResult.Status.SUCCESS -> {
                    //成功
                    LoginActivity.Intent(this)
                }

                NetworkResult.Status.ERROR -> {
                    //失敗
                    if (result.statusCode == AdmittedApi.StatusCode.ERROR_OTTHER.code){
                        //本地端失敗(網路未開啟等等)
                        result.message?.let{
                            showSnackBar(binding.root,it)
                        }
                    }else{
                        //網路失敗
                        result.error!!.message?.let { message ->
                            showSnackBar(binding.root, deCryption(message).msgDesc)
                        }
                    }
                }

                NetworkResult.Status.LOADING -> {
                    //載入中 可顯示載入動畫
                }
            }

        })
    }
    
    private fun enableResetPassword(isEnable:Boolean){
        if (isEnable) {
            binding.btnResetpasswordReset.setBackgroundResource(R.drawable.selector_btn_default)
            binding.btnResetpasswordReset.setOnClickListener (this)

        }else{
            binding.btnResetpasswordReset.setBackgroundResource(R.drawable.vd_bg_btn_disabled)
            binding.btnResetpasswordReset.setOnClickListener(null)
        }
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.iv_resetpassword_back->{

                LoginActivity.Intent(this)
            }
            R.id.btn_resetpassword_reset->{
                closeSoftIntPut(view)
                viewModel.forgotPasword(PasswordForgotRequest(binding.etResetpasswordEmail.text.toString()))
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent()
            .setClass(this@ResetPasswordActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}