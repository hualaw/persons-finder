package com.persons.finder.domain.events

import com.persons.finder.infrastructure.external.GeminiService
import com.persons.finder.infrastructure.persistence.repository.PersonRepository
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class PersonCreatedEventListener(
    private val personRepository: PersonRepository,
    private val geminiService: GeminiService
) {

    @Async
    @EventListener
    @Transactional
    fun handlePersonCreatedEvent(event: PersonCreatedEvent) {
        val bio = geminiService.generateBio(event.jobTitle, event.hobbies)
        val person = personRepository.findById(event.personId).orElseThrow { RuntimeException("Person not found") }
        person.bio = bio
        personRepository.save(person)
    }
}
