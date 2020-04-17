package com.jenzhouu.nombook.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jenzhouu.nombook.model.Ingredient
import java.util.*

class DateTypeConverter {
    @TypeConverter
    fun toTimeStamp(value: Long?): Date? {
        if (value == null) {
            return null
        }
        return Date(value)
    }

    @TypeConverter
    fun fromTimestamp(date: Date?): Long? {
        if (date == null) {
            return Date().time
        }
        return date.time
    }
}

class IngredientsConverter {
    private val gson = Gson()

    @TypeConverter
    fun toIngredient(data: String?): List<Ingredient> {
        if (data.isNullOrEmpty()) {
            return emptyList()
        }
        val listType = object : TypeToken<List<Ingredient>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun fromIngredients(ingredient: List<Ingredient>?): String? {
        if (ingredient == null || ingredient.isEmpty()) {
            return ""
        }
        return gson.toJson(ingredient)
    }
}