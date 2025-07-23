package com.pucetec.alejandro_michael_shipflow.mappers

import com.pucetec.alejandro_michael_shipflow.models.entities.ShippingPackage
import com.pucetec.alejandro_michael_shipflow.models.entities.Status
import com.pucetec.alejandro_michael_shipflow.models.requests.CreatePackageRequest
import com.pucetec.alejandro_michael_shipflow.models.responses.PackageEventResponse
import com.pucetec.alejandro_michael_shipflow.models.responses.PackageResponse
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class PackageMapper(
    private val eventMapper: PackageEventMapper
) {

    fun toEntity(request: CreatePackageRequest, generateTrackingId: Boolean = true): ShippingPackage {
        return ShippingPackage(
            type = request.type,
            weight = request.weight,
            description = request.description.trim(),
            cityFrom = request.cityFrom.trim().uppercase(),
            cityTo = request.cityTo.trim().uppercase(),
            trackingId = if (generateTrackingId) UUID.randomUUID().toString().substring(0, 8) else "",
            estimatedDeliveryDate = request.createdAt.toLocalDate().atStartOfDay().plusDays(5),
            events = listOf()
        ).apply {
            id = request.id
            createdAt = request.createdAt
            updatedAt = request.updatedAt
        }
    }

    fun toResponse(entity: ShippingPackage): PackageResponse {
        val sortedEvents = entity.events.sortedBy { it.createdAt }
        val latestStatus = sortedEvents.lastOrNull()?.status ?: Status.PENDING
        val eventResponses: List<PackageEventResponse> = sortedEvents.map { eventMapper.toResponse(it) }

        return PackageResponse(
            id = entity.id,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            trackingId = entity.trackingId,
            type = entity.type,
            weight = entity.weight,
            description = entity.description,
            cityFrom = entity.cityFrom,
            cityTo = entity.cityTo,
            estimatedDeliveryDate = entity.estimatedDeliveryDate,
            currentStatus = latestStatus,
            events = eventResponses
        )
    }
}