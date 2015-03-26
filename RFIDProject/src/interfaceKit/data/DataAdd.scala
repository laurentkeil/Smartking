package interfaceKit.data

import scalaj.http._
import scala.util._

object DataAdd 
{
  def register(tag: String, userLastname: String, userFirstName: String, userMail: String) = 
  {
    Try(Http.post("http://smarking.azurewebsites.net/api/users").params(Map(("idTag", tag), ("lastname", userLastname), ("firstname", userFirstName), ("mail", userMail))).asString)
  }
  
  def updateTagCarNotComeIn(tagRfid:String)
  {
    
  }
  
  def updateTagCarComeIn(tagRfid:String)
  {
    
  }
  
  def addEntryInParking(tagRfid:String)
  {
    
  }
  
  def addLeavinfFromParking(tagRfid:String)
  {
    
  }
}