import com.articlio.selfMonitor.{Monitor}
import com.articlio.steps._

object Boot extends App {

  Monitor

  try {

    val eLifePipeline = new JATSpipeline
    val PDFconvertedPipeline = new ConvertedCorpusPipeline

    } finally {
        Monitor.shutdown
      }
}