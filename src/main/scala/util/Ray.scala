package util

class Ray(anchor1: Point, anchor2: Point) {
    def getAnchor1 = anchor1

    def getAnchor2 = anchor2

    def direction = anchor2 - anchor1

    def angleTo(ray: Ray): Double = {
        math.acos(limit(this.direction * ray.direction / (this.direction.length * ray.direction.length), -1, 1))
    }

    def limit(x: Double, min: Double, max: Double) = if (x < min) min else if (x > max) max else x

    def getFor(k: Double) = getAnchor1 + (direction * k)

    def intersectAsLine(ray: Ray): Boolean = {
        val c = this.getAnchor1
        val d = this.direction
        val a = ray.getAnchor1
        val b = ray.direction
        if (d.y != 0) {
            val k = (c.x + d.x * (a.y - c.y) / d.y.toDouble - a.x) / (b.x - d.x * b.y / d.y.toDouble)
            k > 0 && k < 1
        } else {
            if (a.y > c.y) a.y + b.y < c.y
            else a.y + b.y > c.y
        }
    }
}
