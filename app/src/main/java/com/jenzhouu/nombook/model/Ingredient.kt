package com.jenzhouu.nombook.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Ingredient(
    @PrimaryKey(autoGenerate = true)
    val ingredientId: Int = 0,
    val name: String,
    val measure: String
) : Parcelable
