package com.example.harmoney.data.core.bd.converter

import com.example.harmoney.data.core.bd.dto.TotalMoneyDto
import com.example.harmoney.domain.models.Money

interface TotalMoneyDtoConverter {
    fun map(total: TotalMoneyDto): Money
}
