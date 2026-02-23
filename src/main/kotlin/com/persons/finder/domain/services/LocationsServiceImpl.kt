package com.persons.finder.domain.services

import com.persons.finder.domain.model.Location
import com.persons.finder.infrastructure.persistence.entity.LocationEntity
import com.persons.finder.infrastructure.persistence.repository.LocationRepository
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LocationsServiceImpl(
    private val locationRepository: LocationRepository,
    private val geometryFactory: GeometryFactory
) : LocationsService {

    @Transactional
    override fun addLocation(location: Location) {
        val point = geometryFactory.createPoint(Coordinate(location.longitude, location.latitude))

        val existingLocation = locationRepository.findByPersonId(location.referenceId)

        val locationEntity = if (existingLocation.isPresent) {
            existingLocation.get().copy(
                latitude = location.latitude,
                longitude = location.longitude,
                geom = point
            )
        } else {
            LocationEntity(
                personId = location.referenceId,
                latitude = location.latitude,
                longitude = location.longitude,
                geom = point
            )
        }

        locationRepository.save(locationEntity)
    }

    @Transactional
    override fun removeLocation(personId: Long) {
        val location = locationRepository.findByPersonId(personId)
        if (location.isPresent) {
            locationRepository.delete(location.get())
        }
    }

    override fun findAround(latitude: Double, longitude: Double, radiusInKm: Double): List<Location> {
        val radiusInMeters = radiusInKm * 1000
        val entities = locationRepository.findNearby(latitude, longitude, radiusInMeters)
        return entities.map {
            Location(
                referenceId = it.personId,
                latitude = it.latitude,
                longitude = it.longitude
            )
        }
    }

    override fun getByPersonId(personId: Long): Location? {
        val entity = locationRepository.findByPersonId(personId)
        return entity.map {
            Location(
                referenceId = it.personId,
                latitude = it.latitude,
                longitude = it.longitude
            )
        }.orElse(null)
    }
}
