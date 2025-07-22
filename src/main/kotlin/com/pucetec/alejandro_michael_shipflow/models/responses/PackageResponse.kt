package com.pucetec.alejandro_michael_shipflow.models.responses

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class PackageResponse(
    var id: Long = 0,
    @JsonProperty(value = "created_at")
    var createdAt: LocalDateTime = LocalDateTime.now(),
    @JsonProperty(value = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    val type: String,
    val weight: Float,
    val description: String,
    @JsonProperty(value = "city_from")
    val cityFrom: String,
    @JsonProperty(value = "city_to")
    val cityTo: String,

    val events: List<PackageEventResponse>,
)