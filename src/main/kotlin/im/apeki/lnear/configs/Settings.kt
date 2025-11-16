package im.apeki.lnear.configs

import im.apeki.lnear.util.config.ConfigField
import im.apeki.lnear.util.config.ConfigFile

@ConfigFile(name = "config.yml")
object Settings {
    @ConfigField(path = "default-radius", comment = "Радиус, доступный по умолчанию")
    var defaultRadius: Int = 100

    @ConfigField(path = "radius-per-group", comment = "На сколько блоков работает /near для разных прав. Формат права - lnear.radius.(значение) (например lnear.radius.vip даст радиус 150 блоков)")
    var radiusPerGroup: Map<String, Int> = mapOf(
        "vip" to 150,
        "premium" to 300,
        "king" to 500
    )

    @ConfigField(path = "hide-invisibility", comment = "Скрывать ли от поиска игроков с эффектом невидимости?")
    var hideInvisibility: Boolean = true
}