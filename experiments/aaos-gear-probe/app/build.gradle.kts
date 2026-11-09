plugins { id("com.android.application") }
android {
    namespace = "com.hmg.validation"
    compileSdk = 34
    defaultConfig { applicationId = "com.hmg.validation.aaosprobe"; minSdk = 28; targetSdk = 34 }
    compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
    useLibrary("android.car")
}
