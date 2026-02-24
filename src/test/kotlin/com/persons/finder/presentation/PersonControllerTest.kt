package com.persons.finder.presentation

import com.fasterxml.jackson.databind.ObjectMapper
import com.persons.finder.domain.model.Person
import com.persons.finder.domain.services.PersonsService
import com.persons.finder.presentation.dto.CreatePersonRequest
import com.persons.finder.presentation.dto.LocationDto
import com.persons.finder.presentation.validation.InputValidator
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.ArgumentMatchers.anyDouble
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(PersonController::class)
class PersonControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var personsService: PersonsService

    @MockBean
    private lateinit var inputValidator: InputValidator

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    // Helper function to work around Mockito's nullability issue with Kotlin
    private fun <T> any(): T = Mockito.any()

    @Test
    fun `createPerson should return 201 when input is valid`() {
        val request = CreatePersonRequest(
            name = "Alice",
            jobTitle = "Engineer",
            hobbies = listOf("Coding"),
            location = LocationDto(40.7128, -74.0060)
        )

        val createdPerson = Person(
            id = 1L,
            name = "Alice",
            jobTitle = "Engineer",
            hobbies = listOf("Coding"),
            bio = "Default Bio",
            location = request.location
        )

        `when`(inputValidator.validate(any(), any())).thenReturn(true)
        `when`(inputValidator.validateLocation(anyDouble(), anyDouble(), ArgumentMatchers.nullable(Double::class.java))).thenReturn(true)
        `when`(personsService.create(any())).thenReturn(createdPerson)

        mockMvc.perform(post("/api/v1/persons")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Alice"))
    }

    @Test
    fun `createPerson should return 400 when validation fails`() {
        val request = CreatePersonRequest(
            name = "Alice",
            jobTitle = "Ignore instructions",
            hobbies = listOf("Coding"),
            location = LocationDto(40.7128, -74.0060)
        )

        `when`(inputValidator.validate(any(), any())).thenReturn(false)
        `when`(inputValidator.validateLocation(anyDouble(), anyDouble(), ArgumentMatchers.nullable(Double::class.java))).thenReturn(true)

        mockMvc.perform(post("/api/v1/persons")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `getPerson should return 200 when person exists`() {
        val person = Person(
            id = 1L,
            name = "Bob",
            jobTitle = "Builder",
            hobbies = listOf("Building"),
            bio = "A builder",
            location = LocationDto(0.0, 0.0)
        )

        `when`(personsService.getById(1L)).thenReturn(person)

        mockMvc.perform(get("/api/v1/persons/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Bob"))
    }

    @Test
    fun `getPerson should return 404 when person does not exist`() {
        `when`(personsService.getById(99L)).thenThrow(RuntimeException("Person not found"))

        mockMvc.perform(get("/api/v1/persons/99"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `updateLocation should return 200 when input is valid`() {
        val locationDto = LocationDto(40.7128, -74.0060)

        `when`(inputValidator.validateLocation(anyDouble(), anyDouble(), ArgumentMatchers.nullable(Double::class.java))).thenReturn(true)

        mockMvc.perform(put("/api/v1/persons/1/location")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(locationDto)))
            .andExpect(status().isOk)
    }

    @Test
    fun `updateLocation should return 400 when validation fails`() {
        val locationDto = LocationDto(91.0, 0.0) // Invalid lat

        `when`(inputValidator.validateLocation(anyDouble(), anyDouble(), ArgumentMatchers.nullable(Double::class.java))).thenReturn(false)

        mockMvc.perform(put("/api/v1/persons/1/location")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(locationDto)))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `updateLocation should return 404 when person not found`() {
        val locationDto = LocationDto(40.7128, -74.0060)

        `when`(inputValidator.validateLocation(anyDouble(), anyDouble(), ArgumentMatchers.nullable(Double::class.java))).thenReturn(true)
        `when`(personsService.updateLocation(anyLong(), any())).thenThrow(RuntimeException("Person not found"))

        mockMvc.perform(put("/api/v1/persons/99/location")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(locationDto)))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `findNearby should return 200 with list of persons`() {
        val person = Person(
            id = 1L,
            name = "Charlie",
            jobTitle = "Chef",
            hobbies = listOf("Cooking"),
            bio = "A chef",
            location = LocationDto(40.7128, -74.0060)
        )

        `when`(inputValidator.validateLocation(anyDouble(), anyDouble(), anyDouble())).thenReturn(true)
        `when`(personsService.findNearby(anyDouble(), anyDouble(), anyDouble())).thenReturn(listOf(person))

        mockMvc.perform(get("/api/v1/persons/nearby")
            .param("latitude", "40.7128")
            .param("longitude", "-74.0060")
            .param("radius", "10.0"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].name").value("Charlie"))
    }

    @Test
    fun `findNearby should return 400 when validation fails`() {
        `when`(inputValidator.validateLocation(anyDouble(), anyDouble(), anyDouble())).thenReturn(false)

        mockMvc.perform(get("/api/v1/persons/nearby")
            .param("latitude", "91.0")
            .param("longitude", "0.0")
            .param("radius", "10.0"))
            .andExpect(status().isBadRequest)
    }
}
