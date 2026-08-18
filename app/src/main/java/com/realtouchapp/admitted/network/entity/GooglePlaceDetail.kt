package com.realtouchapp.admitted.network.entity

data class GooglePlaceDetail(
//    val html_attributions: List<Any>,
    val result: Result?,
    val status: String
){
    data class Geometry(
        val location: Location,
        val viewport: Viewport
    )

    data class CurrentOpeningHours(
        val open_now: Boolean,
        val periods: List<Period>,
        val weekday_text: List<String>
    )
    data class Close(
        val date: String,
        val day: Int,
        val time: String,
        val truncated: Boolean
    )
    data class AddressComponent(
        val long_name: String,
        val short_name: String,
        val types: List<String>
    )
    data class Location(
        val lat: Double,
        val lng: Double
    )
    data class Southwest(
        val lat: Double,
        val lng: Double
    )

    data class Result(
//        val address_components: List<AddressComponent>,
//        val adr_address: String,
//        val business_status: String,
//        val current_opening_hours: CurrentOpeningHours,
//        val formatted_address: String,
        val formatted_phone_number: String?,
//        val geometry: Geometry,
//        val icon: String,
//        val icon_background_color: String,
//        val icon_mask_base_uri: String,
        val international_phone_number: String?,
//        val name: String,
//        val opening_hours: OpeningHours,
//        val photos: List<Photo>,
//        val place_id: String,
//        val plus_code: PlusCode,
//        val rating: Int,
//        val reference: String,
//        val reviews: List<Review>,
//        val types: List<String>,
//        val url: String,
//        val user_ratings_total: Int,
//        val utc_offset: Int,
//        val vicinity: String,
//        val website: String,
//        val wheelchair_accessible_entrance: Boolean
    )
    data class Photo(
        val height: Int,
        val html_attributions: List<String>,
        val photo_reference: String,
        val width: Int
    )
    data class PeriodX(
        val `open`: OpenX
    )
    data class Period(
        val close: Close,
        val `open`: Open
    )
    data class OpenX(
        val day: Int,
        val time: String
    )
    data class OpeningHours(
        val open_now: Boolean,
        val periods: List<PeriodX>,
        val weekday_text: List<String>
    )
    data class Open(
        val date: String,
        val day: Int,
        val time: String,
        val truncated: Boolean
    )
    data class Northeast(
        val lat: Double,
        val lng: Double
    )
    data class PlusCode(
        val compound_code: String,
        val global_code: String
    )
    data class Review(
        val author_name: String,
        val author_url: String,
        val language: String,
        val original_language: String,
        val profile_photo_url: String,
        val rating: Int,
        val relative_time_description: String,
        val text: String,
        val time: Int,
        val translated: Boolean
    )
    data class Viewport(
        val northeast: Northeast,
        val southwest: Southwest
    )
}
