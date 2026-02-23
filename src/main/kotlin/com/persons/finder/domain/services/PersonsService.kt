package com.persons.finder.domain.services

import com.persons.finder.domain.model.Person
import com.persons.finder.presentation.dto.CreatePersonRequest
import com.persons.finder.presentation.dto.LocationDto

interface PersonsService {
    fun getById(id: Long): Person
    fun save(person: Person)
    fun create(request: CreatePersonRequest): Person
    fun updateLocation(personId: Long, location: LocationDto)
    fun findNearby(latitude: Double, longitude: Double, radius: Double): List<Person>
}
