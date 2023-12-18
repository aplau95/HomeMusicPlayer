package com.example.homemusicplayer.service

import java.io.BufferedReader
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader

/**
 * Handles the parsing of data sent through the socket to trigger the callback
 */
class TcpClientHandler(
    private val dataInputStream: DataInputStream,
    private val dataOutputStream: DataOutputStream,
    private val callback: () -> Unit
) : Thread() {

    override fun run() {
        while (true) {
            try {
                if (dataInputStream.available() > 0) {
                    val reader = BufferedReader(InputStreamReader(dataInputStream))
                    if (reader.readLine() == "Test") callback.invoke()
                    dataOutputStream.writeUTF("Hello Client")
                    sleep(2000L)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                try {
                    dataInputStream.close()
                    dataOutputStream.close()
                } catch (ex: IOException) {
                    ex.printStackTrace()
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
                try {
                    dataInputStream.close()
                    dataOutputStream.close()
                } catch (ex: IOException) {
                    ex.printStackTrace()
                }
            }
        }
    }

    companion object {

        private val TAG = TcpClientHandler::class.java.simpleName
    }

}