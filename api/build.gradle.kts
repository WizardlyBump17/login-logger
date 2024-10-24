val jetbrainsAnnotations = "25.0.0"
val ormlite = "5.6"
val mysqlConnectorJ = "9.0.0"
val testContainers = "1.20.1"

dependencies {
    implementation("org.jetbrains:annotations:${jetbrainsAnnotations}")
    implementation("com.j256.ormlite:ormlite-core:${ormlite}")
    testImplementation("com.j256.ormlite:ormlite-jdbc:${ormlite}")
    testImplementation("com.mysql:mysql-connector-j:${mysqlConnectorJ}")
    testImplementation("org.testcontainers:mysql:${testContainers}")
}