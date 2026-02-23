package com.persons.finder.domain.model

import com.persons.finder.presentation.dto.LocationDto

data class Person(
    val id: Long,
    val name: String,
    val jobTitle: String,
    val hobbies: List<String>,
    val bio: String,
    val location: LocationDto? = null
)
