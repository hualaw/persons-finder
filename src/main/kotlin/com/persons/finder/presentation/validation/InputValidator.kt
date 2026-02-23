package com.persons.finder.presentation.validation

import org.springframework.stereotype.Component

@Component
class InputValidator {

    private val forbiddenKeywords = setOf("ignore", "instruction", "command", "disregard", "system")

    fun validate(jobTitle: String, hobbies: List<String>): Boolean {
        if (containsForbiddenKeywords(jobTitle) || hobbies.any { containsForbiddenKeywords(it) }) {
            return false
        }
        return true
    }

    fun validateLocation(latitude: Double, longitude: Double, radius: Double? = null): Boolean {
        if (latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180) {
            return false
        }
        if (radius != null && radius <= 0) {
            return false
        }
        return true
    }

    private fun containsForbiddenKeywords(text: String): Boolean {
        return forbiddenKeywords.any { text.contains(it, ignoreCase = true) }
    }
}
