package com.saintrivers.controltower.common.model.dto

import java.time.LocalDateTime
import java.util.*

data class GroupDto(
    val id: UUID,
    val name: String,
    val image: String,
    val createdDate: LocalDateTime
)