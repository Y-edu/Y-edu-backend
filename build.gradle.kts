plugins {
	java
	id ("java-library")
	id("com.diffplug.spotless") version "7.0.3"
	id ("org.springframework.boot") version "3.4.2"
	id ("io.spring.dependency-management") version "1.1.7"
}


group = "com.yedu"
version = "0.0.1-SNAPSHOT"


allprojects {
	apply(plugin = "java")
	apply(plugin = "java-library")
	apply(plugin = "com.diffplug.spotless")
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
	// lint tool
	spotless {
		java {
			importOrder()
			removeUnusedImports()
			googleJavaFormat()
		}
	}

	repositories {
		mavenCentral()
	}
}

subprojects {
	dependencies {
		implementation ("org.springframework.boot:spring-boot-starter")
		testImplementation ("org.springframework.boot:spring-boot-starter-test")
		compileOnly("org.projectlombok:lombok")
		annotationProcessor( "org.projectlombok:lombok")
	}
}


val jar: Jar by tasks
val bootJar: org.springframework.boot.gradle.tasks.bundling.BootJar by tasks

bootJar.enabled = false
jar.enabled = false
