package com.pucetec.alejandro_michael_shipflow.mappers

import com.pucetec.alejandro_michael_shipflow.models.entities.ShippingPackage
import com.pucetec.alejandro_michael_shipflow.models.requests.PackageRequest
import com.pucetec.alejandro_michael_shipflow.models.responses.PackageResponse
import org.springframework.stereotype.Component

@Component
open class PackageMapper {

    open fun toEntity(request: PackageRequest): ShippingPackage {
        return ShippingPackage(
            type = request.type,
            weight = request.weight,
            description = request.description,
            cityFrom = request.cityFrom,
            cityTo = request.cityTo,
            events = request.events.map {
                PackageEventsMapper().toEntity(request = it)
            }
        ).apply {
            id = request.id
            createdAt = request.createdAt
            updatedAt = request.updatedAt
        }
    }

    open fun toResponse(entity: ShippingPackage): PackageResponse {
        return PackageResponse(
            type = entity.type,
            weight = entity.weight,
            description = entity.description,
            cityFrom = entity.cityFrom,
            cityTo = entity.cityTo,
            events = entity.events.map {
                PackageEventsMapper().toResponse(entity = it)
            }
        ).apply {
            id = entity.id
            createdAt = entity.createdAt
            updatedAt = entity.updatedAt
        }
    }
}