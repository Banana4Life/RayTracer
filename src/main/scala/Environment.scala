import java.awt.Graphics2D
import java.awt.image.BufferedImage

import util.LightMap

import scala.swing.Dimension

class Environment(dim: Dimension, lightSources: Array[LightSource], boxes: Array[Box]) extends BufferedImage(dim.getWidth.toInt, dim.getHeight.toInt, BufferedImage.TYPE_INT_ARGB) with LightMap {
    override var lightMap: Array[Array[(Double, Boolean)]] = LightMap.newMap(dim)
    private val boxImage = new BufferedImage(dim.getWidth.toInt, dim.getHeight.toInt, BufferedImage.TYPE_INT_ARGB)
    private val lightImage = new BufferedImage(dim.getWidth.toInt, dim.getHeight.toInt, BufferedImage.TYPE_INT_ARGB)

    def update() {
        for (box <- boxes) {
            box.render(boxImage.getGraphics.asInstanceOf[Graphics2D])
        }
        for (lightSource <- lightSources) {
            for (box <- boxes) {
                lightSource.drawShadow(this, box, lightCount)
            }
            reset()
        }
        render(lightImage)
        getGraphics.drawImage(boxImage, 0, 0, null)
        getGraphics.drawImage(lightImage, 0, 0, null)
    }

    def lightCount: Int = lightCount(this.lightSources)
    def lightCount(lightSources: Array[LightSource]) = lightSources.foldRight(0){(e, a) => a + e.lightCount}
}