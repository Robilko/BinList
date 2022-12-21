package com.robivan.binlist.data.mapper

import com.robivan.binlist.data.api.model.ResponseDTO
import com.robivan.binlist.data.cache.DetailsCardEntity
import com.robivan.binlist.domain.model.DetailsCard

interface IDetailsCardMapper {
    fun toDetailsCard(entity: DetailsCardEntity) : DetailsCard
    fun toDetailsCard(response: ResponseDTO, number: String) : DetailsCard
    fun toDetailsCardList(list: List<DetailsCardEntity>) : List<DetailsCard>
    fun toDetailsCardEntity(response: ResponseDTO, number: String): DetailsCardEntity
    fun toDetailsCardEntity(card: DetailsCard): DetailsCardEntity
}