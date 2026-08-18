package com.realtouchapp.admitted.begin.view

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.begin.viewmodel.BeginActivityViewModel
import com.realtouchapp.admitted.ext.configureEdgeToEdge
import com.realtouchapp.admitted.databinding.ActivityBeginBinding
import com.realtouchapp.admitted.member.view.activity.LoginActivity
import com.realtouchapp.admitted.member.view.activity.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BeginActivity : AppCompatActivity() ,OnClickListener{

    companion object{
        fun Intent(activity: AppCompatActivity){
            val intent = Intent()
                .setClass(activity, BeginActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }
    }
    private lateinit var binding: ActivityBeginBinding
    private val viewModel by viewModels<BeginActivityViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBeginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureEdgeToEdge()

        initialView()

    }
    private fun initialView(){
        //Already have an account? Log in 分色
        val spb = SpannableStringBuilder()
        spb.append(getString(R.string.tv_begin_account_1)+"\t")
        spb.append(getString(R.string.tv_begin_account_2))
        spb.setSpan(StyleSpan(Typeface.BOLD), getString(R.string.tv_begin_account_1).length, spb.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvBeginLogin.text= spb

        binding.btnBeginStart.setOnClickListener(this)
        binding.tvBeginLogin.setOnClickListener(this)
        viewModel.isLogined {
            InfoActivity.Intent(this)
        }

    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.btn_begin_start->{
                RegisterActivity.Intent(this)
            }
            R.id.tv_begin_login->{
                LoginActivity.Intent(this)
            }
        }

    }
}