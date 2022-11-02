package com.saintrivers.controltower.handler

import com.saintrivers.controltower.common.exception.user.UserNotFoundException
import com.saintrivers.controltower.model.request.ActivationAttemptRequest
import com.saintrivers.controltower.model.request.AppUserProfileRequest
import com.saintrivers.controltower.model.request.AppUserRequest
import com.saintrivers.controltower.service.user.AppUserService
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.util.*

@Component
@SecurityRequirement(name = "OrderUpTowerOAuth")
class AppUserHandler(
    val appUserService: AppUserService
) {
    fun findUserById(req: ServerRequest): Mono<ServerResponse> {
        val userId = req.pathVariable("id")
        return appUserService.findUserByUserId(UUID.fromString(userId))
            .flatMap {
                ServerResponse.ok().bodyValue(it)
            }
            .onErrorResume {
                ServerResponse.badRequest().bodyValue(
                    mapOf("message" to it.localizedMessage)
                )
            }
    }

    fun getUserByUserName(req: ServerRequest): Mono<ServerResponse> {
        val userName = req.pathVariable("username")
        return appUserService.findUserByUsername(userName)
            .flatMap {
                ServerResponse.ok().bodyValue(it)
            }
            .onErrorResume {
                ServerResponse.badRequest().bodyValue(
                    mapOf("message" to it.localizedMessage)
                )
            }
    }

    fun registerUser(req: ServerRequest): Mono<ServerResponse> {
        return req.bodyToMono(AppUserRequest::class.java)
            .flatMap {
                appUserService.registerUser(it)
            }
            .flatMap {
                ServerResponse.ok().bodyValue(it)
            }
            .onErrorResume {
                ServerResponse.badRequest().bodyValue(mapOf("message" to it.localizedMessage))
            }
    }

    fun updateUserProfile(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono(AppUserProfileRequest::class.java)
            .flatMap {
                val userId = req.pathVariable("id")
                println("updateCon : "+ it)
                appUserService.updateUserProfile(userId, it)
            }
            .flatMap {
                ServerResponse.ok().bodyValue(it)
            }
            .onErrorResume {
                ServerResponse.badRequest().bodyValue(mapOf("message" to it.localizedMessage))
            }

    fun updateUserProfileImage(req: ServerRequest): Mono<ServerResponse> {
        val userId = req.pathVariable("userId")
        val imageLink = req.queryParam("imageLink")

        return appUserService.updateUserProfileImage(userId, imageLink.get())
            .flatMap {
                ServerResponse.ok().bodyValue(it)
            }
            .onErrorResume {
                ServerResponse.badRequest().bodyValue(mapOf("message" to it.localizedMessage))
            }
    }
    fun deleteUser(req: ServerRequest): Mono<ServerResponse> =
        appUserService.deleteUser(req.pathVariable("id"))
            .flatMap {
                ServerResponse.accepted().build()
            }
            .switchIfEmpty(Mono.error(UserNotFoundException()))
            .onErrorResume {
                ServerResponse.badRequest().bodyValue(mapOf("message" to it.localizedMessage))
            }

    fun insertActivationAttempt(req: ServerRequest): Mono<ServerResponse> =

        req.bodyToMono(ActivationAttemptRequest::class.java)
            .flatMap {
                appUserService.insertActivationAttempt(it)
            }
            .flatMap {
                ServerResponse.ok().bodyValue(it)
            }
            .onErrorResume {
                ServerResponse.badRequest().bodyValue(mapOf("message" to it.localizedMessage))
            }
    fun getUserByEmail(req: ServerRequest): Mono<ServerResponse> {
        val email = req.queryParam("email")
        return appUserService.findUserByEmail(email.get())
            .flatMap {
                ServerResponse.ok().bodyValue(it)
            }
            .onErrorResume {
                ServerResponse.badRequest().bodyValue(
                    mapOf("message" to it.localizedMessage)
                )
            }
    }
    fun verifyUserByCode(req: ServerRequest): Mono<ServerResponse>{
        val code = req.queryParam("code")
        val id = req.queryParam("id")
        return appUserService.matchVerificationCode(code.get())
            .flatMap {
                if(it){
//                    var user = appUserService.findById(id.get())
                    appUserService.updateUserActivated(id.get())
                        .flatMap{ServerResponse.ok().bodyValue(it)}
                }
                else{
                    ServerResponse.badRequest().bodyValue("message: Invalid verification code")
                }


            }
            .onErrorResume { ServerResponse.badRequest().bodyValue(
                mapOf("message" to it.localizedMessage))
            }
    }

    fun checkIfUserIsInAGroup(req: ServerRequest): Mono<ServerResponse>{
        val userId = req.queryParam("userId").get()
        val groupId = req.queryParam("groupId").get()
        return appUserService.checkIfUserIsInAGroup(UUID.fromString(userId),UUID.fromString(groupId))
            .flatMap {
                ServerResponse.ok().bodyValue(it)
            }
            .onErrorResume {
                ServerResponse.badRequest().bodyValue(
                    mapOf("message" to it.localizedMessage)
                )
            }
    }
}