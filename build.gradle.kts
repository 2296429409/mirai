import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile

    plugins {
        java
        kotlin("jvm") version "1.4.32"
        kotlin("plugin.serialization") version "1.4.32"
        id("net.mamoe.mirai-console") version "2.9.0"
    }

group = "org.example"
version = "0.1.0"

repositories {
    maven{ url =uri("https://maven.aliyun.com/nexus/content/groups/public/")}
    jcenter()
    mavenCentral()
    mavenLocal()
}
tasks.withType(org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile::class.java) {
    kotlinOptions.jvmTarget = "1.8"
}

dependencies {
    // 开发时使用 mirai-core-api，运行时提供 mirai-core
    api("net.mamoe:mirai-core-api:${properties["version.mirai"]}")
    runtimeOnly("net.mamoe:mirai-core:${properties["version.mirai"]}")
    implementation("com.alibaba:fastjson:1.2.75")
    // 可以简单地只添加 api("net.mamoe:mirai-core:2.6.1")
    implementation("org.apache.httpcomponents:httpclient:4.5.13")
    implementation("org.apache.httpcomponents:httpmime:4.5.13")
    // https://mvnrepository.com/artifact/net.coobird/thumbnailator
    implementation("net.coobird:thumbnailator:0.4.17")



}