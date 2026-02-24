package com.persons.finder.infrastructure.external

import com.persons.finder.infrastructure.configuration.GeminiProperties
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.*
import org.springframework.test.web.client.response.MockRestResponseCreators.*
import org.springframework.web.client.RestTemplate
import java.time.Duration

class GeminiServiceTest {

    private lateinit var geminiService: GeminiService
    private lateinit var mockServer: MockRestServiceServer
    private val restTemplate = RestTemplate()

    @BeforeEach
    fun setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate)
        val properties = GeminiProperties(key = "test-key", timeout = Duration.ofSeconds(1))
        geminiService = GeminiService(properties, restTemplate)
    }

    @Test
    fun `generateBio should return bio from API when successful`() {
        val mockResponse = """
            {
              "candidates": [
                {
                  "content": {
                    "parts": [
                      {
                        "text": "This is a mocked bio."
                      }
                    ]
                  }
                }
              ]
            }
        """.trimIndent()

        mockServer.expect(requestTo("https://generativelanguage.googleapis.com/v1beta/models/gemini-3-flash-preview:generateContent"))
            .andExpect(method(HttpMethod.POST))
            .andExpect(header("x-goog-api-key", "test-key"))
            .andRespond(withSuccess(mockResponse, MediaType.APPLICATION_JSON))

        val bio = geminiService.generateBio("Engineer", listOf("Coding"))

        assertEquals("This is a mocked bio.", bio)
        mockServer.verify()
    }

    @Test
    fun `generateBio should return default bio when API fails`() {
        mockServer.expect(requestTo("https://generativelanguage.googleapis.com/v1beta/models/gemini-3-flash-preview:generateContent"))
            .andRespond(withServerError())

        val bio = geminiService.generateBio("Engineer", listOf("Coding"))

        assertEquals("A person of many talents and interests.", bio)
        mockServer.verify()
    }
}
