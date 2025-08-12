package com.app.comtracker.data.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class TrackBodyModelDTO(
    @SerializedName("LeadDateTime")
    val leadDateTime: String,
    @SerializedName("LeadType")
    val leadType: Int,
    @SerializedName("PhoneNumber")
    val phoneNumber: String,
    @SerializedName("Text")
    val text: String
)