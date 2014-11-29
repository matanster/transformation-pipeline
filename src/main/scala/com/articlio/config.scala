package com.articlio
import com.typesafe.config.ConfigFactory
import java.io.File

object config {
  val config = ConfigFactory.parseFile(new File("../config/config.json"))

  val pdfAsJATS = config.getString("locations.pdf-extraction.asJATS")
  val copyTo = config.getString("ready-for-semantic/from-pdf")
  val asText = config.getString("pdf-extraction/asText")
  val asEscapedText = config.getString("pdf-extraction/asEscapedText")
  
  val JATSinput = config.getString("JATS-input/input")
  val JATSformatted = config.getString("JATS-input/formatted")
  val JATSstyled = config.getString("JATS-input/styled")
  val JATSout = config.getString("ready-for-semantic/from-eLife")
}