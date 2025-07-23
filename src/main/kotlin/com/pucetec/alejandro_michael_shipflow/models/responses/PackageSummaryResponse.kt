package com.pucetec.alejandro_michael_shipflow.models.responses

import com.pucetec.alejandro_michael_shipflow.models.entities.Type

data class PackageSummaryResponse(
    val id: Long,
    val trackingId: String,
    val type: Type,
    val cityFrom: String,
    val cityTo: String
)
