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
  
  def stripArtificialNewLine(fileText: String) : String = fileText.filter(_ != '\n')
  
  def XMLescape(fileText: String) : String = {
    val modified  = fileText.replace("&","&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&apos;")
   return modified
  }

  def clean(fileText: String) : String = XMLescape(stripArtificialNewLine(fileText))
      
  def toJatsNaive(fileText: String) : String = {
    val modified  = """<?xml version="1.0" encoding="UTF-8"?><article xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:mml="http://www.w3.org/1998/Math/MathML"><body><sec sec-type="introduction" id="s1"><title>Introduction</title><p>"""  + fileText +  "</p></sec></body></article>"
   return modified
  }
  
  def writer(sourceDirName: String, targetDirName: String, fileName: String, f: String => String) {
    import scala.io.Source
    writeOutputFile(f(Source.fromFile(s"$sourceDirName/$fileName").mkString), targetDirName, fileName)
  }
      
 def HTMLescape(sourceDirName: String, targetDirName: String, fileName: String) {
   val rc = (Seq("cat", sourceDirName + "/" + fileName) #| "xmlstarlet esc" #> new File(s"$targetDirName/$fileName")) ! ProcessLogger((e: String) => 
                        println(s"error processing $fileName: $e")) // xmlstarlet seems to escape for html (much wider escaping) not merely for xml. http://stackoverflow.com/questions/1091945/what-characters-do-i-need-to-escape-in-xml-documents
  }

  def nullInitializer (s: String) = {}

  val base = "pdf/"
  
  val steps: Seq[Step] = Seq(Step(base + "2-as-text", base + "3-escaped", writer(_:String, _:String, _:String, clean), nullInitializer), // for more beautyful code switch from this partial application technique, to currying or other nicer functional design
                                                Step(base + "3-escaped", "ready-for-semantic/from-pdf", writer(_:String, _:String, _:String, toJatsNaive), nullInitializer))

  val pipeline = new Pipeline(steps) 

}
