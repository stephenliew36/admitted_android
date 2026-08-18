package com.realtouchapp.admitted.storage.database.dao

import androidx.room.*
import com.realtouchapp.admitted.network.entity.IntellectualCuriosity

@Dao
interface IntellectualCuriosityDao {


    //儲存單個
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIntellectualCuriosity(data: IntellectualCuriosity)

    //儲存多個
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIntellectualCuriosityListWithoutTransaction(datas: List<IntellectualCuriosity>)

    @Transaction
    fun insertIntellectualCuriosityList(datas: List<IntellectualCuriosity>){
        insertIntellectualCuriosityListWithoutTransaction(datas)
    }

    @Query("SELECT * FROM ${IntellectualCuriosity.TABLE_NAME} order by _id DESC")
    fun getAll(): List<IntellectualCuriosity>?

    //刪除多個
    @Query("DELETE from ${IntellectualCuriosity.TABLE_NAME}")
    fun deleteAllIntellectualCuriosity()

    @Delete
    fun deleteIntellectualCuriosity(intellectualCuriositys: IntellectualCuriosity)
    @Delete
    fun deleteIntellectualCuriosities(intellectualCuriosities: ArrayList<IntellectualCuriosity>)
//    //有條件刪除多個
//    @Query("DELETE FROM ${IntellectualCuriosity.TABLE_NAME} WHERE ${DaoKey.APPLICATIONID} = :applicationID")
//    fun deleteAllByApplicationID(applicationID:String)
}