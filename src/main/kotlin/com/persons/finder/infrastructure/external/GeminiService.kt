package com.persons.finder.infrastructure.external

import org.springframework.stereotype.Service

@Service
class GeminiService {

    fun generateBio(jobTitle: String, hobbies: List<String>): String {
        // Simulate a call to the Gemini API
        Thread.sleep(2000) // Simulate delay
        return "A creative bio for a $jobTitle who loves ${hobbies.joinToString(", ")}."
    }
}
