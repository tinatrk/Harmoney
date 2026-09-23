package com.example.harmoney.data.core.bd.converter

import com.example.harmoney.data.core.bd.dto.TotalMoneyDto
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.Money
import com.example.harmoney.domain.models.MoneyAmount

class TotalMoneyDtoConverterImpl : TotalMoneyDtoConverter {
    override fun map(total: TotalMoneyDto): Money {
        return Money(
            amount = MoneyAmount(total.moneyAmountMinorUnits),
            currency = Currency.fromId(total.currencyId)
        )
    }
}
