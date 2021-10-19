package com.expl.peterpartnertest.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Users (
    @SerializedName("users") val users : List<User>
): Serializable

data class User (
    @SerializedName("card_number") val card_number : String,
    @SerializedName("type") val type : String,
    @SerializedName("cardholder_name") val cardholder_name : String,
    @SerializedName("valid") val valid : String,
    @SerializedName("balance") val balance : Double,
    val balanceCurrency:Double = 0.0,
    @SerializedName("transaction_history") val transaction_history : List<TransactionHistory>
): Serializable

data class TransactionHistory (
    @SerializedName("title") val title : String,
    @SerializedName("icon_url") val icon_url : String,
    @SerializedName("date") val date : String,
    @SerializedName("amount") val amount : Double,
    val currentAmount : Double = 0.0,
): Serializable

