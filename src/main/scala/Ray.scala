import util.Point

class Ray(anchor1: Point, anchor2: Point) {
    val direction = anchor2 - anchor1

    def getAnchor2 = anchor2

    def angleTo(ray: Ray) = math.abs(direction.angle - ray.direction.angle)
}
