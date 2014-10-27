import util.LightMap

class LightSource(pointLights: Array[PointLight]) {
    def drawShadow(lightMap: LightMap, box: Box, lightCount: Int) = {
        for (pointLight <- pointLights) {
            pointLight.renderShadow(lightMap, box, lightCount)
        }
    }

    def lightCount = pointLights.length
}
