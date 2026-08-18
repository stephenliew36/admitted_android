package com.realtouchapp.admitted.main.explore.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.google.gson.Gson
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.ext.testPrint
import com.realtouchapp.admitted.network.api.AdmittedReposritory
import com.realtouchapp.admitted.network.entity.*
import com.realtouchapp.admitted.network.result.base.CommonResponse
import com.realtouchapp.admitted.network.result.base.NetworkResult
import com.realtouchapp.admitted.storage.database.AdmittedDataBase
import com.realtouchapp.admitted.storage.database.DataConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.stream.Collectors
import javax.inject.Inject

/*
    作者: RobertChou
    時間: 2023/3/23 上午 01:34
    信箱: didi31139@gmail.com
    說明:
*/
@HiltViewModel
class ExtracurricularMainFragmentViewModel @Inject constructor(@ApplicationContext private val applicationContext: Context, private val admittedReposritory: AdmittedReposritory) : ViewModel(){

//    private val _categoryTargetSchools = MutableLiveData<ArrayList<HashMap<String, ArrayList<Extracurricular>>>>()
//    val categoryTargetSchools = _categoryTargetSchools
//

    fun getExtraGooglePlace(keyword: String, extraFilter: ExtraFilter,method:(ArrayList<HashMap<String, ArrayList<Extracurricular>>>)->Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            var extracurriculars = ArrayList<Extracurricular>()
            //先判斷 是否為空代表權不
            val categories = ArrayList<HashMap<String, ArrayList<Extracurricular>>>()
            extracurriculars = admittedReposritory.extracurricularDao.getAll() as ArrayList<Extracurricular>
            if (extraFilter.isSaved) { extracurriculars = collect(extracurriculars) { it.extracurricularId !=null }}
            if (keyword.isNullOrEmpty()) {
                //顯示全部分類
                if (extraFilter.isFilter()) {
                     //有分類篩選
                    val result = ArrayList<Extracurricular>()
                    if (extraFilter.isHospitals) { result.addAll(collect(extracurriculars) { it.institutionType == ExtracurricularInstitutionType.HOSPITAL.type })}
                    if (extraFilter.isOrphanages) { result.addAll(collect(extracurriculars) { it.institutionType == ExtracurricularInstitutionType.ORPHANAGE.type })}
                    if (extraFilter.isNursingHomes) { result.addAll(collect(extracurriculars) { it.institutionType == ExtracurricularInstitutionType.NURSING_HOME.type })}
                    if (extraFilter.isLibraries) { result.addAll(collect(extracurriculars) { it.institutionType == ExtracurricularInstitutionType.LIBRARY.type })}
                    if (extraFilter.isMusuemsGalleries) { result.addAll(collect(extracurriculars) { it.institutionType == ExtracurricularInstitutionType.MUSEUM_GALLERY.type })}
                    if (extraFilter.isAnimalShelters) { result.addAll(collect(extracurriculars) { it.institutionType == ExtracurricularInstitutionType.ANIMAL_SHELTER.type })}
                    categories.addMap("filters", result)
                    viewModelScope.launch(Dispatchers.Main) {
                        method(categories)
                    }
                } else {
                    //沒有分類篩選
                    categories.addMap("Hospitals", collect(extracurriculars) { it.institutionType == ExtracurricularInstitutionType.HOSPITAL.type })
                    categories.addMap("Orphanages", collect(extracurriculars) { it.institutionType == ExtracurricularInstitutionType.ORPHANAGE.type })
                    categories.addMap("Nursing Homes", collect(extracurriculars) { it.institutionType == ExtracurricularInstitutionType.NURSING_HOME.type })
                    categories.addMap("Libraries", collect(extracurriculars) { it.institutionType == ExtracurricularInstitutionType.LIBRARY.type })
                    categories.addMap("Museums & Galleries", collect(extracurriculars) { it.institutionType == ExtracurricularInstitutionType.MUSEUM_GALLERY.type })
                    categories.addMap("Animal Shelters", collect(extracurriculars) { it.institutionType == ExtracurricularInstitutionType.ANIMAL_SHELTER.type })
                    viewModelScope.launch(Dispatchers.Main) {
                        method(categories)
                    }
                }


            } else {
                //有篩選文字
                if (extraFilter.isFilter()){
                    val result = ArrayList<Extracurricular>()
                    if (extraFilter.isHospitals) { result.addAll(collect(extracurriculars) { it.institutionType == ExtracurricularInstitutionType.HOSPITAL.type })}
                    if (extraFilter.isOrphanages) { result.addAll(collect(extracurriculars) { it.institutionType == ExtracurricularInstitutionType.ORPHANAGE.type })}
                    if (extraFilter.isNursingHomes) { result.addAll(collect(extracurriculars) { it.institutionType == ExtracurricularInstitutionType.NURSING_HOME.type })}
                    if (extraFilter.isLibraries) { result.addAll(collect(extracurriculars) { it.institutionType == ExtracurricularInstitutionType.LIBRARY.type })}
                    if (extraFilter.isMusuemsGalleries) { result.addAll(collect(extracurriculars) { it.institutionType == ExtracurricularInstitutionType.MUSEUM_GALLERY.type })}
                    if (extraFilter.isAnimalShelters) { result.addAll(collect(extracurriculars) { it.institutionType == ExtracurricularInstitutionType.ANIMAL_SHELTER.type })}
                    categories.addMap("filters", collect(result) { it.name.contains(keyword, ignoreCase = true)})
                    viewModelScope.launch(Dispatchers.Main) {
                        method(categories)
                    }
                }else{
                    categories.addMap("filters", collect(extracurriculars) { it.name.contains(keyword, ignoreCase = true)})
                    viewModelScope.launch(Dispatchers.Main) {
                        method(categories)
                    }
                }
            }

        }
    }
    private fun collect(targetSchools: ArrayList<Extracurricular>, meathod: (target: Extracurricular) -> Boolean): ArrayList<Extracurricular> {
        return targetSchools.stream().filter {
            meathod(it)
        }.collect(Collectors.toList()) as ArrayList<Extracurricular>
    }
    private fun ArrayList<HashMap<String, ArrayList<Extracurricular>>>.addMap(
        titile: String,
        targetSchools: ArrayList<Extracurricular>
    ) {
        if (targetSchools.size > 0) {
            val map = HashMap<String, ArrayList<Extracurricular>>()
            map.put(titile, targetSchools)
            this.add(map)
        }


    }
    
    fun checkLocation(location: GooglePlaceNearby.Location){
        CoroutineScope(Dispatchers.IO).launch {
            var oldlcation = admittedReposritory.keyValueDao.getLocation()
            if(oldlcation!=null) {
                if (calculateDistanceInKilometers(location.lat,location.lng,oldlcation.lat,oldlcation.lng) > 5) {
                    //距離上次5公里重新搜尋
                    getAllplaces(location.lat.toString(),location.lng.toString())
                }
            }else{
                //第一次一定要抓資料
                getAllplaces(location.lat.toString(),location.lng.toString())
            }
            admittedReposritory.keyValueDao.saveLocation(location)
        }

    }


    private fun getAllplaces(lat:String,lng:String){
        CoroutineScope(Dispatchers.IO).launch {
            var extracurriculars = ArrayList<Extracurricular>()

            var hospitals = getPlacesFromUrl(lat,lng,"hospital")?.toExtracurricular(ExtracurricularInstitutionType.HOSPITAL.type)
            hospitals?.let { extracurriculars.addAll(it) }

            var orphanages = getPlacesFromUrl(lat,lng,"orphanage")?.toExtracurricular(ExtracurricularInstitutionType.ORPHANAGE.type)
            orphanages?.let { extracurriculars.addAll(it) }
            var nursings = getPlacesFromUrl(lat,lng,"nursing home")?.toExtracurricular(ExtracurricularInstitutionType.NURSING_HOME.type)
            nursings?.let { extracurriculars.addAll(it) }
            var library = getPlacesFromUrl(lat,lng,"library")?.toExtracurricular(ExtracurricularInstitutionType.LIBRARY.type)
            library?.let { extracurriculars.addAll(it) }
            var musuem = getPlacesFromUrl(lat,lng,"museum+gallery")?.toExtracurricular(ExtracurricularInstitutionType.MUSEUM_GALLERY.type)
            musuem?.let { extracurriculars.addAll(it) }
            var animal = getPlacesFromUrl(lat,lng,"animal shelter")?.toExtracurricular(ExtracurricularInstitutionType.ANIMAL_SHELTER.type)
            animal?.let { extracurriculars.addAll(it) }
            var room = Room.databaseBuilder(applicationContext, AdmittedDataBase::class.java, AdmittedDataBase.DATABASE_NAME).fallbackToDestructiveMigration() /* 如果遷移失敗，重建資料庫*/.addTypeConverter(DataConverter())/*            .addMigrations(AdmittedDataBase.MIGRATION_1_2)*/.build()
            room.getExtracurricularDao().insertExtracurricularList(extracurriculars)

            var results = room.getExtracurricularDao().getAll()
            results?.forEach {
                testPrint("results",it.name)
            }
            room.close()
        }
    }


    fun ArrayList<GooglePlaceNearby.Result>.toExtracurricular(institutionType:String):ArrayList<Extracurricular>{
        val extras=  ArrayList<Extracurricular>()
        forEach {
            extras.add(it.toExtracurricular(institutionType))
        }
        return extras
    }

    private fun getPlacesFromUrl(lat:String,lng:String,keyword:String): ArrayList<GooglePlaceNearby.Result>? {
        val client = OkHttpClient()
        val url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=${lat},${lng}&radius=50000&key=${applicationContext.getString(R.string.google_maps_api_key)}&keyword=${keyword}"

        val request = Request.Builder()
            .url(url)
            .build()

        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            // 使用 Gson 將 JSON 資料轉換成 Kotlin data class
            val gson = Gson()
            val googlePlaceNearby = gson.fromJson(responseBody, GooglePlaceNearby::class.java)

            return googlePlaceNearby.results
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: com.google.gson.JsonSyntaxException) {
            e.printStackTrace()
        }

        return null
    }

    fun calculateDistanceInKilometers(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadiusKm = 6371 // 地球半徑，單位：公里

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)

        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        var number = earthRadiusKm * c
        val wholePart = number.toInt()
        val decimalPart = (number % 1 * 10).toInt() / 10.0
        return wholePart + decimalPart
    }

    fun dialPhoneNumber(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }

    }

    fun getPlacePhone(extracurricular: Extracurricular,method:(GooglePlaceDetail.Result?)->Unit){
        CoroutineScope(Dispatchers.IO).launch {
            val client = OkHttpClient()
            val url = "https://maps.googleapis.com/maps/api/place/details/json?place_id=${extracurricular.placeId}&key=${applicationContext.getString(R.string.google_maps_api_key)}"

            val request = Request.Builder()
                .url(url)
                .build()

            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                // 使用 Gson 將 JSON 資料轉換成 Kotlin data class
                val gson = Gson()
                val googlePlaceNearby = gson.fromJson(responseBody, GooglePlaceDetail::class.java)

                CoroutineScope(Dispatchers.Main).launch {
                    method(googlePlaceNearby.result)
                }

            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: com.google.gson.JsonSyntaxException) {
                e.printStackTrace()
            }
        }
    }

    fun setFav(extracurricular: Extracurricular){
        CoroutineScope(Dispatchers.IO).launch {
            admittedReposritory.extracurricularDao.insertExtracurricular(extracurricular)
        }
    }
}
