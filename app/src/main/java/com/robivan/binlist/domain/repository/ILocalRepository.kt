package com.robivan.binlist.domain.repository

import com.robivan.binlist.domain.model.DetailsCard
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface ILocalRepository {

    fun getAllData(): Single<List<DetailsCard>>

    fun getCardInfoFromCache(number: String): Single<DetailsCard>

    fun saveCardInfo(card: DetailsCard) : Completable
}