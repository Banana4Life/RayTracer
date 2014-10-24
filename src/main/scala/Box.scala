import java.awt.{Graphics, Rectangle}
import util.Point

class Box(a: Int, b: Int, c: Int, d: Int, material: Material) extends Rectangle(a, b, c, d) {
    implicit def Double2Int(d: Double) = d.toInt

    def render(g: Graphics) {
        g.setColor(material.getColor)
        g.fillRect(getX, getY, getWidth, getHeight)
    }

    def getVertices:Array[Point] = getVertices(this)
    def getVertices(rect: Rectangle) = {
        val result = Array(new Point(rect.getLocation), new Point(rect.getLocation), new Point(rect.getLocation), new Point(rect.getLocation))
        result(1).translate(rect.getWidth, 0)
        result(2).translate(0, rect.getHeight)
        result(3).translate(rect.getWidth, rect.getHeight)
        result
    }
}
