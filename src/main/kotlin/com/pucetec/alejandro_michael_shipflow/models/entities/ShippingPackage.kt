package com.pucetec.alejandro_michael_shipflow.models.entities

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "shipping_packages")
data class ShippingPackage(
    @Enumerated(EnumType.STRING)
    val type: Type,

    val weight: Float,

    @Column(length = 50)
    val description: String,

    @Column(name = "city_from")
    val cityFrom: String,

    @Column(name = "city_to")
    val cityTo: String,

    @Column(name = "tracking_id", unique = true, nullable = false)
    val trackingId: String = UUID.randomUUID().toString().substring(0, 8),

    @Column(name = "estimated_delivery_date")
    val estimatedDeliveryDate: LocalDateTime = LocalDateTime.now().plusDays(5),

    @OneToMany(mappedBy = "packageEntity", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val events: List<PackageEvent> = listOf()
) : BaseEntity()

enum class Type(val label: String) {
    DOCUMENT("DOCUMENT"),
    SMALL_BOX("SMALL_BOX"),
    FRAGILE("FRAGILE")
}