package im.apeki.lnear.util.config

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.nio.charset.StandardCharsets

class Config(private val plugin: JavaPlugin, private val configClass: Class<*>) {
    private var config: FileConfiguration? = null
    private var fileName: String = ""

    init {
        init()
    }

    private fun init() {
        val configFileAnno = configClass.getAnnotation(ConfigFile::class.java)
        checkNotNull(configFileAnno) { "Класс " + configClass.getName() + " должен быть обозначен @ConfigFile" }
        fileName = configFileAnno.name

        val dataFolder = plugin.dataFolder
        if (!dataFolder.exists()) {
            check(dataFolder.mkdirs()) { "Не удалось создать папку плагина" }
        }

        val configFile = File(dataFolder, fileName)
        config = YamlConfiguration.loadConfiguration(configFile)

        val configChanged = loadConfig("")

        if (!configFileAnno.header.isEmpty()) {
            config!!.options().header(configFileAnno.header)
        }

        if (configChanged || !configFile.exists()) {
            save()
        }
    }

    private fun loadConfig(prefix: String?): Boolean {
        var changed = false
        for (field in configClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(ConfigField::class.java)) {
                val anno = field.getAnnotation(ConfigField::class.java)
                val path = anno.path.ifEmpty { prefix + field.name }

                if (config!!.contains(path)) {
                    field.isAccessible = true

                    if (Map::class.java.isAssignableFrom(field.type)) {
                        val section = config!!.getConfigurationSection(path)
                        if (section != null) {
                            val mapValue = section.getValues(false)
                            field.set(null, mapValue)
                        }
                    } else {
                        val value = config!!.get(path)
                        if (field.type.isInstance(value)) {
                            field.set(null, value)
                        }
                    }
                } else {
                    field.isAccessible = true
                    config!!.set(path, field.get(null))
                    changed = true
                }
            }
        }
        for (nestedClass in configClass.declaredClasses) {
            val newPrefix = prefix + nestedClass.simpleName + "."
            if (loadConfig(newPrefix)) {
                changed = true
            }
        }
        return changed
    }

    fun save() {
        saveConfigRecursive("")

        val configFileAnno = configClass.getAnnotation(ConfigFile::class.java)
        if (configFileAnno != null && !configFileAnno.header.isEmpty()) {
            config!!.options().header(configFileAnno.header)
        }

        val configFile = File(plugin.dataFolder, fileName)
        config!!.save(configFile)
        addComments(configFile)
    }

    private fun saveConfigRecursive(prefix: String?) {
        for (field in configClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(ConfigField::class.java)) {
                val anno = field.getAnnotation(ConfigField::class.java)
                val path = anno.path.ifEmpty { prefix + field.name }
                field.isAccessible = true
                config!!.set(path, field.get(null))
            }
        }
        for (nestedClass in configClass.getDeclaredClasses()) {
            val newPrefix = prefix + nestedClass.getSimpleName() + "."
            saveConfigRecursive(newPrefix)
        }
    }

    fun reload() {
        val configFile = File(plugin.dataFolder, fileName)
        config = YamlConfiguration.loadConfiguration(configFile)
        loadConfig("")
    }

    private fun addComments(configFile: File) {
        val comments: MutableMap<String, String?> = HashMap()
        collectComments("", comments)

        val yamlContent = StringBuilder()
        BufferedReader(FileReader(configFile, StandardCharsets.UTF_8)).use { reader ->
            var line: String?
            while ((reader.readLine().also { line = it }) != null) {
                val trimmedLine = line!!.trim { it <= ' ' }
                if (!trimmedLine.isEmpty() && !trimmedLine.startsWith("#")) {
                    val parts: Array<String?> = trimmedLine.split(":".toRegex(), limit = 2).toTypedArray()
                    if (parts.isNotEmpty()) {
                        val key = parts[0]!!.trim { it <= ' ' }
                        val fullPath = findFullPath(key, comments)
                        if (fullPath != null && comments.containsKey(fullPath)) {
                            yamlContent.append("  # ").append(comments[fullPath]).append("\n")
                        }
                    }
                }
                yamlContent.append(line).append("\n")
            }
        }
        FileWriter(configFile, StandardCharsets.UTF_8).use { writer ->
            writer.write(yamlContent.toString())
        }
    }

    private fun collectComments(prefix: String?, comments: MutableMap<String, String?>) {
        for (field in configClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(ConfigField::class.java)) {
                val anno = field.getAnnotation(ConfigField::class.java)
                val path = anno.path.ifEmpty { prefix + field.name }
                if (!anno.comment.isEmpty()) {
                    comments[path] = anno.comment
                }
            }
        }
        for (nestedClass in configClass.getDeclaredClasses()) {
            val newPrefix = prefix + nestedClass.getSimpleName() + "."
            collectComments(newPrefix, comments)
        }
    }

    private fun findFullPath(key: String?, comments: MutableMap<String, String?>): String? {
        for (path in comments.keys) {
            if (path.endsWith(".$key") || path == key) {
                return path
            }
        }
        return null
    }
}

