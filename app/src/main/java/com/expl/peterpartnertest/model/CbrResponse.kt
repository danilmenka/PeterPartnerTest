package com.expl.peterpartnertest.model

import com.google.gson.annotations.SerializedName

data class CbrResponse (
    @SerializedName("Valute") val valute : Valute
)

data class Valute(
    @SerializedName("GBP") val gbp : ValuteParams,
    @SerializedName("EUR") val eur : ValuteParams,
    @SerializedName("USD") val usd : ValuteParams
)
data class ValuteParams (
    @SerializedName("ID") val iD : String,
    @SerializedName("NumCode") val numCode : Int,
    @SerializedName("CharCode") val charCode : String,
    @SerializedName("Nominal") val nominal : Int,
    @SerializedName("Name") val name : String,
    @SerializedName("Value") val value : Double,
    @SerializedName("Previous") val previous : Double
)