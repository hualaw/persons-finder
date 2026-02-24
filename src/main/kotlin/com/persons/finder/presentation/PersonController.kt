package com.persons.finder.presentation

import com.persons.finder.domain.model.Person
import com.persons.finder.domain.services.PersonsService
import com.persons.finder.presentation.dto.CreatePersonRequest
import com.persons.finder.presentation.dto.LocationDto
import com.persons.finder.presentation.validation.InputValidator
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/persons")
class PersonController(
    private val personsService: PersonsService,
    private val inputValidator: InputValidator
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @PostMapping
    fun createPerson(@RequestBody request: CreatePersonRequest): ResponseEntity<Person> {
        if (!inputValidator.validate(request.jobTitle, request.hobbies)) {
            logger.warn("event=createPerson reason=validation_failed invalid_job_or_hobbies")
            return ResponseEntity.badRequest().build()
        }
        
        if (!inputValidator.validateLocation(request.location.latitude, request.location.longitude)) {
             logger.warn("event=createPerson reason=validation_failed invalid_location")
             return ResponseEntity.badRequest().build()
        }

        return try {
            val person = personsService.create(request)
            ResponseEntity.status(HttpStatus.CREATED).body(person)
        } catch (e: Exception) {
            logger.error("event=createPerson error={}", e.message)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @GetMapping("/{id}")
    fun getPerson(@PathVariable id: Long): ResponseEntity<Person> {
        try {
            val person = personsService.getById(id)
            return ResponseEntity.ok(person)
        } catch (e: RuntimeException) {
            logger.warn("event=getPerson personId={} reason=not_found", id)
            return ResponseEntity.notFound().build()
        } catch (e: Exception) {
            logger.error("event=getPerson personId={} error={}", id, e.message)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @PutMapping("/{id}/location")
    fun updateLocation(@PathVariable id: Long, @RequestBody location: LocationDto): ResponseEntity<Void> {
        if (!inputValidator.validateLocation(location.latitude, location.longitude)) {
            logger.warn("event=updateLocation personId={} reason=validation_failed invalid_location", id)
            return ResponseEntity.badRequest().build()
        }

        try {
            personsService.updateLocation(id, location)
            return ResponseEntity.ok().build()
        } catch (e: RuntimeException) {
            logger.warn("event=updateLocation personId={} reason=not_found", id)
            return ResponseEntity.notFound().build()
        } catch (e: Exception) {
            logger.error("event=updateLocation personId={} error={}", id, e.message)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @GetMapping("/nearby")
    fun findNearby(@RequestParam latitude: Double, @RequestParam longitude: Double, @RequestParam radius: Double): ResponseEntity<List<Person>> {
        if (!inputValidator.validateLocation(latitude, longitude, radius)) {
            logger.warn("event=findNearby reason=validation_failed invalid_params")
            return ResponseEntity.badRequest().build()
        }
        return try {
            val persons = personsService.findNearby(latitude, longitude, radius)
            ResponseEntity.ok(persons)
        } catch (e: Exception) {
            logger.error("event=findNearby error={}", e.message)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }
}
