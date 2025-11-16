package im.apeki.lnear

import im.apeki.lnear.configs.Messages
import im.apeki.lnear.util.text.Colors
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import java.util.Locale
import java.util.stream.Collectors

class LNearCommand : TabExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (args.isNotEmpty() && args[0].equals("reload", ignoreCase = true)) {
            if (sender.hasPermission("lnear.admin")) {
                sender.sendMessage(Colors.hex("&aКонфиги перезагружены"))
                Main.instance.settings.reload()
                Main.instance.messages.reload()
                return true
            }
        }

        if (sender !is Player) {
            sender.sendMessage(Colors.hex("&cЭта команда доступна только в игре"))
            return true
        }

        if (!sender.hasPermission("lnear.use")) {
            sender.sendMessage(Colors.apply(Messages.noPerms))
            return true
        }

        NearFunctions.collect(sender)
        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): List<String?> {
        if (args.size == 1 && sender.hasPermission("lnear.admin")) {
            return filter(listOf("reload"), args[0])
        }
        return emptyList()
    }

    private fun filter(complete: List<String>, start: String): MutableList<String?> {
        return complete.stream().filter { it: String ->
            it.lowercase(Locale.getDefault()).startsWith(start.lowercase(Locale.getDefault()))
        }.collect(Collectors.toList())
    }
}