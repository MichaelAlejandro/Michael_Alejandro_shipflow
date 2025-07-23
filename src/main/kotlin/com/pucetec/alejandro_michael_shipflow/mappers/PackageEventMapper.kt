package com.pucetec.alejandro_michael_shipflow.mappers

import com.pucetec.alejandro_michael_shipflow.models.entities.PackageEvent
import com.pucetec.alejandro_michael_shipflow.models.entities.ShippingPackage
import com.pucetec.alejandro_michael_shipflow.models.requests.UpdatePackageStatusRequest
import com.pucetec.alejandro_michael_shipflow.models.responses.PackageEventResponse
import com.pucetec.alejandro_michael_shipflow.models.responses.PackageSummaryResponse
import org.springframework.stereotype.Component

@Component
class PackageEventMapper {

    fun toEntity(request: UpdatePackageStatusRequest, packageEntity: ShippingPackage): PackageEvent {
        return PackageEvent(
            status = request.status,
            comment = request.comment,
            packageEntity = packageEntity
        ).apply {
            createdAt = request.createdAt
            updatedAt = request.updatedAt
        }
    }

    fun toResponse(event: PackageEvent): PackageEventResponse {
        return PackageEventResponse(
            id = event.id,
            createdAt = event.createdAt,
            updatedAt = event.updatedAt,
            status = event.status,
            comment = event.comment,
            `package` = PackageSummaryResponse(
                id = event.packageEntity.id,
                trackingId = event.packageEntity.trackingId,
                type = event.packageEntity.type,
                cityFrom = event.packageEntity.cityFrom,
                cityTo = event.packageEntity.cityTo
            )
        )
    }
}