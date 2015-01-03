import com.articlio.selfMonitor.{Monitor}
import com.articlio.steps._

object Boot extends App {

  Monitor

  try {

    val eLifeJATSpipeline = new JATSpipeline
    val PDFpipeline = new ConvertedCorpusPipeline

    } finally {
        Monitor.shutdown
    }
}