package com.realtouchapp.admitted.network.entity

/*
    作者: RobertChou
    時間: 2023/4/3 下午 02:02
    信箱: didi31139@gmail.com
    說明:
*/
data class TargetSchooFilter(
    var isCountry_UnitedStates: Boolean = false,
    var isCountry_Canada: Boolean = false,
    var isCountry_UnitedKingdom: Boolean = false,
    var isDifficulty_FarReach: Boolean = false,
    var isDifficulty_Reach: Boolean = false,
    var isDifficulty_Target: Boolean = false,
    var isDifficulty_Safety: Boolean = false,
    var isPublic_Public: Boolean = false,
    var isPublic_Private: Boolean = false,
    var isOthers_LiberalArts: Boolean = false,
    var isOthers_Art: Boolean = false,
){
    constructor():this(false,false,false,false,false,false,false,false,false,false,false)
    //是否過濾中 — Canada/UK flags are retained for instance-state compat but ignored for filtering.
    fun isFilter():Boolean{
        return isCountry_UnitedStates||isDifficulty_FarReach||isDifficulty_Reach||isDifficulty_Target||isDifficulty_Safety||isPublic_Public||isPublic_Private||isOthers_LiberalArts||isOthers_Art

    }
}
