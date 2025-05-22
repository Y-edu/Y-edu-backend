dependencies {
    implementation (project(":shared:common"))

    implementation ("org.springframework.boot:spring-boot-starter-webflux")
}


val jar: Jar by tasks
val bootJar: org.springframework.boot.gradle.tasks.bundling.BootJar by tasks

bootJar.enabled = false
jar.enabled = true
