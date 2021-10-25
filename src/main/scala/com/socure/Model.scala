package com.socure

object Model {

  type Graph[T] = Map[T, Set[T]]

  final case class Action
  (
    stageName: String,
    mode: String,
    tableName: String
  )

}
