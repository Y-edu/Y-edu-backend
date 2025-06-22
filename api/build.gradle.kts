import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.core.type.TypeReference

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
            "npmName" to "y-edu-client",
            "withInterfaces" to "true",
            "stringEnums" to "true",
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
        image = "amazoncorretto:21"
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
    implementation(project(":shared:sheet-support"))
    implementation("com.yedu:payment-interface:1.0.0")

    implementation("org.springframework.boot:spring-boot-starter-graphql")
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

