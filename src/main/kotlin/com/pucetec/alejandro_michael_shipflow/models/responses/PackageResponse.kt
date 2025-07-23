package com.pucetec.alejandro_michael_shipflow.models.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.pucetec.alejandro_michael_shipflow.models.entities.Status
import com.pucetec.alejandro_michael_shipflow.models.entities.Type
import java.time.LocalDateTime

data class PackageResponse(
    val id: Long,

    @JsonProperty("created_at")
    val createdAt: LocalDateTime,

    @JsonProperty("updated_at")
    val updatedAt: LocalDateTime,

    val trackingId: String,
    val type: Type,
    val weight: Float,
    val description: String,

    @JsonProperty("city_from")
    val cityFrom: String,

    @JsonProperty("city_to")
    val cityTo: String,

    val estimatedDeliveryDate: LocalDateTime,

    val currentStatus: Status,
    val events: List<PackageEventResponse>
)