import java.awt.{Color, Graphics}

import util.LightMap

case class LightSource(pointLights: Array[PointLight]) {
    def this(x: Int, y: Int) {
        this(Array(new PointLight(x, y), new PointLight(x - 3, y), new PointLight(x, y - 3), new PointLight(x + 3, y), new PointLight(x, y + 3)))
    }

    def drawShadow(lightMap: LightMap, box: Box, lightCount: Int) = {
        for (pointLight <- pointLights) {
            pointLight.renderShadow(lightMap, box, lightCount)
            lightMap.reset()
        }
    }

    def render(g: Graphics) {
        g.setColor(Color.YELLOW)
        g.fillOval(pointLights(0).x - 3, pointLights(0).y - 3, 6, 6)
    }

    def lightCount = pointLights.length
}
