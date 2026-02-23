package com.persons.finder.presentation

import com.persons.finder.domain.model.Person
import com.persons.finder.domain.services.PersonsService
import com.persons.finder.presentation.dto.CreatePersonRequest
import com.persons.finder.presentation.dto.LocationDto
import com.persons.finder.presentation.validation.InputValidator
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/persons")
class PersonController(
    private val personsService: PersonsService,
    private val inputValidator: InputValidator
) {

    @PostMapping
    fun createPerson(@RequestBody request: CreatePersonRequest): ResponseEntity<Person> {
        if (!inputValidator.validate(request.jobTitle, request.hobbies)) {
            return ResponseEntity.badRequest().build()
        }
        
        if (!inputValidator.validateLocation(request.location.latitude, request.location.longitude)) {
             return ResponseEntity.badRequest().build()
        }

        val person = personsService.create(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(person)
    }

    @GetMapping("/{id}")
    fun getPerson(@PathVariable id: Long): ResponseEntity<Person> {
        try {
            val person = personsService.getById(id)
            return ResponseEntity.ok(person)
        } catch (e: RuntimeException) {
            return ResponseEntity.notFound().build()
        }
    }

    @PutMapping("/{id}/location")
    fun updateLocation(@PathVariable id: Long, @RequestBody location: LocationDto): ResponseEntity<Void> {
        if (!inputValidator.validateLocation(location.latitude, location.longitude)) {
            return ResponseEntity.badRequest().build()
        }

        try {
            personsService.updateLocation(id, location)
            return ResponseEntity.ok().build()
        } catch (e: RuntimeException) {
            return ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/nearby")
    fun findNearby(@RequestParam latitude: Double, @RequestParam longitude: Double, @RequestParam radius: Double): ResponseEntity<List<Person>> {
        if (!inputValidator.validateLocation(latitude, longitude, radius)) {
            return ResponseEntity.badRequest().build()
        }
        val persons = personsService.findNearby(latitude, longitude, radius)
        return ResponseEntity.ok(persons)
    }
}
