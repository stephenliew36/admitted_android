package com.realtouchapp.admitted.network.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.GsonBuilder
import com.realtouchapp.admitted.encryption.AdmittedEncrption
import com.realtouchapp.admitted.ext.testPrint
import com.realtouchapp.admitted.network.entity.CommunityImpact.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
/**
 * @param role 擔任角色 1:Leader/President 2:Member/Participant
 * @param pointType 1=Community service, 11=Internship,12=School Club, 21=Competition, 22=Certificate,23=Research
 *@param type 機構或是組織規模 1:International  2:Non International
 * */
data class CommunityImpact(
    @ColumnInfo(name = "_id")
    @PrimaryKey
    var id: Int?,
    var description: String?,
    var grade: String?,
    var hoursPerWeek: Int?,
    var name: String?,
    var point: Int?,
    var pointType: Int?,
    var role: Int?,
    var title: String?,
    var type: Int?,
    var weekPerYear: Int?,
    @Ignore
    var actionType: Int?=null
)
{
    constructor():this(null,null,null,null,null,null,1,null,null,null,null,null)
    companion object {
        const val TABLE_NAME = "CommunityImpact"
    }
}