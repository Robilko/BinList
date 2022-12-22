package com.robivan.binlist.ui

interface DetailsOnClickListener {
    fun onCountryClicked(latitude: Double, longitude: Double)
    fun onWebsiteClicked(url: String)
    fun onPhoneClicked(phoneNumber: String)
}
