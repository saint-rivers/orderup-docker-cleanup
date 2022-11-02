package com.saintrivers.controltower.common.model
import java.util.UUID

data class User(
    val id: UUID,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String
){
    fun toAppUser() = AppUser(
        authId = id,
        username = username,
        name = lastName,
        email = email,
        profileImage = null,
        gender = null,
        phoneNumber = null,
        role = null,
        isActivated = null
    )
}

