package com.pucetec.alejandro_michael_shipflow.models.entities

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "package_events")
data class PackageEvent(
    @Enumerated(EnumType.STRING)
    val status: Status,

    val comment: String? = null,

    @ManyToOne
    @JoinColumn(name = "package_id", nullable = false)
    val packageEntity: ShippingPackage
) : BaseEntity()

enum class Status(val label: String) {
    PENDING("PENDING"),
    IN_TRANSIT("IN_TRANSIT"),
    DELIVERED("DELIVERED"),
    ON_HOLD("ON_HOLD"),
    CANCELLED("CANCELLED")
}