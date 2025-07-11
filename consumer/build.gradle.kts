version = "1.0.0"

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
    implementation(project(":shared:rabbitmq-support"))
    implementation(project(":shared:cache-support"))
    implementation(project(":shared:bizppurio-support"))
    implementation(project(":shared:discord-support"))

    implementation ("org.flywaydb:flyway-mysql")
    implementation ("org.flywaydb:flyway-core")
    runtimeOnly ("com.mysql:mysql-connector-j")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation ("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation ("com.fasterxml.jackson.core:jackson-databind:2.18.2")
}

tasks.test {
    systemProperty("spring.profiles.active", "dev")
    useJUnitPlatform()
}

val jar: Jar by tasks
val bootJar: org.springframework.boot.gradle.tasks.bundling.BootJar by tasks

bootJar.enabled = true
jar.enabled = true
