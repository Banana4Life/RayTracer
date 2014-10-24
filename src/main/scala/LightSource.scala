import java.awt.image.BufferedImage

class LightSource(pointLights: Array[PointLight]) {
    def renderShadow(image: BufferedImage, box: Box, lightCount: Int) = {
        for (pointLight <- pointLights) {
            pointLight.renderShadow(image, box, lightCount)
        }
    }

    def lightCount = pointLights.length
}
