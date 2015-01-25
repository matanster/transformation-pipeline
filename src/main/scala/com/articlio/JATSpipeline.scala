package com.articlio.steps

import com.articlio.steps.util.{copy, writeOutputFile}
import com.articlio.config
import java.io.{File}
import sys.process._ // for being able to issue OS commands

// TODO: refactor to use one function that runs a modifier function and writes its output to file
class JATSpipeline {

  def XSL(fileText: String) : String = {
    val xslSources   = "xsl"
    val opener       = """<?xml version="1.0" encoding="UTF-8"?>"""
    val xslEmbedding = """<?xml-stylesheet type="text/xsl" href="jats-html.xsl"?>"""
    val docTypeDef   = """<!DOCTYPE article PUBLIC "-//NLM//DTD Journal Archiving and Interchange DTD v3.0 20080202//EN" "archivearticle3.dtd">"""
    val modified     = fileText.replace(docTypeDef, xslEmbedding) // the first is not needed and makes Chrome abort, 
                                                                  // the second gives us a nice display transform for the xml
    return modified
  }
  
  def clean(fileText: String) : String = {
    val xmlns = """xmlns:mml="http://www.w3.org/1998/Math/MathML" xmlns:xlink="http://www.w3.org/1999/xlink" """
    val docTypeDef   = """<!DOCTYPE article PUBLIC "-//NLM//DTD Journal Archiving and Interchange DTD v3.0 20080202//EN" "archivearticle3.dtd">"""
    val modified     = fileText.replace(docTypeDef, "").replace(xmlns, "") 
                                                                  // the second gives us a nice display transform for the xml
    return modified
  }

  def applyClean(sourceDirName: String, targetDirName: String, fileName: String) {
    import scala.io.Source
    writeOutputFile(clean(Source.fromFile(s"$sourceDirName/$fileName").mkString), targetDirName, fileName)
  }
  
  def applyXSL(sourceDirName: String, targetDirName: String, fileName: String) {
    import scala.io.Source
    writeOutputFile(XSL(Source.fromFile(s"$sourceDirName/$fileName").mkString), targetDirName, fileName)
  }

  def prettify(sourceDirName: String, targetDirName: String, fileName: String) {
    (s"xmllint --format $sourceDirName/$fileName" #> new File(s"$targetDirName/$fileName")).!
  }

  def copyXSL(to: String) = copy("xsl/*", to) // the xsl and css were taken from https://github.com/ncbi/JATSPreviewStylesheets

  def nullInitializer (s: String) = {}

  //val steps: Seq[Step] = Seq(Step("input", "formatted", prettify, nullInitializer),
  //                          Step("formatted", "styled", applyXSL, copyXSL))
    
  val steps: Seq[Step] = Seq(Step(config.JATSinput, config.JATSformatted, prettify, nullInitializer),
                   Step(config.JATSformatted, config.JATSstyled, applyXSL, copyXSL),
                   Step(config.JATSinput, config.JATSout, applyClean, nullInitializer))

  val pipeline = new Pipeline(steps) 

  println("pipeline invoked for originally JATS input")
}
