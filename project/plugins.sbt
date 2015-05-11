resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.3.6")

// For docker
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.0.0-RC1")
