package ru.hodorov.mireaAipThreeTaskTwo.service

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import javax.annotation.PostConstruct

@Service
class AppFolderService {
    private val log = KotlinLogging.logger { }

    @Value("\${app.appFolderName}")
    private lateinit var appFolderName: String

    private lateinit var appFolder: File

    @PostConstruct
    fun postConstruct() {
        val os = System.getProperty("os.name").toUpperCase()
        log.debug { "OS: $os" }
        var appFolderPath: String
        when {
            os.contains("WIN") -> {
                log.debug { "Found windows" }
                appFolderPath = "${System.getenv("APPDATA")}\\$appFolderName"
            }
            os.contains("MAC") -> {
                log.debug { "Found mac" }
                appFolderPath = "${System.getProperty("user.home")}/Library/Application Support/$appFolderName"
            }
            os.contains("NUX") -> {
                log.debug { "Found linux" }
                appFolderPath = "${System.getProperty("user.dir")}/.$appFolderName"
            }
            else -> {
                throw IllegalStateException("Unknown OS: $os")
            }
        }
        appFolder = File(appFolderPath)

        if (!appFolder.exists()) {
            log.info { "App folder not fount. Creating" }
            appFolder.mkdirs()
        }
    }

    fun getFile(filename: String): File {
        val file = File("${appFolder.canonicalPath}/$filename")
        if(!file.exists()) {
            file.createNewFile()
        }
        return file
    }

    fun getAppFolder(): File {
        return appFolder
    }
}