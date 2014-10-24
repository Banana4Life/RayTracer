import java.awt.Color
import java.awt.image.BufferedImage

import scala.swing.Dimension

class Environment(dim: Dimension, lightSources: Array[LightSource], boxes: Array[Box]) extends BufferedImage(dim.getWidth.toInt, dim.getHeight.toInt, BufferedImage.TYPE_INT_ARGB) {
    def update() {
        this.getGraphics.setColor(Color.WHITE)
        this.getGraphics.fillRect(0, 0, this.getWidth, this.getHeight)
        for (lightSource <- lightSources) {
            for (box <- boxes) {
                lightSource.renderShadow(this, box, this.lightCount)
                box.render(this.getGraphics)
            }
        }
    }

    def lightCount: Int = lightCount(this.lightSources)
    def lightCount(lightSources: Array[LightSource]) = lightSources.foldRight(0){(e, a) => a + e.lightCount}
}