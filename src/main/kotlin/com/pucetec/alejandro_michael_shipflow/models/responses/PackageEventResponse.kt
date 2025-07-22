package com.pucetec.alejandro_michael_shipflow.models.responses

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class PackageEventResponse (
    var id: Long = 0,
    @JsonProperty("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @JsonProperty("updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    val status: String,
)