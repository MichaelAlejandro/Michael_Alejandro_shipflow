package com.pucetec.alejandro_michael_shipflow.models.entities

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.persistence.*

@Entity
@Table(name = "shipping_packages")
data class ShippingPackage (
    val type: String,
    val weight: Float,
    @Column(length = 50)
    val description: String,
    @Column(name = "city_from")
    val cityFrom: String,
    @Column(name = "city_to")
    val cityTo: String,

    @OneToMany(mappedBy = "event", cascade = [CascadeType.ALL])
    val events: List<PackageEvent>,
): BaseEntity()

enum class Type(name: String) {
    DOCUMENT("D"),
    SMALL_BOX("SB"),
    FRAGILE("F")
}