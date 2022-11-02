package com.saintrivers.controltower.common.model

import com.saintrivers.controltower.common.model.dto.AppUserDto
import java.util.*

data class AppUser(
    val authId: UUID? = null,
    val username: String,
    val name: String? = null,
    val email: String? = null,
    val profileImage: String? = null,
    val gender: String? = null,
    val phoneNumber: String? = null,
    val role: String? = null,
    val isActivated: Boolean?

){
    fun toDto() = AppUserDto(
        authId = authId,
        username = username,
        name = name,
        email = email,
        profileImage = profileImage,
        gender = gender,
        phoneNumber = phoneNumber,
        role = role,
        isActivated = isActivated
    )
}