/**
 * Copyright 2015, deepsense.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import sbt.Keys._
import sbt._

object CommonSettingsPlugin extends AutoPlugin {
  override def trigger = allRequirements

  lazy val OurIT = config("it") extend Test

  lazy val artifactoryUrl = settingKey[String]("Artifactory URL to deploy packages to")

  object Versions {
    val spark = System.getProperty("sparkVersion", "2.0.0")
    val (scala, java, hadoop, akka) = if (spark == "2.0.0") {
      ("2.11.8", "1.8", "2.7.1", "2.4.9")
    } else {
      ("2.10.5", "1.7", "2.6.0", "2.3.11")
    }
  }

  override def globalSettings = Seq(
    // Set custom URL using -Dartifactory.url
    // sbt -Dartifactory.url=http://192.168.59.104/artifactory/
    artifactoryUrl := sys.props.getOrElse("artifactory.url", "http://artifactory.deepsense.codilime.com:8081/artifactory/")
  )

  override def projectSettings = Seq(
    organization := "io.deepsense",
    // Default scala version
    scalaVersion := Versions.scala,
    // Scala versions used for cross-builds
    // (use `sbt clean "+ publish"` to publish using scala 2.11.8)
    crossScalaVersions := Seq(Versions.scala),
    scalacOptions := Seq(
      "-unchecked", "-deprecation", "-encoding", "utf8", "-feature",
      "-language:existentials", "-language:implicitConversions"
    ),
    javacOptions ++= Seq(
      "-source", Versions.java,
      "-target", Versions.java
    ),
    resolvers ++= Dependencies.resolvers,
    // Disable using the Scala version in output paths and artifacts
    crossPaths := true
  ) ++ ouritSettings ++ testSettings ++ Seq(
    test <<= test in Test
  ) ++ Seq(
    publishTo := {
      Some(Resolver.file("ds-workflow-executor-ivy-repo", new File( "./target/ds-workflow-executor-ivy-repo" )))
    },
    credentials += Credentials(Path.userHome / ".artifactory_credentials")
  )

  lazy val ouritSettings = inConfig(OurIT)(Defaults.testSettings) ++ inConfig(OurIT) {
    Seq(
      testOptions ++= Seq(
        // Show full stacktraces (F), Put results in test-reports
        Tests.Argument(TestFrameworks.ScalaTest, "-oF", "-u", s"target/test-reports-${Versions.spark}")
      ),
      javaOptions := Seq(s"-DlogFile=${name.value}", "-Xmx2G", "-Xms2G", "-XX:MaxPermSize=2G"),
      fork := true,
      unmanagedClasspath += baseDirectory.value / "conf"
    )
  }

  lazy val testSettings = inConfig(Test) {
    Seq(
      testOptions := Seq(
        // Put results in test-reports
        Tests.Argument(
          TestFrameworks.ScalaTest,
          "-o",
          "-u", s"target/test-reports-${Versions.spark}"
        )
      ),
      fork := true,
      javaOptions := Seq(s"-DlogFile=${name.value}"),
      unmanagedClasspath += baseDirectory.value / "conf"
    )
  }

  override def projectConfigurations = OurIT +: super.projectConfigurations
}
