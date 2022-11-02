package com.saintrivers.controltower.service.user

import com.saintrivers.controltower.model.entity.ActivationAttempt
import com.saintrivers.controltower.model.entity.AppUser
import com.saintrivers.controltower.model.request.ActivationAttemptRequest
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.util.UUID

@Repository
interface AppUserRepository : ReactiveCrudRepository<AppUser, Long> {

    @Query("select * from account_users where id = :id")
    fun findByAuthId(id: UUID): Mono<AppUser>

    @Query("select id from account_users where id = :authId")
    fun findIdByAuthId(authId: UUID): Mono<UUID>

    @Query("select exists (select 1 from user_db.public.account_users where email = :email)")
    fun findByEmail(email: String): Mono<Boolean>

    @Query("delete from account_users where id = :authId")
    fun deleteByAuthId(authId: UUID): Mono<Void>

    @Query("select * from account_users where username = :email")
    fun findUserByEmail(email: String): Mono<AppUser>

    @Query(value = "insert into ou_activation_attempts (user_id, otp_code, request_date, expiration_date) values (:userId, :otpCode, :requestDate, :expirationDate)")
    fun saveActivationAttempt(userId:UUID, otpCode:Long, requestDate:LocalDateTime, expirationDate:LocalDateTime): Mono<ActivationAttempt>

    //and expiration_date > now()
    @Query("select exists (select 1 from user_db.public.ou_activation_attempts where otp_code = :code )")
    fun matchVerificationCode(code: Long): Mono<Boolean>



    @Query(value = "update account_users set is_activated = true where id = :authId returning *")
    fun updateUserActivated(authId: UUID): Mono<AppUser>
    @Query(value = "insert into account_users (id, username, name, email, profile_image, gender, phone_number, role, is_activated) values (id, username, name, email, profile_image, gender, phone_number, role, is_activated)")
    fun insertNewUser(id: UUID, username: String, name: String, email: String, profile_image: String, gender: String, phone_number: String, role: String, is_activated: Boolean): Mono<AppUser>

    @Query(value = "update account_users set id = :id where email = :email returning *")
    fun updateUserByEmail(email: String, id: UUID): Mono<AppUser>

    @Query(value="update account_users set profile_image = :imageLink where id = :userId returning *")
    fun updateProfileImage(userId: UUID, imageLink: String): Mono<AppUser>

    @Query("select * from account_users where username = :username")
    fun findUserByUserName(username: String): Mono<AppUser>
    @Query(value="select exists (select 1 from user_groups ug where user_id = :userId and group_id = :groupId)")
    fun checkIfUserIsInAGroup(userId: UUID?, groupId: UUID?): Mono<Boolean>

    @Query(value="select exists (select 1 from user_groups where user_id = :userId and group_id = :groupId and is_owner = true)")
    fun checkIfUserIsAdminOfGroup(userId: UUID?, groupId: UUID): Mono<Boolean>
}