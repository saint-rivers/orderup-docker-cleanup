package com.saintrivers.controltower.router

import com.saintrivers.controltower.handler.AppUserHandler
import com.saintrivers.controltower.handler.GroupHandler
import com.saintrivers.controltower.router.openapi.GroupRouterOperations
import com.saintrivers.controltower.router.openapi.UserRouterOperations
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.*

@Configuration
class ApiRouter(val appUserHandler: AppUserHandler, val groupHandler: GroupHandler) {

    @Bean
    @UserRouterOperations
    fun userRouter(): RouterFunction<ServerResponse> = router {
        "/api/v1".nest {
            POST("/users", appUserHandler::registerUser)
            GET("/users/find/{id}", appUserHandler::findUserById)
            PUT("/users/profile/{id}", appUserHandler::updateUserProfile)
            DELETE("/users/{id}", appUserHandler::deleteUser)
            POST("/users/activation-attempt", appUserHandler::insertActivationAttempt)
            GET("/users", appUserHandler::getUserByEmail)
            POST("/users/verify-code", appUserHandler::verifyUserByCode)
            PUT("/users/update-image/{userId}", appUserHandler::updateUserProfileImage)
            GET("/users/username/{username}", appUserHandler::getUserByUserName)
            GET("/users/check-exist-in-group", appUserHandler::checkIfUserIsInAGroup)
        }
    }

    @Bean
    @GroupRouterOperations
    fun groupRouter(): RouterFunction<ServerResponse> = router {
        "/api/v1".nest {
            GET("/groups/{id}/users", groupHandler::findAllGroupMembers)
            GET("/groups/{id}/find", groupHandler::findGroup)
            POST("/groups", groupHandler::createGroup)
            GET("/groups", groupHandler::findGroups)
            POST("/groups/members", groupHandler::addGroupMember)
            PUT("/groups/update-group/{groupId}", groupHandler::updateUserGroup)
            GET("/groups/find/{userId}", groupHandler::findGroupsByUserId)
            GET("/groups/recently", groupHandler::findGroupsRecentCreated)
            DELETE("/groups/delete/{groupId}", groupHandler::deleteGroupByGroupId)
            DELETE("/groups/{groupId}/remove-member/{userId}", groupHandler::removeMemberFromGroup)
            GET("/groups/{groupId}/count-member", groupHandler::countMemberInGroup)
        }
    }

}