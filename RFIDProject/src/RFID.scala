import java.nio.ByteBuffer
import java.util.UUID
import com.phidgets._
import com.phidgets.event._
import scalaj.http._
import java.io.InputStreamReader
import org.json._
 
/** 
 * @author laurent 
 */ 
object RFID {
  
  def main(args:Array[String]) {
      val rfid = new RFIDPhidget()
      val barriere = new Barriere()
      barriere.Barriere()
      
      rfid.addAttachListener(new AttachListener() {
        def attached(ae : AttachEvent ) 
        {
          try
          {
            (ae.getSource() match { 
              case aeRFID : RFIDPhidget => aeRFID
            }).setAntennaOn(true); 
            
            (ae.getSource() match {
              case aeRFID : RFIDPhidget => aeRFID
            }).setLEDOn(true);
          }
          catch { 
            case exc : PhidgetException => println(exc)
          }
          println("attachment 1 of " + ae);
        }
      });
      rfid.addDetachListener(new DetachListener() {
        def detached(ae : DetachEvent ) {
          println("detachment  1 of " + ae);
        } 
      });
      rfid.addErrorListener(new ErrorListener() {
        def error(ee : ErrorEvent) {
          println("error event for " + ee);
        }
      });
      
      rfid.openAny();
      println("waiting for RFID attachment...");
      rfid.waitForAttachment(1000);
      
      val actionStr = Http.get("http://smarking.azurewebsites.net/api/global/rfid").asString
      val action = new JSONObject(actionStr).getString("value")
      action match {
        case ("in" | "out") => {
              rfid.addTagGainListener(new TagGainListener()
              { 
                def tagGained(oe : TagGainEvent)
                {
                  println("\nTag Gained: " + oe.getValue());
                  //recherche le tag du user en BD
                  val responsePost = Http.post("http://smarking.azurewebsites.net/api/FlowUsers").params("action" ->action).params("idTag" -> oe.getValue).asString        
                  println(responsePost)
                  if (responsePost != "\"NotFound\"" && responsePost != "\"AccessDenied\"") {                    
                     rfid.setOutputState(1, true) //vert si le tag est présent en BD
                     barriere.ouverture()
                     Thread.sleep(2000)
                     barriere.fermeture()
                  } else {
                     rfid.setOutputState(0, true) //rouge si le tag est présent en BD
                  }
                }
              });
              rfid.addTagLossListener(new TagLossListener()
              {
                def tagLost(oe : TagLossEvent)
                {
                  println("Tag Loss : " + oe.getValue());
                  rfid.setOutputState(0, false)
                  rfid.setOutputState(1, false)
                }
              });
              rfid.addOutputChangeListener(new OutputChangeListener()
              {
                def outputChanged(oe : OutputChangeEvent)
                {
                  println(oe.getIndex + " change to " + oe.getState);
                }
              });
        }
        case "write" => {      
              //write a tag:
              try {
                  val uuid = UUID.randomUUID();
                  val time = System.currentTimeMillis()/1000;
                  val tag = (time.toString() ++ uuid.toString.replace("-", "").substring(9,23))
                  rfid.write(tag, RFIDPhidget.PHIDGET_RFID_PROTOCOL_PHIDGETS, false);  //écrit sur la carte
                  println("\nWrite Tag : " + tag);
                  //entre le tag du user en BD
                  val responsePost = Http.post("http://smarking.azurewebsites.net/api/users").params("idTag" -> tag).asString        
                  println(responsePost)
                  rfid.setOutputState(1, true) //ecriture : vert si tag a bien été écrit en BD
                  Thread.sleep(1000)
                  rfid.setOutputState(1, false)
              } catch {
                  case exc : PhidgetException => println(exc)
              }
        }
        case _ => println("Erreur.")
      }
   
      println("Outputting events.  Input to stop.");
      System.in.read();
      print("closing...");
      rfid.close();
  }
}