package com.pucetec.alejandro_michael_shipflow.models.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.pucetec.alejandro_michael_shipflow.models.entities.Status
import java.time.LocalDateTime

data class PackageEventResponse(
    val id: Long,

    @JsonProperty("created_at")
    val createdAt: LocalDateTime,

    @JsonProperty("updated_at")
    val updatedAt: LocalDateTime,

    val status: Status,
    val comment: String?,

    val `package`: PackageSummaryResponse
)