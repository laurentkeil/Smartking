package interfaceKit.controller

import interfaceKit.observable.ObservableSensors
import rx.lang.scala.Observable
import rx.lang.scala.schedulers.NewThreadScheduler
import scala.concurrent.{Await, Promise}
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}
import scala.reflect.runtime.universe._
import interfaceKit.controller._
import scala.concurrent._
import ExecutionContext.Implicits.global

class InterfaceKitOneSensor(val indexSensor:Int)
{
  val endFuture = Promise[Boolean]()
  
  def startSensor(duration:Long)
  {
    Future(launchOneSensor(duration));
  }
  
  def stopSensor()
  {
    endFuture.success(true)
  }
  
  def launchOneSensor(duration:Long) 
  {
    val myStream = InterfaceKit.getStreamForValuesFromSensor(indexSensor, None)
    
    
    val observableSensor = ObservableSensors.observableSimple(myStream, duration)
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