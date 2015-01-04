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
     * To extend use of NLP tools through it, confer to:
     *   https://github.com/sistanlp/processors#annotating-entire-documents
     *   https://github.com/sistanlp/processors#annotating-documents-already-split-into-sentences
     *   https://github.com/sistanlp/processors#using-individual-annotators  
     *   http://nlp.stanford.edu/software/corenlp.shtml (CoreNLP full range of annotators; midway downpage)
     */
    
    } finally {
        Monitor.shutdown
    }
}