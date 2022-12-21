package com.robivan.binlist.data.repository

import com.robivan.binlist.data.api.ApiService
import com.robivan.binlist.data.mapper.IDetailsCardMapper
import com.robivan.binlist.domain.model.DetailsCard
import com.robivan.binlist.domain.repository.IRemoteRepository
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class RemoteRepositoryImpl(
    private val apiService: ApiService,
    private val mapper: IDetailsCardMapper
): IRemoteRepository {

    override fun getCardInfo(request: String): Single<DetailsCard> =
        apiService.getCardDetails(request)
            .flatMap {
                Single.fromCallable{mapper.toDetailsCard(it, request)}
            }.subscribeOn(Schedulers.io())
}