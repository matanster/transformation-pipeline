//
// see http://www.scala-sbt.org/sbt-native-packager/GettingStartedApplications/MyFirstProject.html
// for packaging to run outside sbt
//

import com.typesafe.sbt.SbtNativePackager._

import NativePackagerKeys._

name := "fileMap"

version := "1.0"

packageArchetype.java_application

organization  := "com.articlio"

version       := "0.1"

scalaVersion  := "2.10.4"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

// If changing lang-scala version, make sure src/main/resources/langs.properties is updated too
libraryDependencies += "org.scala-lang" % "scala-compiler" % "2.10.4"

libraryDependencies += "mysql" % "mysql-connector-java" % "latest.release"

resolvers += "Sonatype releases" at "http://oss.sonatype.org/content/repositories/releases/"

libraryDependencies += "com.github.verbalexpressions" %% "scalaverbalexpression" % "1.0.1"

scalacOptions += "-feature"

