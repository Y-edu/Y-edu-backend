
dependencies {
    implementation(project(":shared:common"))

    api("org.springframework.boot:spring-boot-starter-quartz")
}


val jar: Jar by tasks
val bootJar: org.springframework.boot.gradle.tasks.bundling.BootJar by tasks

bootJar.enabled = false
jar.enabled = true
