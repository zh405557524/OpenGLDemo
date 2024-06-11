package com.soul.opengldemo.opengl

/**
 * Description: 图形零件
 * Author: 祝明
 * CreateDate: 2024/6/11 15:21
 * UpdateUser:
 * UpdateDate: 2024/6/11 15:21
 * UpdateRemark:
 */
interface IPart {

    /**
     * 初始化-图形相关数据
     */
    fun init()

    /**
     * 测量-设置试图大小
     */
    fun measure(width: Int, height: Int)

    /**
     * 绘制图形
     */
    fun draw()

}