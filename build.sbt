import com.typesafe.config._

val conf = ConfigFactory.parseFile(new File("conf/application.conf")).resolve()

name := conf.getString("application.name")

version := conf.getString("application.version")

version in Docker := version.value

maintainer in Docker := conf.getString("application.maintainer")

dockerExposedPorts := Seq(9000, 9443)

javaOptions in Test += "-Dconfig.file=conf/application.test.conf"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "be.objectify" %% "deadbolt-java" % "2.3.2",
  "org.apache.commons" % "commons-csv" % "1.0",
  "postgresql" % "postgresql" % "9.1-901-1.jdbc4"
)

resolvers += Resolver.url("Objectify Play Repository", url("http://deadbolt.ws/releases/"))(Resolver.ivyStylePatterns)

watchSources := (watchSources.value
  --- baseDirectory.value / "app/assets" ** "*"
  --- baseDirectory.value / "public"     ** "*").get