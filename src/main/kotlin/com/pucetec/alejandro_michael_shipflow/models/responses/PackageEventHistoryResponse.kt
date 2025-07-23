package com.pucetec.alejandro_michael_shipflow.models.responses

data class PackageEventHistoryResponse(
    val `package`: PackageSummaryResponse,
    val events: List<PackageEventResponse>
)
