package com.saintrivers.controltower.common.model.dto

import java.util.UUID


data class AppUserDto(
    val authId: UUID? = null,
    val username: String,
    val name: String? = null,
    val email: String? = null,
    val profileImage: String? = null,
    val gender: String? = null,
    val phoneNumber: String? = null,
    val role: String? = null,
    val isActivated: Boolean?


)

