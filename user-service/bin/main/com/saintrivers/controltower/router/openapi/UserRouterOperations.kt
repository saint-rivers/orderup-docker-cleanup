package com.saintrivers.controltower.router.openapi


import com.saintrivers.controltower.handler.AppUserHandler
import com.saintrivers.controltower.model.dto.ActivationAttemptDto
import com.saintrivers.controltower.model.dto.AppUserDto
import com.saintrivers.controltower.model.request.ActivationAttemptRequest
import com.saintrivers.controltower.model.request.AppUserProfileRequest
import com.saintrivers.controltower.model.request.AppUserRequest
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


@RouterOperations(
    value = [
        RouterOperation(
            path = "/api/v1/users/find/{id}",
            method = [RequestMethod.GET],
            produces = [MediaType.APPLICATION_JSON_VALUE],
            beanClass = AppUserHandler::class,
            beanMethod = "findUserById",
            operation = Operation(
                operationId = "findUserById",
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
            path = "/api/v1/users",
            method = [RequestMethod.POST],
            produces = [MediaType.APPLICATION_JSON_VALUE],
            beanClass = AppUserHandler::class,
            beanMethod = "registerUser",
            operation = Operation(
                operationId = "registerUser",
                requestBody = RequestBody(content = [Content(schema = Schema(implementation = AppUserRequest::class))]),
                responses = [ApiResponse(
                    content = [Content(array = ArraySchema(schema = Schema(implementation = AppUserDto::class)))]
                )]
            )
        ),
        RouterOperation(
            path = "/api/v1/users/profile/{id}",
            method = [RequestMethod.PUT],
            produces = [MediaType.APPLICATION_JSON_VALUE],
            beanClass = AppUserHandler::class,
            beanMethod = "updateUserProfile",
            operation = Operation(
                operationId = "updateUserProfile",
                parameters = [
                    Parameter(
                        name = "id",
                        `in` = ParameterIn.PATH,
                        style = ParameterStyle.SIMPLE,
                        explode = Explode.FALSE,
                        required = true,
                    )
                ],
                requestBody = RequestBody(content = [Content(schema = Schema(implementation = AppUserProfileRequest::class))]),
                responses = [ApiResponse(
                    content = [Content(schema = Schema(implementation = AppUserDto::class))]
                )]
            )
        ),
        RouterOperation(
            path = "/api/v1/users/{id}",
            method = [RequestMethod.DELETE],
            produces = [MediaType.APPLICATION_JSON_VALUE],
            beanClass = AppUserHandler::class,
            beanMethod = "deleteUser",
            operation = Operation(
                operationId = "deleteUser",
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
                    responseCode = "200"
                )]
            )
        ),
        RouterOperation(
            path = "/api/v1/users/activation-attempt",
            method = [RequestMethod.POST],
            produces = [MediaType.APPLICATION_JSON_VALUE],
            beanClass = AppUserHandler::class,
            beanMethod = "insertActivationAttempt",
            operation = Operation(
                operationId = "insertActivationAttempt",
                requestBody = RequestBody(content = [Content(schema = Schema(implementation = ActivationAttemptRequest::class))]),
                responses = [ApiResponse(
                    content = [Content(array = ArraySchema(schema = Schema(implementation = ActivationAttemptDto::class)))]
                )]
            )
        ),
        RouterOperation(
            path = "/api/v1/users",
            method = [RequestMethod.GET],
            produces = [MediaType.APPLICATION_JSON_VALUE],
            beanClass = AppUserHandler::class,
            beanMethod = "getUserByEmail",
            operation = Operation(
                operationId = "getUserByEmail",
                parameters = [
                    Parameter(
                        name = "email",
                        `in` =  ParameterIn.QUERY,
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
            path = "/api/v1/users/verify-code",
            method = [RequestMethod.POST],
            produces = [MediaType.APPLICATION_JSON_VALUE],
            beanClass = AppUserHandler::class,
            beanMethod = "verifyUserByCode",
            operation = Operation(
                operationId = "verifyUserByCode",
                parameters = [
                    Parameter(
                        name = "code",
                        `in` =  ParameterIn.QUERY,
                        style = ParameterStyle.SIMPLE,
                        explode = Explode.FALSE,
                        required = true,
                    ),
                    Parameter(
                        name = "id",
                        `in` =  ParameterIn.QUERY,
                        style = ParameterStyle.SIMPLE,
                        explode = Explode.FALSE,
                        required = true,
                    )
                ],
                responses = [ApiResponse(
                    content = [Content(array = ArraySchema(schema = Schema(implementation = Boolean::class)))]
                )]
            )
        ),
        RouterOperation(
            path = "/api/v1/users/update-image/{userId}",
            method = [RequestMethod.PUT],
            produces = [MediaType.APPLICATION_JSON_VALUE],
            beanClass = AppUserHandler::class,
            beanMethod = "updateUserProfileImage",
            operation = Operation(
                operationId = "updateUserProfileImage",
                parameters = [
                    Parameter(
                        name = "imageLink",
                        `in` =  ParameterIn.QUERY,
                        style = ParameterStyle.SIMPLE,
                        explode = Explode.FALSE,
                        required = true,
                    ),
                    Parameter(
                        name = "userId",
                        `in` =  ParameterIn.PATH,
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
            path = "/api/v1/users/username/{username}",
            method = [RequestMethod.GET],
            produces = [MediaType.APPLICATION_JSON_VALUE],
            beanClass = AppUserHandler::class,
            beanMethod = "getUserByUserName",
            operation = Operation(
                operationId = "getUserByUserName",
                parameters = [
                    Parameter(
                        name = "username",
                        `in` =  ParameterIn.PATH,
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
            path = "/api/v1/users/check-exist-in-group",
            method = [RequestMethod.GET],
            produces = [MediaType.APPLICATION_JSON_VALUE],
            beanClass = AppUserHandler::class,
            beanMethod = "checkIfUserIsInAGroup",
            operation = Operation(
                operationId = "checkIfUserIsInAGroup",
                parameters = [
                    Parameter(
                        name = "userId",
                        `in` =  ParameterIn.QUERY,
                        style = ParameterStyle.SIMPLE,
                        explode = Explode.FALSE,
                        required = true,
                    ),
                    Parameter(
                        name = "groupId",
                        `in` =  ParameterIn.QUERY,
                        style = ParameterStyle.SIMPLE,
                        explode = Explode.FALSE,
                        required = true,
                    )
                ],
                responses = [ApiResponse(
                    content = [Content(schema = Schema(implementation = Boolean::class))]
                )]
            )
        )
    ]
)
annotation class UserRouterOperations()
