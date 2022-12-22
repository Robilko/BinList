package com.robivan.binlist.data.repository

import com.robivan.binlist.data.cache.DetailCardDao
import com.robivan.binlist.data.mapper.IDetailsCardMapper
import com.robivan.binlist.domain.model.DetailsCard
import com.robivan.binlist.domain.repository.ILocalRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class LocalRepositoryImpl(
    private val cache: DetailCardDao,
    private val mapper: IDetailsCardMapper
) : ILocalRepository {

    override fun getAllData(): Single<List<DetailsCard>> =
        cache.getAllData().flatMap {
            Single.fromCallable { mapper.toDetailsCardList(it) }
        }.subscribeOn(Schedulers.io())

    override fun getCardInfoFromCache(number: String): Single<DetailsCard> =
        cache.getCardInfo(number).flatMap {
            Single.fromCallable { mapper.toDetailsCard(it) }
        }.subscribeOn(Schedulers.io())

    override fun saveCardInfo(card: DetailsCard): Completable =
        cache.saveCardInfo(mapper.toDetailsCardEntity(card)).subscribeOn(Schedulers.io())
}