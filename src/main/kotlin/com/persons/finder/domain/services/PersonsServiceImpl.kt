package com.persons.finder.domain.services

import com.persons.finder.domain.events.PersonCreatedEvent
import com.persons.finder.domain.model.Person
import com.persons.finder.infrastructure.persistence.entity.LocationEntity
import com.persons.finder.infrastructure.persistence.entity.PersonEntity
import com.persons.finder.infrastructure.persistence.repository.LocationRepository
import com.persons.finder.infrastructure.persistence.repository.PersonRepository
import com.persons.finder.presentation.dto.CreatePersonRequest
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PersonsServiceImpl(
    private val personRepository: PersonRepository,
    private val locationRepository: LocationRepository,
    private val eventPublisher: ApplicationEventPublisher
) : PersonsService {

    override fun getById(id: Long): Person {
        val entity = personRepository.findById(id).orElseThrow { RuntimeException("Person not found") }
        return Person(entity.id, entity.name)
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

        val locationEntity = LocationEntity(
            personId = savedPerson.id,
            latitude = request.location.latitude,
            longitude = request.location.longitude
        )
        locationRepository.save(locationEntity)

        eventPublisher.publishEvent(PersonCreatedEvent(savedPerson.id, request.jobTitle, request.hobbies))

        return Person(savedPerson.id, savedPerson.name)
    }
}
