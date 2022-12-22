package com.robivan.binlist.data.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.robivan.binlist.utils.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class DetailsCardEntity(
    @PrimaryKey
    @ColumnInfo(name = "number") val number: String,
    @ColumnInfo(name = "scheme") val scheme: String?,
    @ColumnInfo(name = "type") val type: String?,
    @ColumnInfo(name = "brand") val brand: String?,
    @ColumnInfo(name = "country_name") val countryName: String?,
    @ColumnInfo(name = "country_emoji") val countryEmoji: String?,
    @ColumnInfo(name = "currency") val currency: String?,
    @ColumnInfo(name = "country_latitude") val countryLatitude: Double,
    @ColumnInfo(name = "country_longitude") val countryLongitude: Double,
    @ColumnInfo(name = "bank_name") val bankName: String?,
    @ColumnInfo(name = "bank_url") val bankUrl: String?,
    @ColumnInfo(name = "bank_phone") val bankPhone: String?,
    @ColumnInfo(name = "bank_city") val bankCity: String?,
    @ColumnInfo(name = "timestamp") val timestamp: String
)
