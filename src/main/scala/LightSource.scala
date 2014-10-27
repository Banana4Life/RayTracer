import java.awt.Graphics2D

class LightSource(pointLights: Array[PointLight]) {
    def renderShadow(g2D: Graphics2D, box: Box) = {
        for (pointLight <- pointLights) {
            pointLight.renderShadow(g2D, box)
        }
    }

    def lightCount = pointLights.length
}
