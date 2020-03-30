package com.jenzhouu.nombook.api

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.jenzhouu.nombook.model.Ingredient
import com.jenzhouu.nombook.model.Meal
import java.lang.reflect.Type

class MealDeserializer : JsonDeserializer<Meal> {
    private val gson by lazy { Gson() }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Meal {
        // We only need to custom parse the parseIngredients, so we initially let
        // Gson parse everything else. Ingredients will be null at this point.
        val meal = gson.fromJson<Meal>(json, Meal::class.java)
        return meal.copy(
            ingredients = parseIngredients(json)
        )
    }

    private fun parseIngredients(json: JsonElement): List<Ingredient> {
        val jsonObject = json.asJsonObject
        return (1..20).map {
            Ingredient(
                name = jsonObject.getAsJsonPrimitive("strIngredient$it").asString,
                measure = jsonObject.getAsJsonPrimitive("strMeasure$it").asString
            )
        }.filter {
            it.measure.isNotEmpty() || it.name.isNotEmpty()
        }
    }
}