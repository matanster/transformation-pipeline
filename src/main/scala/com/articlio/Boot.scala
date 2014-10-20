import com.articlio.selfMonitor.{Monitor}

//
// Some mix and match of file IO utility functions here
//
import java.io.{File}
import java.nio.file.{Path, Paths, Files}
import org.apache.commons.io.FileUtils.{deleteDirectory}
import scala.io.Source
import java.nio.charset.StandardCharsets

import sys.process._ // for being able to issue OS commands

//
// Takes an input directory, and transforms each file into a new directory.
// The idea is that this can be extended to create a whole pipeline of transformation, 
// while making every step traceable - as each transformation's output has its own directory
// and the file name remains the same across all directories.
//

object Boot extends App {

  Monitor

  def writeOutputFile(fileText: String, outDir: String, fileName: String) {
    Files.write(Paths.get(outDir + "/" + fileName), fileText.getBytes(StandardCharsets.UTF_8))
  }

  def XSL(fileText: String) : String = {
    val xslSources   = "xsl"
    val opener       = """<?xml version="1.0" encoding="UTF-8"?>"""
    val xslEmbedding = """<?xml-stylesheet type="text/xsl" href="jats-html.xsl"?>"""
    val docTypeDef   = """<!DOCTYPE article PUBLIC "-//NLM//DTD Journal Archiving and Interchange DTD v3.0 20080202//EN" "archivearticle3.dtd">"""
    val modified     = fileText.replace(docTypeDef, xslEmbedding) // the first is not needed and makes Chrome abort, 
                                                                  // the second gives us a nice display transform for the xml
    return modified
  }

  type Transformation = (String, String, String) => Unit

  def applyXSL(sourceDirName: String, targetDirName: String, fileName: String) {
    writeOutputFile(XSL(Source.fromFile(s"$sourceDirName/$fileName").mkString), targetDirName, fileName)
  }

  def prettify(sourceDirName: String, targetDirName: String, fileName: String) {
    (s"xmllint --format $sourceDirName/$fileName" #> new File(s"$targetDirName/$fileName")).!
  }

  case class Step(from: String, to: String, transformation:Transformation)
  val steps: Seq[Step] = Seq(Step("input", "formatted", prettify),
                             Step("formatted", "styled", applyXSL))
  
  def based(dir: String) = "data" + "/" + dir

  //
  // take care of a ready empty target directory for a step
  //
  def createDir(m: Step) = {
    val targetDirName: String = m.to 
    val targetDirObj = Paths.get(based(targetDirName))
    if (Files.exists(targetDirObj)) deleteDirectory(new File(based(targetDirName)))
    Files.createDirectory(targetDirObj)
  }

  steps.foreach(createDir)

  def StepDo(step: Step) {
    // see http://www.scala-lang.org/api/current/index.html#scala.sys.process.package for the way this invokes an OS command
    val sourceDirName = based(step.from)
    val sourceDir = new File(based(step.from))
    val targetDirName = based(step.to)

    val files = sourceDir.listFiles.filter(file => (file.isFile && file.getName.endsWith(".xml")))

    // running concurrently via .par - scala will employ some parallelism by multithreding, matching the number of free cores
    files.par.foreach (file => {
      val fileName = file.getName
      step.transformation(sourceDirName, targetDirName, fileName)
    })
  }

  try {
    steps foreach StepDo

    } finally {
        Monitor.shutdown
      }
}