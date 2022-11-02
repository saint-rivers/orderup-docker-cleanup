package com.saintrivers.controltower.service.user

import com.saintrivers.controltower.common.exception.user.*
import com.saintrivers.controltower.common.model.UserProfileRequest
import com.saintrivers.controltower.common.model.UserRequest
import com.saintrivers.controltower.model.dto.ActivationAttemptDto
import com.saintrivers.controltower.model.dto.AppUserDto
import com.saintrivers.controltower.model.entity.AppUser
import com.saintrivers.controltower.model.request.ActivationAttemptRequest
import com.saintrivers.controltower.model.request.AppUserProfileRequest
import com.saintrivers.controltower.model.request.AppUserRequest
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono
import java.util.*

@Service
class AppUserServiceImpl(
    val appUserRepository: AppUserRepository,
    @Qualifier("KeycloakClient") val keycloakClient: WebClient,
) : AppUserService {

    fun getAuthenticationPrincipal(): Mono<Jwt> =
        ReactiveSecurityContextHolder.getContext()
            .map { it.authentication.principal }
            .cast(Jwt::class.java)

    override fun registerUser(req: AppUserRequest): Mono<AppUserDto> {
        println("user request: "+ req)
        val created: Mono<AppUserDto> =
            // throw error if email exists
            emailExists(req.email)
                .flatMap { exists ->
                    if (exists) return@flatMap Mono.error(UserAlreadyExistsException())
                    else {
                        // if not exists, unwrap access_token:
//                        return@flatMap getAuthenticationPrincipal()
//                            .map {
//                                it.tokenValue
//                            }
//                            // send user creating request to admin client
//                            .flatMap {
//                                val accessToken = it
//                                println("toRequest : "+req.toUserRequest())
//                                keycloakClient.post()
//                                    .uri("/api/user")
//                                    .header("Authorization", "Bearer $accessToken")
//                                    .body(Mono.just(req.toUserRequest()), UserRequest::class.java)
//                                    .retrieve()
//                                    .bodyToMono(AppUserDto::class.java)
//
//                            }
//                            .doOnError { ex -> println("error = "+ ex) }
//                    }
                        keycloakClient.post()
                            .uri("/api/user")
                            .body(Mono.just(req.toUserRequest()), UserRequest::class.java)
                            .retrieve()
                            .bodyToMono(AppUserDto::class.java)
                    }
                }


        // map to an entity
        val userEntity = req.toEntity()
        println("In user service: " )
        // save to local database
        return created
            .flatMap { user ->
            userEntity.authId = user.authId
            userEntity.isActivated = false
            println("userAuthId = "+user.authId)
            appUserRepository
                .save(userEntity)
                .map { it.toDto() }
                .doOnError{ ex -> println("error save :"+ex)}
        }
            .flatMap{
                keycloakClient.get()
                    .uri("/api/user/email/a/"+it.email)
                    .retrieve()
                    .bodyToMono(com.saintrivers.controltower.common.model.User::class.java)
        }
            .flatMap{
                it ->
                appUserRepository.updateUserByEmail(it.email, it.id)
                    .map{it.toDto()}
            }


    }
//    .flatMap{
//        keycloakClient.get()
//            .uri("/api/user/email/a/"+it.email)
//            .retrieve()
//            .bodyToMono(com.saintrivers.controltower.common.model.AppUser::class.java)
//    }

    private fun emailExists(email: String): Mono<Boolean> =
        appUserRepository.findByEmail(email)

    override fun findUserByUserId(id: UUID): Mono<AppUserDto> {
        return appUserRepository.findByAuthId(id)
            .map { it.toDto() }
            .switchIfEmpty(Mono.error(UserNotFoundException()))
    }

    override fun deleteUser(id: String): Mono<Void> =
        checkResourceOwnerThenReturn<Void>(id) {
            getAuthenticationPrincipal()
                .map { jwt ->
                    if (!jwt.claims.containsKey("email")) throw NotLoggedInException()
                    jwt.tokenValue
                }
                .onErrorResume {
                    Mono.error(it)
                }
                .flatMap { token ->
                    keycloakClient.delete()
                        .uri("/api/user/{id}", id)
                        .header("Authorization", "Bearer $token")
                        .retrieve()
                        .bodyToMono(UUID::class.java)
                }
                .flatMap {
                    appUserRepository.findByAuthId(it)
                }
                .flatMap {
                    if (it.isActivated == true) {
                        it.isActivated= false
                        appUserRepository
                            .save(it)
                            .flatMap {
                                Mono.empty<Void>()
                            }
                    } else Mono.error(AccountAlreadyDisabledException())
                }
        }.then()

    override fun updateUserProfile(id: String, req: AppUserProfileRequest): Mono<AppUserDto> =

        checkResourceOwnerThenReturn<com.saintrivers.controltower.common.model.AppUser>(id) {
            keycloakClient.put()
                .uri("/api/user/update-profile/"+id)
                .body(Mono.just(req.toUserProfileRequest()), UserProfileRequest::class.java)
                .retrieve()
                .bodyToMono(com.saintrivers.controltower.common.model.User::class.java)
                .map{it ->
                    println("user : "+it.toAppUser().toDto())
                    it.toAppUser()}

            }.flatMap{
                println("AppUser : "+it.toDto())
                val entity = req.toEntity(it.profileImage, it.role)
                entity.authId = it.authId
                entity.isActivated = it.isActivated

                println("in update: "+ entity.toDto())
                appUserRepository.save(entity)
            }.map { it.toDto() }

    override fun findUserByEmail(email: String): Mono<AppUserDto> {
        val user = appUserRepository.findUserByEmail(email);
        return user.map { it->it.toDto() }
    }

    override fun insertActivationAttempt(activationRequest: ActivationAttemptRequest) : Mono<ActivationAttemptDto> {
        println("actReq: "+activationRequest)
        return appUserRepository.saveActivationAttempt(activationRequest.userId, activationRequest.otpCode, activationRequest.requestDate, activationRequest.expirationDate)
            .map{it->it.toDto()}
    }

    override fun matchVerificationCode(code: String): Mono<Boolean> {
        return appUserRepository.matchVerificationCode(code.toLong())

    }

    override fun updateUserActivated(id: String): Mono<AppUserDto> {
        return appUserRepository.updateUserActivated(UUID.fromString(id))
            .map{it->it.toDto()}
            .log("Dto = ")

    }

    override fun updateUserProfileImage(userId: String, imageLink: String): Mono<AppUserDto> {
        return appUserRepository.updateProfileImage(UUID.fromString(userId), imageLink)
            .map{it-> it.toDto()}
    }

    override fun findUserByUsername(userName: String): Mono<AppUserDto> {
        val user =  appUserRepository.findUserByUserName(userName)
        return user.map{it-> it.toDto()}
    }

    override fun checkIfUserIsInAGroup(userId: UUID?, groupId: UUID?): Mono<Boolean> {
        val result = appUserRepository.checkIfUserIsInAGroup(userId, groupId)
        return result.map{it}
    }



    private fun <R> checkResourceOwnerThenReturn(id: String, afterValidation: (AppUser) -> Mono<out R>): Mono<R> =
        appUserRepository.findByAuthId(UUID.fromString(id)).zipWith(getAuthenticationPrincipal())
            .switchIfEmpty(Mono.error(NotLoggedInException()))
            .flatMap {
                println("authIdDB = "+it.t1.authId+"; auhtIdAuth = "+it.t2.claims["sub"].toString())
                if (it.t1.authId.toString() == it.t2.claims["sub"].toString())
                // is source owner
                    afterValidation(it.t1)
                else
                // is not resource owner
                    Mono.error(NotResourceOwnerException())
            }

}