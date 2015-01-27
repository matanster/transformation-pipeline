resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

//resolvers += Classpaths.typesafeResolver

//addSbtPlugin("com.typesafe.startscript" % "xsbt-start-script-plugin" % "0.5.1")

addSbtPlugin("com.typesafe.sbt" %% "sbt-start-script" % "0.10.0")

addSbtPlugin("io.spray" % "sbt-revolver" % "0.7.1")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "0.8.0-M2")

addSbtPlugin("org.netbeans.nbsbt" % "nbsbt-plugin" % "1.1.2")

addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.3.2")