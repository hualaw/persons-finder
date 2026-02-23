package com.persons.finder.domain.services

import com.persons.finder.domain.model.Person
import com.persons.finder.presentation.dto.CreatePersonRequest

interface PersonsService {
    fun getById(id: Long): Person
    fun save(person: Person)
    fun create(request: CreatePersonRequest): Person
}
