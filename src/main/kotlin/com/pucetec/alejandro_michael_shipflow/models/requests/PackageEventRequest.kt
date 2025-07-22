package com.pucetec.alejandro_michael_shipflow.models.requests

import com.fasterxml.jackson.annotation.JsonProperty
import com.pucetec.alejandro_michael_shipflow.models.entities.ShippingPackage
import java.time.LocalDateTime

data class PackageEventRequest (
    var id: Long = 0,
    @JsonProperty("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @JsonProperty("updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    val status: String,
    val event: ShippingPackage
)