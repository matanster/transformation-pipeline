package com.articlio
import com.typesafe.config.ConfigFactory
import java.io.File

object config {
  val config = ConfigFactory.parseFile(new File("../config/config.json"))

  val pdfAsJATS = config.getString("locations.pdf-extraction.asJATS")
  val copyTo = config.getString("locations.ready-for-semantic.from-pdf")
  val asText = config.getString("locations.pdf-extraction.asText")
  val asEscapedText = config.getString("locations.pdf-extraction.asEscapedText")
  
  val JATSinput = config.getString("locations.JATS-input.input")
  val JATSformatted = config.getString("locations.JATS-input.formatted")
  val JATSstyled = config.getString("locations.JATS-input.styled")
  val JATSout = config.getString("locations.ready-for-semantic.from-eLife")
}