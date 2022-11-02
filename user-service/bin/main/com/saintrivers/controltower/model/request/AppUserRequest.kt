package com.saintrivers.controltower.model.request


import com.saintrivers.controltower.common.model.UserProfileRequest
import com.saintrivers.controltower.common.model.UserRequest
import com.saintrivers.controltower.model.entity.AppUser
import com.saintrivers.controltower.model.entity.Role

data class AppUserRequest(

    val username: String,
    val name: String,
    val email: String,
    val profileImage: String,
    val gender: String,
    val phoneNumber: String,
    val role: String,
    val password: String,
    val isActivated: Boolean
    ) {
    fun toEntity() = AppUser(
        username = username,
        name = name,
        email = email,
        profileImage = profileImage,
        gender = gender,
        phoneNumber = phoneNumber,
        role = Role.valueOf(role),
        isActivated = isActivated
    )

    fun toUserRequest() = UserRequest(
        username = username,
        email = email,
        firstName = name,
        password = password,
        lastName = name
    )
}

data class AppUserProfileRequest(
//    val username: String,
    val email: String,
    val phoneNumber: String,
    val gender: String,
    val name: String,
) {
    fun toEntity(profileImage: String?, role: String?) = AppUser(
//        username = username,
        email = email,
        phoneNumber = phoneNumber,
        gender = gender,
        name = name,
        profileImage = profileImage,
        role = role?.let { Role.valueOf(it) }
    )

    fun toUserProfileRequest() = UserProfileRequest(
//        username = username,
        email = email,
        firstName = name,
        lastName = name,
    )
}