package interfaceKit.controller

import interfaceKit.observable.ObservableSensors
import rx.lang.scala.Observable
import rx.lang.scala.schedulers.NewThreadScheduler
import scala.concurrent.{Await, Promise}
import scala.concurrent.duration._
import scala.io.StdIn
import scala.util.{Failure, Success, Try}

object Main
{
  def main(args: Array[String])
  {
    val interfaceKit: InterfaceKit = new InterfaceKit

    /* We initialize the interface kit */
    interfaceKit.addAttachListener
    interfaceKit.addDetachListener
    interfaceKit.openAny
    interfaceKit.waitForAttachment

    /*
     * Afther the attachement has been done. We create 1 data stream for each sensor.
     * Then we create an observable over the values of the sensor 0 and the sensor 1
     */
    val observableTupleWithInterval = ObservableSensors.observableTupleWithInterval(
      interfaceKit.getStreamForValuesFromSensor(0), interfaceKit.getStreamForValuesFromSensor(1))

    ObservableSensors.waitCar(observableTupleWithInterval)
    ObservableSensors.waitCarToComeIn(observableTupleWithInterval)

    StdIn.readLine()
    interfaceKit.close
  }
}