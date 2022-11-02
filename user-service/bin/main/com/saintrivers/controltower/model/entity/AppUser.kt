package com.saintrivers.controltower.model.entity


import com.saintrivers.controltower.model.dto.AppUserDto
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table("account_users")
class AppUser(
    @Id
    @Column("id")
    var authId: UUID? = UUID.randomUUID(),

    @Column("username")
    var username: String? = null,

    @Column("name")
    var name: String? = null,

    @Column("email")
    var email: String? = null,

    @Column("profile_image")
    var profileImage: String? = null,

    @Column("gender")
    var gender: String? = null,

    @Column("phone_number")
    var phoneNumber: String? = null,

    @Column("role")
    var role: Role? = null,

    @Column("is_activated")
    var isActivated: Boolean? = null,

    )  {

    fun toDto() = AppUserDto(
        authId = authId!!,
        username = username!!,
        name = name!!,
        email = email!!,
        profileImage = profileImage!!,
        gender = gender!!,
        phoneNumber = phoneNumber!!,
        role = role?.name!!,
        isActivated = isActivated!!
    )
}