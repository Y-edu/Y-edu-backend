plugins {
	java
	id ("java-library")
	id("com.diffplug.spotless") version "7.0.3"
	id ("org.springframework.boot") version "3.4.2"
	id ("io.spring.dependency-management") version "1.1.7"
	id("com.google.cloud.tools.jib") version "3.4.4"
	id("org.openapi.generator") version "7.13.0"
}


group = "com.yedu"
version = "0.0.1-SNAPSHOT"


allprojects {
	apply(plugin = "java")
	apply(plugin = "java-library")
	apply(plugin = "com.diffplug.spotless")
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")
	apply(plugin = "com.google.cloud.tools.jib")
	apply(plugin = "org.openapi.generator")
	java {
		toolchain {
			languageVersion = JavaLanguageVersion.of(21)
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
		maven {
			name = "nexusSnapshots"
			url = uri("http://infrabird.duckdns.org:10006/repository/maven-snapshots/")
			isAllowInsecureProtocol = true
			credentials {
				username =
				password =
			}
			content {
				includeVersionByRegex(".*", ".*", ".*-SNAPSHOT")
			}
		}

		maven {
			name = "nexusReleases"
			url = uri("http://infrabird.duckdns.org:10006/repository/maven-releases/")
			isAllowInsecureProtocol = true
			credentials {
				username =
				password =
			}
			content {
				excludeVersionByRegex(".*", ".*", ".*-SNAPSHOT")
			}
		}

		mavenCentral()
	}
}

subprojects {
	dependencies {
		implementation ("org.springframework.boot:spring-boot-starter")
		testImplementation ("org.springframework.boot:spring-boot-starter-test")
		compileOnly("org.projectlombok:lombok")
		annotationProcessor( "org.projectlombok:lombok")
		if ( System.getProperty("os.name").startsWith("Mac")) {
			implementation("io.netty:netty-resolver-dns-native-macos:4.1.68.Final:osx-aarch_64")
		}
	}

}


val jar: Jar by tasks
val bootJar: org.springframework.boot.gradle.tasks.bundling.BootJar by tasks

bootJar.enabled = false
jar.enabled = false
