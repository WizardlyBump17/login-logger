val jetbrainsAnnotations = "25.0.0"
val ormlite = "5.6"

dependencies {
    compileOnly("org.jetbrains:annotations:${jetbrainsAnnotations}")
    implementation("com.j256.ormlite:ormlite-core:${ormlite}")
}