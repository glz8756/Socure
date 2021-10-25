package com.socure

import java.net.URL
import scala.io.{BufferedSource, Source}

import cats.implicits._

object ReadFileFromResource {

  def urlInto(url: URL): Either[Throwable, String] =
    fromURL(url)
      .map(_.getLines.mkString("\n"))

  private[this] def fromURL(url: URL): Either[Throwable, BufferedSource] =
    Either.catchNonFatal(Source.fromURL(url))

  def resourceInto(resourceName: String): Either[Throwable, String] =
    getResource(resourceName).flatMap(urlInto)

  private[this] def getResource(resourceName: String): Either[Throwable, URL] =
    Either.catchNonFatal(getClass.getClassLoader.getResource(resourceName))

}

