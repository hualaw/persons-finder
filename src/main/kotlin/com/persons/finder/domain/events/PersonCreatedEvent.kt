package com.persons.finder.domain.events

data class PersonCreatedEvent(
    val personId: Long,
    val jobTitle: String,
    val hobbies: List<String>
)
