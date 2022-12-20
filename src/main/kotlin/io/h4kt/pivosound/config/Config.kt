package io.h4kt.pivosound.config

import com.typesafe.config.ConfigFactory
import java.io.File

object Config {

    private val configFile: File
        get() {

            var configFile = File("application.conf")
            if (!configFile.exists()) {
                configFile = File("src/main/resources/application.conf")
            }

            return configFile
        }

    private val root = ConfigFactory.parseFile(configFile)

    object Discord {

        val token: String
            get() = root.getString("discord.token")

    }

    object YouTube {

        private val handle = root.getConfig("youtube")

        val username = handle.getString("username")
        val password = handle.getString("password")

    }

}