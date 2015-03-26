package interfaceKit.controller

import interfaceKit.observable.ObservableSensors
import rx.lang.scala.Observable
import rx.lang.scala.schedulers.NewThreadScheduler
import scala.concurrent.{Await, Promise}
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}
import scala.reflect.runtime.universe._

class interfaceKitOneSensor(val indexSensor:Int)
{
  val endFuture = Promise[Boolean]()
  
  def launchOneSensor(duration:Long) 
  {
    val endFuture = Promise[Boolean]()
    val observableSensor = ObservableSensors.observableSimple(InterfaceKit.getStreamForValuesFromSensor(indexSensor), duration)
    val subscriptionCarComeIn = observableSensor.subscribeOn(NewThreadScheduler()).subscribe(onNextValueSensor, ObservableSensors.errorWhatToDo)  
    
    Await.ready(endFuture.future, Duration.Inf)
    subscriptionCarComeIn.unsubscribe()
  }
  
  val onNextValueSensor: (Option[Int]) => Unit =
  {
    case Some(result) => println("Value from Sensor " + indexSensor + " = " + result)
    case None =>
  }
}