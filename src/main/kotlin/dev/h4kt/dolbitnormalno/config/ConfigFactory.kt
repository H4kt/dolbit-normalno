package dev.h4kt.dolbitnormalno.config

import kotlinx.serialization.*
import kotlinx.serialization.hocon.Hocon
import kotlinx.serialization.hocon.decodeFromConfig
import java.io.File
import com.typesafe.config.ConfigFactory as HoconConfigFactory

abstract class ConfigFactory<T : Any>(
    protected val defaultFileName: String,
    protected val format: SerialFormat = Hocon {}
) {

    abstract fun load(
        fileName: String = defaultFileName
    ): T

    @OptIn(ExperimentalSerializationApi::class)
    protected inline fun <reified T : Any> deserialize(
        fileName: String
    ): T {
        val file = File("config/$fileName")
        if (!file.exists()) {
            error("Config file config/$fileName does not exist")
        }

        return when (format) {
            is Hocon -> {
                val config = HoconConfigFactory.parseFile(file)
                format.decodeFromConfig(config)
            }
            is StringFormat ->
                format.decodeFromString(file.readText())
            is BinaryFormat ->
                format.decodeFromByteArray(file.readBytes())
            else ->
                throw UnsupportedOperationException("Unsupported serial format: ${format::class.simpleName}")
        }
    }

}
