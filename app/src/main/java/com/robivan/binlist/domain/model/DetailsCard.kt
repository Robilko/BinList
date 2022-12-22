package com.robivan.binlist.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailsCard(
    val number: String,
    val scheme: String?,
    val type: String?,
    val brand: String?,
    val countryName: String?,
    val countryEmoji: String?,
    val currency: String?,
    val countryLatitude: Double,
    val countryLongitude: Double,
    val bankName: String?,
    val bankUrl: String?,
    val bankPhone: List<String>?,
    val bankCity: String?,
    val timestamp: String
) : Parcelable
