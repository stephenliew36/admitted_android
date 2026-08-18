package com.realtouchapp.admitted.storage.database.dao

import androidx.room.*
import com.realtouchapp.admitted.network.entity.Extracurricular

@Dao
interface ExtracurricularDao {

    //儲存單個
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExtracurricular(data: Extracurricular)

    //儲存多個
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExtracurricularListWithoutTransaction(datas: List<Extracurricular>)

    @Transaction
    fun insertExtracurricularList(datas: List<Extracurricular>){
        insertExtracurricularListWithoutTransaction(datas)
//        datas.forEach {
//            if (countUsersWithplaceId(it.placeId)==0){
//                insertExtracurricular(it)
//            }
//        }
    }
//    @Query("UPDATE ${Extracurricular.TABLE_NAME} SET extracurricularId = :extracurricularId WHERE placeId == :placeId")
//    fun updateFromPlaceID(extracurricularId:String,placeId: String)


    @Query("SELECT * FROM ${Extracurricular.TABLE_NAME} order by _id DESC")
    fun getAll(): List<Extracurricular>?

//    @Query("SELECT COUNT(*) FROM ${Extracurricular.TABLE_NAME} WHERE name = :placeId")
//    fun countUsersWithplaceId(placeId: String): Int

    @Query("UPDATE ${Extracurricular.TABLE_NAME} SET extracurricularId = ''")
    fun clearAllextracurricularId()


    @Transaction
    fun insertFavList(datas: List<Extracurricular>){
        clearAllextracurricularId()
        insertExtracurricularList(datas)
    }

    //刪除多個
    @Query("DELETE from ${Extracurricular.TABLE_NAME}")
    fun deleteAllExtracurricular()

    @Delete
    fun deleteExtracurricular(Extracurricular: Extracurricular)

    @Delete
    fun deleteExtracurricular(datas: List<Extracurricular>)
//    //有條件刪除多個
//    @Query("DELETE FROM ${Extracurricular.TABLE_NAME} WHERE ${DaoKey.APPLICATIONID} = :applicationID")
//    fun deleteAllByApplicationID(applicationID:String)
}