package com.pucetec.alejandro_michael_shipflow.services

import com.pucetec.alejandro_michael_shipflow.mappers.PackageMapper
import com.pucetec.alejandro_michael_shipflow.models.requests.PackageRequest
import com.pucetec.alejandro_michael_shipflow.models.responses.PackageResponse
import com.pucetec.alejandro_michael_shipflow.repositories.PackageRepository
import org.springframework.stereotype.Service

@Service
class PackageService(
    private val packageRepository: PackageRepository,
    private val packageMapper: PackageMapper
) {

    fun save(request: PackageRequest): PackageResponse {
        val savedEntity = packageRepository.save(packageMapper.toEntity(request))
        return packageMapper.toResponse(savedEntity)
    }
}