package com.metoer.ceptedovizborsa.data.Response

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "Currency", strict = false)
class Currency {
    @field:Attribute(name = "CrossOrder", required = false)
    var CrossOrder: String? = null

    @field:Attribute(name = "Kod", required = false)
    var Kod: String? = null

    @field:Attribute(name = "CurrencyCode", required = false)
    var CurrencyCode: String? = null

    @field: Element(name = "BanknoteBuying", required = false)
    var BanknoteBuying: Double? = null

    @field: Element(name = "BanknoteSelling", required = false)
    var BanknoteSelling: Double? = null

    @field: Element(name = "CrossRateOther", required = false)
    var CrossRateOther: Double? = null

    @field: Element(name = "CrossRateUSD", required = false)
    var CrossRateUSD: Double? = null

    @field: Element(name = "CurrencyName", required = false)
    var CurrencyName: String? = null

    @field: Element(name = "ForexBuying", required = false)
    var ForexBuying: Double? = null

    @field: Element(name = "ForexSelling", required = false)
    var ForexSelling: Double? = null

    @field: Element(name = "Isim", required = false)
    var Isim: String? = null

    @field: Element(name = "Unit", required = false)
    var Unit: Int? = null
}