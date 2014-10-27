import java.awt.Graphics2D
import java.awt.image.BufferedImage

import util.LightMap

import scala.collection.mutable.ArrayBuffer
import scala.swing.Dimension

class Environment(dim: Dimension, val lightSources: ArrayBuffer[LightSource], val boxes: ArrayBuffer[Box]) extends BufferedImage(dim.getWidth.toInt, dim.getHeight.toInt, BufferedImage.TYPE_INT_ARGB) with LightMap {
    override var lightMap: Array[Array[(Double, Boolean)]] = null
    private val boxImage = new BufferedImage(dim.getWidth.toInt, dim.getHeight.toInt, BufferedImage.TYPE_INT_ARGB)
    private val lightImage = new BufferedImage(dim.getWidth.toInt, dim.getHeight.toInt, BufferedImage.TYPE_INT_ARGB)

    def update() {
      this.lightMap = LightMap.newMap(dim)
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
    def lightCount(lightSources: Seq[LightSource]) = lightSources.foldRight(0){(e, a) => a + e.lightCount}
}
