package com.saintrivers.controltower.model.request

import com.saintrivers.controltower.model.entity.Group
import java.util.*

data class GroupRequest(
    val name: String,
    val image: String,
    val description: String
) {
    fun toEntity() = Group(
        name = name,
        image = image,
        description = description
    )
}
