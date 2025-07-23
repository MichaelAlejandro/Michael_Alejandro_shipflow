package com.pucetec.alejandro_michael_shipflow.exceptions.handlers

import com.pucetec.alejandro_michael_shipflow.exceptions.exceptions.DescriptionTooLongException
import com.pucetec.alejandro_michael_shipflow.exceptions.exceptions.InvalidStatusTransitionException
import com.pucetec.alejandro_michael_shipflow.exceptions.exceptions.ResourceNotFoundException
import com.pucetec.alejandro_michael_shipflow.exceptions.exceptions.SameCityException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleNotFound(ex: ResourceNotFoundException): ResponseEntity<Map<String, String>> =
        ResponseEntity(mapOf("error" to ex.message.orEmpty()), HttpStatus.NOT_FOUND)

    @ExceptionHandler(SameCityException::class)
    fun handleSameCity(ex: SameCityException): ResponseEntity<Map<String, String>> =
        ResponseEntity(mapOf("error" to ex.message.orEmpty()), HttpStatus.BAD_REQUEST)

    @ExceptionHandler(InvalidStatusTransitionException::class)
    fun handleInvalidTransition(ex: InvalidStatusTransitionException): ResponseEntity<Map<String, String>> =
        ResponseEntity(mapOf("error" to ex.message.orEmpty()), HttpStatus.BAD_REQUEST)

    @ExceptionHandler(DescriptionTooLongException::class)
    fun handleDescriptionTooLong(ex: DescriptionTooLongException): ResponseEntity<Map<String, String>> =
        ResponseEntity(mapOf("error" to ex.message.orEmpty()), HttpStatus.BAD_REQUEST)

    @ExceptionHandler(Exception::class)
    fun handleGeneric(ex: Exception): ResponseEntity<Map<String, String>> =
        ResponseEntity(mapOf("error" to "Unexpected error: ${ex.message}"), HttpStatus.INTERNAL_SERVER_ERROR)
}
