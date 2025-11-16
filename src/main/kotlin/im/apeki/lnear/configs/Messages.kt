package im.apeki.lnear.configs

import im.apeki.lnear.util.config.ConfigField
import im.apeki.lnear.util.config.ConfigFile

@ConfigFile(name = "messages.yml")
object Messages {
    @ConfigField(path = "color-engine", comment = "Способ обработки сообщений (доступные значения - minimessage и legacy)")
    var colorEngine: String = "legacy"

    @ConfigField(path = "no-perms")
    var noPerms: String = "&cУ вас нет прав на эту команду"

    @ConfigField(path = "not-found")
    var notFound: String = "&cРядом с вами не найдено ни одного игрока"

    @ConfigField(path = "message.header")
    var header: String = """&fНайдено &d{amount} &fигроков в радиусе &c{radius} &fблоков рядом с вами:
&8-----------------"""
    @ConfigField(path = "message.text")
    var text: String = "&c{player} &f- &d{distance} &fблоков &8(&b{direction}&8)"
    @ConfigField(path = "message.separator")
    var separator: String = "&f, "
    @ConfigField(path = "message.footer")
    var footer: String = """&8-----------------"""

    @ConfigField(path = "direction.north")
    var north: String = "↑"
    @ConfigField(path = "direction.north-east")
    var northEast: String = "↗"
    @ConfigField(path = "direction.east")
    var east: String = "→"
    @ConfigField(path = "direction.south-east")
    var southEast: String = "↘"
    @ConfigField(path = "direction.south")
    var south: String = "↓"
    @ConfigField(path = "direction.south-west")
    var southWest: String = "↙"
    @ConfigField(path = "direction.west")
    var west: String = "←"
    @ConfigField(path = "direction.north-west")
    var northWest: String = "↖"
}