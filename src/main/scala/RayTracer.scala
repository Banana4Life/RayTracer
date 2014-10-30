import java.util.concurrent.{TimeUnit, Executors}
import javax.swing.WindowConstants

import util.Color

import scala.math.abs
import scala.collection.mutable.ArrayBuffer
import scala.swing.Swing._
import scala.swing._
import scala.swing.event.{MouseReleased, MousePressed}

object RayTracer extends SimpleSwingApplication {
    implicit def whateverToRunnable[F](f: => F) = new Runnable() {
        def run() {
            f
        }
    }

    val executorService = Executors.newSingleThreadScheduledExecutor()

    val top = new MainFrame() {
        title = "Ray Tracer"
        preferredSize = 640 -> 480
        resizable = false


        var lights = ArrayBuffer(
            new LightSource(Array(new PointLight(250, 150)))
        )
        var boxes = ArrayBuffer(
            Box(200, 200, 100, 100, Material(0, 100, Color(255, 0, 0)))
        )

        var environment = new Environment(preferredSize, lights, boxes)

        override def dispose(): Unit = {
            super.dispose()

            executorService.shutdownNow()
        }

        val panel = new Panel {

            override protected def paintComponent(g: Graphics2D): Unit = {
                super.paintComponent(g)
                g.clearRect(0, 0, this.bounds.getWidth.toInt, this.bounds.getHeight.toInt)
                environment.update()
                g.drawImage(environment, 0, 0, null)
            }

            listenTo(mouse.clicks)

            var down: Point = null
            reactions += {
                case e: MousePressed => {
                    val p = e.point
                    down = p
                    e.peer.getButton match {
                        case 3 if canPlaceLightAt(p.x, p.y) => {
                          environment.lightSources.append(new LightSource(p.x, p.y))
                          repaint()
                        }
                        case _ =>
                    }
                }
                case e: MouseReleased => {
                  val p = e.point
                  val b = Box(down.x - abs((p.x - down.x) / 2) + (p.x - down.x) / 2, down.y - abs((p.y - down.y) / 2) + (p.y - down.y) / 2, abs(p.x - down.x), abs(p.y - down.y), Material(0, 100, Color(255, 0, 0)))
                  e.peer.getButton match {
                    case 1 if canPlaceBoxAt(b) => {
                      environment.boxes.append(b)
                      repaint()
                    }
                    case _ =>
                  }
                }
            }

          private def pointInBox(b: Box, x: Int, y: Int) = x >= b.x && x < b.x + b.width && y >= b.y && y < b.y + b.height

          private def pointInAnyBox(x: Int, y: Int): Boolean = {
            for (b <- environment.boxes) {
              if (pointInBox(b, x, y)) {
                return true
              }
            }
            false
          }

          private def canPlaceLightAt(x: Int, y: Int) = {
            for (l <- environment.lightSources) {
              val p = l.pointLights(0)
              if (p.x == x && p.y == y) {
                false
              }
            }

            !pointInAnyBox(x, y)
          }

          private def canPlaceBoxAt(box: Box): Boolean = {
            if (pointInAnyBox(box.x, box.y)) return false
            if (pointInAnyBox(box.x + box.width, box.y)) return false
            if (pointInAnyBox(box.x + box.width, box.y + box.height)) return false
            if (pointInAnyBox(box.x, box.y + box.height)) return false

            for (b <- environment.boxes) {
              if (pointInBox(box, b.x, b.y)) return false
              if (pointInBox(box, b.x + b.width, b.y)) return false
              if (pointInBox(box, b.x + b.width, b.y + b.height)) return false
              if (pointInBox(box, b.x, b.y + b.height)) return false
            }

            for (l <- environment.lightSources) {
              val p = l.pointLights(0)
              if (pointInBox(box, p.x, p.y)) {
                return false
              }
            }

            true
          }
        }

        contents = panel

        peer.setLocationRelativeTo(null)
        peer.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
    }

}
