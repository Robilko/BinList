package com.robivan.binlist.data.mapper

import com.robivan.binlist.data.api.model.ResponseDTO
import com.robivan.binlist.data.cache.DetailsCardEntity
import com.robivan.binlist.domain.model.DetailsCard
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DetailsCardMapper : IDetailsCardMapper {

    override fun toDetailsCardEntity(response: ResponseDTO, number: String): DetailsCardEntity =
        DetailsCardEntity(
            number = formatNumber(number),
            scheme = response.scheme,
            type = response.type,
            brand = response.brand,
            countryName = response.country.name,
            countryEmoji = response.country.emoji,
            currency = response.country.currency,
            countryLatitude = response.country.latitude,
            countryLongitude = response.country.longitude,
            bankName = response.bank?.name,
            bankUrl = response.bank?.url,
            bankCity = response.bank?.city,
            bankPhone = response.bank?.phone?.replace(" OR ", ", "),
            timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        )

    override fun toDetailsCardEntity(card: DetailsCard): DetailsCardEntity =
        DetailsCardEntity(
            number = card.number,
            scheme = card.scheme,
            type = card.type,
            brand = card.brand,
            countryName = card.countryName,
            countryEmoji = card.countryEmoji,
            currency = card.currency,
            countryLatitude = card.countryLatitude,
            countryLongitude = card.countryLongitude,
            bankName = card.bankName,
            bankUrl = card.bankUrl,
            bankCity = card.bankCity,
            bankPhone = card.bankPhone?.joinToString(", "),
            timestamp = card.timestamp
        )

    override fun toDetailsCard(entity: DetailsCardEntity): DetailsCard =
        DetailsCard(
            number = entity.number,
            scheme = entity.scheme,
            type = entity.type,
            brand = entity.brand,
            countryName = entity.countryName,
            countryEmoji = entity.countryEmoji,
            currency = entity.currency,
            countryLatitude = entity.countryLatitude,
            countryLongitude = entity.countryLongitude,
            bankName = entity.bankName,
            bankUrl = entity.bankUrl,
            bankCity = entity.bankCity,
            bankPhone = entity.bankPhone?.split(", "),
            timestamp = entity.timestamp
        )

    override fun toDetailsCard(response: ResponseDTO, number: String): DetailsCard =
        DetailsCard(
            number = formatNumber(number),
            scheme = response.scheme,
            type = response.type,
            brand = response.brand,
            countryName = response.country.name,
            countryEmoji = response.country.emoji,
            currency = response.country.currency,
            countryLatitude = response.country.latitude,
            countryLongitude = response.country.longitude,
            bankName = response.bank?.name,
            bankUrl = response.bank?.url,
            bankCity = response.bank?.city,
            bankPhone = response.bank?.phone?.split(" OR ")?.toList(),
            timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        )

    override fun toDetailsCardList(list: List<DetailsCardEntity>): List<DetailsCard> =
        list.map { toDetailsCard(it) }

    private fun formatNumber(number: String): String {
        return number.substring(0..3) + " " + number.substring(4..5) + "**"
    }
}