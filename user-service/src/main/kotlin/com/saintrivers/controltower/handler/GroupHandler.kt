package com.saintrivers.controltower.handler

import com.saintrivers.controltower.common.exception.user.GroupNotFoundException
import com.saintrivers.controltower.common.exception.user.NotLoggedInException
import com.saintrivers.controltower.common.exception.user.UserNotFoundException
import com.saintrivers.controltower.model.dto.GroupDto
import com.saintrivers.controltower.model.entity.Group
import com.saintrivers.controltower.model.request.GroupRequest
import com.saintrivers.controltower.model.request.MemberRequest
import com.saintrivers.controltower.service.group.GroupService
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.util.*
import java.util.stream.Collectors

@Component
@SecurityRequirement(name = "OrderUpTowerOAuth")
class GroupHandler(
    val groupService: GroupService
) {
    private fun getAuthenticationPrincipal(): Mono<Jwt> =
        ReactiveSecurityContextHolder.getContext()
            .map { it.authentication.principal }
            .cast(Jwt::class.java)
            .log()

    fun findAllGroupMembers(req: ServerRequest): Mono<ServerResponse> =
        groupService.getMembersByGroupId(UUID.fromString(req.pathVariable("id")))
            .collect(Collectors.toList())
            .flatMap {
                ServerResponse.ok().bodyValue(it)
            }
            .onErrorResume {
                ServerResponse.badRequest().bodyValue(
                    mapOf("message" to it.localizedMessage)
                )
            }

    fun findGroups(req: ServerRequest): Mono<ServerResponse> =
        ServerResponse.ok().body(
            groupService.findGroups(),
            GroupDto::class.java
        )

    fun findGroup(req: ServerRequest): Mono<ServerResponse> =
        groupService.findGroup(UUID.fromString(req.pathVariable("id")))
            .flatMap {
                ServerResponse.ok().bodyValue(it)
            }.onErrorResume {
                ServerResponse.badRequest().bodyValue(
                    mapOf("message" to it.localizedMessage)
                )
            }

    fun findGroupsByUserId(req: ServerRequest): Mono<ServerResponse> =
        groupService.getGroupsByUserId(UUID.fromString(req.pathVariable("userId")))
            .collect(Collectors.toList())
            .flatMap {
                ServerResponse.ok().bodyValue(it)
            }
            .onErrorResume {
                ServerResponse.badRequest().bodyValue(
                    mapOf("message" to it.localizedMessage)
                )
            }

    fun createGroup(req: ServerRequest): Mono<ServerResponse> {
//        val userId = UUID.fromString(req.queryParam("userId").get())
        return req.bodyToMono(GroupRequest::class.java)
            .flatMap {
                groupService.create(it)
            }
            .zipWith(getAuthenticationPrincipal())
            .flatMap {
                //add user as admin of the group created
                val sub = it.t2.claims["sub"].toString()
                val userId: UUID?

                if (sub == "") return@flatMap Mono.error(NotLoggedInException())
                else userId = UUID.fromString(sub)
                val memberRequest = MemberRequest(it.t1.id, userId, true)
                groupService.addMember(memberRequest, userId)
                    .zipWith(Mono.just(it.t1))
                    .flatMap {

                        return@flatMap ServerResponse.ok().bodyValue(it.t2)
                    }
            }

    }
    fun addGroupMember(req: ServerRequest): Mono<ServerResponse> {
//        val requesterId = UUID.fromString(req.queryParam("requesterId").get())
        return req.bodyToMono(MemberRequest::class.java)
            .zipWith(getAuthenticationPrincipal())
            .flatMap {
                val memberRequest = it.t1
                val sub = it.t2.claims["sub"].toString()
                val requesterId: UUID?

                if (sub == "") return@flatMap Mono.error(NotLoggedInException())
                else requesterId = UUID.fromString(sub)
                groupService.addMember(it.t1, requesterId)
            }
            .flatMap {
                ServerResponse.ok().body(
                    Mono.just(mapOf("group" to it)), UUID::class.java
                )
            }
            .onErrorResume {
                Mono.just(it.localizedMessage)
                    .flatMap { res ->
                        ServerResponse.badRequest().body(
                            Mono.just(mapOf("message" to res)), Throwable::class.java
                        )
                    }
            }
    }

    fun updateUserGroup(req: ServerRequest): Mono<ServerResponse> {
        val  groupId = req.pathVariable("groupId");
        return req.bodyToMono(GroupRequest::class.java)
            .flatMap {groupService.updateUserGroup(UUID.fromString(groupId),it)}
            .flatMap {
                ServerResponse.ok().body(
                    Mono.just(mapOf("group" to it)), GroupDto::class.java
                )
            }
            .onErrorResume {
                Mono.just(it.localizedMessage)
                    .flatMap { res ->
                        ServerResponse.badRequest().body(
                            Mono.just(mapOf("message" to res)), Throwable::class.java
                        )
                    }
            }
    }

    fun findGroupsRecentCreated(req: ServerRequest): Mono<ServerResponse>{
        return groupService.findGroupsRecentCreated()
            .collect(Collectors.toList())
            .flatMap {
                ServerResponse.ok().body(
                    Mono.just(mapOf("group" to it)), GroupDto::class.java
                )
            }
            .onErrorResume {
                Mono.just(it.localizedMessage)
                    .flatMap { res ->
                        ServerResponse.badRequest().body(
                            Mono.just(mapOf("message" to res)), Throwable::class.java
                        )
                    }
            }
    }

    fun deleteGroupByGroupId(req: ServerRequest): Mono<ServerResponse> {
        val groupId = req.pathVariable("groupId")
        return groupService.deleteGroupByGroupId(UUID.fromString(groupId))
            .flatMap {
                ServerResponse.accepted().bodyValue("Successfully deleted "+it)
            }
            .switchIfEmpty(Mono.error(GroupNotFoundException()))
            .onErrorResume {
                ServerResponse.badRequest().bodyValue(mapOf("message" to it.localizedMessage))
            }
    }

    fun removeMemberFromGroup(req: ServerRequest): Mono<ServerResponse>{
        val groupId = req.pathVariable("groupId")
        val userId = req.pathVariable("userId")
        return groupService.removeMemberFromGroup(UUID.fromString(userId), UUID.fromString(groupId))
            .flatMap {
                ServerResponse.accepted().bodyValue("Successfully remove user: "+it+ " from group")
            }
            .switchIfEmpty(Mono.error(GroupNotFoundException()))
            .onErrorResume {
                ServerResponse.badRequest().bodyValue(mapOf("message" to it.localizedMessage))
            }
    }
    fun countMemberInGroup(req: ServerRequest): Mono<ServerResponse>{
        val groupId = req.pathVariable("groupId")
        return groupService.countMemberInGroup(UUID.fromString(groupId))
            .flatMap {
                ServerResponse.accepted().bodyValue(it)
            }
            .switchIfEmpty(Mono.error(GroupNotFoundException()))
            .onErrorResume {
                ServerResponse.badRequest().bodyValue(mapOf("message" to it.localizedMessage))
            }
    }

}

