
dependencies {
    implementation (project(":shared:common"))

    implementation ("com.fasterxml.jackson.core:jackson-databind:2.18.2")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
}


val jar: Jar by tasks
val bootJar: org.springframework.boot.gradle.tasks.bundling.BootJar by tasks

bootJar.enabled = false
jar.enabled = true
