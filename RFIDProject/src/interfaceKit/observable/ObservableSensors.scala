package interfaceKit.observable

import rx.lang.scala.Observable
import rx.lang.scala.schedulers.NewThreadScheduler
import scala.concurrent.{Await, Promise}
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}
import scala.reflect.runtime.universe._

/**
 * Created by Steven on 18-03-15.
 */
object ObservableSensors
{
  def observableSimple(streamSensor:Stream[Option[Int]], duration:Long) =
  {
    setIntervalToObservable(Observable.from(streamSensor), 500)
  }
  
  def observableTuple(streamSensor0:Stream[Option[Int]], streamSensor1:Stream[Option[Int]]) =
  {
    val obs1 = Observable.from(streamSensor0)
    val obs2 = Observable.from(streamSensor1)
    obs1.zip(obs2)
  }
  
  def setIntervalToObservable[T](obs: Observable[T], duration:Long) =
  {
    Observable.interval(duration milliseconds).take(1).map(x => obs).flatten
  }
  
      val errorWhatToDo = (ex:Throwable) => ex match
  {
    case ex:InterruptedException =>
    case _ => ex.printStackTrace()
  }
  
  val hasValueForEachItemOfTuple = (x:(Option[Int], Option[Int])) => x match
  {
    case (Some(x1), Some(x2)) => true
    case _ => false
  }
}
