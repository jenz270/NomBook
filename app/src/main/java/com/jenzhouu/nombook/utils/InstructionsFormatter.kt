package com.jenzhouu.nombook.utils

object InstructionsFormatter {
    private fun parse(string: String) : List<String> {
        // 1. Remove the "Step" text from the string if it already contains that
        string.replace("STEP", "", true)

        // 2. Place the formatted text into a list
        return string.lines().map { it.trim() }.filter { it.isNotBlank() }
    }

    fun format(string: String) : String {
        val list = parse(string)
        return list.mapIndexed { index, s -> "<b>Step ${index.plus(1)} : </b><br>$s" }
            .joinToString(separator = "<br><br>")
    }
}