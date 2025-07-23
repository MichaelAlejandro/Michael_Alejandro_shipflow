package com.pucetec.alejandro_michael_shipflow.controllers

import com.pucetec.alejandro_michael_shipflow.models.requests.CreatePackageRequest
import com.pucetec.alejandro_michael_shipflow.models.requests.UpdatePackageStatusRequest
import com.pucetec.alejandro_michael_shipflow.models.responses.PackageEventHistoryResponse
import com.pucetec.alejandro_michael_shipflow.models.responses.PackageEventResponse
import com.pucetec.alejandro_michael_shipflow.models.responses.PackageResponse
import com.pucetec.alejandro_michael_shipflow.services.PackageService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/packages")
class PackageController(
    private val packageService: PackageService
) {

    @PostMapping
    fun createPackage(@RequestBody request: CreatePackageRequest): ResponseEntity<PackageResponse> {
        val saved = packageService.create(request)
        return ResponseEntity.ok(saved)
    }

    @GetMapping
    fun getAllPackages(): ResponseEntity<List<PackageResponse>> {
        return ResponseEntity.ok(packageService.getAll())
    }

    @GetMapping("/{trackingId}")
    fun getPackageByTrackingId(@PathVariable trackingId: String): ResponseEntity<PackageResponse> {
        return ResponseEntity.ok(packageService.getByTrackingId(trackingId))
    }

    @PutMapping("/{trackingId}/status")
    fun updatePackageStatus(
        @PathVariable trackingId: String,
        @RequestBody request: UpdatePackageStatusRequest
    ): ResponseEntity<PackageEventResponse> {
        return ResponseEntity.ok(packageService.updateStatus(trackingId, request))
    }

    @GetMapping("/{trackingId}/events")
    fun getPackageEventHistory(@PathVariable trackingId: String): ResponseEntity<PackageEventHistoryResponse> {
        return ResponseEntity.ok(packageService.getEventHistory(trackingId))
    }
}
