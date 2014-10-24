import java.awt.Graphics

class LightSource(pointLights: Array[PointLight]) {
    def renderShadow(g: Graphics, box: Box, lightCount: Int) = {
        for (pointLight <- pointLights) {
            pointLight.renderShadow(g, box, lightCount)
        }
    }

    def lightCount = pointLights.length
}
