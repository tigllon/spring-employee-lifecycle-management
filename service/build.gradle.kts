plugins {
    id("java")
}

group = "org.employee.service"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

application  {
    mainClass.set("org.employee.service.EmployeeConsumerMain")
}

tasks.test {
    useJUnitPlatform()
}