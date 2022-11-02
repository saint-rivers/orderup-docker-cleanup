package com.saintrivers.controltower.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.security.OAuthFlow
import io.swagger.v3.oas.annotations.security.OAuthFlows
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.annotations.servers.Server

@OpenAPIDefinition(
    servers = [Server(url = "/", description = "Default Server URL")],
    info = Info(
        title = "Orderup Tower: User Service",
        description = "Spring Boot API for creating and managing users and groups.",
        version = "1.0.1"
    )
)
@SecurityScheme(
    name = "OrderUpTowerOAuth",
    type = SecuritySchemeType.OAUTH2,
    `in` = SecuritySchemeIn.HEADER,
    flows = OAuthFlows(
//        clientCredentials = OAuthFlow(
//            tokenUrl = "https://tos.orderup.homes/auth/realms/orderup-tower/protocol/openid-connect/token"
//
////            tokenUrl = "http://keycloak-server-demo:8800/auth/realms/orderup-tower/protocol/openid-connect/token"
//        ),
        password = OAuthFlow(

            tokenUrl = "https://tos.orderup.homes/auth/realms/orderup-tower/protocol/openid-connect/token"
        )

    )
)
class OpenApiConfig