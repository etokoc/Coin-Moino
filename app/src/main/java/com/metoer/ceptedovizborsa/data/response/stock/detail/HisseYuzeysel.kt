package com.metoer.ceptedovizborsa.data.response.stock.detail


import com.google.gson.annotations.SerializedName

data class HisseYuzeysel(
    @SerializedName("aciklama")
    var aciklama: String,
    @SerializedName("acilis")
    var acilis: Double,
    @SerializedName("alis")
    var alis: Double,
    @SerializedName("aorT1")
    var aorT1: Double,
    @SerializedName("aorT2")
    var aorT2: Double,
    @SerializedName("aort")
    var aort: Double,
    @SerializedName("aydusuk")
    var aydusuk: Double,
    @SerializedName("ayortalama")
    var ayortalama: Double,
    @SerializedName("ayyuksek")
    var ayyuksek: Double,
    @SerializedName("beta")
    var beta: Double,
    @SerializedName("donem")
    var donem: String,
    @SerializedName("dunkukapanis")
    var dunkukapanis: Double,
    @SerializedName("dusuK1")
    var dusuK1: Double,
    @SerializedName("dusuK2")
    var dusuK2: Double,
    @SerializedName("dusuk")
    var dusuk: Double,
    @SerializedName("fiyatadimi")
    var fiyatadimi: Double,
    @SerializedName("fiyatkaz")
    var fiyatkaz: Double,
    @SerializedName("hacimloT1")
    var hacimloT1: Double,
    @SerializedName("hacimloT2")
    var hacimloT2: Double,
    @SerializedName("hacimlot")
    var hacimlot: Double,
    @SerializedName("hacimtL1")
    var hacimtL1: Double,
    @SerializedName("hacimtL2")
    var hacimtL2: Double,
    @SerializedName("hacimtl")
    var hacimtl: Double,
    @SerializedName("hacimtldun")
    var hacimtldun: Double,
    @SerializedName("hacimyuzdedegisim")
    var hacimyuzdedegisim: Double,
    @SerializedName("haftadusuk")
    var haftadusuk: Double,
    @SerializedName("haftaortalama")
    var haftaortalama: Double,
    @SerializedName("haftayuksek")
    var haftayuksek: Double,
    @SerializedName("izafikapanis")
    var izafikapanis: Double,
    @SerializedName("kapaniS1")
    var kapaniS1: Double,
    @SerializedName("kapaniS2")
    var kapaniS2: Double,
    @SerializedName("kapanis")
    var kapanis: Double,
    @SerializedName("kapanisfark")
    var kapanisfark: Any,
    @SerializedName("kaykar")
    var kaykar: Double,
    @SerializedName("net")
    var net: Double,
    @SerializedName("netkar")
    var netkar: Double,
    @SerializedName("oncekiaykapanis")
    var oncekiaykapanis: Double,
    @SerializedName("oncekihaftakapanis")
    var oncekihaftakapanis: Double,
    @SerializedName("oncekikapanis")
    var oncekikapanis: Double,
    @SerializedName("oncekiyilkapanis")
    var oncekiyilkapanis: Double,
    @SerializedName("ozsermaye")
    var ozsermaye: Double,
    @SerializedName("piydeg")
    var piydeg: Double,
    @SerializedName("saklamaor")
    var saklamaor: Double,
    @SerializedName("satis")
    var satis: Double,
    @SerializedName("sektorid")
    var sektorid: Int,
    @SerializedName("sembol")
    var sembol: String,
    @SerializedName("sembolid")
    var sembolid: Int,
    @SerializedName("sermaye")
    var sermaye: Double,
    @SerializedName("taban")
    var taban: Double,
    @SerializedName("tarih")
    var tarih: String,
    @SerializedName("tavan")
    var tavan: Double,
    @SerializedName("xU100AG")
    var xU100AG: Double,
    @SerializedName("yildusuk")
    var yildusuk: Double,
    @SerializedName("yilortalama")
    var yilortalama: Double,
    @SerializedName("yilyuksek")
    var yilyuksek: Double,
    @SerializedName("yukseK1")
    var yukseK1: Double,
    @SerializedName("yukseK2")
    var yukseK2: Double,
    @SerializedName("yuksek")
    var yuksek: Double,
    @SerializedName("yuzdedegisim")
    var yuzdedegisim: Double,
    @SerializedName("yuzdedegisimS1")
    var yuzdedegisimS1: Double,
    @SerializedName("yuzdedegisimS2")
    var yuzdedegisimS2: Double
)