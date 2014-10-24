import java.awt.image.BufferedImage

import util.Point

class PointLight(a: Int, b: Int, brightness: Int) extends Point(a, b) {
    implicit def Tupel32Tupel2(t: (Double, Ray, Ray)) = (t._2, t._3)

    def renderShadow(image: BufferedImage, box: Box, lightCount: Int) = {
        val rays = new Array[Ray](4)
        for (i <- 0 to 3) {
            rays(i) = new Ray(new Point(this), box.getVertices(i))
        }
        val ray1, ray2 = getFurthest(rays)
    }

    def getFurthest(ray: Ray, rays: Array[Ray]): (Double, Ray, Ray) = {
        if (rays.length < 2) {
            (ray angleTo rays(0), ray, rays(0))
        } else {

        }
    }
    def getFurthest(rays: Array[Ray]): (Ray, Ray) = getFurthest(rays(0), rays.drop(1))
}
