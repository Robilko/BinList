package com.robivan.binlist.data.api.model

import com.google.gson.annotations.SerializedName

data class ResponseDTO(
    @SerializedName("scheme")
    val scheme: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("brand")
    val brand: String?,
    @SerializedName("country")
    val country: CountryDTO,
    @SerializedName("bank")
    val bank: BankDTO?
)

