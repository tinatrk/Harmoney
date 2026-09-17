package com.example.harmoney.domain.models

import com.example.harmoney.core.util.IdEnum
import com.example.harmoney.core.util.IdEnumRegistry

/**
`id` - ISO 4217 numeric code
 */
enum class Currency(
    /** ISO 4217 numeric code */
    override val id: Long,
    val isoCode: String,
    val symbol: String,
    val minorUnitScale: Int
) : IdEnum {

    AED(
        id = 784L,
        isoCode = "AED",
        symbol = "AED",
        minorUnitScale = 2
    ),
    AFN(
        id = 971L,
        isoCode = "AFN",
        symbol = "؋",
        minorUnitScale = 2
    ),
    ALL(
        id = 8L,
        isoCode = "ALL",
        symbol = "ALL",
        minorUnitScale = 2
    ),
    AMD(
        id = 51L,
        isoCode = "AMD",
        symbol = "֏",
        minorUnitScale = 2
    ),
    AOA(
        id = 973L,
        isoCode = "AOA",
        symbol = "AOA",
        minorUnitScale = 2
    ),
    ARS(
        id = 32L,
        isoCode = "ARS",
        symbol = "ARS",
        minorUnitScale = 2
    ),
    AUD(
        id = 36L,
        isoCode = "AUD",
        symbol = "A\$",
        minorUnitScale = 2
    ),
    AWG(
        id = 533L,
        isoCode = "AWG",
        symbol = "AWG",
        minorUnitScale = 2
    ),
    AZN(
        id = 944L,
        isoCode = "AZN",
        symbol = "₼",
        minorUnitScale = 2
    ),
    BAM(
        id = 977L,
        isoCode = "BAM",
        symbol = "BAM",
        minorUnitScale = 2
    ),
    BBD(
        id = 52L,
        isoCode = "BBD",
        symbol = "BBD",
        minorUnitScale = 2
    ),
    BDT(
        id = 50L,
        isoCode = "BDT",
        symbol = "৳",
        minorUnitScale = 2
    ),
    BHD(
        id = 48L,
        isoCode = "BHD",
        symbol = "BHD",
        minorUnitScale = 3
    ),
    BIF(
        id = 108L,
        isoCode = "BIF",
        symbol = "BIF",
        minorUnitScale = 0
    ),
    BMD(
        id = 60L,
        isoCode = "BMD",
        symbol = "BMD",
        minorUnitScale = 2
    ),
    BND(
        id = 96L,
        isoCode = "BND",
        symbol = "BND",
        minorUnitScale = 2
    ),
    BOB(
        id = 68L,
        isoCode = "BOB",
        symbol = "BOB",
        minorUnitScale = 2
    ),
    BOV(//NOT
        id = 984L,
        isoCode = "BOV",
        symbol = "BOV",
        minorUnitScale = 2
    ),
    BRL(
        id = 986L,
        isoCode = "BRL",
        symbol = "R\$",
        minorUnitScale = 2
    ),
    BSD(
        id = 44L,
        isoCode = "BSD",
        symbol = "BSD",
        minorUnitScale = 2
    ),
    BTN(
        id = 64L,
        isoCode = "BTN",
        symbol = "BTN",
        minorUnitScale = 2
    ),
    BWP(
        id = 72L,
        isoCode = "BWP",
        symbol = "BWP",
        minorUnitScale = 2
    ),
    BYN(
        id = 933L,
        isoCode = "BYN",
        symbol = "BYN",
        minorUnitScale = 2
    ),
    BZD(
        id = 84L,
        isoCode = "BZD",
        symbol = "BZD",
        minorUnitScale = 2
    ),
    CAD(
        id = 124L,
        isoCode = "CAD",
        symbol = "CA\$",
        minorUnitScale = 2
    ),
    CDF(
        id = 976L,
        isoCode = "CDF",
        symbol = "CDF",
        minorUnitScale = 2
    ),
    CHE(//NOT
        id = 947L,
        isoCode = "CHE",
        symbol = "CHE",
        minorUnitScale = 2
    ),
    CHF(
        id = 756L,
        isoCode = "CHF",
        symbol = "CHF",
        minorUnitScale = 2
    ),
    CHW(
        id = 948L,
        isoCode = "CHW",
        symbol = "CHW",
        minorUnitScale = 2
    ),
    CLF(//NOT
        id = 990L,
        isoCode = "CLF",
        symbol = "CLF",
        minorUnitScale = 4
    ),
    CLP(
        id = 152L,
        isoCode = "CLP",
        symbol = "CLP",
        minorUnitScale = 0
    ),
    CNY(
        id = 156L,
        isoCode = "CNY",
        symbol = "¥",
        minorUnitScale = 2
    ),
    COP(
        id = 170L,
        isoCode = "COP",
        symbol = "COP",
        minorUnitScale = 2
    ),
    COU(//NOT
        id = 970L,
        isoCode = "COU",
        symbol = "COU",
        minorUnitScale = 2
    ),
    CRC(
        id = 188L,
        isoCode = "CRC",
        symbol = "₡",
        minorUnitScale = 2
    ),
    CUP(
        id = 192L,
        isoCode = "CUP",
        symbol = "CUP",
        minorUnitScale = 2
    ),
    CVE(
        id = 132L,
        isoCode = "CVE",
        symbol = "CVE",
        minorUnitScale = 2
    ),
    CZK(
        id = 203L,
        isoCode = "CZK",
        symbol = "CZK",
        minorUnitScale = 2
    ),
    DJF(
        id = 262L,
        isoCode = "DJF",
        symbol = "DJF",
        minorUnitScale = 0
    ),
    DKK(
        id = 208L,
        isoCode = "DKK",
        symbol = "DKK",
        minorUnitScale = 2
    ),
    DOP(
        id = 214L,
        isoCode = "DOP",
        symbol = "DOP",
        minorUnitScale = 2
    ),
    DZD(
        id = 12L,
        isoCode = "DZD",
        symbol = "DZD",
        minorUnitScale = 2
    ),
    EGP(
        id = 818L,
        isoCode = "EGP",
        symbol = "EGP",
        minorUnitScale = 2
    ),
    ERN(
        id = 232L,
        isoCode = "ERN",
        symbol = "ERN",
        minorUnitScale = 2
    ),
    ETB(
        id = 230L,
        isoCode = "ETB",
        symbol = "ETB",
        minorUnitScale = 2
    ),
    EUR(
        id = 978L,
        isoCode = "EUR",
        symbol = "€",
        minorUnitScale = 2
    ),
    FJD(
        id = 242L,
        isoCode = "FJD",
        symbol = "FJD",
        minorUnitScale = 2
    ),
    FKP(
        id = 238L,
        isoCode = "FKP",
        symbol = "FKP",
        minorUnitScale = 2
    ),
    GBP(
        id = 826L,
        isoCode = "GBP",
        symbol = "£",
        minorUnitScale = 2
    ),
    GEL(
        id = 981L,
        isoCode = "GEL",
        symbol = "₾",
        minorUnitScale = 2
    ),
    GHS(
        id = 936L,
        isoCode = "GHS",
        symbol = "₵",
        minorUnitScale = 2
    ),
    GIP(
        id = 292L,
        isoCode = "GIP",
        symbol = "GIP",
        minorUnitScale = 2
    ),
    GMD(
        id = 270L,
        isoCode = "GMD",
        symbol = "GMD",
        minorUnitScale = 2
    ),
    GNF(
        id = 324L,
        isoCode = "GNF",
        symbol = "GNF",
        minorUnitScale = 0
    ),
    GTQ(
        id = 320L,
        isoCode = "GTQ",
        symbol = "GTQ",
        minorUnitScale = 2
    ),
    GYD(
        id = 328L,
        isoCode = "GYD",
        symbol = "GYD",
        minorUnitScale = 2
    ),
    HKD(
        id = 344L,
        isoCode = "HKD",
        symbol = "HK\$",
        minorUnitScale = 2
    ),
    HNL(
        id = 340L,
        isoCode = "HNL",
        symbol = "HNL",
        minorUnitScale = 2
    ),
    HTG(
        id = 332L,
        isoCode = "HTG",
        symbol = "HTG",
        minorUnitScale = 2
    ),
    HUF(
        id = 348L,
        isoCode = "HUF",
        symbol = "HUF",
        minorUnitScale = 2
    ),
    IDR(
        id = 360L,
        isoCode = "IDR",
        symbol = "IDR",
        minorUnitScale = 2
    ),
    ILS(
        id = 376L,
        isoCode = "ILS",
        symbol = "₪",
        minorUnitScale = 2
    ),
    INR(
        id = 356L,
        isoCode = "INR",
        symbol = "₹",
        minorUnitScale = 2
    ),
    IQD(
        id = 368L,
        isoCode = "IQD",
        symbol = "IQD",
        minorUnitScale = 3
    ),
    IRR(
        id = 364L,
        isoCode = "IRR",
        symbol = "IRR",
        minorUnitScale = 2
    ),
    ISK(
        id = 352L,
        isoCode = "ISK",
        symbol = "ISK",
        minorUnitScale = 0
    ),
    JMD(
        id = 388L,
        isoCode = "JMD",
        symbol = "JMD",
        minorUnitScale = 2
    ),
    JOD(
        id = 400L,
        isoCode = "JOD",
        symbol = "JOD",
        minorUnitScale = 3
    ),
    JPY(
        id = 392L,
        isoCode = "JPY",
        symbol = "¥",
        minorUnitScale = 0
    ),
    KES(
        id = 404L,
        isoCode = "KES",
        symbol = "KES",
        minorUnitScale = 2
    ),
    KGS(
        id = 417L,
        isoCode = "KGS",
        symbol = "KGS",
        minorUnitScale = 2
    ),
    KHR(
        id = 116L,
        isoCode = "KHR",
        symbol = "៛",
        minorUnitScale = 2
    ),
    KMF(
        id = 174L,
        isoCode = "KMF",
        symbol = "KMF",
        minorUnitScale = 0
    ),
    KPW(
        id = 408L,
        isoCode = "KPW",
        symbol = "KPW",
        minorUnitScale = 2
    ),
    KRW(
        id = 410L,
        isoCode = "KRW",
        symbol = "₩",
        minorUnitScale = 0
    ),
    KWD(
        id = 414L,
        isoCode = "KWD",
        symbol = "KWD",
        minorUnitScale = 3
    ),
    KYD(
        id = 136L,
        isoCode = "KYD",
        symbol = "KYD",
        minorUnitScale = 2
    ),
    KZT(
        id = 398L,
        isoCode = "KZT",
        symbol = "₸",
        minorUnitScale = 2
    ),
    LAK(
        id = 418L,
        isoCode = "LAK",
        symbol = "₭",
        minorUnitScale = 2
    ),
    LBP(
        id = 422L,
        isoCode = "LBP",
        symbol = "LBP",
        minorUnitScale = 2
    ),
    LKR(
        id = 144L,
        isoCode = "LKR",
        symbol = "LKR",
        minorUnitScale = 2
    ),
    LRD(
        id = 430L,
        isoCode = "LRD",
        symbol = "LRD",
        minorUnitScale = 2
    ),
    LSL(
        id = 426L,
        isoCode = "LSL",
        symbol = "LSL",
        minorUnitScale = 2
    ),
    LYD(
        id = 434L,
        isoCode = "LYD",
        symbol = "LYD",
        minorUnitScale = 3
    ),
    MAD(
        id = 504L,
        isoCode = "MAD",
        symbol = "MAD",
        minorUnitScale = 2
    ),
    MDL(
        id = 498L,
        isoCode = "MDL",
        symbol = "MDL",
        minorUnitScale = 2
    ),
    MGA(
        id = 969L,
        isoCode = "MGA",
        symbol = "MGA",
        minorUnitScale = 2
    ),
    MKD(
        id = 807L,
        isoCode = "MKD",
        symbol = "MKD",
        minorUnitScale = 2
    ),
    MMK(
        id = 104L,
        isoCode = "MMK",
        symbol = "MMK",
        minorUnitScale = 2
    ),
    MNT(
        id = 496L,
        isoCode = "MNT",
        symbol = "₮",
        minorUnitScale = 2
    ),
    MOP(
        id = 446L,
        isoCode = "MOP",
        symbol = "MOP",
        minorUnitScale = 2
    ),
    MRU(
        id = 929L,
        isoCode = "MRU",
        symbol = "MRU",
        minorUnitScale = 2
    ),
    MUR(
        id = 480L,
        isoCode = "MUR",
        symbol = "MUR",
        minorUnitScale = 2
    ),
    MVR(
        id = 462L,
        isoCode = "MVR",
        symbol = "MVR",
        minorUnitScale = 2
    ),
    MWK(
        id = 454L,
        isoCode = "MWK",
        symbol = "MWK",
        minorUnitScale = 2
    ),
    MXN(
        id = 484L,
        isoCode = "MXN",
        symbol = "MX\$",
        minorUnitScale = 2
    ),
    MXV(//NOT
        id = 979L,
        isoCode = "MXV",
        symbol = "MXV",
        minorUnitScale = 2
    ),
    MYR(
        id = 458L,
        isoCode = "MYR",
        symbol = "MYR",
        minorUnitScale = 2
    ),
    MZN(
        id = 943L,
        isoCode = "MZN",
        symbol = "MZN",
        minorUnitScale = 2
    ),
    NAD(
        id = 516L,
        isoCode = "NAD",
        symbol = "NAD",
        minorUnitScale = 2
    ),
    NGN(
        id = 566L,
        isoCode = "NGN",
        symbol = "₦",
        minorUnitScale = 2
    ),
    NIO(
        id = 558L,
        isoCode = "NIO",
        symbol = "NIO",
        minorUnitScale = 2
    ),
    NOK(
        id = 578L,
        isoCode = "NOK",
        symbol = "NOK",
        minorUnitScale = 2
    ),
    NPR(
        id = 524L,
        isoCode = "NPR",
        symbol = "NPR",
        minorUnitScale = 2
    ),
    NZD(
        id = 554L,
        isoCode = "NZD",
        symbol = "NZ\$",
        minorUnitScale = 2
    ),
    OMR(
        id = 512L,
        isoCode = "OMR",
        symbol = "OMR",
        minorUnitScale = 3
    ),
    PAB(
        id = 590L,
        isoCode = "PAB",
        symbol = "PAB",
        minorUnitScale = 2
    ),
    PEN(
        id = 604L,
        isoCode = "PEN",
        symbol = "PEN",
        minorUnitScale = 2
    ),
    PGK(
        id = 598L,
        isoCode = "PGK",
        symbol = "PGK",
        minorUnitScale = 2
    ),
    PHP(
        id = 608L,
        isoCode = "PHP",
        symbol = "₱",
        minorUnitScale = 2
    ),
    PKR(
        id = 586L,
        isoCode = "PKR",
        symbol = "PKR",
        minorUnitScale = 2
    ),
    PLN(
        id = 985L,
        isoCode = "PLN",
        symbol = "PLN",
        minorUnitScale = 2
    ),
    PYG(
        id = 600L,
        isoCode = "PYG",
        symbol = "₲",
        minorUnitScale = 0
    ),
    QAR(
        id = 634L,
        isoCode = "QAR",
        symbol = "QAR",
        minorUnitScale = 2
    ),
    RON(
        id = 946L,
        isoCode = "RON",
        symbol = "RON",
        minorUnitScale = 2
    ),
    RSD(
        id = 941L,
        isoCode = "RSD",
        symbol = "RSD",
        minorUnitScale = 2
    ),
    RUB(
        id = 643L,
        isoCode = "RUB",
        symbol = "₽",
        minorUnitScale = 2
    ),
    RWF(
        id = 646L,
        isoCode = "RWF",
        symbol = "RWF",
        minorUnitScale = 0
    ),
    SAR(
        id = 682L,
        isoCode = "SAR",
        symbol = "SAR",
        minorUnitScale = 2
    ),
    SBD(
        id = 90L,
        isoCode = "SBD",
        symbol = "SBD",
        minorUnitScale = 2
    ),
    SCR(
        id = 690L,
        isoCode = "SCR",
        symbol = "SCR",
        minorUnitScale = 2
    ),
    SDG(
        id = 938L,
        isoCode = "SDG",
        symbol = "SDG",
        minorUnitScale = 2
    ),
    SEK(
        id = 752L,
        isoCode = "SEK",
        symbol = "SEK",
        minorUnitScale = 2
    ),
    SGD(
        id = 702L,
        isoCode = "SGD",
        symbol = "SGD",
        minorUnitScale = 2
    ),
    SHP(
        id = 654L,
        isoCode = "SHP",
        symbol = "SHP",
        minorUnitScale = 2
    ),
    SLE(
        id = 925L,
        isoCode = "SLE",
        symbol = "SLE",
        minorUnitScale = 2
    ),
    SOS(
        id = 706L,
        isoCode = "SOS",
        symbol = "SOS",
        minorUnitScale = 2
    ),
    SRD(
        id = 968L,
        isoCode = "SRD",
        symbol = "SRD",
        minorUnitScale = 2
    ),
    SSP(
        id = 728L,
        isoCode = "SSP",
        symbol = "SSP",
        minorUnitScale = 2
    ),
    STN(
        id = 930L,
        isoCode = "STN",
        symbol = "STN",
        minorUnitScale = 2
    ),
    SVC(
        id = 222L,
        isoCode = "SVC",
        symbol = "SVC",
        minorUnitScale = 2
    ),
    SYP(
        id = 760L,
        isoCode = "SYP",
        symbol = "SYP",
        minorUnitScale = 2
    ),
    SZL(
        id = 748L,
        isoCode = "SZL",
        symbol = "SZL",
        minorUnitScale = 2
    ),
    THB(
        id = 764L,
        isoCode = "THB",
        symbol = "฿",
        minorUnitScale = 2
    ),
    TJS(
        id = 972L,
        isoCode = "TJS",
        symbol = "TJS",
        minorUnitScale = 2
    ),
    TMT(
        id = 934L,
        isoCode = "TMT",
        symbol = "ТМТ",
        minorUnitScale = 2
    ),
    TND(
        id = 788L,
        isoCode = "TND",
        symbol = "TND",
        minorUnitScale = 3
    ),
    TOP(
        id = 776L,
        isoCode = "TOP",
        symbol = "TOP",
        minorUnitScale = 2
    ),
    TRY(
        id = 949L,
        isoCode = "TRY",
        symbol = "₺",
        minorUnitScale = 2
    ),
    TTD(
        id = 780L,
        isoCode = "TTD",
        symbol = "TTD",
        minorUnitScale = 2
    ),
    TWD(
        id = 901L,
        isoCode = "TWD",
        symbol = "NT\$",
        minorUnitScale = 2
    ),
    TZS(
        id = 834L,
        isoCode = "TZS",
        symbol = "TZS",
        minorUnitScale = 2
    ),
    UAH(
        id = 980L,
        isoCode = "UAH",
        symbol = "₴",
        minorUnitScale = 2
    ),
    UGX(
        id = 800L,
        isoCode = "UGX",
        symbol = "UGX",
        minorUnitScale = 0
    ),
    USD(
        id = 840L,
        isoCode = "USD",
        symbol = "\$",
        minorUnitScale = 2
    ),
    USN(//NOT
        id = 997L,
        isoCode = "USN",
        symbol = "USN",
        minorUnitScale = 2
    ),
    UYI(//NOT
        id = 940L,
        isoCode = "UYI",
        symbol = "UYI",
        minorUnitScale = 0
    ),
    UYU(
        id = 858L,
        isoCode = "UYU",
        symbol = "UYU",
        minorUnitScale = 2
    ),
    UYW(//NOT
        id = 927L,
        isoCode = "UYW",
        symbol = "UYW",
        minorUnitScale = 4
    ),
    UZS(
        id = 860L,
        isoCode = "UZS",
        symbol = "UZS",
        minorUnitScale = 2
    ),
    VED(//NOT
        id = 926L,
        isoCode = "VED",
        symbol = "VED",
        minorUnitScale = 2
    ),
    VES(
        id = 928L,
        isoCode = "VES",
        symbol = "VES",
        minorUnitScale = 2
    ),
    VND(
        id = 704L,
        isoCode = "VND",
        symbol = "₫",
        minorUnitScale = 0
    ),
    VUV(
        id = 548L,
        isoCode = "VUV",
        symbol = "VUV",
        minorUnitScale = 0
    ),
    WST(
        id = 882L,
        isoCode = "WST",
        symbol = "WST",
        minorUnitScale = 2
    ),
    XAD(//NOT
        id = 396L,
        isoCode = "XAD",
        symbol = "XAD",
        minorUnitScale = 2
    ),
    XAF(
        id = 950L,
        isoCode = "XAF",
        symbol = "FCFA",
        minorUnitScale = 0
    ),
    XAG(
        id = 961L,
        isoCode = "XAG",
        symbol = "XAG",
        minorUnitScale = -1
    ),
    XAU(
        id = 959L,
        isoCode = "XAU",
        symbol = "XAU",
        minorUnitScale = -1
    ),
    XBA(//NOT
        id = 955L,
        isoCode = "XBA",
        symbol = "XBA",
        minorUnitScale = -1
    ),
    XBB(//NOT
        id = 956L,
        isoCode = "XBB",
        symbol = "XBB",
        minorUnitScale = -1
    ),
    XBC(//NOT
        id = 957L,
        isoCode = "XBC",
        symbol = "XBC",
        minorUnitScale = -1
    ),
    XBD(//NOT
        id = 958L,
        isoCode = "XBD",
        symbol = "XBD",
        minorUnitScale = -1
    ),
    XCD(
        id = 951L,
        isoCode = "XCD",
        symbol = "EC\$",
        minorUnitScale = 2
    ),
    XCG(
        id = 532L,
        isoCode = "XCG",
        symbol = "Cg.",
        minorUnitScale = 2
    ),
    XDR(
        id = 960L,
        isoCode = "XDR",
        symbol = "XDR",
        minorUnitScale = -1
    ),
    XOF(
        id = 952L,
        isoCode = "XOF",
        symbol = "XOF",
        minorUnitScale = 0
    ),
    XPD(
        id = 964L,
        isoCode = "XPD",
        symbol = "XPD",
        minorUnitScale = -1
    ),
    XPF(
        id = 953L,
        isoCode = "XPF",
        symbol = "CFPF",
        minorUnitScale = 0
    ),
    XPT(
        id = 962L,
        isoCode = "XPT",
        symbol = "XPT",
        minorUnitScale = -1
    ),
    XSU(//NOT
        id = 994L,
        isoCode = "XSU",
        symbol = "XSU",
        minorUnitScale = -1
    ),
    XTS(//NOT
        id = 963L,
        isoCode = "XTS",
        symbol = "XTS",
        minorUnitScale = -1
    ),
    XUA(//NOT
        id = 965L,
        isoCode = "XUA",
        symbol = "XUA",
        minorUnitScale = -1
    ),
    XXX(//NOT
        id = 999L,
        isoCode = "XXX",
        symbol = "¤",
        minorUnitScale = -1
    ),
    YER(
        id = 886L,
        isoCode = "YER",
        symbol = "YER",
        minorUnitScale = 2
    ),
    ZAR(
        id = 710L,
        isoCode = "ZAR",
        symbol = "ZAR",
        minorUnitScale = 2
    ),
    ZMW(
        id = 967L,
        isoCode = "ZMW",
        symbol = "ZMW",
        minorUnitScale = 2
    ),
    ZWG(
        id = 924L,
        isoCode = "ZWG",
        symbol = "ZWG",
        minorUnitScale = 2
    );

    companion object {
        private val registry = IdEnumRegistry(Currency::class.java)

        private val byIsoCode: Map<String, Currency> =
            entries.associateByUnique { it.isoCode }

        fun fromId(id: Long) = registry.fromId(id)

        fun fromIsoCodeOrNull(isoCode: String): Currency? =
            byIsoCode[isoCode.trim().uppercase()]

        fun fromIsoCode(isoCode: String): Currency =
            byIsoCode[isoCode.trim().uppercase()]
                ?: error("Unknown ${Currency::class.java.simpleName} isoCode: $isoCode")
    }
}

private inline fun <T, K> Iterable<T>.associateByUnique(
    keySelector: (T) -> K
): Map<K, T> {
    val result = HashMap<K, T>()

    for (item in this) {
        val key = keySelector(item)
        val previous = result[key]

        check(previous == null) {
            "Duplicate key '$key' for $previous and $item"
        }

        result[key] = item
    }

    return result
}
