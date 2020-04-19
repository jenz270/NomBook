package com.jenzhouu.nombook.ui.recipedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.jenzhouu.nombook.R
import com.jenzhouu.nombook.databinding.FragmentRecipeDetailsBinding
import com.jenzhouu.nombook.model.Meal
import com.jenzhouu.nombook.utils.InstructionsFormatter
import com.squareup.picasso.Picasso

class RecipeDetailsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentRecipeDetailsBinding>(
            inflater, R.layout.fragment_recipe_details, container, false
        )

        val bundle = arguments
        var recipe: Meal? = null
        if (bundle != null) {
            recipe = bundle.getParcelable("recipe")
        }

        binding.backButton.setOnClickListener {
            view?.findNavController()?.navigateUp()
        }

        if (recipe != null) {
            binding.recipeNameText.text = recipe.name
            Picasso.get()
                .load(recipe.image)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(binding.recipeImage)
            var ingredientsText = ""
            recipe.ingredients?.forEach {
                ingredientsText += it?.measure + " " + it?.name + "\n"
            }
            binding.ingredientsList.text = ingredientsText

            val instructions = InstructionsFormatter.format(recipe.instructions)
            binding.instructionsList.text =
                HtmlCompat.fromHtml(instructions, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }

        return binding.root
    }
}
