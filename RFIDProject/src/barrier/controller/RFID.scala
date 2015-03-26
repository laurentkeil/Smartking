package barrier.controller

import java.nio.ByteBuffer
import java.util.UUID
import com.phidgets._
import com.phidgets.event._
import scalaj.http._
import scala.util._
import java.io.InputStreamReader
import org.json._
import javax.swing._
import java.awt.event._
import java.awt._
import interfaceKit.controller.InterfaceKit
import interfaceKit.observable.ObservableSensors
import rx.lang.scala.Observable
import interfaceKit.controller._

/**
 * @author laurent
 */
object RFID {

  val interfaceKit = new InterfaceKit()
  /* We initialize the interface kit */
  interfaceKit.addAttachListener
  interfaceKit.addDetachListener
  interfaceKit.openAny

  var action = "no"
  var inscription = false
  
  def RFID(rfid : RFIDPhidget ) {
    
      rfid.addAttachListener(new AttachListener() {
        def attached(ae: AttachEvent) {
          try {
            (ae.getSource() match {
              case aeRFID: RFIDPhidget => aeRFID
            }).setAntennaOn(true)
  
            (ae.getSource() match {
              case aeRFID: RFIDPhidget => aeRFID
            }).setLEDOn(true)
          } catch {
            case exc: PhidgetException => println(exc)
          }
          println("attachment 1 of " + ae)
        }
      })
    
      rfid.addTagLossListener(new TagLossListener() {
        def tagLost(oe: TagLossEvent) {
          println("Tag Loss : " + oe.getValue());
          rfid.setOutputState(0, false)
          rfid.setOutputState(1, false)
        }
      })
    
      rfid.addOutputChangeListener(new OutputChangeListener() {
        def outputChanged(oe: OutputChangeEvent) {
          println(oe.getIndex + " change to " + oe.getState);
        }
      })

      rfid.openAny();
      println("waiting for RFID attachment...");
      rfid.waitForAttachment(1000);
    
  }
  

  def in() {
    action = "in"
  }

  def out() {
    action = "out"
  }
<<<<<<< HEAD

  def found(tag: String): Option[JSONObject] = {
    val responseGet = Http.get("http://smarking.azurewebsites.net/api/users/" + tag).asString
    println(responseGet)
    if (responseGet != "\"TagNotFound\"") {
      Some(new JSONObject(responseGet))
    } else {
      None
    }
  }

  def passed(id: String) = {
    //recherche le tag du user en BD
    if (action == "in" || action == "out") {
      val responseGet = Http.get("http://smarking.azurewebsites.net/api/Tags/" + id).asString
      println(responseGet)
      val responsePost = Http.post("http://smarking.azurewebsites.net/api/Flow").params(Map(("id", id), ("type", "1"), ("floor", "1"), ("date", "2015-03-25T22:34:33.967675+00:00"))).asString
      println(responsePost)
      if (responsePost != "\"NotFound\"" && responsePost != "\"AccessDenied\"") {
        action = "fin"
        true
      } else {
        false
=======
  
  def found(tag : String) : Option[JSONObject] = {
          val responseGet = Http.get("http://smarking.azurewebsites.net/api/users/" + tag).asString
          if (responseGet != "\"TagNotFound\"") {
              Some(new JSONObject(responseGet))
          } else {
              None
          }
  }
  
  def passed (tag : String) = {
      //recherche le tag du user en BD
      if (action == "in" || action == "out") {
            val responseGet = Http.get("http://smarking.azurewebsites.net/api/Tags/in/" + tag).asString
            println(responseGet)
            if (responseGet == "\"Ok\"") {
                true
            } else {
                false
            }
>>>>>>> origin/master
      }
    }
  }
<<<<<<< HEAD

  def carPassed() : Boolean = 
  {
    if (action == "fin") 
    {
      if (interfaceKit.isAttached) 
      {
        val interfaceKitWaitCar = new InterfaceKitWaitCar()
        interfaceKitWaitCar.waitForCarToPassBarrier(interfaceKit, "230")  // PUT RIFD HERE
      } 
      else 
      {
        println("You must attach the interfaceKit");
        false
      }
    }
    else
      false
=======
  
  def carPassed (tag : String) = {
    
          val responsePost = Http.post("http://smarking.azurewebsites.net/api/FlowUsers").params("action" ->action).params("idTag" -> tag).asString        
          println(responsePost)
          
          if (interfaceKit.isAttached) {
            /*val observableTupleWithConditions = ObservableSensors.observableTuple(
              interfaceKit.getStreamForValuesFromSensor(0), interfaceKit.getStreamForValuesFromSensor(1))
  
            val observableTupleWithInterval: Observable[(Option[Int], Option[Int])] =
              ObservableSensors.setIntervalToObservable(observableTupleWithConditions)
  
            if(ObservableSensors.waitCar(observableTupleWithInterval)) {
              println("wait car")
              if(ObservableSensors.waitCarToComeIn(observableTupleWithInterval)) {
                println("wait car to come in")
                
                  val responsePost = Http.post("http://smarking.azurewebsites.net/api/FlowUsers").params("action" ->action).params("idTag" -> tag).asString        
                  println(responsePost)
                   // le mec est rentré
              } 
            }*/
            true
          }
          else {
            println("You must attach the interfaceKit");
            false
          }
>>>>>>> origin/master
  }

  def genTag(): String = {
    val uuid = UUID.randomUUID();
    val time = System.currentTimeMillis() / 1000;
    time.toString() ++ uuid.toString.replace("-", "").substring(9, 23)
  }

  def register(tag: String, userLastname: String, userFirstName: String, userMail: String) = {
    action = "write"
    Try(Http.post("http://smarking.azurewebsites.net/api/users").params(Map(("idTag", tag), ("lastname", userLastname), ("firstname", userFirstName), ("mail", userMail))).asString)
  }
  
  /*
  class updateListener extends ActionListener {
    def actionPerformed(arg0: ActionEvent) {
      action = "update"
      //write a tag:
      try { //TODO
        val tag = tagLu.getText
        println("\nWrite Tag : " + tag);
        //entre le tag du user en BD
        val responsePost = Http.post("http://smarking.azurewebsites.net/api/users").params(Map(("idTag", tag))).asString
        println(responsePost)
        rfid.setOutputState(1, true) //ecriture : vert si tag a bien été écrit en BD
        JOptionPane.showMessageDialog(null, "Les informations ont été mise à jour", "Mis à jour", JOptionPane.INFORMATION_MESSAGE);
        Thread.sleep(1000)
        rfid.setOutputState(1, false)
      } catch {
        case exc: PhidgetException =>
          println(exc)
          rfid.setOutputState(0, true)
          JOptionPane.showMessageDialog(null, "Les informations n'ont pas pu être mise à jour", "Mis à jour", JOptionPane.ERROR_MESSAGE);
          Thread.sleep(1000)
          rfid.setOutputState(0, false)
      }
    }
  }*/

}