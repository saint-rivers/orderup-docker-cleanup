package com.saintrivers.controltower.service.group

import com.saintrivers.controltower.model.dto.AppUserDto
import com.saintrivers.controltower.model.dto.GroupDto
import com.saintrivers.controltower.model.request.GroupRequest
import com.saintrivers.controltower.model.request.MemberRequest
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigInteger
import java.util.UUID

interface GroupService {

    fun create(groupRequest: GroupRequest): Mono<GroupDto>
    fun addMember(memberRequest: MemberRequest, requesterId: UUID): Mono<UUID>
    fun getMembersByGroupId(groupId: UUID): Flux<AppUserDto>
    fun findGroups(): Flux<GroupDto>
    fun findGroup(groupId: UUID): Mono<GroupDto>
    fun updateUserGroup(groupId: UUID, groupRequest: GroupRequest?): Mono<GroupDto>
    fun getGroupsByUserId(userId: UUID?): Flux<GroupDto>
    fun findGroupsRecentCreated(): Flux<GroupDto>
    fun deleteGroupByGroupId(groupId: UUID?): Mono<UUID>
    fun removeMemberFromGroup(userId: UUID?, groupId: UUID?): Mono<String>
    fun countMemberInGroup(groupId: UUID?): Mono<BigInteger>
}