

dependencies {

    implementation("org.springframework.boot:spring-boot-starter-amqp:2.7.4")

    implementation("org.springframework.boot:spring-boot-starter-web:2.7.4")

    annotationProcessor ("org.projectlombok:lombok")
    compileOnly ("org.projectlombok:lombok")
    testAnnotationProcessor ("org.projectlombok:lombok")
    testCompileOnly ("org.projectlombok:lombok")

    implementation("org.springframework.boot:spring-boot-starter-test:2.7.4")

    implementation("org.springframework.amqp:spring-rabbit-test:2.4.7")

    implementation("org.springframework.boot:spring-boot-starter-mail:2.7.4")

    implementation("org.springframework.boot:spring-boot-starter-freemarker:2.7.4")

    implementation("org.springframework.boot:spring-boot-devtools:2.7.4")

    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:3.1.3")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    testImplementation("io.projectreactor:reactor-test")

    implementation ("org.springdoc:springdoc-openapi-ui:1.6.11")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation ("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:r2dbc-postgresql")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation(project(":user-service"))
}
