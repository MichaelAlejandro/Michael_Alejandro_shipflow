package com.pucetec.alejandro_michael_shipflow.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.pucetec.alejandro_michael_shipflow.exceptions.exceptions.ResourceNotFoundException
import com.pucetec.alejandro_michael_shipflow.mappers.PackageEventMapper
import com.pucetec.alejandro_michael_shipflow.mappers.PackageMapper
import com.pucetec.alejandro_michael_shipflow.models.entities.*
import com.pucetec.alejandro_michael_shipflow.models.requests.CreatePackageRequest
import com.pucetec.alejandro_michael_shipflow.models.requests.UpdatePackageStatusRequest
import com.pucetec.alejandro_michael_shipflow.models.responses.*
import com.pucetec.alejandro_michael_shipflow.routes.Routes
import com.pucetec.alejandro_michael_shipflow.services.PackageService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*
import java.time.LocalDateTime
import kotlin.test.assertEquals

@WebMvcTest(PackageController::class)
class PackageControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var packageService: PackageService

    @Autowired
    private lateinit var packageMapper: PackageMapper

    @Autowired
    private lateinit var packageEventMapper: PackageEventMapper

    private lateinit var objectMapper: ObjectMapper
    private val baseUrl = Routes.PACKAGES

    @BeforeEach
    fun setup() {
        objectMapper = ObjectMapper()
            .registerModule(JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
    }

    @Test
    fun should_create_package() {
        val request = CreatePackageRequest(
            id = 0L,
            type = Type.SMALL_BOX,
            weight = 1.2f,
            description = "Envío urgente",
            cityFrom = "Quito",
            cityTo = "Guayaquil"
        )

        val entity = mock(ShippingPackage::class.java)
        val response = mock(PackageResponse::class.java)

        `when`(packageService.create(request)).thenReturn(response)
        `when`(packageMapper.toResponse(entity)).thenReturn(response)

        val json = objectMapper.writeValueAsString(request)

        val result = mockMvc.post(baseUrl) {
            contentType = MediaType.APPLICATION_JSON
            content = json
        }.andExpect {
            status { isOk() }
        }.andReturn()

        assertEquals(200, result.response.status)
    }

    @Test
    fun should_get_all_packages() {
        val entity = mock(ShippingPackage::class.java)
        val response = mock(PackageResponse::class.java)

        `when`(packageService.getAll()).thenReturn(listOf(response))
        `when`(packageMapper.toResponse(entity)).thenReturn(response)

        val result = mockMvc.get(baseUrl)
            .andExpect {
                status { isOk() }
            }.andReturn()

        assertEquals(200, result.response.status)
    }

    @Test
    fun should_get_package_by_tracking_id() {
        val trackingId = "ABC123"
        val entity = mock(ShippingPackage::class.java)
        val response = mock(PackageResponse::class.java)

        `when`(packageService.getByTrackingId(trackingId)).thenReturn(response)

        val result = mockMvc.get("$baseUrl/$trackingId")
            .andExpect {
                status { isOk() }
            }.andReturn()

        assertEquals(200, result.response.status)
    }

    @Test
    fun should_return_404_when_tracking_id_not_found() {
        val trackingId = "NOT_FOUND"
        `when`(packageService.getByTrackingId(trackingId)).thenThrow(ResourceNotFoundException("Not found"))

        val result = mockMvc.get("$baseUrl/$trackingId")
            .andExpect {
                status { isNotFound() }
            }.andReturn()

        assertEquals(404, result.response.status)
    }

    @Test
    fun should_update_package_status() {
        val trackingId = "ABC123"
        val request = UpdatePackageStatusRequest(
            status = Status.IN_TRANSIT,
            comment = "Despachado"
        )

        val eventEntity = mock(PackageEvent::class.java)
        val eventResponse = mock(PackageEventResponse::class.java)

        `when`(packageService.updateStatus(trackingId, request)).thenReturn(eventResponse)

        val json = objectMapper.writeValueAsString(request)

        val result = mockMvc.put("$baseUrl/$trackingId/status") {
            contentType = MediaType.APPLICATION_JSON
            content = json
        }.andExpect {
            status { isOk() }
        }.andReturn()

        assertEquals(200, result.response.status)
    }

    @Test
    fun should_get_event_history() {
        val trackingId = "ABC123"
        val history = mock(PackageEventHistoryResponse::class.java)

        `when`(packageService.getEventHistory(trackingId)).thenReturn(history)

        val result = mockMvc.get("$baseUrl/$trackingId/events")
            .andExpect {
                status { isOk() }
            }.andReturn()

        assertEquals(200, result.response.status)
    }

    @TestConfiguration
    class Config {
        @Bean
        fun packageService(): PackageService = mock(PackageService::class.java)

        @Bean
        fun packageMapper(): PackageMapper = mock(PackageMapper::class.java)

        @Bean
        fun packageEventMapper(): PackageEventMapper = mock(PackageEventMapper::class.java)
    }
}
