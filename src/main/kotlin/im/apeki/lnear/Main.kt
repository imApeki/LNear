package im.apeki.lnear

import im.apeki.lnear.configs.Messages
import im.apeki.lnear.configs.Settings
import im.apeki.lnear.util.config.Config
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
    companion object {
        lateinit var instance: Main
            private set
    }

    lateinit var settings: Config
        private set

    lateinit var messages: Config
        private set

    override fun onEnable() {
        instance = this
        settings = Config(this, Settings::class.java)
        messages = Config(this, Messages::class.java)

        val command = checkNotNull(getCommand("lnear")) {
            "Команда не найдена"
        }

        val lNearCommand = LNearCommand()
        command.setExecutor(lNearCommand)
        command.tabCompleter = lNearCommand
    }
}