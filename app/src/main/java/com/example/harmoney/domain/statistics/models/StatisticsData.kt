package com.example.harmoney.domain.statistics.models

sealed interface StatisticsData {
    data class Original(val statistics: OriginalStatistics) : StatisticsData
    data class Converted(val statistics: ConvertedStatistics) : StatisticsData
}
