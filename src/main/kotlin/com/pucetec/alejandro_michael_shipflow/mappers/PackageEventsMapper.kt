package com.pucetec.alejandro_michael_shipflow.mappers

import com.pucetec.alejandro_michael_shipflow.models.entities.PackageEvent
import com.pucetec.alejandro_michael_shipflow.models.requests.PackageEventRequest
import com.pucetec.alejandro_michael_shipflow.models.responses.PackageEventResponse
import org.springframework.stereotype.Component

@Component
open class PackageEventsMapper {

    open fun toEntity(request: PackageEventRequest): PackageEvent {
        return PackageEvent(
            status = request.status,
            event = null
        ).apply {
            id = request.id
            createdAt = request.createdAt
            updatedAt = request.updatedAt
        }
    }

    open fun toResponse(entity: PackageEvent): PackageEventResponse {
        return PackageEventResponse(
            id = entity.id,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            status = entity.status
        )
    }
}