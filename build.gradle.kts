plugins {
	java
	application
	id("org.springframework.boot") version "2.7.15" apply false
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
}

group = "com.employee"
version = "0.0.1-SNAPSHOT"

java{
	sourceCompatibility = JavaVersion.VERSION_11
}

repositories {
	mavenCentral()
}


//dependencies {
//	implementation("org.springframework.boot:spring-boot-starter")
//	testImplementation("org.springframework.boot:spring-boot-starter-test")
//	implementation("org.springframework.boot:spring-boot-starter-web")
//	implementation(project(":controller"))
//	// https://mvnrepository.com/artifact/org.springframework/spring-aop
//
//}

tasks.withType<Test> {
	useJUnitPlatform()
}


project(":controller") {
	apply(plugin = "application")
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")

	dependencies {
		implementation("org.springframework.boot:spring-boot-starter-web:2.7.15")
		testImplementation(platform("org.junit:junit-bom:5.9.1"))
		testImplementation("org.junit.jupiter:junit-jupiter")
		// https://mvnrepository.com/artifact/com.rabbitmq/amqp-client
		implementation("com.rabbitmq:amqp-client")
		implementation("org.springframework.boot:spring-boot-starter-integration")
		implementation("org.springframework.integration:spring-integration-http")
		testImplementation("org.springframework.integration:spring-integration-test")
		// https://mvnrepository.com/artifact/org.springframework/spring-messaging
		implementation("org.springframework:spring-messaging:5.3.29")
		// https://mvnrepository.com/artifact/org.springframework.integration/spring-integration-core
		implementation("org.springframework.integration:spring-integration-core")
		// https://mvnrepository.com/artifact/org.springframework.integration/spring-integration-amqp
		implementation("org.springframework.integration:spring-integration-amqp")
		testImplementation("org.springframework.boot:spring-boot-starter-test")
		implementation(project(":model"))

	}

//	evaluationDependsOn(":rabbitmq")
}


project(":service") {
	apply(plugin = "application")
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")

	dependencies {
		implementation("org.springframework.boot:spring-boot-starter-web:2.7.15")
		testImplementation(platform("org.junit:junit-bom:5.9.1"))
		testImplementation("org.junit.jupiter:junit-jupiter")
		// https://mvnrepository.com/artifact/com.rabbitmq/amqp-client
		implementation("com.rabbitmq:amqp-client")
		implementation("org.springframework.boot:spring-boot-starter-integration")
		testImplementation("org.springframework.integration:spring-integration-test")
		// https://mvnrepository.com/artifact/org.springframework/spring-messaging
		implementation("org.springframework:spring-messaging:5.3.29")
		// https://mvnrepository.com/artifact/org.springframework.integration/spring-integration-core
		implementation("org.springframework.integration:spring-integration-core")
		// https://mvnrepository.com/artifact/org.springframework.integration/spring-integration-amqp
		implementation("org.springframework.integration:spring-integration-amqp")
		testImplementation("org.springframework.boot:spring-boot-starter-test")
		implementation(project(":repository"))
		implementation(project(":model"))
	}
}
project(":repository") {
	apply(plugin="java")
	apply(plugin="application")
//	apply(plugin="org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")
	dependencyManagement {
		imports {
			mavenBom("org.springframework.boot:spring-boot-dependencies:1.5.8.RELEASE")
		}

	}
	dependencies {
		implementation("org.springframework.boot:spring-boot-starter-web")
		implementation(project(":model"))
	}
}


project(":model") {
	apply(plugin="java")
	apply(plugin="application")
}
application {
	mainClass.set("com.employee.rabbitmq.RabbitmqApplication")
}

