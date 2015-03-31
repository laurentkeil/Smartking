package viewScala

import interfaceKit.controller._
import rx.lang.scala.Observable
import rx.lang.scala.schedulers.NewThreadScheduler

import scala.concurrent.{Await, Promise}
import scala.concurrent.duration._
import scala.io.StdIn
import scala.util.{Failure, Success, Try}
import scala.concurrent._
import ExecutionContext.Implicits.global

object Test 
{
  def main(args: Array[String]) 
  {
    test2()
  }
  
  def test1()
  {
    InterfaceKit.waitForAttachment
    
    println("début test")
    
    val oneSensor = new InterfaceKitOneSensor(0)
    oneSensor.startSensor(100)
    
    StdIn.readLine()
    oneSensor.stopSensor()
    InterfaceKit.close
    println("fin test")
  }
  
  def test2()
  {
    InterfaceKit.waitForAttachment
    
    println("début test")
    
    val oneSensor = new InterfaceKitWaitCar()
    oneSensor.waitForCarToPassBarrier("50");
    
    StdIn.readLine()
    InterfaceKit.close
    println("fin test")
  }
}