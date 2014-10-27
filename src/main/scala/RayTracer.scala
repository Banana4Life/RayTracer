import java.util.concurrent.{TimeUnit, Executors}
import javax.swing.WindowConstants

import util.Color

import scala.swing.Swing._
import scala.swing._
import scala.swing.event.{MouseReleased, MousePressed}

object RayTracer extends SimpleSwingApplication {
    implicit def whateverToRunnable[F](f: => F) = new Runnable() { def run() { f } }

    val executorService = Executors.newSingleThreadScheduledExecutor()

    val top = new MainFrame() {
        title = "Ray Tracer"
        preferredSize = 640 -> 480
        resizable = false

        var lights = Array(
          new LightSource(Array(new PointLight(100, 100, 100))),
          new LightSource(Array(new PointLight(200, 100, 100)))
        )
        var boxes = Array(
          Box( 40, 290,  50,  50, Material(0, 100, Color(255,   0,   0))),
          Box(300, 200,  40,  50, Material(0, 100, Color(  0, 255,   0))),
          Box( 30,  30,  20,  50, Material(0, 100, Color(  0,   0, 255))),
          Box(350, 250,  20,  50, Material(0, 100, Color(  0, 255, 255)))
        )

        var environment = new Environment(preferredSize, lights, boxes)

        override def dispose(): Unit = {
            super.dispose()

            executorService.shutdownNow()
        }

        val panel = new Panel {
            override def paint(g: Graphics2D): Unit = {
                super.paint(g)

                g.drawImage(environment, 0, 0, null)
            }

          listenTo(mouse.clicks)

          var down: Point = null
          reactions += {
            case MousePressed(_, p, _, _, _) => down = p
            case MouseReleased(_, p, _, _, _) => p
          }
        }

        contents = panel

        executorService.scheduleAtFixedRate({
            environment.update()
            panel.repaint()
        }, 33, 33, TimeUnit.MILLISECONDS)

        peer.setLocationRelativeTo(null)
        peer.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
    }

}
