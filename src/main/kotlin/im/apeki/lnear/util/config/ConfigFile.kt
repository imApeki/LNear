package im.apeki.lnear.util.config

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class ConfigFile(val name: String, val header: String = "")