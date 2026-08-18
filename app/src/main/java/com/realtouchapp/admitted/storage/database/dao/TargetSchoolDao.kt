package com.realtouchapp.admitted.storage.database.dao

import androidx.room.*
import com.realtouchapp.admitted.network.entity.TargetSchool

@Dao
interface TargetSchoolDao {


    //儲存單個
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTargetSchool(data: TargetSchool)

    //儲存多個
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTargetSchoolListWithoutTransaction(datas: List<TargetSchool>)

    @Transaction
    fun insertTargetSchoolList(datas: List<TargetSchool>){
        insertTargetSchoolListWithoutTransaction(datas)
    }

    //    //有條件刪除多個
    @Query("SELECT * FROM ${TargetSchool.TABLE_NAME} WHERE _id = :schoolId")
    fun getById(schoolId:Int):TargetSchool?

    @Query("SELECT * FROM ${TargetSchool.TABLE_NAME} WHERE _id = :schoolId AND enable = 1")
    fun getEnabledById(schoolId:Int):TargetSchool?

    @Query("SELECT * FROM ${TargetSchool.TABLE_NAME} order by schoolName ASC")
    fun getAll(): List<TargetSchool>?

    @Query("SELECT * FROM ${TargetSchool.TABLE_NAME} WHERE enable = 1 order by schoolName ASC")
    fun getAllEnabled(): List<TargetSchool>?

    @Query("SELECT * FROM ${TargetSchool.TABLE_NAME} WHERE _id IN (:ids) order by schoolName ASC")
    fun getFromIds(ids:List<Int>): List<TargetSchool>?

    @Query("SELECT * FROM ${TargetSchool.TABLE_NAME} WHERE _id IN (:ids) AND enable = 1 order by schoolName ASC")
    fun getEnabledFromIds(ids:List<Int>): List<TargetSchool>?

    @Query("SELECT * FROM ${TargetSchool.TABLE_NAME} WHERE schoolName LIKE '%' || :keyword || '%' order by schoolName ASC")
    fun getAll(keyword:String): List<TargetSchool>?

    @Query("SELECT * FROM ${TargetSchool.TABLE_NAME} WHERE schoolName LIKE '%' || :keyword || '%' AND enable = 1 order by schoolName ASC")
    fun getAllEnabled(keyword:String): List<TargetSchool>?

    //刪除多個
    @Query("DELETE from ${TargetSchool.TABLE_NAME}")
    fun deleteAllTargetSchool()

    @Delete
    fun deleteTargetSchool(TargetSchools: TargetSchool)

    @Delete
    fun deleteTargetSchools(datas: List<TargetSchool>)

//    @Query("SELECT * FROM users WHERE age > :minAge AND gender = :gender " +
//            "AND (first_name LIKE '%' || :name || '%' OR last_name LIKE '%' || :name || '%')" +
//            "AND address IN (:addresses)")
//    List<User> getUsersByCriteria(int minAge, String gender, String name, List<String> addresses);
//}

    //country
    //難度
    //public
    //others
    fun createQueryCountry(countries:ArrayList<String>):String{
        var countyRules = ""
        if (countries.size == 1){
            //只有一項
            countyRules = " country = ${countries.get(0)}"
            return countyRules
        }else  if (countries.size >1){
            for (country in countries){
                countyRules = "("
                if (country.equals(countries.last())){
                    //最後一個不加OR
                    countyRules = countyRules + " country = ${country})"
                }else{
                    countyRules = countyRules + " country = ${country} OR "
                }
            }
            return countyRules
        }else{
            return ""
        }
        return ""
    }
    fun createQueryOther(isLiberalArtsSchools:Int,isArtsSchools:Int):String{
        var rules = ""
        if (isLiberalArtsSchools == 1) {
            rules = "(isLiberalArtsSchools = 1 "
        }
        if (isArtsSchools ==1){
            if (!rules.equals("")){
                rules = rules +"OR isArtsSchools = 1)"
            }else{
                rules = "(isArtsSchools = 1 )"
            }
        }else{
            if (!rules.equals("")){
                rules = rules +")"
            }
        }
        return rules
    }

    fun createQueryIsPublic(isPublic:Int):String{
        var rules = ""
        if (isPublic == 1) {
            rules = "(isPublic = 1 )"
        }else{
            rules = "(isPublic = 0 )"
        }
        return rules
    }

//    //有條件刪除多個
//    @Query("DELETE FROM ${TargetSchool.TABLE_NAME} WHERE ${DaoKey.APPLICATIONID} = :applicationID")
//    fun deleteAllByApplicationID(applicationID:String)
}