import java.awt.Color
import java.util.concurrent.{TimeUnit, Executors}
import javax.swing.WindowConstants

import scala.swing.Swing._
import scala.swing._

object RayTracer extends SimpleSwingApplication {
    implicit def whateverToRunnable[F](f: => F) = new Runnable() { def run() { f } }

    val executorService = Executors.newSingleThreadScheduledExecutor()

    val top = new MainFrame() {
        title = "Ray Tracer"
        preferredSize = 640 -> 480
        resizable = false

        var lights = Array(new LightSource(Array(new PointLight(100, 100, 100))))
        var boxes = Array(new Box(10, 290, 50, 50, new Material(0, 100, new Color(255, 0, 0))), new Box(300, 200, 40, 50, new Material(0, 100, new Color(0, 255, 0))))
        var environment = new Environment(preferredSize, lights, boxes)

        reactions += {
            case e => println(e.getClass.getName)
        }

        override def dispose(): Unit = {
            super.dispose()

            executorService.shutdownNow()
        }

        val panel = new Panel {
            override def paint(g: Graphics2D): Unit = {
                super.paint(g)

                g.drawImage(environment, 0, 0, null)
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
