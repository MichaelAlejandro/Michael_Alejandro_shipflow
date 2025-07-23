package com.pucetec.alejandro_michael_shipflow.models.requests

import com.fasterxml.jackson.annotation.JsonProperty
import com.pucetec.alejandro_michael_shipflow.models.entities.Type
import java.time.LocalDateTime

data class CreatePackageRequest(
    var id: Long = 0,

    val type: Type,
    val weight: Float,
    val description: String,

    @JsonProperty("city_from")
    val cityFrom: String,

    @JsonProperty("city_to")
    val cityTo: String,

    @JsonProperty("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @JsonProperty("updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now()
)