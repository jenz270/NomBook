package com.jenzhouu.nombook.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ingredient (
    val name: String,
    val measure: String
) : Parcelable
