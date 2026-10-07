package com.example.harmoney.presentation.converters

import com.example.harmoney.domain.models.ConvertedTotal

interface ConvertedTotalFormatter {
    fun getOriginalsStr(total: ConvertedTotal): String
}
