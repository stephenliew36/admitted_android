package com.realtouchapp.admitted.member.view.activity

import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.begin.view.BeginActivity
import com.realtouchapp.admitted.begin.view.SplashActivity
import com.realtouchapp.admitted.databinding.ActivityLoginBinding
import com.realtouchapp.admitted.common.parity.GoogleProfileNameMapper
import com.realtouchapp.admitted.ext.closeSoftIntPut
import com.realtouchapp.admitted.ext.configureEdgeToEdge
import com.realtouchapp.admitted.ext.deCryption
import com.realtouchapp.admitted.ext.showSnackBar
import com.realtouchapp.admitted.ext.testPrint
import com.realtouchapp.admitted.member.view.activity.model.LoginActivityViewModel
import com.realtouchapp.admitted.network.api.AdmittedApi
import com.realtouchapp.admitted.network.request.LoginGoogleRequest
import com.realtouchapp.admitted.network.request.LoginRequest
import com.realtouchapp.admitted.network.result.base.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() , View.OnClickListener {
    companion object{
        fun Intent(activity: AppCompatActivity){
            val intent = Intent()
                .setClass(activity, LoginActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }
        const val REQUEST_CODE = 2000
    }
    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginActivityViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureEdgeToEdge()

        initialView()
        addTextWatcher()
        subscribeUi()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE -> {
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
    private fun initialView(){
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
        binding.tvLoginPrivacy.text = spbPrivacy
        binding.tvLoginPrivacy.movementMethod = android.text.method.LinkMovementMethod.getInstance()


        googleSigIn()

        binding.ivLoginBack.setOnClickListener(this)
        binding.tvLoginForgot.setOnClickListener(this)
        binding.btnLoginApple.setOnClickListener(this)
        binding.btnLoginGoogle.setOnClickListener(this)

    }
    private fun addTextWatcher(){

        binding.etLoginEmail.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                checkText()
            }

        })

        binding.etLoginPassword.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                checkText()
            }
            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

    private fun checkText(){
        if (binding.etLoginEmail.text.isNullOrEmpty()
            ||binding.etLoginPassword.text.isNullOrEmpty()){
            enableLogin(false)
        }else if (binding.etLoginPassword.text!=null && binding.etLoginPassword.text!!.length>=8){
            enableLogin(true)
        }else{
            enableLogin(false)
        }

    }

    private fun subscribeUi() {
        viewModel.response.observe(this, Observer { result ->
            when (result.status) {
                NetworkResult.Status.SUCCESS -> {
                    when(result.action){
                        NetworkResult.Action.LOGIN,NetworkResult.Action.LOGIN_GOOGLE,NetworkResult.Action.LOGIN_APPLE->{
                            //登入成功
                            Log.e("Intent","SplashActivity.Intent(this)")
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

    private fun enableLogin(isEnable:Boolean){
        if (isEnable) {
            binding.btnLoginLogin.setBackgroundResource(R.drawable.selector_btn_default)
            binding.btnLoginLogin.setOnClickListener (this)

        }else{
            binding.btnLoginLogin.setBackgroundResource(R.drawable.vd_bg_btn_disabled)
            binding.btnLoginLogin.setOnClickListener(null)
        }
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.iv_login_back->{
                BeginActivity.Intent(this)
            }
            R.id.tv_login_forgot->{
                ResetPasswordActivity.Intent(this)

            }
            R.id.tv_login_privacy->{
                Toast.makeText(this, binding.tvLoginPrivacy.text, Toast.LENGTH_SHORT).show()
            }
            R.id.btn_login_login->{
                //登入
                viewModel.login(LoginRequest(binding.etLoginEmail.text.toString(),binding.etLoginPassword.text.toString()))
                //關閉輸入鍵盤
                closeSoftIntPut(view)
            }
            R.id.btn_login_apple->{
            }
            R.id.btn_login_google->{
                loginGoogle()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent()
            .setClass(this@LoginActivity, BeginActivity::class.java)
        startActivity(intent)
        finish()
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
        val signInIntent: Intent = mGoogleSignInClient!!.getSignInIntent()

        startActivityForResult(signInIntent, REQUEST_CODE)

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

    var login_info = ""

    private fun displayInfo(account: GoogleSignInAccount) {
        login_info += "GoogleSignInAccount\n"
        account.serverAuthCode?.let { login_info += "\nServerAuthCode:$it" }
        account.account?.let { login_info += "\nAccount:$it" }
        account.displayName?.let { login_info += "\nDisplayName:$it" }
        account.email?.let { login_info += "\nEmail:$it" }
        account.familyName?.let { login_info += "\nFamilyName:$it" }
        account.givenName?.let { login_info += "\nGivenName:$it" }
        account.id?.let { login_info += "\nId:$it" }
        account.idToken?.let { login_info += "\nIdToken:$it" }
        account.isExpired?.let { login_info += "\nIsExpired:$it" }
        account.photoUrl?.let { login_info += "\nPhotoUrl:$it" }

    }
}
