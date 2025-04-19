
dependencies {
    implementation(project(":shared:common"))
    implementation(project(":shared:cache-support"))

    implementation ("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework:spring-tx")
}


val jar: Jar by tasks
val bootJar: org.springframework.boot.gradle.tasks.bundling.BootJar by tasks

bootJar.enabled = false
jar.enabled = true
