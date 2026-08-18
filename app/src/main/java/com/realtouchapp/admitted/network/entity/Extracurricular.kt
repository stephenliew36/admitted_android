package com.realtouchapp.admitted.network.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = Extracurricular.TABLE_NAME)
data class Extracurricular(
    var extracurricularId: Int?,
    val url: String,
    val activityType: String,
    val address: String,
    val description: String,
    val email: String,
    val imageUrl: String,
    /*機構類型 目前先以下六個 "hospital", "orphanage", "library", "museum & gallery", "animal shelter", "nursing home",*/
    val institutionType: String,
    val lat: String,
    val lng: String,
    val name: String,
    val phone: String,
    @ColumnInfo(name = "_id")
    @PrimaryKey
    val placeId: String
):java.io.Serializable{
    companion object {
        const val TABLE_NAME = "Extracurricular"
    }
}

enum class ExtracurricularInstitutionType(val type:String){
    HOSPITAL("hospital"),
    ORPHANAGE("orphanage"),
    LIBRARY("library"),
    MUSEUM_GALLERY("museum & gallery"),
    ANIMAL_SHELTER("animal shelter"),
    NURSING_HOME("nursing home")

}