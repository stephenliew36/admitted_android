package com.realtouchapp.admitted.network.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.GsonBuilder
import com.realtouchapp.admitted.encryption.AdmittedEncrption
import com.realtouchapp.admitted.ext.testPrint


/**

 * */
data class Subscribe(
    var subscribeID: String?,
    var method: String ="2",
    var type: String?,
    var subscriptionAt: String?,
    var amount: String?,
    var currency: String?,
    var receipt: String?,
    var payInfo: String?
)
{
    constructor():this(null,"2",null,null,null,null,null,null)
    companion object {
        const val TABLE_NAME = "Subscribe"
    }
}