import com.articlio.selfMonitor.{Monitor}
import com.articlio.steps._

object Boot extends App {

  Monitor

  try {

    val eLifeJATSpipeline = new JATSpipeline
    val PDFpipeline = new ConvertedCorpusPipeline

    /* uncomment to use sistaNLP, when you have ~4GB free RAM for it to bootstrap
     *
     * import com.articlio.sistaNlp._
     * val a = new sistaNlp
     * 
     */
    
    } finally {
        Monitor.shutdown
    }
}