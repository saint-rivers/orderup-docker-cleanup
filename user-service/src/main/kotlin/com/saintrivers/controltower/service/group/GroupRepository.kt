package com.saintrivers.controltower.service.group

import com.saintrivers.controltower.model.entity.AppUser
import com.saintrivers.controltower.model.entity.Group
import com.saintrivers.controltower.model.request.GroupRequest
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigInteger
import java.util.UUID

interface GroupRepository : ReactiveCrudRepository<Group, UUID> {

    @Query("insert into user_groups(group_id, user_id, is_owner) values (:groupId, :userId, :isOwner) returning group_id")
    fun addMember(groupId: UUID, userId: UUID, isOwner: Boolean): Mono<UUID>

//    @Query("select exists (select 1 from group_members where user_id = :userId and group_id = :groupId)")
//    fun memberExistsInGroup(groupId: UUID, userId: Long): Mono<Boolean>

    @Query("select au.* from user_groups gm inner join account_users au on au.id = gm.user_id where gm.group_id = :groupId")
    fun findAllByGroupId(groupId: UUID): Flux<AppUser>

    @Query("select count(*) from user_db.public.user_groups where group_id = :groupId and user_id = :userId")
    fun findRecord(groupId: UUID, userId: UUID): Mono<Long>

    @Query(
        "select g.* from user_groups gm " +
                "inner join groups g " +
                "on g.id = gm.group_id " +
                "where gm.user_id = :userId"
    )
    fun findAllGroupsByMemberId(userId: UUID): Flux<Group>

    @Query(value="update groups set name = :name, group_image = :image, description = :description, created_date = now() where id = :groupId returning *")
    fun updateUserGroup(groupId: UUID, name: String, image: String, description: String): Mono<Group>

    @Query(value="select *  from groups  order by created_date desc limit(4) ")
    fun findGroupsRecentCreated(): Flux<Group>

    @Query(value="delete from groups where id = :groupId returning id")
    fun deleteGroupByGroupId(groupId: UUID): Mono<UUID>

    @Query(value="delete  from user_groups ug using account_users au where user_id = :userId and group_id = :groupId returning au.username")
    fun removeMemberFromGroup(userId: UUID, groupId: UUID?): Mono<String>
    @Query(value="select count(*) from user_groups where group_id = :groupId")
    fun countMemberInGroup(groupId: UUID?): Mono<BigInteger>


}