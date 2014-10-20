import com.articlio.selfMonitor.{Monitor}
import com.articlio.steps._
import com.articlio.steps.util.{copy, writeOutputFile}
import java.io.{File}
import sys.process._ // for being able to issue OS commands

object JATSpipeline {

  def XSL(fileText: String) : String = {
    val xslSources   = "xsl"
    val opener       = """<?xml version="1.0" encoding="UTF-8"?>"""
    val xslEmbedding = """<?xml-stylesheet type="text/xsl" href="jats-html.xsl"?>"""
    val docTypeDef   = """<!DOCTYPE article PUBLIC "-//NLM//DTD Journal Archiving and Interchange DTD v3.0 20080202//EN" "archivearticle3.dtd">"""
    val modified     = fileText.replace(docTypeDef, xslEmbedding) // the first is not needed and makes Chrome abort, 
                                                                  // the second gives us a nice display transform for the xml
    return modified
  }

  def applyXSL(sourceDirName: String, targetDirName: String, fileName: String) {
    import scala.io.Source
    writeOutputFile(XSL(Source.fromFile(s"$sourceDirName/$fileName").mkString), targetDirName, fileName)
  }

  def prettify(sourceDirName: String, targetDirName: String, fileName: String) {
    (s"xmllint --format $sourceDirName/$fileName" #> new File(s"$targetDirName/$fileName")).!
  }

  def copyXSL(to: String) = copy("xsl/*", to)

  def nullInitializer (s: String) = {}

  val steps: Seq[Step] = Seq(Step("input", "formatted", prettify, nullInitializer),
                             Step("formatted", "styled", applyXSL, copyXSL))

  val pipeline = new Pipeline(steps) 

}

object Boot extends App {

  Monitor

  try {

    JATSpipeline

    } finally {
        Monitor.shutdown
      }
}