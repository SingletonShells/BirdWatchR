package com.example.AllAroundTheBirds.DataClasses

import com.google.gson.annotations.SerializedName

data class EbirdNearbyHotspotItem(

	@field:SerializedName("lng")
	val lng: Any? = null,

	@field:SerializedName("countryCode")
	val countryCode: String? = null,

	@field:SerializedName("subnational1Code")
	val subnational1Code: String? = null,

	@field:SerializedName("latestObsDt")
	val latestObsDt: String? = null,

	@field:SerializedName("locName")
	val locName: String? = null,

	@field:SerializedName("locId")
	val locId: String? = null,

	@field:SerializedName("lat")
	val lat: Any? = null,

	@field:SerializedName("numSpeciesAllTime")
	val numSpeciesAllTime: Int? = null,

	@field:SerializedName("subnational2Code")
	val subnational2Code: String? = null
)
data class EbirdNearbyObservationItem(

	@field:SerializedName("lng")
	val lng: Any? = null,

	@field:SerializedName("speciesCode")
	val speciesCode: String? = null,

	@field:SerializedName("locName")
	val locName: String? = null,

	@field:SerializedName("sciName")
	val sciName: String? = null,

	@field:SerializedName("howMany")
	val howMany: Int? = null,

	@field:SerializedName("obsValid")
	val obsValid: Boolean? = null,

	@field:SerializedName("subId")
	val subId: String? = null,

	@field:SerializedName("locationPrivate")
	val locationPrivate: Boolean? = null,

	@field:SerializedName("obsDt")
	val obsDt: String? = null,

	@field:SerializedName("obsReviewed")
	val obsReviewed: Boolean? = null,

	@field:SerializedName("comName")
	val comName: String? = null,

	@field:SerializedName("locId")
	val locId: String? = null,

	@field:SerializedName("lat")
	val lat: Any? = null
)