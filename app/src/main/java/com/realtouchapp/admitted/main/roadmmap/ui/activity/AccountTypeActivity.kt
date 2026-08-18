package com.realtouchapp.admitted.main.roadmmap.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.databinding.ActivityAccountTypeBinding
import com.realtouchapp.admitted.ext.configureEdgeToEdge

class AccountTypeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountTypeBinding

    companion object {
        val BUNDLE_SUBSCRIPTIONTYPE = "SUBSCRIPTIONTYPE"
        val BUNDLE_EXPIRATIONDATE = "EXPIRATIONDATE"
        fun Intent(activity: AppCompatActivity, subscriptionType: String, expirationDate: String) {
            val bundle = Bundle().apply {
                putString(BUNDLE_SUBSCRIPTIONTYPE, subscriptionType)
                putString(BUNDLE_EXPIRATIONDATE, expirationDate)
            }

            val intent = android.content.Intent().setClass(activity, AccountTypeActivity::class.java).putExtras(bundle)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureEdgeToEdge()

        val subscriptionType = when (intent.getStringExtra(BUNDLE_SUBSCRIPTIONTYPE)) {
            "0" -> "Monthly"
            "1" -> "Quarterly"
            "2" -> "Yearly"
            else -> ""
        }
        binding.tvActivityAccounttypeInfo.text = getString(R.string.account_type_time_info, subscriptionType, intent.getStringExtra(BUNDLE_EXPIRATIONDATE))
        binding.ivActivityBack.setOnClickListener { finish()   }
    }

}