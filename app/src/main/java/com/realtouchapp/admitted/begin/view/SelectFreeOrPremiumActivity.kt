package com.realtouchapp.admitted.begin.view

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.realtouchapp.admitted.ext.configureEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import com.realtouchapp.admitted.databinding.ActivitySelectfreeorpermiumBinding
import com.realtouchapp.admitted.main.roadmmap.ui.activity.MainActivity
import com.realtouchapp.admitted.storage.datastore.AdmittedDataStore
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.core.graphics.toColorInt

@AndroidEntryPoint
class SelectFreeOrPremiumActivity : AppCompatActivity() {
    companion object{
        fun Intent(activity: AppCompatActivity){
            val intent = android.content.Intent()
                .setClass(activity, SelectFreeOrPremiumActivity::class.java)
            intent.setFlags (android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK or android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(intent)
            activity.finish()
        }

    }

    private lateinit var binding: ActivitySelectfreeorpermiumBinding
    @Inject
    lateinit var admittedDataStore: AdmittedDataStore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectfreeorpermiumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureEdgeToEdge(
            backgroundColor = "#F1E9E5".toColorInt()
        )

        binding.rlContinueWithFreeVersion.setOnClickListener {
            lifecycleScope.launch {
                admittedDataStore.setFreeVersion(true)
                MainActivity.Intent(this@SelectFreeOrPremiumActivity)
            }
        }
        binding.btnUpgradeToPremium.setOnClickListener {
            SubscribeActivity.intent(this@SelectFreeOrPremiumActivity)
        }
    }
}