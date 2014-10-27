package util

import scala.swing.Dimension

class LightMap(var lightMap: Array[Array[Int]]) {
    def this(dim: Dimension) = this(LightMap.newMap(dim))

    def rasterize(shadow: (Ray, Point, Ray)) {
        lightMap = LightMap.rasterize(shadow, lightMap)
    }
}

object LightMap {
    def newMap(dim: Dimension): Array[Array[Int]] = {
        val lightMap = new Array[Array[Int]](dim.getWidth.toInt)
        for (x <- 0 to dim.getWidth.toInt - 1) {
            lightMap(x) = new Array[Int](dim.getHeight.toInt)
            for (y <- 0 to dim.getHeight.toInt - 1) {
                lightMap(x)(y) = 0
            }
        }
        lightMap
    }

    def rasterize(shadow: (Ray, Point, Ray), lightMap: Array[Array[Int]]): Array[Array[Int]] = {
        return lightMap
    }
}