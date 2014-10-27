import java.awt.Graphics2D
import java.awt.image.BufferedImage

import util.LightMap

import scala.swing.Dimension

class Environment(dim: Dimension, lightSources: Array[LightSource], boxes: Array[Box]) extends BufferedImage(dim.getWidth.toInt, dim.getHeight.toInt, BufferedImage.TYPE_INT_ARGB) with LightMap {
    override var lightMap: Array[Array[Double]] = LightMap.newMap(dim)

    def update() {
        for (lightSource <- lightSources) {
            for (box <- boxes) {
                lightSource.drawShadow(this, box, lightCount)
            }
        }
        render(this)
        for (box <- boxes) {
            box.render(getGraphics.asInstanceOf[Graphics2D])
        }
    }

    def lightCount: Int = lightCount(this.lightSources)
    def lightCount(lightSources: Array[LightSource]) = lightSources.foldRight(0){(e, a) => a + e.lightCount}
}