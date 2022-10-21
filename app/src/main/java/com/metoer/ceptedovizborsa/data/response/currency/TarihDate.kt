package com.metoer.ceptedovizborsa.data.response.currency

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "Tarih_Date", strict = false)
class TarihDate {
    @field:Attribute(name = "Tarih", required = false)
    var Tarih: String? = null

    @field:Attribute(name = "Date", required = false)
    var Date: String? = null

    @field:Attribute(name = "Bulten_No", required = false)
    var Bulten_No: String? = null

    @field:ElementList(entry = "Currency", inline = true)
    var Currency: List<Currency>? = null
}