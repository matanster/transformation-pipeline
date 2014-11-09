package com.articlio.steps

import com.articlio.steps.util.{copy, writeOutputFile}
import java.io.{File}
import sys.process._ // for being able to issue OS commands

// TODO: refactor to use one function that runs a modifier function and writes its output to file
class ConvertedCorpusPipeline {

  def XSL(fileText: String) : String = {
    val xslSources   = "xsl"
    val opener       = """<?xml version="1.0" encoding="UTF-8"?>"""
    val xslEmbedding = """<?xml-stylesheet type="text/xsl" href="jats-html.xsl"?>"""
    val docTypeDef   = """<!DOCTYPE article PUBLIC "-//NLM//DTD Journal Archiving and Interchange DTD v3.0 20080202//EN" "archivearticle3.dtd">"""
    val modified     = fileText.replace(docTypeDef, xslEmbedding) // the first is not needed and makes Chrome abort, 
                                                                  // the second gives us a nice display transform for the xml
    return modified
  }
  
  def XMLescape(fileText: String) : String = {
    val modified     = fileText.replace("&","&amp").replace("<", "&lt").replace(">", "&gt").replace("\"", "&quot").replace("'", "&apos")
   return modified
  }

  def writer(sourceDirName: String, targetDirName: String, fileName: String, f: String => String) {
    import scala.io.Source
    writeOutputFile(XMLescape(Source.fromFile(s"$sourceDirName/$fileName").mkString), targetDirName, fileName)
  }
      
 def HTMLescape(sourceDirName: String, targetDirName: String, fileName: String) {
   val rc = (Seq("cat", sourceDirName + "/" + fileName) #| "xmlstarlet esc" #> new File(s"$targetDirName/$fileName")) ! ProcessLogger((e: String) => 
                        println(s"error processing $fileName: $e")) // xmlstarlet seems to escape for html (much wider escaping) not merely for xml. http://stackoverflow.com/questions/1091945/what-characters-do-i-need-to-escape-in-xml-documents
  }

  def nullInitializer (s: String) = {}

  val steps: Seq[Step] = Seq(Step("input-converted-corpus", "toJATS", writer(_:String, _:String, _:String, XMLescape), nullInitializer)) // switch from this partial application technique, to currying or other nicer functional design

  val pipeline = new Pipeline(steps) 

}
