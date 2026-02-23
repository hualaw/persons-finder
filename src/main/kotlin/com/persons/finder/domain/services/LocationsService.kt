package com.persons.finder.domain.services

import com.persons.finder.domain.model.Location

interface LocationsService {
    fun addLocation(location: Location)
    fun removeLocation(personId: Long)
    fun findAround(latitude: Double, longitude: Double, radiusInKm: Double) : List<Location>
    fun getByPersonId(personId: Long): Location?
}
