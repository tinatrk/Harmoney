package com.example.harmoney.domain.models

import com.example.harmoney.core.util.IdEnum
import com.example.harmoney.core.util.IdEnumRegistry

enum class Currency(
    override val id: Long,
    val numericCode: Int,
    val code: String,
    val symbol: String,
    val minorUnitScale: Int
) : IdEnum {

    AED(
        id = 1L,
        numericCode = 784,
        code = "AED",
        symbol = "AED",
        minorUnitScale = 2
    ),
    AFN(
        id = 2L,
        numericCode = 971,
        code = "AFN",
        symbol = "؋",
        minorUnitScale = 2
    ),
    ALL(
        id = 3L,
        numericCode = 8,
        code = "ALL",
        symbol = "ALL",
        minorUnitScale = 2
    ),
    AMD(
        id = 4L,
        numericCode = 51,
        code = "AMD",
        symbol = "֏",
        minorUnitScale = 2
    ),
    AOA(
        id = 5L,
        numericCode = 973,
        code = "AOA",
        symbol = "AOA",
        minorUnitScale = 2
    ),
    ARS(
        id = 6L,
        numericCode = 32,
        code = "ARS",
        symbol = "ARS",
        minorUnitScale = 2
    ),
    AUD(
        id = 7L,
        numericCode = 36,
        code = "AUD",
        symbol = "A\$",
        minorUnitScale = 2
    ),
    AWG(
        id = 8L,
        numericCode = 533,
        code = "AWG",
        symbol = "AWG",
        minorUnitScale = 2
    ),
    AZN(
        id = 9L,
        numericCode = 944,
        code = "AZN",
        symbol = "₼",
        minorUnitScale = 2
    ),
    BAM(
        id = 10L,
        numericCode = 977,
        code = "BAM",
        symbol = "BAM",
        minorUnitScale = 2
    ),
    BBD(
        id = 11L,
        numericCode = 52,
        code = "BBD",
        symbol = "BBD",
        minorUnitScale = 2
    ),
    BDT(
        id = 12L,
        numericCode = 50,
        code = "BDT",
        symbol = "৳",
        minorUnitScale = 2
    ),
    BHD(
        id = 13L,
        numericCode = 48,
        code = "BHD",
        symbol = "BHD",
        minorUnitScale = 3
    ),
    BIF(
        id = 14L,
        numericCode = 108,
        code = "BIF",
        symbol = "BIF",
        minorUnitScale = 0
    ),
    BMD(
        id = 15L,
        numericCode = 60,
        code = "BMD",
        symbol = "BMD",
        minorUnitScale = 2
    ),
    BND(
        id = 16L,
        numericCode = 96,
        code = "BND",
        symbol = "BND",
        minorUnitScale = 2
    ),
    BOB(
        id = 17L,
        numericCode = 68,
        code = "BOB",
        symbol = "BOB",
        minorUnitScale = 2
    ),
    BOV(//NOT
        id = 18L,
        numericCode = 984,
        code = "BOV",
        symbol = "BOV",
        minorUnitScale = 2
    ),
    BRL(
        id = 19L,
        numericCode = 986,
        code = "BRL",
        symbol = "R\$",
        minorUnitScale = 2
    ),
    BSD(
        id = 20L,
        numericCode = 44,
        code = "BSD",
        symbol = "BSD",
        minorUnitScale = 2
    ),
    BTN(
        id = 21L,
        numericCode = 64,
        code = "BTN",
        symbol = "BTN",
        minorUnitScale = 2
    ),
    BWP(
        id = 22L,
        numericCode = 72,
        code = "BWP",
        symbol = "BWP",
        minorUnitScale = 2
    ),
    BYN(
        id = 23L,
        numericCode = 933,
        code = "BYN",
        symbol = "BYN",
        minorUnitScale = 2
    ),
    BZD(
        id = 24L,
        numericCode = 84,
        code = "BZD",
        symbol = "BZD",
        minorUnitScale = 2
    ),
    CAD(
        id = 25L,
        numericCode = 124,
        code = "CAD",
        symbol = "CA\$",
        minorUnitScale = 2
    ),
    CDF(
        id = 26L,
        numericCode = 976,
        code = "CDF",
        symbol = "CDF",
        minorUnitScale = 2
    ),
    CHE(//NOT
        id = 27L,
        numericCode = 947,
        code = "CHE",
        symbol = "CHE",
        minorUnitScale = 2
    ),
    CHF(
        id = 28L,
        numericCode = 756,
        code = "CHF",
        symbol = "CHF",
        minorUnitScale = 2
    ),
    CHW(
        id = 29L,
        numericCode = 948,
        code = "CHW",
        symbol = "CHW",
        minorUnitScale = 2
    ),
    CLF(//NOT
        id = 30L,
        numericCode = 990,
        code = "CLF",
        symbol = "CLF",
        minorUnitScale = 4
    ),
    CLP(
        id = 31L,
        numericCode = 152,
        code = "CLP",
        symbol = "CLP",
        minorUnitScale = 0
    ),
    CNY(
        id = 32L,
        numericCode = 156,
        code = "CNY",
        symbol = "¥",
        minorUnitScale = 2
    ),
    COP(
        id = 33L,
        numericCode = 170,
        code = "COP",
        symbol = "COP",
        minorUnitScale = 2
    ),
    COU(//NOT
        id = 34L,
        numericCode = 970,
        code = "COU",
        symbol = "COU",
        minorUnitScale = 2
    ),
    CRC(
        id = 35L,
        numericCode = 188,
        code = "CRC",
        symbol = "₡",
        minorUnitScale = 2
    ),
    CUP(
        id = 36L,
        numericCode = 192,
        code = "CUP",
        symbol = "CUP",
        minorUnitScale = 2
    ),
    CVE(
        id = 37L,
        numericCode = 132,
        code = "CVE",
        symbol = "CVE",
        minorUnitScale = 2
    ),
    CZK(
        id = 38L,
        numericCode = 203,
        code = "CZK",
        symbol = "CZK",
        minorUnitScale = 2
    ),
    DJF(
        id = 39L,
        numericCode = 262,
        code = "DJF",
        symbol = "DJF",
        minorUnitScale = 0
    ),
    DKK(
        id = 40L,
        numericCode = 208,
        code = "DKK",
        symbol = "DKK",
        minorUnitScale = 2
    ),
    DOP(
        id = 41L,
        numericCode = 214,
        code = "DOP",
        symbol = "DOP",
        minorUnitScale = 2
    ),
    DZD(
        id = 42L,
        numericCode = 12,
        code = "DZD",
        symbol = "DZD",
        minorUnitScale = 2
    ),
    EGP(
        id = 43L,
        numericCode = 818,
        code = "EGP",
        symbol = "EGP",
        minorUnitScale = 2
    ),
    ERN(
        id = 44L,
        numericCode = 232,
        code = "ERN",
        symbol = "ERN",
        minorUnitScale = 2
    ),
    ETB(
        id = 45L,
        numericCode = 230,
        code = "ETB",
        symbol = "ETB",
        minorUnitScale = 2
    ),
    EUR(
        id = 46L,
        numericCode = 978,
        code = "EUR",
        symbol = "€",
        minorUnitScale = 2
    ),
    FJD(
        id = 47L,
        numericCode = 242,
        code = "FJD",
        symbol = "FJD",
        minorUnitScale = 2
    ),
    FKP(
        id = 48L,
        numericCode = 238,
        code = "FKP",
        symbol = "FKP",
        minorUnitScale = 2
    ),
    GBP(
        id = 49L,
        numericCode = 826,
        code = "GBP",
        symbol = "£",
        minorUnitScale = 2
    ),
    GEL(
        id = 50L,
        numericCode = 981,
        code = "GEL",
        symbol = "₾",
        minorUnitScale = 2
    ),
    GHS(
        id = 51L,
        numericCode = 936,
        code = "GHS",
        symbol = "₵",
        minorUnitScale = 2
    ),
    GIP(
        id = 52L,
        numericCode = 292,
        code = "GIP",
        symbol = "GIP",
        minorUnitScale = 2
    ),
    GMD(
        id = 53L,
        numericCode = 270,
        code = "GMD",
        symbol = "GMD",
        minorUnitScale = 2
    ),
    GNF(
        id = 54L,
        numericCode = 324,
        code = "GNF",
        symbol = "GNF",
        minorUnitScale = 0
    ),
    GTQ(
        id = 55L,
        numericCode = 320,
        code = "GTQ",
        symbol = "GTQ",
        minorUnitScale = 2
    ),
    GYD(
        id = 56L,
        numericCode = 328,
        code = "GYD",
        symbol = "GYD",
        minorUnitScale = 2
    ),
    HKD(
        id = 57L,
        numericCode = 344,
        code = "HKD",
        symbol = "HK\$",
        minorUnitScale = 2
    ),
    HNL(
        id = 58L,
        numericCode = 340,
        code = "HNL",
        symbol = "HNL",
        minorUnitScale = 2
    ),
    HTG(
        id = 59L,
        numericCode = 332,
        code = "HTG",
        symbol = "HTG",
        minorUnitScale = 2
    ),
    HUF(
        id = 60L,
        numericCode = 348,
        code = "HUF",
        symbol = "HUF",
        minorUnitScale = 2
    ),
    IDR(
        id = 61L,
        numericCode = 360,
        code = "IDR",
        symbol = "IDR",
        minorUnitScale = 2
    ),
    ILS(
        id = 62L,
        numericCode = 376,
        code = "ILS",
        symbol = "₪",
        minorUnitScale = 2
    ),
    INR(
        id = 63L,
        numericCode = 356,
        code = "INR",
        symbol = "₹",
        minorUnitScale = 2
    ),
    IQD(
        id = 64L,
        numericCode = 368,
        code = "IQD",
        symbol = "IQD",
        minorUnitScale = 3
    ),
    IRR(
        id = 65L,
        numericCode = 364,
        code = "IRR",
        symbol = "IRR",
        minorUnitScale = 2
    ),
    ISK(
        id = 66L,
        numericCode = 352,
        code = "ISK",
        symbol = "ISK",
        minorUnitScale = 0
    ),
    JMD(
        id = 67L,
        numericCode = 388,
        code = "JMD",
        symbol = "JMD",
        minorUnitScale = 2
    ),
    JOD(
        id = 68L,
        numericCode = 400,
        code = "JOD",
        symbol = "JOD",
        minorUnitScale = 3
    ),
    JPY(
        id = 69L,
        numericCode = 392,
        code = "JPY",
        symbol = "¥",
        minorUnitScale = 0
    ),
    KES(
        id = 70L,
        numericCode = 404,
        code = "KES",
        symbol = "KES",
        minorUnitScale = 2
    ),
    KGS(
        id = 71L,
        numericCode = 417,
        code = "KGS",
        symbol = "KGS",
        minorUnitScale = 2
    ),
    KHR(
        id = 72L,
        numericCode = 116,
        code = "KHR",
        symbol = "៛",
        minorUnitScale = 2
    ),
    KMF(
        id = 73L,
        numericCode = 174,
        code = "KMF",
        symbol = "KMF",
        minorUnitScale = 0
    ),
    KPW(
        id = 74L,
        numericCode = 408,
        code = "KPW",
        symbol = "KPW",
        minorUnitScale = 2
    ),
    KRW(
        id = 75L,
        numericCode = 410,
        code = "KRW",
        symbol = "₩",
        minorUnitScale = 0
    ),
    KWD(
        id = 76L,
        numericCode = 414,
        code = "KWD",
        symbol = "KWD",
        minorUnitScale = 3
    ),
    KYD(
        id = 77L,
        numericCode = 136,
        code = "KYD",
        symbol = "KYD",
        minorUnitScale = 2
    ),
    KZT(
        id = 78L,
        numericCode = 398,
        code = "KZT",
        symbol = "₸",
        minorUnitScale = 2
    ),
    LAK(
        id = 79L,
        numericCode = 418,
        code = "LAK",
        symbol = "₭",
        minorUnitScale = 2
    ),
    LBP(
        id = 80L,
        numericCode = 422,
        code = "LBP",
        symbol = "LBP",
        minorUnitScale = 2
    ),
    LKR(
        id = 81L,
        numericCode = 144,
        code = "LKR",
        symbol = "LKR",
        minorUnitScale = 2
    ),
    LRD(
        id = 82L,
        numericCode = 430,
        code = "LRD",
        symbol = "LRD",
        minorUnitScale = 2
    ),
    LSL(
        id = 83L,
        numericCode = 426,
        code = "LSL",
        symbol = "LSL",
        minorUnitScale = 2
    ),
    LYD(
        id = 84L,
        numericCode = 434,
        code = "LYD",
        symbol = "LYD",
        minorUnitScale = 3
    ),
    MAD(
        id = 85L,
        numericCode = 504,
        code = "MAD",
        symbol = "MAD",
        minorUnitScale = 2
    ),
    MDL(
        id = 86L,
        numericCode = 498,
        code = "MDL",
        symbol = "MDL",
        minorUnitScale = 2
    ),
    MGA(
        id = 87L,
        numericCode = 969,
        code = "MGA",
        symbol = "MGA",
        minorUnitScale = 2
    ),
    MKD(
        id = 88L,
        numericCode = 807,
        code = "MKD",
        symbol = "MKD",
        minorUnitScale = 2
    ),
    MMK(
        id = 89L,
        numericCode = 104,
        code = "MMK",
        symbol = "MMK",
        minorUnitScale = 2
    ),
    MNT(
        id = 90L,
        numericCode = 496,
        code = "MNT",
        symbol = "₮",
        minorUnitScale = 2
    ),
    MOP(
        id = 91L,
        numericCode = 446,
        code = "MOP",
        symbol = "MOP",
        minorUnitScale = 2
    ),
    MRU(
        id = 92L,
        numericCode = 929,
        code = "MRU",
        symbol = "MRU",
        minorUnitScale = 2
    ),
    MUR(
        id = 93L,
        numericCode = 480,
        code = "MUR",
        symbol = "MUR",
        minorUnitScale = 2
    ),
    MVR(
        id = 94L,
        numericCode = 462,
        code = "MVR",
        symbol = "MVR",
        minorUnitScale = 2
    ),
    MWK(
        id = 95L,
        numericCode = 454,
        code = "MWK",
        symbol = "MWK",
        minorUnitScale = 2
    ),
    MXN(
        id = 96L,
        numericCode = 484,
        code = "MXN",
        symbol = "MX\$",
        minorUnitScale = 2
    ),
    MXV(//NOT
        id = 97L,
        numericCode = 979,
        code = "MXV",
        symbol = "MXV",
        minorUnitScale = 2
    ),
    MYR(
        id = 98L,
        numericCode = 458,
        code = "MYR",
        symbol = "MYR",
        minorUnitScale = 2
    ),
    MZN(
        id = 99L,
        numericCode = 943,
        code = "MZN",
        symbol = "MZN",
        minorUnitScale = 2
    ),
    NAD(
        id = 100L,
        numericCode = 516,
        code = "NAD",
        symbol = "NAD",
        minorUnitScale = 2
    ),
    NGN(
        id = 101L,
        numericCode = 566,
        code = "NGN",
        symbol = "₦",
        minorUnitScale = 2
    ),
    NIO(
        id = 102L,
        numericCode = 558,
        code = "NIO",
        symbol = "NIO",
        minorUnitScale = 2
    ),
    NOK(
        id = 103L,
        numericCode = 578,
        code = "NOK",
        symbol = "NOK",
        minorUnitScale = 2
    ),
    NPR(
        id = 104L,
        numericCode = 524,
        code = "NPR",
        symbol = "NPR",
        minorUnitScale = 2
    ),
    NZD(
        id = 105L,
        numericCode = 554,
        code = "NZD",
        symbol = "NZ\$",
        minorUnitScale = 2
    ),
    OMR(
        id = 106L,
        numericCode = 512,
        code = "OMR",
        symbol = "OMR",
        minorUnitScale = 3
    ),
    PAB(
        id = 107L,
        numericCode = 590,
        code = "PAB",
        symbol = "PAB",
        minorUnitScale = 2
    ),
    PEN(
        id = 108L,
        numericCode = 604,
        code = "PEN",
        symbol = "PEN",
        minorUnitScale = 2
    ),
    PGK(
        id = 109L,
        numericCode = 598,
        code = "PGK",
        symbol = "PGK",
        minorUnitScale = 2
    ),
    PHP(
        id = 110L,
        numericCode = 608,
        code = "PHP",
        symbol = "₱",
        minorUnitScale = 2
    ),
    PKR(
        id = 111L,
        numericCode = 586,
        code = "PKR",
        symbol = "PKR",
        minorUnitScale = 2
    ),
    PLN(
        id = 112L,
        numericCode = 985,
        code = "PLN",
        symbol = "PLN",
        minorUnitScale = 2
    ),
    PYG(
        id = 113L,
        numericCode = 600,
        code = "PYG",
        symbol = "₲",
        minorUnitScale = 0
    ),
    QAR(
        id = 114L,
        numericCode = 634,
        code = "QAR",
        symbol = "QAR",
        minorUnitScale = 2
    ),
    RON(
        id = 115L,
        numericCode = 946,
        code = "RON",
        symbol = "RON",
        minorUnitScale = 2
    ),
    RSD(
        id = 116L,
        numericCode = 941,
        code = "RSD",
        symbol = "RSD",
        minorUnitScale = 2
    ),
    RUB(
        id = 117L,
        numericCode = 643,
        code = "RUB",
        symbol = "₽",
        minorUnitScale = 2
    ),
    RWF(
        id = 118L,
        numericCode = 646,
        code = "RWF",
        symbol = "RWF",
        minorUnitScale = 0
    ),
    SAR(
        id = 119L,
        numericCode = 682,
        code = "SAR",
        symbol = "SAR",
        minorUnitScale = 2
    ),
    SBD(
        id = 120L,
        numericCode = 90,
        code = "SBD",
        symbol = "SBD",
        minorUnitScale = 2
    ),
    SCR(
        id = 121L,
        numericCode = 690,
        code = "SCR",
        symbol = "SCR",
        minorUnitScale = 2
    ),
    SDG(
        id = 122L,
        numericCode = 938,
        code = "SDG",
        symbol = "SDG",
        minorUnitScale = 2
    ),
    SEK(
        id = 123L,
        numericCode = 752,
        code = "SEK",
        symbol = "SEK",
        minorUnitScale = 2
    ),
    SGD(
        id = 124L,
        numericCode = 702,
        code = "SGD",
        symbol = "SGD",
        minorUnitScale = 2
    ),
    SHP(
        id = 125L,
        numericCode = 654,
        code = "SHP",
        symbol = "SHP",
        minorUnitScale = 2
    ),
    SLE(
        id = 126L,
        numericCode = 925,
        code = "SLE",
        symbol = "SLE",
        minorUnitScale = 2
    ),
    SOS(
        id = 127L,
        numericCode = 706,
        code = "SOS",
        symbol = "SOS",
        minorUnitScale = 2
    ),
    SRD(
        id = 128L,
        numericCode = 968,
        code = "SRD",
        symbol = "SRD",
        minorUnitScale = 2
    ),
    SSP(
        id = 129L,
        numericCode = 728,
        code = "SSP",
        symbol = "SSP",
        minorUnitScale = 2
    ),
    STN(
        id = 130L,
        numericCode = 930,
        code = "STN",
        symbol = "STN",
        minorUnitScale = 2
    ),
    SVC(
        id = 131L,
        numericCode = 222,
        code = "SVC",
        symbol = "SVC",
        minorUnitScale = 2
    ),
    SYP(
        id = 132L,
        numericCode = 760,
        code = "SYP",
        symbol = "SYP",
        minorUnitScale = 2
    ),
    SZL(
        id = 133L,
        numericCode = 748,
        code = "SZL",
        symbol = "SZL",
        minorUnitScale = 2
    ),
    THB(
        id = 134L,
        numericCode = 764,
        code = "THB",
        symbol = "฿",
        minorUnitScale = 2
    ),
    TJS(
        id = 135L,
        numericCode = 972,
        code = "TJS",
        symbol = "TJS",
        minorUnitScale = 2
    ),
    TMT(
        id = 136L,
        numericCode = 934,
        code = "TMT",
        symbol = "ТМТ",
        minorUnitScale = 2
    ),
    TND(
        id = 137L,
        numericCode = 788,
        code = "TND",
        symbol = "TND",
        minorUnitScale = 3
    ),
    TOP(
        id = 138L,
        numericCode = 776,
        code = "TOP",
        symbol = "TOP",
        minorUnitScale = 2
    ),
    TRY(
        id = 139L,
        numericCode = 949,
        code = "TRY",
        symbol = "₺",
        minorUnitScale = 2
    ),
    TTD(
        id = 140L,
        numericCode = 780,
        code = "TTD",
        symbol = "TTD",
        minorUnitScale = 2
    ),
    TWD(
        id = 141L,
        numericCode = 901,
        code = "TWD",
        symbol = "NT\$",
        minorUnitScale = 2
    ),
    TZS(
        id = 142L,
        numericCode = 834,
        code = "TZS",
        symbol = "TZS",
        minorUnitScale = 2
    ),
    UAH(
        id = 143L,
        numericCode = 980,
        code = "UAH",
        symbol = "₴",
        minorUnitScale = 2
    ),
    UGX(
        id = 144L,
        numericCode = 800,
        code = "UGX",
        symbol = "UGX",
        minorUnitScale = 0
    ),
    USD(
        id = 145L,
        numericCode = 840,
        code = "USD",
        symbol = "\$",
        minorUnitScale = 2
    ),
    USN(//NOT
        id = 146L,
        numericCode = 997,
        code = "USN",
        symbol = "USN",
        minorUnitScale = 2
    ),
    UYI(//NOT
        id = 147L,
        numericCode = 940,
        code = "UYI",
        symbol = "UYI",
        minorUnitScale = 0
    ),
    UYU(
        id = 148L,
        numericCode = 858,
        code = "UYU",
        symbol = "UYU",
        minorUnitScale = 2
    ),
    UYW(//NOT
        id = 149L,
        numericCode = 927,
        code = "UYW",
        symbol = "UYW",
        minorUnitScale = 4
    ),
    UZS(
        id = 150L,
        numericCode = 860,
        code = "UZS",
        symbol = "UZS",
        minorUnitScale = 2
    ),
    VED(//NOT
        id = 151L,
        numericCode = 926,
        code = "VED",
        symbol = "VED",
        minorUnitScale = 2
    ),
    VES(
        id = 152L,
        numericCode = 928,
        code = "VES",
        symbol = "VES",
        minorUnitScale = 2
    ),
    VND(
        id = 153L,
        numericCode = 704,
        code = "VND",
        symbol = "₫",
        minorUnitScale = 0
    ),
    VUV(
        id = 154L,
        numericCode = 548,
        code = "VUV",
        symbol = "VUV",
        minorUnitScale = 0
    ),
    WST(
        id = 155L,
        numericCode = 882,
        code = "WST",
        symbol = "WST",
        minorUnitScale = 2
    ),
    XAD(//NOT
        id = 156L,
        numericCode = 396,
        code = "XAD",
        symbol = "XAD",
        minorUnitScale = 2
    ),
    XAF(
        id = 157L,
        numericCode = 950,
        code = "XAF",
        symbol = "FCFA",
        minorUnitScale = 0
    ),
    XAG(
        id = 158L,
        numericCode = 961,
        code = "XAG",
        symbol = "XAG",
        minorUnitScale = -1
    ),
    XAU(
        id = 159L,
        numericCode = 959,
        code = "XAU",
        symbol = "XAU",
        minorUnitScale = -1
    ),
    XBA(//NOT
        id = 160L,
        numericCode = 955,
        code = "XBA",
        symbol = "XBA",
        minorUnitScale = -1
    ),
    XBB(//NOT
        id = 161L,
        numericCode = 956,
        code = "XBB",
        symbol = "XBB",
        minorUnitScale = -1
    ),
    XBC(//NOT
        id = 162L,
        numericCode = 957,
        code = "XBC",
        symbol = "XBC",
        minorUnitScale = -1
    ),
    XBD(//NOT
        id = 163L,
        numericCode = 958,
        code = "XBD",
        symbol = "XBD",
        minorUnitScale = -1
    ),
    XCD(
        id = 164L,
        numericCode = 951,
        code = "XCD",
        symbol = "EC\$",
        minorUnitScale = 2
    ),
    XCG(
        id = 165L,
        numericCode = 532,
        code = "XCG",
        symbol = "Cg.",
        minorUnitScale = 2
    ),
    XDR(
        id = 166L,
        numericCode = 960,
        code = "XDR",
        symbol = "XDR",
        minorUnitScale = -1
    ),
    XOF(
        id = 167L,
        numericCode = 952,
        code = "XOF",
        symbol = "XOF",
        minorUnitScale = 0
    ),
    XPD(
        id = 168L,
        numericCode = 964,
        code = "XPD",
        symbol = "XPD",
        minorUnitScale = -1
    ),
    XPF(
        id = 169L,
        numericCode = 953,
        code = "XPF",
        symbol = "CFPF",
        minorUnitScale = 0
    ),
    XPT(
        id = 170L,
        numericCode = 962,
        code = "XPT",
        symbol = "XPT",
        minorUnitScale = -1
    ),
    XSU(//NOT
        id = 171L,
        numericCode = 994,
        code = "XSU",
        symbol = "XSU",
        minorUnitScale = -1
    ),
    XTS(//NOT
        id = 172L,
        numericCode = 963,
        code = "XTS",
        symbol = "XTS",
        minorUnitScale = -1
    ),
    XUA(//NOT
        id = 173L,
        numericCode = 965,
        code = "XUA",
        symbol = "XUA",
        minorUnitScale = -1
    ),
    XXX(//NOT
        id = 174L,
        numericCode = 999,
        code = "XXX",
        symbol = "¤",
        minorUnitScale = -1
    ),
    YER(
        id = 175L,
        numericCode = 886,
        code = "YER",
        symbol = "YER",
        minorUnitScale = 2
    ),
    ZAR(
        id = 176L,
        numericCode = 710,
        code = "ZAR",
        symbol = "ZAR",
        minorUnitScale = 2
    ),
    ZMW(
        id = 177L,
        numericCode = 967,
        code = "ZMW",
        symbol = "ZMW",
        minorUnitScale = 2
    ),
    ZWG(
        id = 178L,
        numericCode = 924,
        code = "ZWG",
        symbol = "ZWG",
        minorUnitScale = 2
    );

    companion object {
        private val registry = IdEnumRegistry(Currency::class.java) { it.id }
        fun fromId(id: Long) = registry.fromId(id)
    }
}
