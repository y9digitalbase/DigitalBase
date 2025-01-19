plugins {
    id("net.risesoft.y9.conventions-war")
    id("net.risesoft.y9.docker")
    id("net.risesoft.y9.lombok")
    alias(libs.plugins.org.springframework.boot)
}

dependencies {
    implementation(platform(project(":y9-digitalbase-dependencies")))
    providedRuntime(platform(libs.spring.boot.bom))

    implementation("org.apereo.cas:cas-server-core")
    implementation("org.apereo.cas:cas-server-core-authentication-api")
    implementation("org.apereo.cas:cas-server-core-authentication")
    implementation("org.apereo.cas:cas-server-support-person-directory")
    implementation("org.apereo.cas:cas-server-core-rest-api")
    implementation("org.apereo.cas:cas-server-core-web-api")
    implementation("org.apereo.cas:cas-server-core-util-api")
    implementation("org.apereo.cas:cas-server-core-services-api")
    implementation("org.apereo.cas:cas-server-core-services-registry")
    implementation(libs.googlecode.cqengine)
    implementation("org.apereo.cas:cas-server-core-services-authentication")
    implementation("org.apereo.cas:cas-server-core-tickets-api")
    implementation("org.apereo.cas:cas-server-core-api-ticket")
    implementation("org.apereo.cas:cas-server-core-validation-api")
    implementation("org.apereo.cas:cas-server-core-webflow-api")
    implementation("org.apereo.cas:cas-server-core-cookie-api")
    implementation("org.apereo.cas:cas-server-core-cookie")
    implementation("org.apereo.cas:cas-server-support-jpa-util")
    implementation("org.apereo.cas:cas-server-support-jpa-service-registry")
    implementation("org.apereo.cas:cas-server-core-authentication-attributes")
    implementation("org.springframework.security:spring-security-config")
    implementation("org.apereo.cas:cas-server-support-ldap-core")
    implementation(libs.hibernate.core)
    implementation("org.apereo.cas:cas-server-support-redis-core")
    implementation("org.apereo.cas:cas-server-support-redis-ticket-registry")
    implementation("org.apereo.cas:cas-server-support-session-redis")
    implementation("org.springframework.data:spring-data-redis")
    implementation(libs.lettucemod)
    implementation("org.apereo.cas:cas-server-core-api-logout")
    implementation("org.apereo.cas:cas-server-core-logout")
    implementation("org.apereo.cas:cas-server-core-logout-api")
    implementation("org.apereo.cas:cas-server-core-logging")
    implementation("org.springframework:spring-context-indexer")
    implementation(libs.uasparser)
    implementation(libs.jbcrypt)
    implementation(libs.google.zxing.core)
    implementation("org.apereo.cas:cas-server-support-token-tickets")
    implementation("org.apereo.cas:cas-server-support-token-core-api")
    implementation("org.apereo.cas:cas-server-support-oauth-webflow")
    implementation("org.apereo.cas:cas-server-support-oauth-api")
    implementation("org.apereo.cas:cas-server-support-oauth-core-api")
    implementation("org.apereo.cas:cas-server-support-oauth-core")
    implementation("org.apereo.cas:cas-server-support-oauth-services")
    implementation("org.apereo.cas:cas-server-support-pac4j-api")
    implementation("org.apereo.cas:cas-server-webapp-init")
    implementation("org.apereo.cas:cas-server-support-webconfig")
    implementation("org.apereo.cas:cas-server-webapp-starter-tomcat") {
        // 排除项目依赖
        exclude("org.springframework.cloud", "spring-cloud-config-client")
    }
    implementation(project(":y9-digitalbase-common:risenet-y9boot-common-nacos"))
    implementation("org.springframework.boot:spring-boot-docker-compose")
    implementation(libs.mysql.connector.j)
    implementation(libs.mariadb.java.client)
    implementation(libs.postgresql)
    implementation("com.oracle.database.jdbc:ojdbc17")
    implementation(libs.kingbase.kingbase8)
    implementation(libs.kingbase.kesdialect.hibernate4)
    implementation(libs.dameng.dmdialect.hibernate62)
    implementation(libs.dameng.dmjdbcdriver18)

    providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
}

description = "risenet-y9boot-webapp-sso-server"
val finalName = "sso"
y9Docker {
    appName = finalName
}
tasks.bootWar {
    archiveFileName.set("${finalName}.${archiveExtension.get()}")
}