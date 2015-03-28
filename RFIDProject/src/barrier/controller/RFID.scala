package barrier.controller

import java.nio.ByteBuffer
import model._
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
import interfaceKit.data._

/**
 * @author laurent
 */
object RFID 
{
  val rfid: RFIDPhidget = new RFIDPhidget()
  addAttachListener()
  tagLossListener()
  addOutputChangeListener()
  openAny()
  waitForAttachement()
  
  var action = "no"
  var inscription = false

  def addAttachListener()
  {
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
  }
  
  def tagLossListener()
  {
    rfid.addTagLossListener(new TagLossListener() {
      def tagLost(oe: TagLossEvent) {
        println("Tag Loss : " + oe.getValue());
        rfid.setOutputState(0, false)
        rfid.setOutputState(1, false)
      }
    })
  }
  
  def addOutputChangeListener()
  {
    rfid.addOutputChangeListener(new OutputChangeListener() {
      def outputChanged(oe: OutputChangeEvent) {
        println(oe.getIndex + " change to " + oe.getState);
        }
     })
  }
  
  def openAny() = rfid.openAny()
  
  def waitForAttachement()
  {
      println("waiting for RFID attachment...");
      rfid.waitForAttachment(1000);
  }

  def in() {
    action = "in"
  }

  def out() {
    action = "out"
  }
  
  def inscriptionTag(person:Person): Boolean =
  {
    val tag = genTag()
    
    try {
        rfid.write(tag, RFIDPhidget.PHIDGET_RFID_PROTOCOL_PHIDGETS, false) //écrit sur la tag       
        register(tag, person.lastName, person.firstName, person.mail) match {
          case Success(rep) => {
            println("\nWrite Tag : " + tag)
            true
          }
          case Failure(exc) => {
            false
          }
        }
    } catch {
      case exc : Exception => false
    }
  }

  def ledRedOn() = rfid.setOutputState(0, true)
  def ledRedOff() = rfid.setOutputState(0, false)

  def ledGreenOn() = rfid.setOutputState(1, true)
  def ledGreenOff() = rfid.setOutputState(1, false)
  
  def carPassed (tag : String) : Boolean =
  {
     val responsePost = Http.post("http://smarking.azurewebsites.net/api/FlowUsers").params("action" ->action).params("idTag" -> tag).asString        
     println(responsePost)
      
     if (InterfaceKit.isAttached)
     {
        val interfaceKitWaitCar = new InterfaceKitWaitCar()
        interfaceKitWaitCar.waitForCarToPassBarrier(tag)  // PUT RIFD HERE
     } 
     else 
     {
        println("You must attach the interfaceKit");
        false
     }
  }

  def genTag(): String = {
    val uuid = UUID.randomUUID();
    val time = System.currentTimeMillis() / 1000;
    time.toString() ++ uuid.toString.replace("-", "").substring(9, 23)
  }

  def register(tag: String, userLastname: String, userFirstName: String, userMail: String) = 
  {
    action = "write"
    DataAdd.register(tag, userLastname, userFirstName, userMail)
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