package com.persons.finder.domain.services

import com.persons.finder.domain.model.Location
import com.persons.finder.infrastructure.persistence.entity.LocationEntity
import com.persons.finder.infrastructure.persistence.repository.LocationRepository
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LocationsServiceImpl(
    private val locationRepository: LocationRepository,
    private val geometryFactory: GeometryFactory
) : LocationsService {

    private val logger = LoggerFactory.getLogger(javaClass)

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

        try {
            locationRepository.save(locationEntity)
        } catch (e: Exception) {
            logger.error("event=addLocation personId={} error={}", location.referenceId, e.message)
            throw e
        }
    }

    @Transactional
    override fun removeLocation(personId: Long) {
        val location = locationRepository.findByPersonId(personId)
        if (location.isPresent) {
            locationRepository.delete(location.get())
        } else {
            logger.warn("event=removeLocation personId={} reason=not_found", personId)
        }
    }

    override fun findAround(latitude: Double, longitude: Double, radiusInKm: Double): List<Location> {
        try {
            val radiusInMeters = radiusInKm * 1000
            val entities = locationRepository.findNearby(latitude, longitude, radiusInMeters)
            return entities.map {
                Location(
                    referenceId = it.personId,
                    latitude = it.latitude,
                    longitude = it.longitude
                )
            }
        } catch (e: Exception) {
            logger.error("event=findAround error={}", e.message)
            throw e
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
