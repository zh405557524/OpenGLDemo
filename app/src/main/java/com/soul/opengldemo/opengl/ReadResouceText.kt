package com.soul.opengldemo.opengl

import android.content.Context
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * Description: TODO
 * Author: 祝明
 * CreateDate: 2024/6/6 18:54
 * UpdateUser:
 * UpdateDate: 2024/6/6 18:54
 * UpdateRemark:
 */
class ReadResouceText {

    companion object {
        fun readResourceText(context: Context, resourceId: Int): String {
            val body = StringBuilder()
            try {
                val inputStream = context.resources.openRawResource(resourceId)
                val inputStreamReader = InputStreamReader(inputStream)
                val bufferedReader = BufferedReader(inputStreamReader)
                var nextLine: String?
                while (bufferedReader.readLine().also { nextLine = it } != null) {
                    body.append(nextLine)
                    body.append("\n")
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return body.toString()
        }

    }
}