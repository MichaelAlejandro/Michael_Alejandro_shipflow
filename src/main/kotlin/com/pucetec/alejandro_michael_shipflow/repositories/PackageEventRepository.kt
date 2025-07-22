package com.pucetec.alejandro_michael_shipflow.repositories

import com.pucetec.alejandro_michael_shipflow.models.entities.PackageEvent
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PackageEventRepository: JpaRepository<PackageEvent, Long>