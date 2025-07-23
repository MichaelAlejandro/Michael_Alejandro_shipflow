package com.pucetec.alejandro_michael_shipflow.models.requests

import com.fasterxml.jackson.annotation.JsonProperty
import com.pucetec.alejandro_michael_shipflow.models.entities.Status
import java.time.LocalDateTime

data class UpdatePackageStatusRequest(
    var id: Long = 0,

    val status: Status,
    val comment: String? = null,

    @JsonProperty("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @JsonProperty("updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now()
)