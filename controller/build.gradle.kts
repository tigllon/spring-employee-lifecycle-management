
group = "org.employee.controller"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

application  {
    mainClass.set("org.employee.controller.EmployeePublisherMain");
}


tasks.test {
    useJUnitPlatform()
}