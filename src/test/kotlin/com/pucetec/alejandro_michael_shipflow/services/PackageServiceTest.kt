package com.pucetec.alejandro_michael_shipflow.services

import com.pucetec.alejandro_michael_shipflow.exceptions.exceptions.*
import com.pucetec.alejandro_michael_shipflow.mappers.PackageEventMapper
import com.pucetec.alejandro_michael_shipflow.mappers.PackageMapper
import com.pucetec.alejandro_michael_shipflow.models.entities.*
import com.pucetec.alejandro_michael_shipflow.models.requests.CreatePackageRequest
import com.pucetec.alejandro_michael_shipflow.models.requests.UpdatePackageStatusRequest
import com.pucetec.alejandro_michael_shipflow.models.responses.PackageEventResponse
import com.pucetec.alejandro_michael_shipflow.models.responses.PackageResponse
import com.pucetec.alejandro_michael_shipflow.repositories.PackageEventRepository
import com.pucetec.alejandro_michael_shipflow.repositories.PackageRepository
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import java.time.LocalDateTime

class PackageServiceTest {

    private lateinit var packageRepository: PackageRepository
    private lateinit var packageEventRepository: PackageEventRepository
    private lateinit var packageMapper: PackageMapper
    private lateinit var eventMapper: PackageEventMapper
    private lateinit var packageService: PackageService

    @BeforeEach
    fun setUp() {
        packageRepository = mock(PackageRepository::class.java)
        packageEventRepository = mock(PackageEventRepository::class.java)
        packageMapper = mock(PackageMapper::class.java)
        eventMapper = mock(PackageEventMapper::class.java)
        packageService = PackageService(packageRepository, packageEventRepository, packageMapper, eventMapper)
    }

    @Test
    fun should_create_package_successfully() {
        val request = CreatePackageRequest(
            id = 1L,
            type = Type.DOCUMENT,
            weight = 0.8f,
            description = "Documentos legales",
            cityFrom = "Quito",
            cityTo = "Cuenca"
        )

        val entity = ShippingPackage(
            type = request.type,
            weight = request.weight,
            description = request.description,
            cityFrom = request.cityFrom,
            cityTo = request.cityTo
        )

        val saved = entity.copy()
        val response = mock(PackageResponse::class.java)

        `when`(packageMapper.toEntity(request)).thenReturn(entity)
        `when`(packageRepository.save(entity)).thenReturn(saved)
        `when`(packageMapper.toResponse(saved)).thenReturn(response)


    }

    @Test
    fun should_throw_same_city_exception() {
        val request = CreatePackageRequest(
            id = 1L,
            type = Type.FRAGILE,
            weight = 1.5f,
            description = "Lentes",
            cityFrom = "Quito",
            cityTo = "Quito"
        )

        assertThrows<SameCityException> {
            packageService.create(request)
        }
    }

    @Test
    fun should_throw_description_too_long_exception() {
        val request = CreatePackageRequest(
            id = 1L,
            type = Type.SMALL_BOX,
            weight = 2.0f,
            description = "a".repeat(51),
            cityFrom = "Loja",
            cityTo = "Ambato"
        )

        assertThrows<DescriptionTooLongException> {
            packageService.create(request)
        }
    }

    @Test
    fun should_return_all_packages() {
        val pkg = mock(ShippingPackage::class.java)
        val response = mock(PackageResponse::class.java)

        `when`(packageRepository.findAll()).thenReturn(listOf(pkg))
        `when`(packageMapper.toResponse(pkg)).thenReturn(response)

        val result = packageService.getAll()

        assertEquals(1, result.size)
    }

    @Test
    fun should_return_package_by_tracking_id() {
        val pkg = mock(ShippingPackage::class.java)
        val response = mock(PackageResponse::class.java)

        `when`(packageRepository.findByTrackingId("ABC123")).thenReturn(pkg)
        `when`(packageMapper.toResponse(pkg)).thenReturn(response)

        val result = packageService.getByTrackingId("ABC123")

        assertNotNull(result)
    }

    @Test
    fun should_throw_when_package_not_found_by_tracking_id() {
        `when`(packageRepository.findByTrackingId("NOT_FOUND")).thenReturn(null)

        assertThrows<ResourceNotFoundException> {
            packageService.getByTrackingId("NOT_FOUND")
        }
    }

    @Test
    fun should_return_event_history() {
        val now = LocalDateTime.now()
        val pkg = ShippingPackage(
            type = Type.FRAGILE,
            weight = 1.0f,
            description = "Lentes",
            cityFrom = "Loja",
            cityTo = "Cuenca"
        )

        val event = PackageEvent(Status.IN_TRANSIT, "En camino", pkg).apply {
            createdAt = now
            updatedAt = now
        }

        val pkgWithEvents = pkg.copy(events = listOf(event))
        val response = mock(PackageEventResponse::class.java)

        `when`(packageRepository.findByTrackingId("ABC123")).thenReturn(pkgWithEvents)
        `when`(eventMapper.toResponse(event)).thenReturn(response)

        val result = packageService.getEventHistory("ABC123")

        assertEquals(1, result.events.size)
    }

    @Test
    fun should_throw_when_event_history_tracking_id_not_found() {
        `when`(packageRepository.findByTrackingId("none")).thenReturn(null)

        assertThrows<ResourceNotFoundException> {
            packageService.getEventHistory("none")
        }
    }

    @Test
    fun should_update_status_successfully() {
        val now = LocalDateTime.now()
        val pkg = ShippingPackage(
            type = Type.SMALL_BOX,
            weight = 1.5f,
            description = "Audífonos",
            cityFrom = "Quito",
            cityTo = "Guayaquil",
            events = listOf(
                PackageEvent(Status.PENDING, "Inicial", mock()).apply {
                    createdAt = now.minusHours(1)
                }
            )
        )

        val request = UpdatePackageStatusRequest(
            status = Status.IN_TRANSIT,
            comment = "Salió"
        )

        val newEvent = PackageEvent(Status.IN_TRANSIT, "Salió", pkg)
        val response = mock(PackageEventResponse::class.java)

        `when`(packageRepository.findByTrackingId("ABC123")).thenReturn(pkg)
        `when`(eventMapper.toEntity(request, pkg)).thenReturn(newEvent)
        `when`(packageEventRepository.save(newEvent)).thenReturn(newEvent)
        `when`(eventMapper.toResponse(newEvent)).thenReturn(response)

        val result = packageService.updateStatus("ABC123", request)

        assertNotNull(result)
    }

    @Test
    fun should_throw_when_invalid_status_transition() {
        val pkg = ShippingPackage(
            type = Type.DOCUMENT,
            weight = 1.0f,
            description = "Contrato",
            cityFrom = "Quito",
            cityTo = "Ibarra",
            events = listOf(PackageEvent(Status.DELIVERED, "Finalizado", mock()))
        )

        val request = UpdatePackageStatusRequest(
            status = Status.IN_TRANSIT,
            comment = "Rollback"
        )

        `when`(packageRepository.findByTrackingId("ABC123")).thenReturn(pkg)

        assertThrows<InvalidStatusTransitionException> {
            packageService.updateStatus("ABC123", request)
        }
    }

    @Test
    fun should_throw_when_tracking_id_not_found_on_status_update() {
        val request = UpdatePackageStatusRequest(
            status = Status.CANCELLED,
            comment = "No existe"
        )

        `when`(packageRepository.findByTrackingId("NOT_FOUND")).thenReturn(null)

        assertThrows<ResourceNotFoundException> {
            packageService.updateStatus("NOT_FOUND", request)
        }
    }
}
