plugins {
    // Apply the java plugin to add support for Java
    java

    // Apply the application plugin to add support for building a CLI application
    // You can run your app via task "run": ./gradlew run
    application

    /*
     * Adds tasks to export a runnable jar.
     * In order to create it, launch the "shadowJar" task.
     * The runnable jar will be found in build/libs/projectname-all.jar
     */
    id("com.github.johnrengelman.shadow") version "8.1.1"

    // Spring Boot plugin
    id("org.springframework.boot") version "3.4.0"
    id("io.spring.dependency-management") version "1.1.3"
    id("org.openjfx.javafxplugin") version "0.1.0"
}

javafx {
   version = "17"
   modules = listOf("javafx.base", "javafx.controls", "javafx.fxml", "javafx.swing", "javafx.graphics")
}

repositories {
    mavenCentral()
}

val javaFXModules = listOf(
    "base",
    "controls",
    "fxml",
    "swing",
    "graphics"
)

val supportedPlatforms = listOf("linux", "mac", "win") // All required for OOP

dependencies {
    // Suppressions for SpotBugs
    compileOnly("com.github.spotbugs:spotbugs-annotations:4.8.6")

    // Lombok dependencies
    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")

    // JavaFX dependencies
    val javaFxVersion = 15
    for (platform in supportedPlatforms) {
        for (module in javaFXModules) {
            implementation("org.openjfx:javafx-$module:$javaFxVersion:$platform")
        }
    }

    // TMDb API dependency
    implementation("com.uwetrottmann.tmdb2:tmdb-java:2.2.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("org.json:json:20231013")

    // API SLF4J e Logback
    implementation("org.slf4j:slf4j-api:2.0.13")
    implementation("ch.qos.logback:logback-classic:1.4.14")

    // Spring Boot dependencies
    implementation("org.springframework.boot:spring-boot-starter-actuator:3.4.0")
    implementation("org.springframework.boot:spring-boot-starter-validation:3.4.0")
    implementation("org.springframework.boot:spring-boot-starter-web:3.4.0")
    implementation("org.springframework.boot:spring-boot-starter-webflux:3.4.0")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.4.0")
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive:3.4.0")

    implementation("org.springframework.security:spring-security-oauth2-resource-server:6.4.1")
    implementation("org.springframework.security:spring-security-oauth2-jose:6.4.1")

    implementation("org.zalando:logbook-spring-boot-starter:3.10.0")

    // Querydsl dependencies
    val querydslVersion = "5.1.0"
    compileOnly("com.querydsl:querydsl-jpa:$querydslVersion:jakarta")
    compileOnly("com.querydsl:querydsl-apt:$querydslVersion:jakarta")
    implementation("jakarta.persistence:jakarta.persistence-api:3.2.0")

    // MapStruct
    implementation("org.mapstruct:mapstruct:1.6.3")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

    // Database dependencies
    implementation("com.mysql:mysql-connector-j:9.1.0")
    implementation("org.flywaydb:flyway-core:11.1.0")
    implementation("org.hibernate.orm:hibernate-core:6.6.4.Final")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.2.0")

    // JUnit for testing
    val jUnitVersion = "5.11.2"
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jUnitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jUnitVersion")

    // Lombok per i test
    testCompileOnly("org.projectlombok:lombok:1.18.36")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.36")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17)) // Sostituisci con la versione corretta
    }
}

tasks.withType<JavaCompile> {
    options.annotationProcessorPath = configurations["annotationProcessor"]
    options.compilerArgs.add("-Amapstruct.defaultComponentModel=spring")
    options.compilerArgs.add("-Amapstruct.unmappedTargetPolicy=ERROR")
}

tasks.withType<Test> {
    // Enables JUnit 5 Jupiter module
    useJUnitPlatform()
}

application {
    // Define the main class for the application
    mainClass.set("it.unibo.samplejavafx.App")
}
