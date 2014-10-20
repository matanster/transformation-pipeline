import com.typesafe.sbt.SbtStartScript

organization  := "com.articlio"

version       := "0.1"

//
// akka & spray
//

scalaVersion  := "2.10.4"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

seq(SbtStartScript.startScriptForClassesSettings: _*)

// If changing lang-scala version, make sure src/main/resources/langs.properties is updated too
libraryDependencies += "org.scala-lang" % "scala-compiler" % "2.10.4"

libraryDependencies += "mysql" % "mysql-connector-java" % "latest.release"

resolvers += "Sonatype releases" at "http://oss.sonatype.org/content/repositories/releases/"

libraryDependencies += "com.github.verbalexpressions" %% "scalaverbalexpression" % "1.0.1"

scalacOptions += "-feature"