package com.robivan.binlist.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface DetailCardDao {
    @Query("SELECT * FROM details_card WHERE number LIKE :number")
    fun getCardInfo(number: String): Single<DetailsCardEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCardInfo(entity: DetailsCardEntity): Completable

    @Query("SELECT * FROM details_card")
    fun getAllData(): Single<List<DetailsCardEntity>>
}