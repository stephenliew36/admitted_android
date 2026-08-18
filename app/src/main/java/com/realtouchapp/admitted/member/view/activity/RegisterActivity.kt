package com.realtouchapp.admitted.member.view.activity

import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.begin.view.BeginActivity
import com.realtouchapp.admitted.begin.view.SplashActivity
import com.realtouchapp.admitted.databinding.ActivityRegisterBinding
import com.realtouchapp.admitted.common.parity.GoogleProfileNameMapper
import com.realtouchapp.admitted.ext.closeSoftIntPut
import com.realtouchapp.admitted.ext.configureEdgeToEdge
import com.realtouchapp.admitted.ext.deCryption
import com.realtouchapp.admitted.ext.showSnackBar
import com.realtouchapp.admitted.ext.testPrint
import com.realtouchapp.admitted.member.view.activity.model.RegisterActivityViewModel
import com.realtouchapp.admitted.network.api.AdmittedApi
import com.realtouchapp.admitted.network.request.LoginGoogleRequest
import com.realtouchapp.admitted.network.request.RegisteRequest
import com.realtouchapp.admitted.network.request.RegisterCheckRequest
import com.realtouchapp.admitted.network.result.base.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity(), OnClickListener {
    companion object {
        fun Intent(activity: AppCompatActivity) {
            val intent = Intent()
                .setClass(activity, RegisterActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }
    }

    private val viewModel by viewModels<RegisterActivityViewModel>()
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureEdgeToEdge()

        initialView()
        addTextWatcher()
        setClick()
        subscribeUi()

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            LoginActivity.REQUEST_CODE -> {
                val task: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)

                    account?.let {
                        testPrint("LoginActivity",account.email?:"")
//                        displayInfo(it)
                        getGoogleAccessToken(account)

                    }
                } catch (e: ApiException) {
                    testPrint("LoginActivity",e.message.toString())
                }
            }
        }
//        }
    }

    private fun initialView() {
        //By continuing, you agree to Admitted’s Terms & Conditions and Privacy Policy 分顏色
        val spbPrivacy = SpannableStringBuilder()
        spbPrivacy.append(getString(R.string.tv_register_privacy) + "\t")
        var length = spbPrivacy.length
        spbPrivacy.append(getString(R.string.tv_register_privacy1) + "\t")
        val clickableSpan1 = object : ClickableSpan() {
            override fun onClick(view: View) {
                // 這裡放你的點擊事件動作
                startActivity(
                    android.content.Intent(
                        android.content.Intent.ACTION_VIEW,
                        Uri.parse("${AdmittedApi.SERVER_ADDRESS}/termsConditions?lang=en")
                    )
                )
            }
        }
        spbPrivacy.setSpan(
            clickableSpan1,
            length,
            spbPrivacy.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spbPrivacy.setSpan(
            StyleSpan(Typeface.BOLD),
            length,
            spbPrivacy.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )


        spbPrivacy.append(getString(R.string.tv_register_privacy2) + "\t")
        length = spbPrivacy.length
        spbPrivacy.append(getString(R.string.tv_register_privacy3) + "\t")
        val clickableSpan2 = object : ClickableSpan() {
            override fun onClick(view: View) {
                // 這裡放你的點擊事件動作
                startActivity(
                    android.content.Intent(
                        android.content.Intent.ACTION_VIEW,
                        Uri.parse("${AdmittedApi.SERVER_ADDRESS}/privacy?lang=en")
                    )
                )
            }
        }
        spbPrivacy.setSpan(
            StyleSpan(Typeface.BOLD),
            length,
            spbPrivacy.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spbPrivacy.setSpan(
            clickableSpan2,
            length,
            spbPrivacy.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvRegisterPrivacy.text = spbPrivacy
        binding.tvRegisterPrivacy.movementMethod = android.text.method.LinkMovementMethod.getInstance()

        //Already have an account? Log in 分色
        val spbLogin = SpannableStringBuilder()
        spbLogin.append(getString(R.string.tv_begin_account_1) + "\t")
        spbLogin.append(getString(R.string.tv_begin_account_2))
        spbLogin.setSpan(
            StyleSpan(Typeface.BOLD),
            getString(R.string.tv_begin_account_1).length,
            spbLogin.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvRegisterLogin.text = spbLogin
        binding.ivRegisterBack.setOnClickListener(this)


    }

    private fun addTextWatcher() {
        binding.etRegisterFirstname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                checkText()
            }

        })

        binding.etRegisterLastname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                checkText()
            }

        })

        binding.etRegisterEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                checkText()
            }

        })

        binding.etRegisterPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.length!! < 8) {
                    showPasswordErrro(true)
                } else {
                    showPasswordErrro(false)
                }
                checkText()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

    //顯示密碼錯誤
    private fun showPasswordErrro(isError: Boolean) {
        if (isError) {
            binding.ivRegisterPasswordError.visibility = View.VISIBLE
            binding.tvRegisterError.visibility = View.VISIBLE
            binding.etRegisterPassword.setBackgroundResource(R.drawable.bg_et_error)
        } else {
            binding.ivRegisterPasswordError.visibility = View.INVISIBLE
            binding.tvRegisterError.visibility = View.INVISIBLE
            binding.etRegisterPassword.setBackgroundResource(R.drawable.selector_et_default)
        }

    }

    private fun checkText() {
        if (binding.etRegisterFirstname.text.isNullOrEmpty()
            || binding.etRegisterLastname.text.isNullOrEmpty()
            || binding.etRegisterEmail.text.isNullOrEmpty()
            || binding.etRegisterPassword.text.isNullOrEmpty()
        ) {
            enableCreateAccount(false)
        } else if (binding.etRegisterPassword.text != null && binding.etRegisterPassword.text!!.length >= 8) {
            enableCreateAccount(true)
        } else {
            enableCreateAccount(false)
        }

    }

    private fun setClick() {
        binding.ivRegisterBack.setOnClickListener(this)
        binding.btnRegisterFb.setOnClickListener(this)
        binding.btnRegisterGoogle.setOnClickListener(this)
        binding.tvRegisterLogin.setOnClickListener(this)
    }

    private fun subscribeUi() {
        viewModel.response.observe(this, Observer { result ->
            when (result.status) {

                NetworkResult.Status.SUCCESS -> {
                    //成功
                    VerifyEmailActivity.Intent(
                        this, RegisteRequest(
                            binding.etRegisterEmail.text.toString(),
                            "",
                            binding.etRegisterPassword.text.toString(),
                            binding.etRegisterPassword.text.toString(),
                            binding.etRegisterFirstname.text.toString(),
                            binding.etRegisterLastname.text.toString(),
                        )
                    )

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
        viewModel.responseGoogle.observe(this, Observer { result ->
            when (result.status) {
                NetworkResult.Status.SUCCESS -> {
                    when(result.action){
                        NetworkResult.Action.LOGIN,NetworkResult.Action.LOGIN_GOOGLE,NetworkResult.Action.LOGIN_APPLE->{
                            //登入成功
                            SplashActivity.Intent(this)
                        }
                        else->{

                        }
                    }

                }

                NetworkResult.Status.ERROR -> {
                    //登入失敗
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

    private fun enableCreateAccount(isEnable: Boolean) {
        if (isEnable) {
            binding.btnRegisterCreate.setBackgroundResource(R.drawable.selector_btn_default)
            binding.btnRegisterCreate.setOnClickListener(this)

        } else {
            binding.btnRegisterCreate.setBackgroundResource(R.drawable.vd_bg_btn_disabled)
            binding.btnRegisterCreate.setOnClickListener(null)
        }
    }

    private var mGoogleSignInClient: GoogleSignInClient? = null
    fun googleSigIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode(getString(R.string.google_web_client_id))
            .requestIdToken(getString(R.string.google_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

    }

    private fun loginGoogle() {
        googleSigIn()
        val signInIntent: Intent = mGoogleSignInClient!!.getSignInIntent()
        startActivityForResult(signInIntent, LoginActivity.REQUEST_CODE)

    }

    private fun getGoogleAccessToken(account: GoogleSignInAccount) {
        val client = OkHttpClient()
        val requestBody: RequestBody = FormBody.Builder()
            .add("grant_type", "authorization_code")
            .add("client_id", getString(R.string.google_web_client_id))
            .add("client_secret", getString(R.string.google_client_secret))
            .add("redirect_uri", "")
            .add("code", account.serverAuthCode!!)
            .build()
        val request: Request = Request.Builder()
            .url("https://oauth2.googleapis.com/token")
            .post(requestBody)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                viewModel.loginGoogleFailed()
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val jsonObject = JSONObject(response.body!!.string())
                    runOnUiThread {
                        try {
                            val accessToken = jsonObject.getString("access_token")
                            val profileName = GoogleProfileNameMapper.resolve(
                                account.givenName,
                                account.familyName,
                                account.displayName
                            )
                            viewModel.loginGoogle(
                                LoginGoogleRequest(
                                    id = account.id!!,
                                    token = accessToken,
                                    email = account.email,
                                    name = profileName.fullName,
                                    firstName = profileName.firstName,
                                    lastName = profileName.lastName
                                )
                            )
                        } catch (e: JSONException) {
                            viewModel.loginGoogleFailed()
                            e.printStackTrace()
                        }
                    }
                } catch (e: JSONException) {
                    viewModel.loginGoogleFailed()
                    e.printStackTrace()
                }
            }
        })
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_register_back -> {
                BeginActivity.Intent(this)
            }
//            R.id.tv_register_privacy -> {
//                Toast.makeText(this, binding.tvRegisterPrivacy.text, Toast.LENGTH_SHORT).show()
//            }
            R.id.btn_register_create -> {
                closeSoftIntPut(view)
                viewModel.registerCheck(RegisterCheckRequest(binding.etRegisterEmail.text.toString()))

            }
            R.id.btn_register_fb -> {
                Toast.makeText(this, binding.btnRegisterFb.text, Toast.LENGTH_SHORT).show()
            }
            R.id.btn_register_google -> {
                loginGoogle()
            }
            R.id.tv_register_login -> {
                LoginActivity.Intent(this)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent()
            .setClass(this@RegisterActivity, BeginActivity::class.java)
        startActivity(intent)
        finish()
    }


}
