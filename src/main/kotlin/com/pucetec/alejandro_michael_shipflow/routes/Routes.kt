package com.pucetec.alejandro_michael_shipflow.routes

object Routes {
    const val BASE_URL = "/api"

    const val PACKAGES = "$BASE_URL/packages"
    const val PACKAGE_BY_TRACKING = "$PACKAGES/{trackingId}"
    const val PACKAGE_STATUS_UPDATE = "$PACKAGES/{trackingId}/status"
    const val PACKAGE_EVENTS = "$PACKAGES/{trackingId}/events"
}
