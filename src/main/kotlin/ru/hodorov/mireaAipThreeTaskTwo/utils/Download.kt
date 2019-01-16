package ru.hodorov.mireaAipThreeTaskTwo.utils

import mu.KotlinLogging
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class Download(private val url: URL, private val file: File) : Observable(), Runnable {
    private val log = KotlinLogging.logger { }

    public var size: Int = -1
        private set
    public var downloaded: Int = 0
        private set
    public val progress: Float
        get() = downloaded.toFloat() / size * 100
    public var status: DownloadStatus = DownloadStatus.DOWNLOADING
        private set(value) {
            field = value
            stateChanged()
        }
    private val thread = Thread(this)

    init {
        thread.start()
    }

    fun cancel() {
        status = DownloadStatus.CANCELLED
    }

    private fun error() {
        status = DownloadStatus.ERROR
    }

    override fun run() {
        var connection: HttpURLConnection? = null
        var fos: FileOutputStream? = null
        try {
            connection = url.openConnection() as HttpURLConnection
            connection.connect()
            // Make sure response code is in the 200 range.
            if (connection.responseCode / 100 != 2) {
                error()
            }

            val contentLength = connection.contentLength
            if (contentLength < 1) {
                error()
            }

            if (size == -1) {
                size = contentLength
                stateChanged()
            }
            fos = FileOutputStream(file)
            while (status == DownloadStatus.DOWNLOADING) {
                /* Size buffer according to how much of the file is left to download. */
                val buffer = ByteArray(Math.min(size - downloaded, MAX_BUFFER_SIZE))
                // Read from server into buffer.
                val read = connection.inputStream.read(buffer)
                if (read == -1)
                    break
                // Write buffer to file.
                fos.write(buffer, 0, read)
                downloaded += read
                stateChanged()
            }
            if (status == DownloadStatus.DOWNLOADING) {
                connection.disconnect()
                fos.close()
                status = DownloadStatus.COMPLETE
            }
        } catch (e: Exception) {
            log.error(e) { "Error while download file" }
            connection?.disconnect()
            fos?.close()
            error()
        }
    }

    private fun stateChanged() {
        setChanged()
        notifyObservers()
    }

    companion object {
        private val MAX_BUFFER_SIZE = 1000000 //Bytes
    }

    enum class DownloadStatus {
        DOWNLOADING,
        COMPLETE,
        CANCELLED,
        ERROR
    }
}