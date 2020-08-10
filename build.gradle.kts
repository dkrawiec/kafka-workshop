group = "com.virtuslab.kafka.workshop"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url = uri("https://packages.confluent.io/maven/")
    }
    jcenter()
}

plugins {
    scala
    java
    application
    id("com.commercehub.gradle.plugin.avro") version "0.21.0"
}

dependencies {
    val kafkaVersion = "2.5.0"
    implementation("org.scala-lang:scala-library:2.12.10")
    implementation("org.apache.kafka", "kafka-streams", kafkaVersion)
    implementation("org.apache.kafka", "kafka-clients", kafkaVersion)
    implementation("org.apache.kafka", "kafka-clients", kafkaVersion)
    compileOnly("org.apache.avro", "avro", "1.10.0")
    implementation("io.confluent", "kafka-avro-serializer", "5.5.0")
    implementation("org.apache.kafka", "kafka-streams-test-utils", kafkaVersion)
    testImplementation("junit", "junit", "4.12")
}

tasks.named<Wrapper>("wrapper") {
    gradleVersion = "6.5.1"
    distributionType = Wrapper.DistributionType.ALL
}