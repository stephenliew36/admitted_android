package com.realtouchapp.admitted.begin.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import com.realtouchapp.admitted.ext.configureEdgeToEdge
import com.realtouchapp.admitted.databinding.ActivityPremiumBinding

class PremiumActivity:AppCompatActivity() {

    companion object{
        fun Intent(activity: AppCompatActivity){
            val intent = android.content.Intent()
                .setClass(activity, PremiumActivity::class.java)
            activity.startActivity(intent)
        }
    }
    private lateinit var binding: ActivityPremiumBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPremiumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureEdgeToEdge(
            backgroundColor = "#F1E9E5".toColorInt()
        )

        binding.ivClosePremium.setOnClickListener {
            finish()
        }

        binding.btnUpgradeToPremium.setOnClickListener {
            SubscribeActivity.intent(this@PremiumActivity)
        }

    }

}