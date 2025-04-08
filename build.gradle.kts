plugins {
	java
	id ("org.springframework.boot") version "3.4.2"
	id ("io.spring.dependency-management") version "1.1.7"
}


group = "com.yedu"
version = "0.0.1-SNAPSHOT"


allprojects {
	apply(plugin = "java")
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")
	java {
		toolchain {
			languageVersion = JavaLanguageVersion.of(17)
		}
	}

	configurations {
		compileOnly {
			extendsFrom(configurations.annotationProcessor.get())
		}
	}
	repositories {
		mavenCentral()
	}
}

subprojects {
	dependencies {
		compileOnly("org.projectlombok:lombok")
		annotationProcessor( "org.projectlombok:lombok")
	}
}


val jar: Jar by tasks
val bootJar: org.springframework.boot.gradle.tasks.bundling.BootJar by tasks

bootJar.enabled = false
jar.enabled = false
