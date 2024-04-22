plugins {
    application
    checkstyle
    jacoco
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

application {
    mainClass.set("hexlet.code.App")
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.1"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testImplementation ("org.assertj:assertj-core:3.25.2")

    implementation("io.javalin:javalin:6.1.3")
    implementation("io.javalin:javalin-bundle:6.1.3")
    implementation("io.javalin:javalin-rendering:6.1.3")

    implementation("gg.jte:jte:3.1.10")

    implementation("org.slf4j:slf4j-simple:2.0.9")
    implementation ("org.slf4j:slf4j-api:2.0.7")
    implementation ("org.slf4j:slf4j-log4j12:2.0.7")
    implementation ("org.projectlombok:lombok:1.18.26")



}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    reports {
        xml.required = true
    }
}
