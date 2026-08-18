package com.realtouchapp.admitted.storage.database.dao

import androidx.room.*
import com.realtouchapp.admitted.network.entity.Honors

@Dao
interface HonorsDao {

    //儲存單個
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHonors(data: Honors)

    //儲存多個
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHonorsListWithoutTransaction(datas: List<Honors>)

    @Transaction
    fun insertHonorsList(datas: List<Honors>){
        insertHonorsListWithoutTransaction(datas)
    }
    @Query("SELECT * FROM ${Honors.TABLE_NAME} order by _id DESC")
    fun getAll(): List<Honors>?

    //條件讀取列表
//    @Query("SELECT * FROM  ${Honors.TABLE_NAME} WHERE ${DaoKey.APPLICATIONID} = :applicationID  ORDER BY ${"insertTime"} DESC")
//    fun getHonorsListByApplicationID(applicationID:String): List<Honors>
//
//    //多條件讀取
//    @Query("SELECT * FROM ${Honors.TABLE_NAME} WHERE ${DaoKey.APPLICATIONID} = :applicationID AND ${"HonorsID"} = :HonorsID   ORDER BY ${"insertTime"} DESC")
//    fun getHonorsListByApplicationID(applicationID:String, HonorsID:String): List<Honors>
//
//    //多條件讀取
//    @Query("SELECT * FROM ${Honors.TABLE_NAME} WHERE ${DaoKey.APPLICATIONID} = :applicationID AND ${"HonorsID"} = :HonorsID   ORDER BY ${"insertTime"} DESC")
//    fun getHonorsByApplicationID(applicationID:String, HonorsID:String): Honors
//
//    //相似搜尋
//    @Query("SELECT * FROM ${Honors.TABLE_NAME} WHERE ${DaoKey.APPLICATIONID} = :applicationID AND ${"HonorsID"} = :keyword ORDER BY ${"insertTime"} DESC")
//    fun getHonorsListLikeKeyword(applicationID:String,keyword:String): List<Honors>
//
//    //刪除單個
//    @Query("DELETE FROM ${Honors.TABLE_NAME} WHERE ${DaoKey.APPLICATIONID} = :applicationID AND ${"HonorsID"} = :HonorsID")
//    fun deleteByID(applicationID:String,HonorsID:String)

    //刪除多個
    @Query("DELETE from ${Honors.TABLE_NAME}")
    fun deleteAllHonors()

    @Delete
    fun deleteHonors(honors: Honors)

    @Delete
    fun deleteHonors(datas: List<Honors>)
//    //有條件刪除多個
//    @Query("DELETE FROM ${Honors.TABLE_NAME} WHERE ${DaoKey.APPLICATIONID} = :applicationID")
//    fun deleteAllByApplicationID(applicationID:String)
}