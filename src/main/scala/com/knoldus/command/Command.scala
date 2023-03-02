package com.knoldus.command

import akka.http.scaladsl.model.HttpResponse

trait Command {
  def completer: HttpResponse => Unit
}
