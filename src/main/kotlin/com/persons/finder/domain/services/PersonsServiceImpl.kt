package com.persons.finder.domain.services

import com.persons.finder.domain.events.PersonCreatedEvent
import com.persons.finder.domain.model.Person
import com.persons.finder.infrastructure.persistence.entity.LocationEntity
import com.persons.finder.infrastructure.persistence.entity.PersonEntity
import com.persons.finder.infrastructure.persistence.repository.LocationRepository
import com.persons.finder.infrastructure.persistence.repository.PersonRepository
import com.persons.finder.presentation.dto.CreatePersonRequest
import com.persons.finder.presentation.dto.LocationDto
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Coordinate

@Service
class PersonsServiceImpl(
    private val personRepository: PersonRepository,
    private val locationRepository: LocationRepository,
    private val eventPublisher: ApplicationEventPublisher,
    private val geometryFactory: GeometryFactory
) : PersonsService {

    override fun getById(id: Long): Person {
        val entity = personRepository.findById(id).orElseThrow { RuntimeException("Person not found") }
        val locationEntity = locationRepository.findByPersonId(id)
        val locationDto = locationEntity.map { LocationDto(it.latitude, it.longitude) }.orElse(null)

        return Person(
            id = entity.id,
            name = entity.name,
            jobTitle = entity.jobTitle,
            hobbies = entity.hobbies.toList(),
            bio = entity.bio,
            location = locationDto
        )
    }

    override fun save(person: Person) {
        // Implementation for save
    }

    @Transactional
    override fun create(request: CreatePersonRequest): Person {
        val personEntity = PersonEntity(
            name = request.name,
            jobTitle = request.jobTitle,
            hobbies = request.hobbies.toTypedArray(),
            bio = "the default bio"
        )
        val savedPerson = personRepository.save(personEntity)

        val point = geometryFactory.createPoint(Coordinate(request.location.longitude, request.location.latitude))

        val locationEntity = LocationEntity(
            personId = savedPerson.id,
            latitude = request.location.latitude,
            longitude = request.location.longitude,
            geom = point
        )
        locationRepository.save(locationEntity)

        eventPublisher.publishEvent(PersonCreatedEvent(savedPerson.id, request.jobTitle, request.hobbies))

        return Person(
            id = savedPerson.id,
            name = savedPerson.name,
            jobTitle = savedPerson.jobTitle,
            hobbies = savedPerson.hobbies.toList(),
            bio = savedPerson.bio,
            location = request.location
        )
    }

    @Transactional
    override fun updateLocation(personId: Long, location: LocationDto) {
        if (!personRepository.existsById(personId)) {
            throw RuntimeException("Person not found")
        }

        val point = geometryFactory.createPoint(Coordinate(location.longitude, location.latitude))

        val existingLocation = locationRepository.findByPersonId(personId)

        val locationEntity = if (existingLocation.isPresent) {
            existingLocation.get().copy(
                latitude = location.latitude,
                longitude = location.longitude,
                geom = point
            )
        } else {
            LocationEntity(
                personId = personId,
                latitude = location.latitude,
                longitude = location.longitude,
                geom = point
            )
        }

        locationRepository.save(locationEntity)
    }

    override fun findNearby(latitude: Double, longitude: Double, radius: Double): List<Person> {
        val locations = locationRepository.findNearby(latitude, longitude, radius)
        val personIds = locations.map { it.personId }
        val persons = personRepository.findAllById(personIds)

        val locationMap = locations.associateBy { it.personId }

        return persons.map { person ->
            val loc = locationMap[person.id]
            Person(
                id = person.id,
                name = person.name,
                jobTitle = person.jobTitle,
                hobbies = person.hobbies.toList(),
                bio = person.bio,
                location = loc?.let { LocationDto(it.latitude, it.longitude) }
            )
        }
    }
}
