package im.apeki.lnear.util.text

import im.apeki.lnear.configs.Messages
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

object Colors {
    fun hex(text: String): Component {
        val base = Component.empty().decoration(TextDecoration.ITALIC, false)
        val coloredText = LegacyComponentSerializer.legacyAmpersand().deserialize(text)
        return base.append(coloredText)
    }

    fun hex(texts: List<String>): List<Component> {
        return texts.map { hex(it) }
    }

    fun miniMessage(text: String): Component {
        return MiniMessage.miniMessage()
            .deserialize(text)
    }

    fun miniMessage(texts: List<String>): List<Component> {
        return texts.map { miniMessage(it) }
    }

    fun apply(text: String): Component {
        return if (Messages.colorEngine.equals("minimessage", ignoreCase = true)) {
            miniMessage(text)
        } else {
            hex(text)
        }
    }

    fun apply(text: List<String>): List<Component> {
        return if (Messages.colorEngine.equals("minimessage", ignoreCase = true)) {
            miniMessage(text)
        } else {
            hex(text)
        }
    }
}