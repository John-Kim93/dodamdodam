package com.ssafy.family.data.remote.req

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CreateFamilyReq(
    @SerializedName("role")
    val role: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("birthday")
    val birthday: String,
    @SerializedName("image")
    val image: String,
) : Parcelable
