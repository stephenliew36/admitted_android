package com.realtouchapp.admitted.storage.database.dao

import androidx.room.*
import com.realtouchapp.admitted.network.entity.Course

@Dao
interface CourseDao {


    //儲存單個
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCourse(data: Course)

    //儲存多個
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCourseListWithoutTransaction(datas: List<Course>)

    @Transaction
    fun insertCourseList(datas: List<Course>){
        insertCourseListWithoutTransaction(datas)
    }

    @Query("SELECT * FROM ${Course.TABLE_NAME} order by _id DESC")
    fun getAll(): List<Course>?

    //刪除多個
    @Query("DELETE from ${Course.TABLE_NAME}")
    fun deleteAllCourse()

    @Delete
    fun deleteCourse(data: Course)

    @Delete
    fun deleteCourses(datas: List<Course>)
//    //有條件刪除多個
//    @Query("DELETE FROM ${Course.TABLE_NAME} WHERE ${DaoKey.APPLICATIONID} = :applicationID")
//    fun deleteAllByApplicationID(applicationID:String)
}