package util

import java.awt.image.BufferedImage

import scala.swing.Dimension

trait LightMap {
    var lightMap: Array[Array[(Double, Boolean)]]

    def rasterize(shadow: (Ray, Array[Point], Ray))(lightCount: Int) {
        lightMap = LightMap.rasterize(shadow)(lightMap, lightCount)(lightMap.length, lightMap(0).length)
    }

    def render(image: BufferedImage) = LightMap.render(image, lightMap)

    def reset() = lightMap = lightMap.map(a => a.map(e => (e._1, false)))
}

object LightMap {
    var colorMap: Map[Double, Int] = Map()

    def newMap(dim: Dimension): Array[Array[(Double, Boolean)]] = {
        val lightMap = new Array[Array[(Double, Boolean)]](dim.getWidth.toInt)
        for (x <- 0 to dim.getWidth.toInt - 1) {
            lightMap(x) = new Array[(Double, Boolean)](dim.getHeight.toInt)
            for (y <- 0 to dim.getHeight.toInt - 1) {
                lightMap(x)(y) = (1.0, false)
            }
        }
        lightMap
    }

    def rasterize(shadow: (Ray, Array[Point], Ray))(lightMap: Array[Array[(Double, Boolean)]], lightCount: Int)(width: Int, height: Int): Array[Array[(Double, Boolean)]] = {
        var activeEdgeList: List[(Int, Double, Double, Int)] = List()
        // Edge for first ray
        activeEdgeList ::= getActiveEdge(new Ray(getBorderIntersection(shadow._1)(width, height), shadow._1.getAnchor2))
        // other edges
        if (shadow._1.getAnchor2.x == shadow._2(0).x) activeEdgeList ::= getActiveEdge(new Ray(shadow._1.getAnchor2, shadow._2(0)))
        if (shadow._3.getAnchor2.x == shadow._2(0).x) activeEdgeList ::= getActiveEdge(new Ray(shadow._3.getAnchor2, shadow._2(0)))
        if (shadow._2.length > 1) {
            if (shadow._2(0).x == shadow._2(1).x) activeEdgeList ::= getActiveEdge(new Ray(shadow._2(0), shadow._2(1)))
            if (shadow._1.getAnchor2.x == shadow._2(1).x) activeEdgeList ::= getActiveEdge(new Ray(shadow._1.getAnchor2, shadow._2(1)))
            if (shadow._3.getAnchor2.x == shadow._2(1).x) activeEdgeList ::= getActiveEdge(new Ray(shadow._3.getAnchor2, shadow._2(1)))
        }
        // Edge for second ray
        activeEdgeList ::= getActiveEdge(new Ray(shadow._3.getAnchor2, getBorderIntersection(shadow._3)(width, height)))
        // Edge at x = 0
        if (getBorderIntersection(shadow._1)(width, height).x == 0 || getBorderIntersection(shadow._3)(width, height).x == 0) activeEdgeList ::= getActiveEdge(new Ray(new Point(0, getBorderIntersection(shadow._1)(width, height).y), new Point(0, getBorderIntersection(shadow._3)(width, height).y)))
        if (getBorderIntersection(shadow._1)(width, height).x > 0 && getBorderIntersection(shadow._3)(width, height).x > 0 && getBorderIntersection(shadow._1)(width, height).x < shadow._1.getAnchor1.x) activeEdgeList ::= getActiveEdge(new Ray(new Point(0, 0), new Point(0, height)))
        rasterize(activeEdgeList)(lightMap, lightCount)(width, height)
    }

    def rasterize(edgeList: List[(Int, Double, Double, Int)])(lightMap: Array[Array[(Double, Boolean)]], lightCount: Int)(width: Int, height: Int): Array[Array[(Double, Boolean)]] = {
        var y = 0
        var activeEdgeList = edgeList
        while (activeEdgeList.length > 0 && y < height) {
            activeEdgeList = activeEdgeList.sortWith((a, b) => if (a._1 == b._1) a._2 < b._2 else a._1 < b._1)
            while (y == activeEdgeList(0)._1) {
                for (x <- activeEdgeList(0)._2.toInt to (if (activeEdgeList.length > 1 && y == activeEdgeList(1)._1) activeEdgeList(1)._2.toInt else width) - 1) {
                    if (!lightMap(x)(y)._2) lightMap(x)(y) = (if (lightMap(x)(y)._1 - 1 / lightCount.toDouble > 0) lightMap(x)(y)._1 - 1 / lightCount.toDouble else 0, true)
                }
                activeEdgeList = (activeEdgeList :+ incrementEdge(activeEdgeList(0))).drop(1)
                if (activeEdgeList.length > 1 && y == activeEdgeList(0)._1) activeEdgeList = (activeEdgeList :+ incrementEdge(activeEdgeList(0))).drop(1)
            }
            activeEdgeList = activeEdgeList.filter(a => a._4 != 0)
            y += 1
        }
        lightMap
    }

    def incrementEdge(edge: (Int, Double, Double, Int)): (Int, Double, Double, Int) = {
        (edge._1 + 1, edge._2 + edge._3, edge._3, edge._4 - 1)
    }

    def getActiveEdge(ray: Ray): (Int, Double, Double, Int) = {
        val anchor = if (ray.getAnchor1.y < ray.getAnchor2.y) ray.getAnchor1 else ray.getAnchor2
        (anchor.y, anchor.x, if (ray.direction.y != 0) ray.direction.x / ray.direction.y.toDouble else 0, math.abs(ray.direction.y))
    }

    def getBorderIntersection(ray: Ray)(width: Int, height: Int): Point = {
        ray.getFor(math.min(math.max((0 - ray.getAnchor1.x) / ray.direction.x.toDouble, (width - ray.getAnchor1.x) / ray.direction.x.toDouble), math.max((0 - ray.getAnchor1.y) / ray.direction.y.toDouble, (height - ray.getAnchor1.y) / ray.direction.y.toDouble)))
    }

    def render(image: BufferedImage, lightMap: Array[Array[(Double, Boolean)]]) {
        for (x <- 0 to math.max(0, math.min(lightMap.length, image.getWidth) - 1)) {
            for (y <- 0 to math.max(0, math.min(lightMap(0).length, image.getHeight) - 1)) {
                if (!colorMap.contains(lightMap(x)(y)._1)) {
                    colorMap += (lightMap(x)(y)._1 -> new java.awt.Color(0, 0, 0, 1 - lightMap(x)(y)._1.toFloat).getRGB)
                }
                image.setRGB(x, y, colorMap(lightMap(x)(y)._1))
            }
        }
    }
}
