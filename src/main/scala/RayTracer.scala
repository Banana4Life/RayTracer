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
                    e.peer.getButton match {
                        case 1 => down = e.point
                        case 3 => environment.lightSources.append(new LightSource(Array(new PointLight(e.point.x, e.point.y))))
                        case _ =>
                    }
                }
                case e: MouseReleased => e.peer.getButton match {
                    case 1 => environment.boxes.append(Box(down.x - abs((e.point.x - down.x) / 2) + (e.point.x - down.x) / 2, down.y - abs((e.point.y - down.y) / 2) + (e.point.y - down.y) / 2, abs(e.point.x - down.x), abs(e.point.y - down.y), Material(0, 100, Color(123, 123, 123))))
                    case _ =>
                }
            }
        }

        contents = panel

        executorService.scheduleAtFixedRate({
            panel.repaint()
        }, 33, 33, TimeUnit.MILLISECONDS)

        peer.setLocationRelativeTo(null)
        peer.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
    }

}
