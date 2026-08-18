package com.realtouchapp.admitted.member.view.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.begin.view.SelectFreeOrPremiumActivity
import com.realtouchapp.admitted.begin.view.SelectParentOrStudentActivity
import com.realtouchapp.admitted.databinding.ActivityVerifyEmailBinding
import com.realtouchapp.admitted.ext.closeSoftIntPut
import com.realtouchapp.admitted.ext.configureEdgeToEdge
import com.realtouchapp.admitted.ext.deCryption
import com.realtouchapp.admitted.ext.showSnackBar
import com.realtouchapp.admitted.member.view.activity.model.VerifyEmailViewModel
import com.realtouchapp.admitted.network.api.AdmittedApi
import com.realtouchapp.admitted.network.request.LoginRequest
import com.realtouchapp.admitted.network.request.RegisteGetcodeRequest
import com.realtouchapp.admitted.network.request.RegisteRequest
import com.realtouchapp.admitted.network.result.DeCryptionResponse
import com.realtouchapp.admitted.network.result.base.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyEmailActivity : AppCompatActivity(), OnClickListener {

    companion object{
        const val BUNDLE_REGISTER_REQUEST = "BUNDLE_REGISTER_REQUEST"
        fun Intent(activity: AppCompatActivity,registeRequest: RegisteRequest){
            val intent = Intent()
                .setClass(activity, VerifyEmailActivity::class.java)
                .putExtra(BUNDLE_REGISTER_REQUEST,registeRequest)
            activity.startActivity(intent)
            activity.finish()
        }
    }
    private val viewModel by viewModels<VerifyEmailViewModel>()
    private lateinit var registeRequest: RegisteRequest
    private lateinit var binding: ActivityVerifyEmailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerifyEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureEdgeToEdge()

        registeRequest = intent.extras!!.getSerializable(BUNDLE_REGISTER_REQUEST) as RegisteRequest
        addTextWatcher()
        setClick()
        subscribeUi()
    }

    override fun onStart() {
        super.onStart()
        binding.etVerifyCode1.requestFocus()

    }
    private fun addTextWatcher() {
        binding.etVerifyCode1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable) {
                checkText()
                if (p0.length==1){
                    binding.etVerifyCode2.requestFocus()
                }
            }

        })
        binding.etVerifyCode2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable) {
                checkText()
                if (p0.length==1){
                    binding.etVerifyCode3.requestFocus()
                }
            }

        })

        binding.etVerifyCode3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable) {
                if (p0.length==1){
                    binding.etVerifyCode4.requestFocus()
                }
                checkText()
            }

        })

        binding.etVerifyCode4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable) {
                if (p0.length==1){
                    binding.etVerifyCode5.requestFocus()
                }
                checkText()
            }

        })
        binding.etVerifyCode5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable) {
                if (p0.length==1){
                    binding.etVerifyCode6.requestFocus()
                }
                checkText()
            }

        })
        binding.etVerifyCode6.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable) {
                checkText()
            }

        })
    }

    private fun subscribeUi() {
        viewModel.response.observe(this, Observer { result ->
            when (result.status) {

                NetworkResult.Status.SUCCESS -> {
                    //成功
                    when (result.action) {
                        NetworkResult.Action.LOGIN -> {
                            //登入成功

                            // 選擇學生/家長
                            SelectParentOrStudentActivity.intent(this)
                            // 選擇免費/訂閱
//                            SelectFreeOrPremiumActivity.Intent(this)
//                            BasicInfoActivity.Intent(this)
                        }
                        NetworkResult.Action.REGISTER -> {
                            //註冊成功
                            viewModel.login(LoginRequest(registeRequest.email,registeRequest.password))
                        }
                        NetworkResult.Action.REGISTERGETCODE -> {
                            //傳送驗證碼成功
                            binding.etVerifyCode1.setText("")
                            binding.etVerifyCode2.setText("")
                            binding.etVerifyCode3.setText("")
                            binding.etVerifyCode4.setText("")
                            binding.etVerifyCode5.setText("")
                            binding.etVerifyCode6.setText("")
                            showSnackBar(binding.root,(result.data!!.data as DeCryptionResponse).msgDesc)

                        }
                        else -> {}

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

        })
    }

    private fun checkText(){
        if (binding.etVerifyCode1.text.isNullOrEmpty()
            ||binding.etVerifyCode2.text.isNullOrEmpty()
            ||binding.etVerifyCode3.text.isNullOrEmpty()
            ||binding.etVerifyCode4.text.isNullOrEmpty()
            ||binding.etVerifyCode5.text.isNullOrEmpty()
            ||binding.etVerifyCode6.text.isNullOrEmpty()){
            enableCreateAccount(false)
        }else{
            enableCreateAccount(true)
        }
    }

    private fun enableCreateAccount(isEnable: Boolean) {
        if (isEnable) {
            binding.btnVerifyemailCreate.setBackgroundResource(R.drawable.selector_btn_default)
            binding.btnVerifyemailCreate.setOnClickListener(this)

        } else {
            binding.btnVerifyemailCreate.setBackgroundResource(R.drawable.vd_bg_btn_disabled)
            binding.btnVerifyemailCreate.setOnClickListener(null)
        }
    }
    private fun setClick(){
        binding.ivVerifyemailBack.setOnClickListener(this)
        binding.tvVerifyemailSend.setOnClickListener(this)

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_verifyemail_back->{
                RegisterActivity.Intent(this)
            }
            R.id.tv_verifyemail_send->{
                closeSoftIntPut(view)
                viewModel.registerGetCode(RegisteGetcodeRequest(registeRequest.email))

            }

            R.id.btn_verifyemail_create->{
                closeSoftIntPut(view)
                registeRequest.checkcode=binding.etVerifyCode1.text.toString() +binding.etVerifyCode2.text.toString() +binding.etVerifyCode3.text.toString() +
                        binding.etVerifyCode4.text.toString() +
                        binding.etVerifyCode5.text.toString() +
                        binding.etVerifyCode6.text.toString()
                viewModel.register(registeRequest)
            }
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent()
            .setClass(this@VerifyEmailActivity, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }
}