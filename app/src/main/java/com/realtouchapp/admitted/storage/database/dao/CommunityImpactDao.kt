package com.realtouchapp.admitted.storage.database.dao

import androidx.room.*
import com.realtouchapp.admitted.network.entity.CommunityImpact

@Dao
interface CommunityImpactDao {


    //儲存單個
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCommunityImpact(data: CommunityImpact)

    //儲存多個
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCommunityImpactListWithoutTransaction(datas: List<CommunityImpact>)

    @Transaction
    fun insertCommunityImpactList(datas: List<CommunityImpact>){
        insertCommunityImpactListWithoutTransaction(datas)
    }

    @Query("SELECT * FROM ${CommunityImpact.TABLE_NAME} order by _id ASC")
    fun getAll(): List<CommunityImpact>?

    //刪除多個
    @Query("DELETE from ${CommunityImpact.TABLE_NAME}")
    fun deleteAllCommunityImpact()

    @Delete
    fun deleteCommunityImpact(communityImpacts: CommunityImpact)

    @Delete
    fun deleteCommunityImpacts(datas: List<CommunityImpact>)

//    //有條件刪除多個
//    @Query("DELETE FROM ${CommunityImpact.TABLE_NAME} WHERE ${DaoKey.APPLICATIONID} = :applicationID")
//    fun deleteAllByApplicationID(applicationID:String)
}