package com.realtouchapp.admitted.network.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.GsonBuilder
import com.realtouchapp.admitted.encryption.AdmittedEncrption
import com.realtouchapp.admitted.ext.testPrint
import com.realtouchapp.admitted.network.entity.IntellectualCuriosity.Companion.TABLE_NAME

@Entity (tableName = TABLE_NAME)
/**
 * @param role 擔任角色 1:Leader/President 2:Member/Participant
 * @param type 類型 1:Academic 2:Art 3:Athletics:Club 4:Athletics:JV/Varsity 5:Career Oriented 6:Internship 7:Journalism/Publication
 * @param pointType 1=Community service, 11=Internship,12=School Club, 21=Competition, 22=Certificate,23=Research
 *
 *
 * */
data class IntellectualCuriosity(
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
) {
    constructor():this(null,null,null,null,null,null,null,null,null,null,null,null)
    companion object {
        const val TABLE_NAME = "IntellectualCuriosity"
    }
}