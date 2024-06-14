package com.soul.opengldemo.opengl.arrows.obj

import android.content.Context

/**
 * Description: TODO
 * Author: 祝明
 * CreateDate: 2024/6/14 16:26
 * UpdateUser:
 * UpdateDate: 2024/6/14 16:26
 * UpdateRemark:
 */
data class TextureCoord(val u: Float, val v: Float)
data class Normal(val x: Float, val y: Float, val z: Float)
data class Face(val indices: List<Int>)


fun loadOBJFile(context: Context, fileName: String): String {
    return context.assets.open(fileName).bufferedReader().use {
        it.readText()
    }
}
data class Vertex(val x: Float, val y: Float, val z: Float)

fun parseOBJ(fileContent: String): Pair<FloatArray, IntArray> {
    val vertexList = mutableListOf<Vertex>()
    val indexList = mutableListOf<Int>()

    fileContent.lines().forEach { line ->
        when {
            line.startsWith("v ") -> {
                val parts = line.split(" ").map { it.trim() }
                vertexList.add(Vertex(parts[1].toFloat(), parts[2].toFloat(), parts[3].toFloat()))
            }
            line.startsWith("f ") -> {
                val parts = line.split(" ").map { it.trim() }
                parts.drop(1).forEach {
                    val vertexIndex = it.split("/")[0].toInt() - 1  // OBJ index starts at 1
                    indexList.add(vertexIndex)
                }
            }
        }
    }

    val vertices = FloatArray(vertexList.size * 3) { index ->
        when (index % 3) {
            0 -> vertexList[index / 3].x
            1 -> vertexList[index / 3].y
            2 -> vertexList[index / 3].z
            else -> 0f // never happens
        }
    }

    val indices = indexList.toIntArray()

    return Pair(vertices, indices)
}
