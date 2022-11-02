package com.saintrivers.controltower.model.request

import com.saintrivers.controltower.model.entity.ActivationAttempt
import java.time.LocalDateTime
import java.util.*

data class ActivationAttemptRequest (
    val userId: UUID,
    val otpCode: Long,
    val requestDate: LocalDateTime,
    val expirationDate:LocalDateTime
    ) {
    fun toEntity() = ActivationAttempt(
        userId = userId,
        otpCode = otpCode,
        requestDate = requestDate,
        expirationDate = expirationDate
    )
}