package com.jenzhouu.nombook.ui.recipedetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.jenzhouu.nombook.R
import com.jenzhouu.nombook.model.Meals
import com.squareup.picasso.Picasso

class RecipeDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipe_details, container, false)
        val recipeName = view.findViewById<TextView>(R.id.recipe_name_text)
        val recipeImage = view.findViewById<ImageView>(R.id.recipe_image)
        val ingredientsList = view.findViewById<TextView>(R.id.ingredients_list)
        val instructionsList = view.findViewById<TextView>(R.id.instructions_list)
        val bundle = arguments
        var meal: Meals? = null

        if (bundle != null) {
            meal = bundle.getParcelable("randomMeal")
        }

        if (meal != null) {
            // get the first and only item
            val recipe = meal.mealsList[0]

            recipeName.text = recipe.name

            Picasso.get()
                .load(recipe.image)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(recipeImage)

            var ingredientsText = ""
            recipe.ingredients.forEach{
                ingredientsText += it.measure + " " + it.name + "\n"
            }

            ingredientsList.text = ingredientsText
            instructionsList.text = recipe.instructions
        }

        return view
    }
}
