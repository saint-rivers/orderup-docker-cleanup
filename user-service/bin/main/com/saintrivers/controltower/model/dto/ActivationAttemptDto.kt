package com.saintrivers.controltower.model.dto

import org.springframework.data.relational.core.mapping.Column
import java.time.LocalDateTime
import java.util.*

data class ActivationAttemptDto(

    var id: UUID?,
    var userId: UUID? = null,
    var otpCode: Long? = null,
    var requestDate: LocalDateTime? = null,
    var expirationDate: LocalDateTime? = null
)
