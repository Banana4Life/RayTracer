import java.awt.{Color, Rectangle, Dimension}
import java.util.concurrent.{TimeUnit, Executors}
import javax.swing.{WindowConstants, JFrame}

import scala.swing.Swing._
import scala.swing.event.{MousePressed, MouseEntered, ButtonClicked}
import scala.swing._

object RayTracer extends SimpleSwingApplication {

  implicit def whateverToRunnable[F](f: => F) = new Runnable() { def run() { f } }

  val executorService = Executors.newSingleThreadScheduledExecutor()

  val top = new MainFrame() {
    title = "Test Frame!"

    preferredSize = 640 -> 480

    resizable = false

    reactions += {
      case e => println(e.getClass.getName)
    }

    override def dispose(): Unit = {
      super.dispose()

      executorService.shutdownNow()
    }

    val panel = new Panel {

      var i = 0
      var s = -1

      override def paint(g: Graphics2D): Unit = {
        super.paint(g)

        g.setColor(Color.RED)
        g.drawString("lulzz", 100 + i, 100 + i * s)
        i += 1
        s *= -1
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
