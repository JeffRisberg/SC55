import sbt._
import Keys._
import org.scalatra.sbt._
import org.scalatra.sbt.PluginKeys._
import com.mojolly.scalate.ScalatePlugin._
import ScalateKeys._

object SC55Build extends Build {
  val Organization = "com.incra"
  val Name = "SC55"
  val Version = "0.1.0-SNAPSHOT"
  val ScalaVersion = "2.10.4"
  val ScalatraVersion = "2.3.0"

  lazy val project = Project(
    "SC55",
    file("."),
    settings = ScalatraPlugin.scalatraWithJRebel ++ scalateSettings ++ Seq(
      organization := Organization,
      name := Name,
      version := Version,
      scalaVersion := ScalaVersion,
      resolvers += Classpaths.typesafeReleases,
      libraryDependencies ++= Seq(
        "org.scalatra" %% "scalatra" % ScalatraVersion,
        "org.scalatra" %% "scalatra-scalate" % ScalatraVersion,
        "ch.qos.logback" % "logback-classic" % "1.0.6" % "runtime",
        "org.scalatra" %% "scalatra-json" % "2.3.0",
        "org.scalatest" %% "scalatest" % "2.2.4",
        "org.mockito" % "mockito-all" % "1.9.5",
        "org.json4s" %% "json4s-jackson" % "3.2.9",
        "com.escalatesoft.subcut" %% "subcut" % "2.0",
        "mysql" % "mysql-connector-java" % "5.1.34",
        "com.zaxxer" % "HikariCP-java6" % "2.2.5",
        "com.typesafe.slick" %% "slick" % "2.0.1",
        "com.typesafe" % "config" % "1.0.2",
        "org.clapper" %% "grizzled-slf4j" % "1.0.2",
        "org.eclipse.jetty" % "jetty-webapp" % "9.1.3.v20140225" % "container",
        "org.eclipse.jetty" % "jetty-plus" % "9.1.3.v20140225" % "container",
        "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container;provided;test" artifacts (Artifact("javax.servlet", "jar", "jar"))
      ),
      scalateTemplateConfig in Compile <<= (sourceDirectory in Compile) { base =>
        Seq(
          TemplateConfig(
            base / "webapp" / "WEB-INF" / "templates",
            Seq.empty, /* default imports should be added here */
            Seq(
              Binding("context", "_root_.org.scalatra.scalate.ScalatraRenderContext", importMembers = true, isImplicit = true)
            ), /* add extra bindings here */
            Some("templates")
          )
        )
      }
    )
  )
}
