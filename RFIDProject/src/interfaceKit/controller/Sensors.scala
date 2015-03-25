package interfaceKit.controller

/**
 * Created by Steven on 18-03-15.
 */
object Sensors
{
  val functionSharpSensor = (x:Int) =>
  {
    if(x!=20)
    {
      val result = 4800 / (x - 20)
      if(result > 10 && result < 100) Some(result) else None
    }
    else None
  }

  val functionIrSensor = (x:Int) =>
  {
    if(x < 500) Some(1) else Some(0)
  }

  val listAnalogSensors: List[(Int, (Int) => Option[Int])] = List(
    (0, (x:Int) => functionSharpSensor(x)),
    (1, (x:Int) => functionSharpSensor(x)),
    (2, (x:Int) => Some(x)),
    (3, (x:Int) => Some(x)),
    (4, (x:Int) => Some(x)),
    (5, (x:Int) => Some(x)),
    (6, (x:Int) => Some(x)),
    (7, (x:Int) => Some(x)))

}
