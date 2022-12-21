package com.robivan.binlist.domain.repository

import com.robivan.binlist.domain.model.DetailsCard
import io.reactivex.rxjava3.core.Single

interface IRemoteRepository {

    fun getCardInfo(request: String): Single<DetailsCard>
}