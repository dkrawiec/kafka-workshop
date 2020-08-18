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
    val kafkaVersion = "2.5.1"
    val confluentVersion = "5.5.0"
    implementation("org.scala-lang:scala-library:2.13.2")
    implementation("org.apache.kafka", "kafka-streams-scala_2.13", kafkaVersion)
    implementation("org.apache.kafka", "kafka-clients", kafkaVersion)
    implementation("org.apache.kafka", "kafka-clients", kafkaVersion)
    compileOnly("org.apache.avro", "avro", "1.10.0")
    implementation("io.confluent", "kafka-avro-serializer", confluentVersion)
    implementation("io.confluent", "kafka-streams-avro-serde", confluentVersion)
    implementation("org.apache.kafka", "kafka-streams-test-utils", kafkaVersion)
    implementation("org.apache.logging.log4j:log4j-api:2.13.3")
    implementation("org.apache.logging.log4j:log4j-core:2.13.3")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.13.3")
    testImplementation("org.scalatest:scalatest_2.13:3.2.0")
    testRuntimeOnly("com.vladsch.flexmark:flexmark-all:0.35.10")
}

tasks.named<Wrapper>("wrapper") {
    gradleVersion = "6.5.1"
    distributionType = Wrapper.DistributionType.ALL
}