package com.saintrivers.controltower.model.entity

import com.saintrivers.controltower.model.dto.ActivationAttemptDto
import com.saintrivers.controltower.model.dto.AppUserDto
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Table("activation_attemps")
class ActivationAttempt (
    @Id
    @Column("id")
    var id: UUID? = UUID.randomUUID(),

    @Column("user_id")
    var userId: UUID? = null,

    @Column("otp_code")
    var otpCode: Long? = null,

    @Column("request_date")
    var requestDate:LocalDateTime? = null,

    @Column("expiration_date")
    var expirationDate:LocalDateTime? = null
){
    fun toDto() = ActivationAttemptDto(
        id = id,
        userId = userId,
        otpCode = otpCode,
        requestDate = requestDate,
        expirationDate = expirationDate
    )
}