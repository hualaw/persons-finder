package com.persons.finder.domain.repository

import com.persons.finder.domain.model.Person

interface PersonRepository {
    fun findById(id: Long): Person?
    fun save(person: Person)
}
