package com.pucetec.alejandro_michael_shipflow.services

import com.pucetec.alejandro_michael_shipflow.exceptions.exceptions.DescriptionTooLongException
import com.pucetec.alejandro_michael_shipflow.exceptions.exceptions.InvalidStatusTransitionException
import com.pucetec.alejandro_michael_shipflow.exceptions.exceptions.ResourceNotFoundException
import com.pucetec.alejandro_michael_shipflow.exceptions.exceptions.SameCityException
import com.pucetec.alejandro_michael_shipflow.mappers.PackageEventMapper
import com.pucetec.alejandro_michael_shipflow.mappers.PackageMapper
import com.pucetec.alejandro_michael_shipflow.models.entities.PackageEvent
import com.pucetec.alejandro_michael_shipflow.models.entities.Status
import com.pucetec.alejandro_michael_shipflow.models.requests.CreatePackageRequest
import com.pucetec.alejandro_michael_shipflow.models.requests.UpdatePackageStatusRequest
import com.pucetec.alejandro_michael_shipflow.models.responses.PackageEventHistoryResponse
import com.pucetec.alejandro_michael_shipflow.models.responses.PackageEventResponse
import com.pucetec.alejandro_michael_shipflow.models.responses.PackageResponse
import com.pucetec.alejandro_michael_shipflow.models.responses.PackageSummaryResponse
import com.pucetec.alejandro_michael_shipflow.repositories.PackageEventRepository
import com.pucetec.alejandro_michael_shipflow.repositories.PackageRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PackageService(
    private val packageRepository: PackageRepository,
    private val packageEventRepository: PackageEventRepository,
    private val packageMapper: PackageMapper,
    private val eventMapper: PackageEventMapper
) {

    @Transactional
    fun create(request: CreatePackageRequest): PackageResponse {
        val origin = request.cityFrom.trim().uppercase()
        val destination = request.cityTo.trim().uppercase()

        if (origin == destination) {
            throw SameCityException("La ciudad de origen no puede ser igual a la ciudad de destino.")
        }

        if (request.description.length > 50) {
            throw DescriptionTooLongException("La descripción no puede exceder los 50 caracteres.")
        }

        val entity = packageMapper.toEntity(request)
        val saved = packageRepository.save(entity)

        val initialEvent = PackageEvent(
            status = Status.PENDING,
            comment = "Registro inicial",
            packageEntity = saved
        )
        packageEventRepository.save(initialEvent)

        val withEvent = saved.copy(events = listOf(initialEvent))
        return packageMapper.toResponse(withEvent)
    }

    fun getAll(): List<PackageResponse> {
        return packageRepository.findAll().map { packageMapper.toResponse(it) }
    }

    fun getByTrackingId(trackingId: String): PackageResponse {
        val entity = packageRepository.findByTrackingId(trackingId)
            ?: throw ResourceNotFoundException("Paquete con trackingId $trackingId no encontrado.")
        return packageMapper.toResponse(entity)
    }

    fun getEventHistory(trackingId: String): PackageEventHistoryResponse {
        val entity = packageRepository.findByTrackingId(trackingId)
            ?: throw ResourceNotFoundException("Paquete con trackingId $trackingId no encontrado.")

        val summary = PackageSummaryResponse(
            id = entity.id,
            trackingId = entity.trackingId,
            type = entity.type,
            cityFrom = entity.cityFrom,
            cityTo = entity.cityTo
        )

        val events = entity.events.sortedByDescending { it.createdAt }
            .map { eventMapper.toResponse(it) }

        return PackageEventHistoryResponse(
            `package` = summary,
            events = events
        )
    }

    @Transactional
    fun updateStatus(trackingId: String, request: UpdatePackageStatusRequest): PackageEventResponse {
        val entity = packageRepository.findByTrackingId(trackingId)
            ?: throw ResourceNotFoundException("Paquete con trackingId $trackingId no encontrado.")

        val currentStatus = entity.events.maxByOrNull { it.createdAt }?.status

        if (!isValidTransition(currentStatus, request.status)) {
            throw InvalidStatusTransitionException("Transición inválida: $currentStatus → ${request.status}")
        }

        val newEvent = eventMapper.toEntity(request, entity)
        val savedEvent = packageEventRepository.save(newEvent)

        return eventMapper.toResponse(savedEvent)
    }

    private fun isValidTransition(current: Status?, next: Status): Boolean {
        return when (current) {
            null -> next == Status.PENDING
            Status.PENDING -> next == Status.IN_TRANSIT
            Status.IN_TRANSIT -> next in listOf(Status.DELIVERED, Status.ON_HOLD, Status.CANCELLED)
            Status.ON_HOLD -> next in listOf(Status.IN_TRANSIT, Status.CANCELLED)
            else -> false
        }
    }
}
