package com.ssafy.family.data.remote.res

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AllAlbum(
    @SerializedName("hashTags")
    val hashTags: List<HashTag>,
    @SerializedName("mainPicture")
    val mainPicture: Picture
): Parcelable

