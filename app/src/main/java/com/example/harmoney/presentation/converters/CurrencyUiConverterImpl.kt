package com.example.harmoney.presentation.converters

import com.example.harmoney.R
import com.example.harmoney.core.util.StringProvider
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.presentation.models.CurrencyUi

class CurrencyUiConverterImpl(private val stringProvider: StringProvider) : CurrencyUiConverter {
    override fun map(currency: Currency): CurrencyUi {
        return CurrencyUi(
            currency = currency,
            name = stringProvider.getString(currency.toNameRes())
        )
    }

    override fun map(currencyList: List<Currency>): List<CurrencyUi> {
        return currencyList.map { currency -> this.map(currency) }
    }

    @Suppress("detekt:LongMethod", "detekt:CyclomaticComplexMethod")
    private fun Currency.toNameRes(): Int =
        when (this) {
            Currency.AED -> R.string.currency_name_aed
            Currency.AFN -> R.string.currency_name_afn
            Currency.ALL -> R.string.currency_name_all
            Currency.AMD -> R.string.currency_name_amd
            Currency.AOA -> R.string.currency_name_aoa
            Currency.ARS -> R.string.currency_name_ars
            Currency.AUD -> R.string.currency_name_aud
            Currency.AWG -> R.string.currency_name_awg
            Currency.AZN -> R.string.currency_name_azn
            Currency.BAM -> R.string.currency_name_bam
            Currency.BBD -> R.string.currency_name_bbd
            Currency.BDT -> R.string.currency_name_bdt
            Currency.BHD -> R.string.currency_name_bhd
            Currency.BIF -> R.string.currency_name_bif
            Currency.BMD -> R.string.currency_name_bmd
            Currency.BND -> R.string.currency_name_bnd
            Currency.BOB -> R.string.currency_name_bob
            Currency.BOV -> R.string.currency_name_bov //NOT
            Currency.BRL -> R.string.currency_name_brl
            Currency.BSD -> R.string.currency_name_bsd
            Currency.BTN -> R.string.currency_name_btn
            Currency.BWP -> R.string.currency_name_bwp
            Currency.BYN -> R.string.currency_name_byn
            Currency.BZD -> R.string.currency_name_bzd
            Currency.CAD -> R.string.currency_name_cad
            Currency.CDF -> R.string.currency_name_cdf
            Currency.CHE -> R.string.currency_name_che //NOT
            Currency.CHF -> R.string.currency_name_chf
            Currency.CHW -> R.string.currency_name_chw
            Currency.CLF -> R.string.currency_name_clf //NOT
            Currency.CLP -> R.string.currency_name_clp
            Currency.CNY -> R.string.currency_name_cny
            Currency.COP -> R.string.currency_name_cop
            Currency.COU -> R.string.currency_name_cou //NOT
            Currency.CRC -> R.string.currency_name_crc
            Currency.CUP -> R.string.currency_name_cup
            Currency.CVE -> R.string.currency_name_cve
            Currency.CZK -> R.string.currency_name_czk
            Currency.DJF -> R.string.currency_name_djf
            Currency.DKK -> R.string.currency_name_dkk
            Currency.DOP -> R.string.currency_name_dop
            Currency.DZD -> R.string.currency_name_dzd
            Currency.EGP -> R.string.currency_name_egp
            Currency.ERN -> R.string.currency_name_ern
            Currency.ETB -> R.string.currency_name_etb
            Currency.EUR -> R.string.currency_name_eur
            Currency.FJD -> R.string.currency_name_fjd
            Currency.FKP -> R.string.currency_name_fkp
            Currency.GBP -> R.string.currency_name_gbp
            Currency.GEL -> R.string.currency_name_gel
            Currency.GHS -> R.string.currency_name_ghs
            Currency.GIP -> R.string.currency_name_gip
            Currency.GMD -> R.string.currency_name_gmd
            Currency.GNF -> R.string.currency_name_gnf
            Currency.GTQ -> R.string.currency_name_gtq
            Currency.GYD -> R.string.currency_name_gyd
            Currency.HKD -> R.string.currency_name_hkd
            Currency.HNL -> R.string.currency_name_hnl
            Currency.HTG -> R.string.currency_name_htg
            Currency.HUF -> R.string.currency_name_huf
            Currency.IDR -> R.string.currency_name_idr
            Currency.ILS -> R.string.currency_name_ils
            Currency.INR -> R.string.currency_name_inr
            Currency.IQD -> R.string.currency_name_iqd
            Currency.IRR -> R.string.currency_name_irr
            Currency.ISK -> R.string.currency_name_isk
            Currency.JMD -> R.string.currency_name_jmd
            Currency.JOD -> R.string.currency_name_jod
            Currency.JPY -> R.string.currency_name_jpy
            Currency.KES -> R.string.currency_name_kes
            Currency.KGS -> R.string.currency_name_kgs
            Currency.KHR -> R.string.currency_name_khr
            Currency.KMF -> R.string.currency_name_kmf
            Currency.KPW -> R.string.currency_name_kpw
            Currency.KRW -> R.string.currency_name_krw
            Currency.KWD -> R.string.currency_name_kwd
            Currency.KYD -> R.string.currency_name_kyd
            Currency.KZT -> R.string.currency_name_kzt
            Currency.LAK -> R.string.currency_name_lak
            Currency.LBP -> R.string.currency_name_lbp
            Currency.LKR -> R.string.currency_name_lkr
            Currency.LRD -> R.string.currency_name_lrd
            Currency.LSL -> R.string.currency_name_lsl
            Currency.LYD -> R.string.currency_name_lyd
            Currency.MAD -> R.string.currency_name_mad
            Currency.MDL -> R.string.currency_name_mdl
            Currency.MGA -> R.string.currency_name_mga
            Currency.MKD -> R.string.currency_name_mkd
            Currency.MMK -> R.string.currency_name_mmk
            Currency.MNT -> R.string.currency_name_mnt
            Currency.MOP -> R.string.currency_name_mop
            Currency.MRU -> R.string.currency_name_mru
            Currency.MUR -> R.string.currency_name_mur
            Currency.MVR -> R.string.currency_name_mvr
            Currency.MWK -> R.string.currency_name_mwk
            Currency.MXN -> R.string.currency_name_mxn
            Currency.MXV -> R.string.currency_name_mxv //NOT
            Currency.MYR -> R.string.currency_name_myr
            Currency.MZN -> R.string.currency_name_mzn
            Currency.NAD -> R.string.currency_name_nad
            Currency.NGN -> R.string.currency_name_ngn
            Currency.NIO -> R.string.currency_name_nio
            Currency.NOK -> R.string.currency_name_nok
            Currency.NPR -> R.string.currency_name_npr
            Currency.NZD -> R.string.currency_name_nzd
            Currency.OMR -> R.string.currency_name_omr
            Currency.PAB -> R.string.currency_name_pab
            Currency.PEN -> R.string.currency_name_pen
            Currency.PGK -> R.string.currency_name_pgk
            Currency.PHP -> R.string.currency_name_php
            Currency.PKR -> R.string.currency_name_pkr
            Currency.PLN -> R.string.currency_name_pln
            Currency.PYG -> R.string.currency_name_pyg
            Currency.QAR -> R.string.currency_name_qar
            Currency.RON -> R.string.currency_name_ron
            Currency.RSD -> R.string.currency_name_rsd
            Currency.RUB -> R.string.currency_name_rub
            Currency.RWF -> R.string.currency_name_rwf
            Currency.SAR -> R.string.currency_name_sar
            Currency.SBD -> R.string.currency_name_sbd
            Currency.SCR -> R.string.currency_name_scr
            Currency.SDG -> R.string.currency_name_sdg
            Currency.SEK -> R.string.currency_name_sek
            Currency.SGD -> R.string.currency_name_sgd
            Currency.SHP -> R.string.currency_name_shp
            Currency.SLE -> R.string.currency_name_sle
            Currency.SOS -> R.string.currency_name_sos
            Currency.SRD -> R.string.currency_name_srd
            Currency.SSP -> R.string.currency_name_ssp
            Currency.STN -> R.string.currency_name_stn
            Currency.SVC -> R.string.currency_name_svc
            Currency.SYP -> R.string.currency_name_syp
            Currency.SZL -> R.string.currency_name_szl
            Currency.THB -> R.string.currency_name_thb
            Currency.TJS -> R.string.currency_name_tjs
            Currency.TMT -> R.string.currency_name_tmt
            Currency.TND -> R.string.currency_name_tnd
            Currency.TOP -> R.string.currency_name_top
            Currency.TRY -> R.string.currency_name_try
            Currency.TTD -> R.string.currency_name_ttd
            Currency.TWD -> R.string.currency_name_twd
            Currency.TZS -> R.string.currency_name_tzs
            Currency.UAH -> R.string.currency_name_uah
            Currency.UGX -> R.string.currency_name_ugx
            Currency.USD -> R.string.currency_name_usd
            Currency.USN -> R.string.currency_name_usn //NOT
            Currency.UYI -> R.string.currency_name_uyi //NOT
            Currency.UYU -> R.string.currency_name_uyu
            Currency.UYW -> R.string.currency_name_uyw //NOT
            Currency.UZS -> R.string.currency_name_uzs
            Currency.VED -> R.string.currency_name_ved //NOT
            Currency.VES -> R.string.currency_name_ves
            Currency.VND -> R.string.currency_name_vnd
            Currency.VUV -> R.string.currency_name_vuv
            Currency.WST -> R.string.currency_name_wst
            Currency.XAD -> R.string.currency_name_xad //NOT
            Currency.XAF -> R.string.currency_name_xaf
            Currency.XAG -> R.string.currency_name_xag
            Currency.XAU -> R.string.currency_name_xau
            Currency.XBA -> R.string.currency_name_xba //NOT
            Currency.XBB -> R.string.currency_name_xbb //NOT
            Currency.XBC -> R.string.currency_name_xbc //NOT
            Currency.XBD -> R.string.currency_name_xbd //NOT
            Currency.XCD -> R.string.currency_name_xcd
            Currency.XCG -> R.string.currency_name_xcg
            Currency.XDR -> R.string.currency_name_xdr
            Currency.XOF -> R.string.currency_name_xof
            Currency.XPD -> R.string.currency_name_xpd
            Currency.XPF -> R.string.currency_name_xpf
            Currency.XPT -> R.string.currency_name_xpt
            Currency.XSU -> R.string.currency_name_xsu //NOT
            Currency.XTS -> R.string.currency_name_xts //NOT
            Currency.XUA -> R.string.currency_name_xua //NOT
            Currency.XXX -> R.string.currency_name_xxx //NOT
            Currency.YER -> R.string.currency_name_yer
            Currency.ZAR -> R.string.currency_name_zar
            Currency.ZMW -> R.string.currency_name_zmw
            Currency.ZWG -> R.string.currency_name_zwg
        }
}
