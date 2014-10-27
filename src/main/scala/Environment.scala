import java.awt.{AlphaComposite, Graphics2D, Color}
import java.awt.image.BufferedImage

import scala.swing.Dimension

class Environment(dim: Dimension, lightSources: Array[LightSource], boxes: Array[Box]) extends BufferedImage(dim.getWidth.toInt, dim.getHeight.toInt, BufferedImage.TYPE_INT_ARGB) {
    def update() {
        val g2D = this.getGraphics.asInstanceOf[Graphics2D]
        g2D.setColor(Color.WHITE)
        g2D.fillRect(0, 0, this.getWidth, this.getHeight)
        g2D.setColor(new Color(0, 0, 0, 1f / this.lightCount))
        g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER))
        for (lightSource <- lightSources) {
            for (box <- boxes) {
                lightSource.renderShadow(g2D, box)
            }
        }
        for (box <- boxes) {
            box.render(g2D)
        }
    }

    def lightCount: Int = lightCount(this.lightSources)
    def lightCount(lightSources: Array[LightSource]) = lightSources.foldRight(0){(e, a) => a + e.lightCount}
}