package com.robivan.binlist.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailsCard(
    val number: String,
    val scheme: String?,
    val type: String?,
    val countryName: String?,
    val countryEmoji: String?,
    val currency: String?,
    val countryLatitude: Int?,
    val countryLongitude: Int?,
    val bankName: String?,
    val bankUrl: String?,
    val bankPhone: List<String>?,
    val bankCity: String?,
    val timestamp: String
) : Parcelable