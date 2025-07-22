package com.pucetec.alejandro_michael_shipflow.controllers

import com.pucetec.alejandro_michael_shipflow.models.requests.PackageRequest
import com.pucetec.alejandro_michael_shipflow.models.responses.PackageResponse
import com.pucetec.alejandro_michael_shipflow.services.PackageService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/packages")
open class PackageController(
    private val service: PackageService
) {
    @PostMapping
    open fun createPackage(
        @RequestBody request: PackageRequest
    ): PackageResponse {
        return service.save(request)
    }
}