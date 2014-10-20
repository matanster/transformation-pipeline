import com.articlio.selfMonitor.{Monitor}
import com.articlio.steps._

object Boot extends App {

  Monitor

  try {

    val pipeline = new JATSpipeline

    } finally {
        Monitor.shutdown
      }
}