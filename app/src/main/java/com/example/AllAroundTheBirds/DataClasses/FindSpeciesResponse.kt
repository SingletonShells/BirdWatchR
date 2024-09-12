package com.example.AllAroundTheBirds.DataClasses

import com.google.gson.annotations.SerializedName

data class FindSpeciesResponseItem(

	@field:SerializedName("speciesCode")
	val speciesCode: String? = null,

	@field:SerializedName("comNameCodes")
	val comNameCodes: List<String?>? = null,

	@field:SerializedName("bandingCodes")
	val bandingCodes: List<Any?>? = null,

	@field:SerializedName("comName")
	val comName: String? = null,

	@field:SerializedName("sciName")
	val sciName: String? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("sciNameCodes")
	val sciNameCodes: List<String?>? = null,

	@field:SerializedName("taxonOrder")
	val taxonOrder: Any? = null,

	@field:SerializedName("order")
	val order: String? = null,

	@field:SerializedName("familySciName")
	val familySciName: String? = null,

	@field:SerializedName("familyCode")
	val familyCode: String? = null,

	@field:SerializedName("familyComName")
	val familyComName: String? = null
)
