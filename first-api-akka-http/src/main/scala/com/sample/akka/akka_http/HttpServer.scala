package com.sample.akka.akka_http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.pathPrefix
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._

import scala.util.{Failure, Success}


object HttpServer extends App {
  val host = "localhost"
  val port = 6060

  implicit val system = ActorSystem("HTTP_SERVER")
  implicit val materializer: ActorMaterializer.type = ActorMaterializer

  import system.dispatcher

  val route =
    pathPrefix("api") {
      get {
        path("hello") {
          complete(StatusCodes.OK, "Hello from Server")
        }
      }
    }

  val binding = Http().newServerAt(host, port).bindFlow(route)

  binding.onComplete {
    case Success(value) =>
      println(s"Server is listening on http://$host:${port}")
    case Failure(exception) =>
      println(s"Failure : $exception")
      system.terminate()
  }

}
