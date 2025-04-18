
dependencies {
    implementation(project(":shared:common"))
    implementation(project(":shared:bizppurio-support"))
    implementation(project(":shared:cache-support"))

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
    // https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui
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
