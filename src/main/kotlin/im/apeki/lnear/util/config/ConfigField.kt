package im.apeki.lnear.util.config

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class ConfigField(val path: String = "", val comment: String = "")