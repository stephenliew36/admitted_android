package com.realtouchapp.admitted.network.entity

/*
    作者: RobertChou
    時間: 2023/4/3 下午 02:02
    信箱: didi31139@gmail.com
    說明:
*/
data class ExtraFilter(
    var isSaved: Boolean = false,
    var isHospitals: Boolean = false,
    var isOrphanages: Boolean = false,
    var isNursingHomes: Boolean = false,
    var isLibraries: Boolean = false,
    var isMusuemsGalleries: Boolean = false,
    var isAnimalShelters: Boolean = false
){
    constructor():this(false,false,false,false,false,false,false)
    //是否過濾中
    fun isFilter():Boolean{
        return isHospitals||isOrphanages||isNursingHomes||isLibraries||isMusuemsGalleries||isAnimalShelters

    }
}
