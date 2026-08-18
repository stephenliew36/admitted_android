package com.realtouchapp.admitted.network.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.GsonBuilder
import com.realtouchapp.admitted.encryption.AdmittedEncrption
import com.realtouchapp.admitted.ext.testPrint


@Entity(tableName = Honors.TABLE_NAME)
/**
 * 說明
 *@param grade 獲獎年級  -1:尚未輸入 9:9th grade 10:10th grade 11:11th grade 12:12th grade
 *@param type 榮譽類型 1:Competition 2:Certificate 3:Research
 *@param rank 名次 1:First place 2:Sceond place 3:Third place 4:others
 *@param researchType 研究類型 1:SCI/SSCI 2:International Journal 3:International Conference 4:Student Journal 5:Self Research
 *@param pointType 1=Community service, 11=Internship,12=School Club, 21=Competition, 22=Certificate,23=Research
 *@param level 榮譽規模 1:School 2:State/Regional 3:National 4:International
 *
 */
data class Honors(
    @ColumnInfo(name = "_id")
    @PrimaryKey
    var id: Int?,
    var grade: String?,
    var level: Int?,
    var point: Int?,
    var pointType: Int?,
    var rank: Int?,
    var researchType: Int?,
    var title: String?,
    var type: Int?,
    @Ignore
    var actionType: Int?=null
)
{
    constructor():this(null,null,null,null,null,null,null,null,null,null)
    companion object {
        const val TABLE_NAME = "Honors"
    }
}