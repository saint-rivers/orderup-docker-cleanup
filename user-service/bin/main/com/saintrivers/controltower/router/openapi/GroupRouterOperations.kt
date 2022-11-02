package com.saintrivers.controltower.router.openapi


import com.saintrivers.controltower.handler.AppUserHandler
import com.saintrivers.controltower.handler.GroupHandler
import com.saintrivers.controltower.model.dto.AppUserDto
import com.saintrivers.controltower.model.dto.GroupDto
import com.saintrivers.controltower.model.request.GroupRequest
import com.saintrivers.controltower.model.request.MemberRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.Explode
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.enums.ParameterStyle
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMethod
import java.util.*


@RouterOperations(
    value = [
        RouterOperation(
            path = "/api/v1/groups/{id}/find",
            method = [RequestMethod.GET],
            produces = [MediaType.APPLICATION_JSON_VALUE],
            beanClass = GroupHandler::class,
            beanMethod = "findGroup",
            operation = Operation(
                operationId = "findGroup",
                parameters = [
                    Parameter(
                        name = "id",
                        `in` = ParameterIn.PATH,
                        style = ParameterStyle.SIMPLE,
                        explode = Explode.FALSE,
                        required = true,
                    )
                ],
                responses = [ApiResponse(
                    content = [Content(schema = Schema(implementation = GroupDto::class))]
                )]
            )
        ),
        RouterOperation(
            path = "/api/v1/groups/{id}/users",
            method = [RequestMethod.GET],
            produces = [MediaType.APPLICATION_JSON_VALUE],
            beanClass = GroupHandler::class,
            beanMethod = "findAllGroupMembers",
            operation = Operation(
                operationId = "findAllGroupMembers",
                parameters = [
                    Parameter(
                        name = "id",
                        `in` = ParameterIn.PATH,
                        style = ParameterStyle.SIMPLE,
                        explode = Explode.FALSE,
                        required = true,
                    )
                ],
                responses = [ApiResponse(
                    content = [Content(array = ArraySchema(schema = Schema(implementation = AppUserDto::class)))]
                )]
            )
        ),
        RouterOperation(
            path = "/api/v1/groups",
            method = [RequestMethod.GET],
            produces = [MediaType.APPLICATION_JSON_VALUE],
            beanClass = GroupHandler::class,
            beanMethod = "findGroups",
            operation = Operation(
                operationId = "findGroups",
                responses = [ApiResponse(
                    content = [Content(array = ArraySchema(schema = Schema(implementation = AppUserDto::class)))]
                )]
            )
        ),
        RouterOperation(
            path = "/api/v1/groups",
            method = [RequestMethod.POST],
            produces = [MediaType.APPLICATION_JSON_VALUE],
            beanClass = GroupHandler::class,
            beanMethod = "createGroup",
            operation = Operation(
                operationId = "createGroup",
//                parameters = [
//                    Parameter(
//                        name = "userId",
//                        `in` =  ParameterIn.QUERY,
//                        style = ParameterStyle.SIMPLE,
//                        explode = Explode.FALSE,
//                        required = true,
//                    )
//                ],
                requestBody = RequestBody(content = [Content(schema = Schema(implementation = GroupRequest::class))]),
                responses = [ApiResponse(
                    content = [Content(schema = Schema(implementation = GroupDto::class))]
                )]
            )
        ),
        RouterOperation(
            path = "/api/v1/groups/members",
            method = [RequestMethod.POST],
            produces = [MediaType.APPLICATION_JSON_VALUE],
            beanClass = GroupHandler::class,
            beanMethod = "addGroupMember",
            operation = Operation(
                operationId = "addGroupMember",
                requestBody = RequestBody(content = [Content(schema = Schema(implementation = MemberRequest::class))]),
                responses = [ApiResponse(
                    content = [Content(schema = Schema(implementation = UUID::class))]
                )]
            )
        ),
        RouterOperation(
            path = "/api/v1/groups/update-group/{groupId}",
            method = [RequestMethod.PUT],
            produces = [MediaType.APPLICATION_JSON_VALUE],
            beanClass = GroupHandler::class,
            beanMethod = "updateUserGroup",
            operation = Operation(
                operationId = "updateUserGroup",
                parameters = [
                    Parameter(
                        name = "groupId",
                        `in` =  ParameterIn.PATH,
                        style = ParameterStyle.SIMPLE,
                        explode = Explode.FALSE,
                        required = true,
                    )
                ],
                requestBody = RequestBody(content = [Content(schema = Schema(implementation = GroupRequest::class))]),
                responses = [ApiResponse(
                    content = [Content(schema = Schema(implementation = GroupDto::class))]
                )]
            )
        ),
        RouterOperation(
            path = "/api/v1/groups/find/{userId}",
            method = [RequestMethod.GET],
            produces = [MediaType.APPLICATION_JSON_VALUE],
            beanClass = GroupHandler::class,
            beanMethod = "findGroupsByUserId",
            operation = Operation(
                operationId = "findGroupsByUserId",
                parameters = [
                    Parameter(
                        name = "userId",
                        `in` =  ParameterIn.PATH,
                        style = ParameterStyle.SIMPLE,
                        explode = Explode.FALSE,
                        required = true,
                    )
                ],
                responses = [ApiResponse(
                    content = [Content(array = ArraySchema(schema = Schema(implementation = GroupDto::class)))]
                )]
            )
        ),
        RouterOperation(
            path = "/api/v1/groups/recently",
            method = [RequestMethod.GET],
            produces = [MediaType.APPLICATION_JSON_VALUE],
            beanClass = GroupHandler::class,
            beanMethod = "findGroupsRecentCreated",
            operation = Operation(
                operationId = "findGroupsRecentCreated",
                responses = [ApiResponse(
                    content = [Content(array = ArraySchema(schema = Schema(implementation = GroupDto::class)))]
                )]
            )
        ),
        RouterOperation(
            path = "/api/v1/groups/delete/{groupId}",
            method = [RequestMethod.DELETE],
            produces = [MediaType.APPLICATION_JSON_VALUE],
            beanClass = GroupHandler::class,
            beanMethod = "deleteGroupByGroupId",
            operation = Operation(
                operationId = "deleteGroupByGroupId",
                parameters = [
                    Parameter(
                        name = "groupId",
                        `in` = ParameterIn.PATH,
                        style = ParameterStyle.SIMPLE,
                        explode = Explode.FALSE,
                        required = true,
                    )
                ],
                responses = [ApiResponse(
                    content = [Content(schema = Schema(implementation = UUID::class))]
                )]
            )
        ),
        RouterOperation(
            path = "/api/v1/groups/{groupId}/remove-member/{userId}",
            method = [RequestMethod.DELETE],
            produces = [MediaType.APPLICATION_JSON_VALUE],
            beanClass = GroupHandler::class,
            beanMethod = "removeMemberFromGroup",
            operation = Operation(
                operationId = "removeMemberFromGroup",
                parameters = [
                    Parameter(
                        name = "groupId",
                        `in` = ParameterIn.PATH,
                        style = ParameterStyle.SIMPLE,
                        explode = Explode.FALSE,
                        required = true,
                    ),
                    Parameter(
                        name = "userId",
                        `in` = ParameterIn.PATH,
                        style = ParameterStyle.SIMPLE,
                        explode = Explode.FALSE,
                        required = true,
                    )
                ],
                responses = [ApiResponse(
                    content = [Content(schema = Schema(implementation = String::class))]
                )]
            )
        ),
        RouterOperation(
            path = "/api/v1/groups/{groupId}/count-member",
            method = [RequestMethod.GET],
            produces = [MediaType.APPLICATION_JSON_VALUE],
            beanClass = GroupHandler::class,
            beanMethod = "countMemberInGroup",
            operation = Operation(
                operationId = "countMemberInGroup",
                parameters = [
                    Parameter(
                        name = "groupId",
                        `in` = ParameterIn.PATH,
                        style = ParameterStyle.SIMPLE,
                        explode = Explode.FALSE,
                        required = true,
                    )
                ],
                responses = [ApiResponse(
                    content = [Content(schema = Schema(implementation = Long::class))]
                )]
            )
        )
    ]
)
annotation class GroupRouterOperations()
