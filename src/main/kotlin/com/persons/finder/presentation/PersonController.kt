package com.persons.finder.presentation

import com.persons.finder.domain.model.Person
import com.persons.finder.domain.services.PersonsService
import com.persons.finder.presentation.dto.CreatePersonRequest
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

        val person = personsService.create(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(person)
    }

    @GetMapping("/{id}")
    fun getPerson(@PathVariable id: Long): ResponseEntity<Person> {
        val person = personsService.getById(id)
        return ResponseEntity.ok(person)
    }
}
