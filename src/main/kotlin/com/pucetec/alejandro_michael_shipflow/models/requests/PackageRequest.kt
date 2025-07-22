package com.pucetec.alejandro_michael_shipflow.models.requests

import com.fasterxml.jackson.annotation.JsonProperty
import com.pucetec.alejandro_michael_shipflow.models.entities.PackageEvent
import java.time.LocalDateTime

data class PackageRequest(
    var id: Long = 0,
    @JsonProperty(value = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @JsonProperty(value = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    val type: String,
    val weight: Float,
    val description: String,
    @JsonProperty(value = "city_from")
    val cityFrom: String,
    @JsonProperty(value = "city_to")
    val cityTo: String,

    val events: List<PackageEventRequest>
)