package com.robivan.binlist.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DetailsCardEntity::class], version = 1, exportSchema = false)
abstract class DetailsCardDataBase : RoomDatabase() {

    abstract fun detailsCardDao() : DetailCardDao
}