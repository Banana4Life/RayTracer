package util

class Ray(anchor1: Point, anchor2: Point) {
    def getAnchor1 = anchor1
    def getAnchor2 = anchor2

    def direction = anchor2 - anchor1
    def angleTo(ray: Ray) = math.abs(direction.angle - ray.direction.angle)
    def getFor(k: Double) = getAnchor1 + (direction * k)
}
