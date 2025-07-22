package com.pucetec.alejandro_michael_shipflow.repositories

import com.pucetec.alejandro_michael_shipflow.models.entities.ShippingPackage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PackageRepository: JpaRepository<ShippingPackage, Long>