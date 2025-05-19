dependencies {
    implementation (project(":shared:common"))

    implementation ("com.google.apis:google-api-services-sheets:v4-rev20250211-2.0.0")
    implementation ("com.google.auth:google-auth-library-oauth2-http:1.22.0")
    implementation ("com.google.api-client:google-api-client:2.2.0")
    implementation ("com.google.oauth-client:google-oauth-client-jetty:1.34.1")
}


val jar: Jar by tasks
val bootJar: org.springframework.boot.gradle.tasks.bundling.BootJar by tasks

bootJar.enabled = false
jar.enabled = true
