package com.socure

import com.socure.Model._
import com.socure.ReadFileFromResource.resourceInto

import java.io.FileNotFoundException

object ExecutionPlanBuilder extends App {

  if (args.length < 1) {
    println("insufficient arguments")
    System.exit(0);

  }
  val filename = args(0)
  val linesStr = resourceInto(filename) match {
    case Right(lines) => lines
    case Left(_) => throw new FileNotFoundException(filename)
  }

  val listActions = linesStr.split("\n")
    .map(_.split(" "))
    .filter(_.length == 3)
    .map(f => Action(f(0), f(1), f(2))).toList

  val stages = listActions.map(a => a.stageName).foldLeft(List[String]()) { (acc, s) =>
    if (!acc.contains(s)) s :: acc else acc
  }.reverse

  val dependencies: Graph[String] = listActions.groupBy(_.tableName).map {
    case (k, list) => {
      val writeList = list.filter(_.mode == "writes").map(x => x.stageName)
      val readList = list.filter(_.mode == "reads").map(x => x.stageName)
      readList.map(r => (r, writeList.toSet))
    }
  }.flatten.toMap

  def CreateExectionPlan(stages: List[String], dependencies: Graph[String]): List[String] = {
    var plan: List[String] = List()
    var previousPlanSiz = -1
    while (previousPlanSiz != plan.size) {
      previousPlanSiz = plan.size
      val toDoStages = stages.filterNot(stage => plan.contains(stage))
      val intermediateResult = toDoStages.foldLeft(List[String]()) { case (accResult, stage) =>
        val dependency = dependencies.getOrElse(stage, Set())
        if (!dependencies.contains(stage) || dependency.forall(plan.contains(_))) stage :: accResult else accResult
      }
      plan = plan ++ intermediateResult
    }
    // if root stage is not in the first stage in the plan, then return empty List
    if (plan.size != stages.size || plan(0) != stages(0)) Nil else plan
  }

  val exectionPlan = CreateExectionPlan(stages, dependencies) match {
    case Nil => "no feasible execution plans can be generated!!!"
    case list => list.mkString("->")
  }
  println(exectionPlan)
}
