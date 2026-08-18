package com.realtouchapp.admitted.begin.view

import android.content.Intent
import android.os.Bundle
import android.util.ArrayMap
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.AcknowledgePurchaseResponseListener
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.ProductDetailsResponseListener
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesResponseListener
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.begin.viewmodel.SubscribeActivityViewModel
import com.realtouchapp.admitted.begin.viewmodel.SubscribeStudentInfo
import com.realtouchapp.admitted.ext.configureEdgeToEdge
import com.realtouchapp.admitted.databinding.ActivitySubscribeBinding
import com.realtouchapp.admitted.ext.deCryption
import com.realtouchapp.admitted.ext.showSnackBar
import com.realtouchapp.admitted.network.api.AdmittedApi
import com.realtouchapp.admitted.network.entity.Student
import com.realtouchapp.admitted.network.entity.Subscribe
import com.realtouchapp.admitted.network.result.base.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Currency

@AndroidEntryPoint
class SubscribeActivity : AppCompatActivity() ,OnClickListener {
    companion object{
        private const val TAG = "SubscribeActivity"
        private const val STUDENT_INFO_KEY = "StudentInfoKey"
        private const val IS_INTENT_FROM_MAIN_KEY = "isIntentFromMainKey"

        private const val MONTHLY_BASE_PLAN_ID = "month"
        private const val QUARTERLY_BASE_PLAN_ID1 = "season"
        private const val QUARTERLY_BASE_PLAN_ID2 = "seasion"
        private const val YEARLY_BASE_PLAN_ID = "year"
        private const val FREE_3_DAYS_OFFER_ID = "free3days"

        fun intent(activity: AppCompatActivity) {
            val intent = Intent() .setClass(activity, SubscribeActivity::class.java)
            activity.startActivity(intent)
        }

        fun intentWithStudent(
            activity: AppCompatActivity,
            student: Student,
            method: Int,
            productId: String,
            isIntentFromMain: Boolean = false
        ) {
            val intent = Intent()
                .setClass(activity, SubscribeActivity::class.java)
                .putExtra(STUDENT_INFO_KEY, SubscribeStudentInfo(student, method, productId))
                .putExtra(IS_INTENT_FROM_MAIN_KEY, isIntentFromMain)

            intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(intent)
        }
    }

    private val viewModel by viewModels<SubscribeActivityViewModel>()
    private lateinit var binding: ActivitySubscribeBinding

    private var subscribeType = ""
    private var targetBasePlanIds: List<String> = emptyList()
    private var subscribe :Subscribe = Subscribe()

//    private val productID = "subscribe_active_month"
    private val defaultProductID = "member"
    private var subProductDetail: ProductDetails? = null
    private var billingClient: BillingClient? = null
    private var isBillingReady = false

    private var isIntentFromMain: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubscribeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        subscribe()

        configureEdgeToEdge()

        loadIntentExtras()
        createConnection()
        initialView()
    }

    private fun loadIntentExtras() {
        viewModel.studentInfo = intent.getSerializableExtra(STUDENT_INFO_KEY) as? SubscribeStudentInfo
        Log.d(TAG, "loadIntentExtras: studentInfo=${viewModel.studentInfo}")
        isIntentFromMain = intent.getBooleanExtra(IS_INTENT_FROM_MAIN_KEY, false)
        Log.d(TAG, "loadIntentExtras: isIntentFromMain=$isIntentFromMain")
    }

    private fun initialView(){
        binding.ivSubscribeBack.setOnClickListener(this)
        binding.cvActivitySubscribeMonthly.setOnClickListener(this)
        binding.cvActivitySubscribeQuarter.setOnClickListener(this)
        binding.cvActivitySubscribeYear.setOnClickListener(this)

    }

    private fun selecetSubscribe(type: Int) {
        binding.cbActivitySubscribeMonthly.isChecked = false
        binding.cbActivitySubscribeQuarter.isChecked = false
        binding.cbActivitySubscribeYear.isChecked = false
        enableContinue(isBillingReady)
        if (!isBillingReady) {
            showSnackBar(binding.root, getString(R.string.billing_unavailable_message))
        }
        subscribeType = type.toString()
        when(type){
            0 -> {
                targetBasePlanIds = listOf(MONTHLY_BASE_PLAN_ID)
                binding.cbActivitySubscribeMonthly.isChecked = true
            }
            1 -> {
                targetBasePlanIds = listOf(QUARTERLY_BASE_PLAN_ID1, QUARTERLY_BASE_PLAN_ID2)
                binding.cbActivitySubscribeQuarter.isChecked = true
            }
            2 -> {
                targetBasePlanIds = listOf(YEARLY_BASE_PLAN_ID)
                binding.cbActivitySubscribeYear.isChecked = true
            }
        }

    }

    private fun createConnection(){
        //购买交易更新监听，需要在初始化BillingClient时设置
        //所有购买都会回调此监听
        val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
//                for (purchase in purchases) {
//                    purchase.originalJson
//                }
            } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                // Handle an error caused by a user cancelling the purchase flow.
                showSnackBar(binding.root, getString(R.string.billing_cancelled_message))
//                for (purchase in purchases!!) {
//                    purchase.isAcknowledged
//                }
            } else {
                // Handle any other error codes.
            }

            when (billingResult.responseCode) {
                BillingClient.BillingResponseCode.OK -> {
                //购买商品成功
                    purchases?.let {
                        subscribe.payInfo = it.get(0).originalJson
                        handlePurchaseSuccessful(subscribe)
                    }

                }
                BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
                    purchases?.let {
                        subscribe.payInfo = it.get(0).originalJson
                        lifecycleScope.launch {
                            handlePurchase(it.get(0))
                        }
                    }

                }
                BillingClient.BillingResponseCode.USER_CANCELED -> {
                    //取消购买
                }
                BillingClient.BillingResponseCode.DEVELOPER_ERROR -> {


                }
                BillingClient.BillingResponseCode.BILLING_UNAVAILABLE -> {
                    isBillingReady = false
                    enableContinue(false)
                    showSnackBar(binding.root, getString(R.string.billing_unavailable_message))

                }
                else -> {
                //购买失败，具体异常码可以到BillingClient.BillingResponseCode中查看

                }
            }
        }

        //初始化BillingClient
        billingClient = BillingClient.newBuilder(this)
            .setListener(purchasesUpdatedListener)
            //支持待处理的交易
            .enablePendingPurchases()
            .build()

        //与GooglePlay连接状态监听
        val stateListener = object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    isBillingReady = true
                //连接成功，可以进行查询商品等操作
                    val productDetailsResponseListener = object : ProductDetailsResponseListener {
                        override fun onProductDetailsResponse(billingResult: BillingResult, productDetailsList: List<ProductDetails>) {
                            if (billingResult.responseCode != BillingClient.BillingResponseCode.OK || productDetailsList.isEmpty()) {
                                isBillingReady = false
                                enableContinue(false)
                                showSnackBar(binding.root, getString(R.string.billing_unavailable_message))
                                return
                            }
                            //productDetailsList为可用商品的集合
                            var text = ""
                            for (productDetail in productDetailsList) {
                                text += productDetail.name + "," + productDetail.productId + "," + productDetail.title + "," + productDetail.description
                                productDetail.subscriptionOfferDetails?.let {
                                    subProductDetail =productDetail
                                }
                                if (BillingClient.ProductType.INAPP == productDetail.productType && productDetail.oneTimePurchaseOfferDetails != null) {
                                    val googleProductPrice = productDetail.oneTimePurchaseOfferDetails!!.formattedPrice
                                    val googleCurrencyCode = productDetail.oneTimePurchaseOfferDetails!!.priceCurrencyCode
                                    val currencySymbol = currencyArrayMap[googleCurrencyCode] ?: googleCurrencyCode
                                    val replacePrice = replaceCurrencySymbol(googleProductPrice, googleCurrencyCode, currencySymbol)
                                } else if (BillingClient.ProductType.SUBS == productDetail.productType && productDetail.subscriptionOfferDetails != null) {
                                    val googleProductPrice = productDetail.subscriptionOfferDetails!![0].pricingPhases.pricingPhaseList[0].formattedPrice
                                    val googleCurrencyCode = productDetail.subscriptionOfferDetails!![0].pricingPhases.pricingPhaseList[0].priceCurrencyCode
                                    val currencySymbol = currencyArrayMap[googleCurrencyCode] ?: googleCurrencyCode
                                    val replacePrice = replaceCurrencySymbol(googleProductPrice, googleCurrencyCode, currencySymbol)
                                }
                            }


                        }
                    }
                    val subscriptionProductInfo = mutableListOf<QueryProductDetailsParams.Product>()
                    val productId = viewModel.studentInfo?.productId ?: defaultProductID
                    subscriptionProductInfo.add(
                        QueryProductDetailsParams.Product.newBuilder()
                            .setProductId(productId)
                            .setProductType(BillingClient.ProductType.SUBS)
                            .build()
                    )
                    val subscriptionProductDetailsParams = QueryProductDetailsParams.newBuilder()
                        .setProductList(subscriptionProductInfo)
                        .build()
                    billingClient!!.queryProductDetailsAsync(subscriptionProductDetailsParams, productDetailsResponseListener)
                } else {
                    isBillingReady = false
                    enableContinue(false)
                    showSnackBar(binding.root, getString(R.string.billing_unavailable_message))
                }
            }
            override fun onBillingServiceDisconnected() {
                isBillingReady = false
                enableContinue(false)
                showSnackBar(binding.root, getString(R.string.billing_unavailable_message))
            }
        }

        //与GooglePlay建立连接
        billingClient!!.startConnection(stateListener)
        for (availableCurrency in Currency.getAvailableCurrencies()) {
            //currentcyCode为key,货币符号为value
            //对于没有特定符号的货币，symbol与currencyCode相同。
            currencyArrayMap[availableCurrency.currencyCode] = availableCurrency.symbol
        }



        val purchasesResponseListener = PurchasesResponseListener { billingResult, list ->
        //list为购买交易的集合
        }


        //订阅商品交易查询
        val subscriptionsPurchasesQuery = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)
            .build()
        billingClient!!.queryPurchasesAsync(subscriptionsPurchasesQuery, purchasesResponseListener)

    }

    val currencyArrayMap = ArrayMap<String, String>()

    private fun buy() {

    }

    private fun subscribe(){
        viewModel.response.observe(this) { result ->
            Log.d(TAG, "subscribe: response=$result")

            when (result.status) {
                NetworkResult.Status.SUCCESS -> {
                    //成功
                    when (result.action) {
                        NetworkResult.Action.POST_SUBSCRIBE -> {
                            SplashActivity.Intent(this)
                        }
                        NetworkResult.Action.ADD_STUDENT_TO_PARENT -> {
                            viewModel.getStudentsUnderParent()
                        }
                        NetworkResult.Action.GET_STUDENTS -> {
                            if (isIntentFromMain) {
                                ParentMainActivity.intent(this)
                            } else {
                                ParentEditStudentActivity.intent(this)
                            }
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
                    } else if (result.statusCode == AdmittedApi.StatusCode.ERROR_SUBSCRIBE_FAILED.code) {
                        back()
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


    // 替換商品價格中的貨幣符號
    fun replaceCurrencySymbol(priceStr: String, currencyCode: String, currencySymbol: String?): String {
        var replacedPrice = priceStr
        if (priceStr.startsWith("$")) {
            if (currencySymbol != null) {
                if (currencySymbol == currencyCode) {
                // 沒有貨幣符號的情況，將貨幣碼拼接到前面
                    replacedPrice = "$currencySymbol$priceStr"
                } else {
                    if (!priceStr.startsWith(currencySymbol)) {
                        replacedPrice = priceStr.replace("$", currencySymbol)
                    }
                }
            }
        }
        return replacedPrice
    }

    private fun enableContinue(isEnable: Boolean) {
        if (isEnable) {
            binding.btnBeginStart.setBackgroundResource(R.drawable.selector_btn_default)
            binding.btnBeginStart.setOnClickListener(this)

        } else {
            binding.btnBeginStart.setBackgroundResource(R.drawable.vd_bg_btn_disabled)
            binding.btnBeginStart.setOnClickListener(null)
        }
    }


    fun buySubmite (billingClient: BillingClient,
                    productDetails: ProductDetails,
                    selectedOfferToken: String) {
        // An activity reference from which the billing flow will be launched.

        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                // retrieve a value for "productDetails" by calling queryProductDetailsAsync()
                .setProductDetails(productDetails)
                // to get an offer token, call ProductDetails.subscriptionOfferDetails()
                // for a list of offers that are available to the user
                .setOfferToken(selectedOfferToken)
                .build()
        )

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        // Launch the billing flow
        val billingResult = billingClient.launchBillingFlow(this, billingFlowParams)
        if (billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
            showSnackBar(binding.root, getString(R.string.billing_unavailable_message))
        }

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_subscribe_back -> {
                back()

//                viewModel.logout(this@SubscribeActivity,{BeginActivity.Intent(this@SubscribeActivity)})
//                SelectFreeOrPremiumActivity.Intent(this@SubscribeActivity)
            }
            R.id.cv_activity_subscribe_monthly -> {
                selecetSubscribe(0)
            }
            R.id.cv_activity_subscribe_quarter -> {
                selecetSubscribe(1)
            }
            R.id.cv_activity_subscribe_year -> {
                selecetSubscribe(2)
            }
            R.id.btn_begin_start -> {
//                viewModel.studentInfo?.let {
//                    subscribe.type = subscribeType
//                    subscribe.payInfo = "{}"
//                    viewModel.addStudentAndPostSubscribe(subscribe)
//                }

                subProductDetail?.let { it1 ->
                    billingClient?.let { it2 ->
                        subscribe.type = subscribeType

                        val offerDetail = subProductDetail!!.subscriptionOfferDetails!!.firstOrNull {
                            targetBasePlanIds.contains(it.basePlanId)
                                    && it.offerId == FREE_3_DAYS_OFFER_ID
                        }
                        buySubmite(
                            it2,
                            it1,
                            offerDetail?.offerToken ?: ""
                        )
                    }
                }
            }
        }

    }

    private suspend fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                try {
                    withContext(Dispatchers.IO) {
                        billingClient!!.acknowledgePurchase(acknowledgePurchaseParams.build(),
                            object : AcknowledgePurchaseResponseListener {
                                override fun onAcknowledgePurchaseResponse(p0: BillingResult) {
                                    handlePurchaseSuccessful(subscribe)
                                }
                            })
                    }
                    // 这里可以添加处理确认购买成功的代码
                } catch (e: Exception) {
                    // 这里可以处理错误情况，例如：网络问题或确认失败
                }
            }
        }
    }

    private fun handlePurchaseSuccessful(subscribe: Subscribe) {
        viewModel.studentInfo?.let {
            viewModel.addStudentAndPostSubscribe(subscribe)
        } ?: run {
            viewModel.postSubscribe(subscribe)
        }
    }

    private fun back() {
        viewModel.studentInfo?.let {
            ScanStudentQrcodeActivity.intent(this, isIntentFromMain)
        } ?: run {
            finish()
        }
    }
}
