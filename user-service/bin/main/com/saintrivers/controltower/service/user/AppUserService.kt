package com.saintrivers.controltower.service.user

import com.saintrivers.controltower.model.dto.ActivationAttemptDto
import com.saintrivers.controltower.model.dto.AppUserDto
import com.saintrivers.controltower.model.request.ActivationAttemptRequest
import com.saintrivers.controltower.model.request.AppUserProfileRequest
import com.saintrivers.controltower.model.request.AppUserRequest
import reactor.core.publisher.Mono
import java.util.UUID

interface AppUserService {

    fun registerUser(req: AppUserRequest): Mono<AppUserDto>
    fun findUserByUserId(id: UUID): Mono<AppUserDto>
    fun deleteUser(id: String): Mono<Void>
    fun updateUserProfile(id: String, req: AppUserProfileRequest): Mono<AppUserDto>
    fun findUserByEmail(email: String): Mono<AppUserDto>
    fun insertActivationAttempt(activationRequest: ActivationAttemptRequest): Mono<ActivationAttemptDto>
    fun matchVerificationCode(code: String): Mono<Boolean>
    fun updateUserActivated(id: String):Mono<AppUserDto>
    fun updateUserProfileImage(userId: String, get: String): Mono<AppUserDto>
    fun findUserByUsername(username: String): Mono<AppUserDto>
    fun checkIfUserIsInAGroup(userId: UUID?, groupId: UUID?): Mono<Boolean>

}