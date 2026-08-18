package com.realtouchapp.admitted.storage.database.dao

import androidx.room.*
import com.realtouchapp.admitted.network.entity.Test

@Dao
interface TestDao {


    //儲存單個
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTest(data: Test)

    //儲存多個
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTestListWithoutTransaction(datas: List<Test>)

    @Transaction
    fun insertTestList(datas: List<Test>){
        insertTestListWithoutTransaction(datas)
    }

    @Query("SELECT * FROM ${Test.TABLE_NAME} order by _id DESC")
    fun getAll(): List<Test>?

    //刪除多個
    @Query("DELETE from ${Test.TABLE_NAME}")
    fun deleteAllTest()

    @Delete
    fun deleteTest(tests: Test)
    @Delete
    fun deleteTest(datas: List<Test>)
//    //有條件刪除多個
//    @Query("DELETE FROM ${Test.TABLE_NAME} WHERE ${DaoKey.APPLICATIONID} = :applicationID")
//    fun deleteAllByApplicationID(applicationID:String)
}