package com.metoer.ceptedovizborsa.data.response.firebase

import com.google.gson.annotations.SerializedName

data class FirebaseDataItem(
    @SerializedName("isForcedUpdate")
    var isForcedUpdate: Boolean)
