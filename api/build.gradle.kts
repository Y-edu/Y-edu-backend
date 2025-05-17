version = "1.0.0"


openApiGenerate {
    generatorName.set("typescript-fetch")
    inputSpec.set("$projectDir/openapi.json")
    outputDir.set("$projectDir/generated-sources/typescript")
    apiPackage.set("api")
    modelPackage.set("model")
    configOptions.set(
        mapOf(
            "supportsES6" to "true",
            "npmName" to "my-typescript-client",
            "withInterfaces" to "true",
            "enumVars" to "original", // enum 값을 문자열 그대로 사용 (기본값은 대문자 변환)
            "modelPropertyNaming" to "camelCase", // 모델 속성 이름을 camelCase로 변경 (기본값은 원본 이름)
            "apiPackage" to "api", // API 클래스가 생성될 패키지 (현재 설정 유지)
            "modelPackage" to "model", // 모델 클래스가 생성될 패키지 (현재 설정 유지)
            "prependFormOrBodyParameters" to "true", // POST, PUT 등의 body 파라미터를 함수 파라미터 목록의 처음에 위치
            "generateModelConstructors" to "true", // 모델 클래스에 생성자 추가
            "useSingleRequestParameter" to "true", // API 함수의 파라미터를 하나의 객체로 묶어서 생성 (선택 사항)
            "ensureUniqueParameterNames" to "true", // 파라미터 이름이 충돌하지 않도록 보장
            "parameterNaming" to "camelCase", // API 함수 파라미터 이름을 camelCase로 변경
            "sortParamsByRequiredFlag" to "true", // 필수 파라미터를 먼저 정렬
            "sortModelPropertiesByName" to "true", // 모델 속성을 이름순으로 정렬
            "sortOperationsByPath" to "true" // API operations를 경로별로 정렬
        )
    )
}

tasks.register<Exec>("downloadOpenApiSpec") {
    commandLine("curl", "-o", "openapi.json", "http://localhost:8080/v3/api-docs")
}

tasks.register("generateTypeScriptClient") {
    dependsOn("downloadOpenApiSpec", "openApiGenerate")
}


jib {
    val repositoryUsername = "y-edu"
    val repositoryToken = System.getenv("DEPLOY_TOKEN")
    ?: project.findProperty("DEPLOY_TOKEN") as String?
    ?: throw GradleException("Missing DEPLOY_TOKEN environment variable or deployToken property in gradle.properties")


    from {
        image = "amazoncorretto:17"
    }
    to {
        image = "ghcr.io/$repositoryUsername/${project.name}:${project.version}"
        auth {
            username = repositoryUsername
            password = repositoryToken
        }
    }
    container {
        creationTime = "USE_CURRENT_TIMESTAMP"
        jvmFlags = listOf("-XX:+UseContainerSupport",
            "-Dfile.encoding=UTF-8"
        )
    }
}


dependencies {
    implementation(project(":shared:common"))
    implementation(project(":shared:cache-support"))
    implementation(project(":shared:discord-support"))
    implementation(project(":shared:rabbitmq-support"))

    implementation ("org.springframework.boot:spring-boot-starter-validation")
    implementation ("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation ("org.springframework.boot:spring-boot-starter-data-redis")
    implementation ("org.springframework.boot:spring-boot-starter-security")
    implementation ("org.springframework.boot:spring-boot-starter-web")
    implementation ("org.springframework.boot:spring-boot-starter-webflux")
    runtimeOnly ("com.mysql:mysql-connector-j")
    testImplementation ("org.springframework.boot:spring-boot-starter-test")
    testImplementation ("io.projectreactor:reactor-test")
    testImplementation ("org.springframework.security:spring-security-test")
    testRuntimeOnly ("org.junit.platform:junit-platform-launcher")
    //test
    testRuntimeOnly ("com.h2database:h2")
    implementation ("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
    //swagger 추가

    // prometheus metric
    implementation ("org.springframework.boot:spring-boot-starter-actuator")
    runtimeOnly ("io.micrometer:micrometer-registry-prometheus")

    // flyway 추가
    implementation ("org.flywaydb:flyway-mysql")
    implementation ("org.flywaydb:flyway-core")

    // aws s3
    implementation ("org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE")

    // Spring boot 3.x이상에서 QueryDsl 패키지를 정의하는 방법
    implementation ("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    annotationProcessor ("com.querydsl:querydsl-apt:5.0.0:jakarta")
    annotationProcessor ("jakarta.annotation:jakarta.annotation-api")
    annotationProcessor ("jakarta.persistence:jakarta.persistence-api")

    implementation ("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation ("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation ("io.jsonwebtoken:jjwt-jackson:0.11.5")
    //jwt 추가
    
    // excel 읽기용 poi
    implementation ("org.apache.poi:poi-ooxml:5.4.0")
}

tasks.test {
    systemProperty("spring.profiles.active", "dev")
    useJUnitPlatform()
}

val jar: Jar by tasks
val bootJar: org.springframework.boot.gradle.tasks.bundling.BootJar by tasks

bootJar.enabled = true
jar.enabled = true
