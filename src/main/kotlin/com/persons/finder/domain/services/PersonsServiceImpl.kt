package com.persons.finder.domain.services

import com.persons.finder.domain.events.PersonCreatedEvent
import com.persons.finder.domain.model.Location
import com.persons.finder.domain.model.Person
import com.persons.finder.infrastructure.persistence.entity.PersonEntity
import com.persons.finder.infrastructure.persistence.repository.PersonRepository
import com.persons.finder.presentation.dto.CreatePersonRequest
import com.persons.finder.presentation.dto.LocationDto
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PersonsServiceImpl(
    private val personRepository: PersonRepository,
    private val locationsService: LocationsService,
    private val eventPublisher: ApplicationEventPublisher
) : PersonsService {

    override fun getById(id: Long): Person {
        val entity = personRepository.findById(id).orElseThrow { RuntimeException("Person not found") }
        val location = locationsService.getByPersonId(id)
        val locationDto = location?.let { LocationDto(it.latitude, it.longitude) }

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

        val location = Location(
            referenceId = savedPerson.id,
            latitude = request.location.latitude,
            longitude = request.location.longitude
        )
        locationsService.addLocation(location)

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

        val locationModel = Location(
            referenceId = personId,
            latitude = location.latitude,
            longitude = location.longitude
        )
        locationsService.addLocation(locationModel)
    }

    override fun findNearby(latitude: Double, longitude: Double, radius: Double): List<Person> {
        // Convert radius from meters to km for LocationsService
        val radiusInKm = radius / 1000.0
        val locations = locationsService.findAround(latitude, longitude, radiusInKm)
        
        if (locations.isEmpty()) {
            return emptyList()
        }

        val personIds = locations.map { it.referenceId }
        val persons = personRepository.findAllById(personIds)

        val locationMap = locations.associateBy { it.referenceId }

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
