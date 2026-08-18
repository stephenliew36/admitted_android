package com.realtouchapp.admitted.network.entity

data class GooglePlaceNearby(
    val next_page_token: String,
    val results: ArrayList<Result>,
    val status: String
){
    data class Result(
        val business_status: String,
        val geometry: Geometry,
        val icon: String,
        val icon_background_color: String,
        val icon_mask_base_uri: String,
        val name: String,
        val opening_hours: OpeningHours,
        val photos: ArrayList<Photo>,
        val place_id: String,
        val plus_code: PlusCode,
        val rating: Double,
        val reference: String,
        val scope: String,
        val types: ArrayList<String>,
        val user_ratings_total: Int,
        /**地址*/
        val vicinity: String
    ){
        fun toExtracurricular(institutionType:String):Extracurricular{
            return Extracurricular(null,"","",vicinity,"","","",institutionType,geometry.location.lat.toString(),geometry.location.lng.toString(),name,"",place_id)
        }
    }
    data class Geometry(
        val location: Location,
        val viewport: Viewport
    )

    data class Location(
        val lat: Double,
        val lng: Double
    )


    data class Northeast(
        val lat: Double,
        val lng: Double
    )

    data class OpeningHours(
        val open_now: Boolean
    )

    data class Photo(
        val height: Int,
        val html_attributions: List<String>,
        val photo_reference: String,
        val width: Int
    )

    data class PlusCode(
        val compound_code: String,
        val global_code: String
    )
    data class Southwest(
        val lat: Double,
        val lng: Double
    )
    data class Viewport(
        val northeast: Northeast,
        val southwest: Southwest
    )
}