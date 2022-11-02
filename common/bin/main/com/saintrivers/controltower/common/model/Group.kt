package com.saintrivers.controltower.common.model

import com.saintrivers.controltower.common.model.dto.GroupDto
import java.time.LocalDateTime
import java.util.UUID

data class Group(
    val id:UUID,
    val name: String,
    val image: String,
    val createdDate: LocalDateTime
){
    fun toDto() = GroupDto(
        id = id!!,
        name = name!!,
        image = image!!,
        createdDate = createdDate!!
    )
}