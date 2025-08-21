plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("maven-publish")
    id("application")
}

group = "org.housingstudio"
version = System.getenv("VERSION") ?: "1.0-SNAPSHOT"

application {
    mainClass.set("org.housingstudio.hsl.Main")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
    testCompileOnly("org.projectlombok:lombok:1.18.32")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.32")

    compileOnly("org.jetbrains:annotations:24.0.1")
    testCompileOnly("org.jetbrains:annotations:24.0.1")

    implementation("com.google.code.gson:gson:2.11.0")
    implementation("com.moandjiezana.toml:toml4j:0.7.2")
    implementation("com.github.qibergames:futura:1.1.1")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/housing-studio/hsl")
            credentials {
                username = System.getenv("MAVEN_USERNAME")
                password = System.getenv("MAVEN_PASSWORD")
            }
        }
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.test {
    useJUnitPlatform()
}

tasks.build {
    dependsOn("shadowJar")
}
