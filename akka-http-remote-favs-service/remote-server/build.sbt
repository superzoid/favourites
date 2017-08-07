lazy val akkaHttpVersion = "10.0.7"
lazy val akkaVersion    = "2.5.2"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "com.example",
      scalaVersion    := "2.12.2"
    )),
    name := "Akka Http Favourites Service",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-stream"                 % akkaVersion,
      "com.typesafe.akka" %% "akka-remote"                 % akkaVersion,
      "io.monix"          %% "shade"                       % "1.9.5",
      "com.typesafe.akka" %% "akka-testkit"                % akkaVersion % Test,
      "org.scalatest"     %% "scalatest"                   % "3.0.1"     % Test,
      "org.scalamock"     %% "scalamock-scalatest-support" % "3.5.0"     % Test
    )
  )
