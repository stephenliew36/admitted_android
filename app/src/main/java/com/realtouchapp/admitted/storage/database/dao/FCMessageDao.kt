package com.realtouchapp.admitted.storage.database.dao

import androidx.room.*
import com.realtouchapp.admitted.network.entity.FCMessage

@Dao
interface FCMessageDao {


    //儲存單個
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFCMessage(data: FCMessage)

    //儲存多個
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFCMessageListWithoutTransaction(datas: List<FCMessage>)

    @Transaction
    fun insertFCMessageList(datas: List<FCMessage>){
        insertFCMessageListWithoutTransaction(datas)
    }

    @Query("SELECT * FROM ${FCMessage.TABLE_NAME} order by _id DESC")
    fun getAll(): List<FCMessage>?

    //刪除多個
    @Query("DELETE from ${FCMessage.TABLE_NAME}")
    fun deleteAllFCMessage()

    @Delete
    fun deleteFCMessage(FCMessages: FCMessage)
    @Delete
    fun deleteFCMessage(datas: List<FCMessage>)
//    //有條件刪除多個
//    @Query("DELETE FROM ${FCMessage.TABLE_NAME} WHERE ${DaoKey.APPLICATIONID} = :applicationID")
//    fun deleteAllByApplicationID(applicationID:String)
}