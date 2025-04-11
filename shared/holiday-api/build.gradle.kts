
dependencies {
    implementation(project(":shared:common"))
    implementation ("org.springframework.boot:spring-boot-starter-webflux")
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:4.0.2")
}


val jar: Jar by tasks
val bootJar: org.springframework.boot.gradle.tasks.bundling.BootJar by tasks

bootJar.enabled = false
jar.enabled = true
