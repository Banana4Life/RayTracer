import java.awt.{Color, Graphics}

import util.LightMap

import scala.collection.mutable.ArrayBuffer

case class LightSource(pointLights: Array[PointLight]) {
    def this(x: Int, y: Int) {
        this(Array(new PointLight(x, y), new PointLight(x - 3, y), new PointLight(x, y - 3), new PointLight(x + 3, y), new PointLight(x, y + 3)))
    }

    def drawShadow(lightMap: LightMap, boxes: ArrayBuffer[Box], lightCount: Int) = {
        for (pointLight <- pointLights) {
            for (box <- boxes) {
                pointLight.renderShadow(lightMap, box, lightCount)
            }
            lightMap.reset()
        }
    }

    def render(g: Graphics) {
        g.setColor(new Color(255, 100, 0))
        g.fillOval(pointLights(0).x - 3, pointLights(0).y - 3, 6, 6)
    }

    def lightCount = pointLights.length
}
