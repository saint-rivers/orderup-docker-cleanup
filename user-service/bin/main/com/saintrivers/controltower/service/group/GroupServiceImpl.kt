package com.saintrivers.controltower.service.group

import com.saintrivers.controltower.common.exception.user.*
import com.saintrivers.controltower.model.dto.AppUserDto
import com.saintrivers.controltower.model.dto.GroupDto
import com.saintrivers.controltower.model.request.GroupRequest
import com.saintrivers.controltower.model.request.MemberRequest
import com.saintrivers.controltower.service.user.AppUserRepository
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigInteger
import java.time.LocalDateTime
import java.util.*

@Service
class GroupServiceImpl(val groupRepository: GroupRepository, val appUserRepository: AppUserRepository) : GroupService {

    fun Jwt.getSub(): UUID = UUID.fromString(this.claims["sub"].toString())

    private fun getAuthenticationPrincipal(): Mono<Jwt> =
        ReactiveSecurityContextHolder.getContext()
            .map { it.authentication.principal }
            .cast(Jwt::class.java)

    override fun create(groupRequest: GroupRequest): Mono<GroupDto> {
        val entity = groupRequest.toEntity()
        entity.createdDate = LocalDateTime.now()

        return groupRepository.save(entity)
            .map{it->it.toDto()}
    }

    override fun addMember(memberRequest: MemberRequest, requesterId: UUID): Mono<UUID> =
        appUserRepository
            // check if provided IDs exist
            .findByAuthId(memberRequest.userId)
            .switchIfEmpty(Mono.error(UserNotFoundException()))
            .flatMap { groupRepository.findById(memberRequest.groupId) }
            .switchIfEmpty(Mono.error(GroupNotFoundException()))
            .flatMap { appUserRepository.findIdByAuthId(requesterId) }
            .switchIfEmpty(Mono.error(UserNotFoundException()))

            // check if the record already exists
            .flatMap { groupRepository.findRecord(groupId = memberRequest.groupId, userId = memberRequest.userId) }
            .map {
                println("Member Count : "+it)
               if (it > 0) throw MemberAlreadyAddedException()
                else Unit
            }
            .onErrorResume {
                Mono.error(MemberAlreadyAddedException())
            }

            // insert record after the previous checks
            .then(appUserRepository.findIdByAuthId(memberRequest.userId))
            .zipWith(appUserRepository.findIdByAuthId(requesterId))
            .flatMap {
                val member = it.t1
//                val addedBy = it.t2
                groupRepository.addMember(
                    groupId = memberRequest.groupId,
                    userId = member,
                    isOwner = memberRequest.admin
                )
            }
            .doOnError {
                println(it.localizedMessage)
            }
//            .onErrorResume {
//                Mono.error(RuntimeException("unable to add member to group"))
//            }
//

    override fun getMembersByGroupId(groupId: UUID): Flux<AppUserDto> =
        groupRepository.findById(groupId)
            .switchIfEmpty(Mono.error(GroupNotFoundException()))
            .flatMapMany {
                groupRepository.findAllByGroupId(groupId)
            }
            .map { it.toDto() }

    override fun findGroups(): Flux<GroupDto> {

        return groupRepository.findAll()
            .map{it.toDto()}
    }

    override fun findGroup(groupId: UUID): Mono<GroupDto> =
        groupRepository.findById(groupId)
            .map { it.toDto() }
            .switchIfEmpty(Mono.error(GroupNotFoundException()))

    override fun updateUserGroup(groupId: UUID,groupRequest: GroupRequest?): Mono<GroupDto> {
        if (groupRequest != null) {
            return groupRepository.updateUserGroup(groupId, groupRequest.name, groupRequest.image, groupRequest.description)
                .map{it ->
                    println("group = "+it.toDto())
                    it.toDto()}
                .switchIfEmpty(Mono.error(GroupNotFoundException()))
        }
        return Mono.error(GroupRequestIsNull())
    }

    override fun getGroupsByUserId(userId: UUID?): Flux<GroupDto> {
        return appUserRepository.findByAuthId(userId!!)
            .switchIfEmpty(Mono.error(UserNotFoundException()))
            .flatMapMany{
                groupRepository.findAllGroupsByMemberId(userId)
            }
            .map{
                it->it.toDto()
            }

    }

    override fun findGroupsRecentCreated(): Flux<GroupDto> {
        return groupRepository.findGroupsRecentCreated()
            .map{
                it->it.toDto()
            }
    }

    override fun deleteGroupByGroupId(groupId: UUID?): Mono<UUID> {
        return groupRepository.deleteGroupByGroupId(groupId!!)
            .map{it}
    }

    override fun removeMemberFromGroup(userId: UUID?, groupId: UUID?): Mono<String> {

        return getAuthenticationPrincipal()
            .flatMap {
                jwt ->
                println("jwt = "+jwt.claims["sub"].toString())
                if (!jwt.claims.containsKey("email")) throw NotLoggedInException()

                Mono.just(jwt.claims["sub"].toString())
            }
            .flatMap{
                appUserRepository.checkIfUserIsAdminOfGroup(UUID.fromString(it), groupId!!)
            }
            .switchIfEmpty(Mono.error(UserIsNotAdmin()))
            .flatMap{
                appUserRepository.findByAuthId(userId!!)
            }
            .switchIfEmpty(Mono.error(UserNotFoundException()))

            .flatMap{
                groupRepository.findById(groupId!!)

            }
            .switchIfEmpty(Mono.error(GroupNotFoundException()))
            .flatMap{
                groupRepository.removeMemberFromGroup(userId!!, groupId!!)
            }
    }

    override fun countMemberInGroup(groupId: UUID?): Mono<BigInteger> {
        return groupRepository.countMemberInGroup(groupId)
    }

}