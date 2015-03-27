package viewScala

import interfaceKit.controller._

object Test 
{
  def main(args: Array[String]) 
  {
    InterfaceKit.waitForAttachment
    
    println("début test")
    
    val waitCar = new InterfaceKitWaitCar()
    waitCar.waitForCarToPassBarrier("20")
    
    println("fin test")
  }
}