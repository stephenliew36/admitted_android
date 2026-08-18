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
@Entity(tableName = FCMessage.TABLE_NAME)
data class FCMessage(
    @ColumnInfo(name = "_id")
    @PrimaryKey(autoGenerate = true)
    var notificatinoId: Int?,
    var title: String?,
    var content: String?,
    var createTimestamp: String?,
    var readTimestamp: String?,
    var actionType: Int?
)
{
    constructor():this(null,"2",null,null,null,null)
    companion object {
        const val TABLE_NAME = "FCMessage"
    }
}