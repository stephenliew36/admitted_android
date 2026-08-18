package com.realtouchapp.admitted.network.request.user

import com.google.gson.GsonBuilder
import com.realtouchapp.admitted.encryption.AdmittedEncrption
import com.realtouchapp.admitted.ext.testPrint

/** 檢查訂閱資訊新增訂閲並新增學生 */
data class PostUserStudentRequest(
    /** GET取得的method直接回傳?? */
    val method: String,
    /** 訂閱類型 */
    val type: String,
    /** 訂閱日期 */
    val subscriptionAt: String? = null,
    /** 付款金額 */
    val amount: String? = null,
    /** 幣值 */
    val currency: String? = null,
    /** Json格式字串用來記錄Apple或是Google的付款資訊 (GOOGLE時必要) */
    val payInfo: String
) {
    companion object {
        /** GET取得的method直接回傳 - Google-家長1 */
        val METHOD_GOOGLE_PARENT1 = "201"
        /** GET取得的method直接回傳 - Google-家長2 */
        val METHOD_GOOGLE_PARENT2 = "202"
        /** GET取得的method直接回傳 - Google-家長3 */
        val METHOD_GOOGLE_PARENT3 = "203"
        /** GET取得的method直接回傳 - Google-家長4 */
        val METHOD_GOOGLE_PARENT4 = "204"
        /** GET取得的method直接回傳 - Google-家長5 */
        val METHOD_GOOGLE_PARENT5 = "205"

        /** 訂閱類型-月 */
        val TYPE_MONTHLY = "0"
        /** 訂閱類型-季 */
        val TYPE_QUARTERLY = "1"
        /** 訂閱類型-年 */
        val TYPE_YEARLY = "2"

        /** 幣值-無 */
        val CURRENCY_NONE = "0"
        /** 幣值-美金(US$ ) */
        val CURRENCY_USD = "1"
        /** 幣值-台幣(NT$ ) */
        val CURRENCY_TWD = "2"
    }
}

/** 設定/解除目前查看的學生 */
data class PutUserStudentRequest(
    /** True=目前查看; False=離開 */
    val current: Boolean
)