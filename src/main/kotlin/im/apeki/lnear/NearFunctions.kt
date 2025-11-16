package im.apeki.lnear

import im.apeki.lnear.configs.Messages
import im.apeki.lnear.util.text.Colors
import im.apeki.lnear.configs.Settings
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType

object NearFunctions {
    fun collect(player: Player) {
        val radius = findMaxRadius(player)

        val players = player.world.players
            .filter { it.world.name == player.world.name &&
                    it.location.distance(player.location) < radius &&
                    !isVanished(it) &&
                    !(Settings.hideInvisibility && it.hasPotionEffect(PotionEffectType.INVISIBILITY)) &&
                    it.name != player.name
            }

        if (players.isEmpty()) {
            player.sendMessage(Colors.apply(Messages.notFound))
            return
        }

        player.sendMessage(Colors.apply(
            Messages.header
                .replace("{amount}", players.size.toString())
                .replace("{radius}", radius.toString())
        ))

        var message = ""
        for (p in players) {
            message += Messages.text
                .replace("{player}", p.name)
                .replace("{distance}", p.location.distance(player.location).toInt().toString())
                .replace("{direction}", getDirectionArrow(player.location, p.location))

            if (p != players[players.size - 1]) {
                message += Messages.separator
            }
        }
        player.sendMessage(Colors.apply(message))

        player.sendMessage(Colors.apply(
            Messages.footer
                .replace("{amount}", players.size.toString())
                .replace("{radius}", radius.toString())
        ))
    }

    private fun findMaxRadius(player: Player): Int {
        val maxRadius = Settings.radiusPerGroup
            .filterKeys { player.hasPermission("lnear.radius.$it") }
            .values
            .maxOrNull()

        return if (maxRadius != null) {
            maxOf(Settings.defaultRadius, maxRadius)
        } else {
            Settings.defaultRadius
        }
    }

    private fun isVanished(player: Player): Boolean {
        for (value in player.getMetadata("vanished")) {
            if (value.asBoolean()) {
                return true
            }
        }
        return false
    }

    private fun getDirectionArrow(from: Location, to: Location): String {
        val directionVector = to.toVector().subtract(from.toVector())
        val tempLocation = Location(from.world, 0.0, 0.0, 0.0)
        tempLocation.direction = directionVector
        val targetYaw = tempLocation.yaw

        var deltaAngle = from.yaw - targetYaw

        while (deltaAngle <= -180) deltaAngle += 360
        while (deltaAngle > 180) deltaAngle -= 360

        return when {
            deltaAngle >= 157.5 || deltaAngle < -157.5 -> Messages.south
            deltaAngle >= 112.5 -> Messages.southWest
            deltaAngle >= 67.5 -> Messages.west
            deltaAngle >= 22.5 -> Messages.northWest
            deltaAngle >= -22.5 -> Messages.north
            deltaAngle >= -67.5 -> Messages.northEast
            deltaAngle >= -112.5 -> Messages.east
            deltaAngle >= -157.5 -> Messages.southEast
            else -> "?"
        }
    }
}