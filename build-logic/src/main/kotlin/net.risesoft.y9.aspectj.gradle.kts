import io.freefair.gradle.plugins.aspectj.AjcAction

plugins {
    id("java-library")
    id("io.freefair.aspectj.post-compile-weaving")
}

dependencies {
    api("org.aspectj:aspectjrt")
    api("org.aspectj:aspectjtools")
    api("org.aspectj:aspectjweaver")

    aspect("org.springframework:spring-aspects")
    testAspect("org.springframework:spring-aspects")
}

tasks.named("compileJava") {
    configure<AjcAction> {
        enabled = true
        classpath
        options {
            aspectpath.setFrom(configurations.aspect)
            compilerArgs = listOf("-showWeaveInfo", "-verbose")
        }
    }
}

tasks.named("compileTestJava") {
    configure<AjcAction> {
        enabled = true
        classpath
        options {
            aspectpath.setFrom(configurations.aspect)
            compilerArgs = listOf("-showWeaveInfo", "-verbose")
        }
    }
}