package com.pucetec.alejandro_michael_shipflow.models.entities

import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.*

@Entity
@Table(name = "package_events")
data class PackageEvent (
    val status: String,

    @ManyToOne
    @JoinColumn(name = "package_id", nullable = false)
    val event: ShippingPackage? = null,
): BaseEntity()

enum class Status(val statusName: String){
    PENDING("PENDING"),
    IN_TRANSIT("IN_TRANSIT"),
    DELIVERED("DELIVERED"),
    ON_HOLD("ON_HOLD"),
    CANCELED("CANCELED")
}
