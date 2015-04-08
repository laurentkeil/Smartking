package interfaceKit.data

import scalaj.http._
import scala.util._

object DataAdd 
{
  def register (tag: String, userLastname: String, userFirstName: String, userMail: String) = 
  {
    Try(Http.post("http://smarking.azurewebsites.net/api/users").params(Map(("idTag", tag), ("lastname", userLastname), ("firstname", userFirstName), ("mail", userMail))).asString)
  }
  
  def updateUser (idUser : String, userLastname: String, userFirstName: String, userMail: String) = 
  {
    Try(Http("http://smarking.azurewebsites.net/api/users").method("put").params(Map(("id", idUser), ("lastname", userLastname), ("firstname", userFirstName), ("mail", userMail))).asString)
  }
  
  def updateTagCarNotComeIn(tagRfid:String)
  {
    
  }
  
  def updateTagCarComeIn(tagRfid:String)
  {
    
  }
  
  def addFlowParking(idTag : String, action : String)
  {
    Try(Http.post("http://smarking.azurewebsites.net/api/FlowUsers").params("action" -> action).params("idTag" -> idTag).asString)  
  }
  
  def addLeavingFromParking(tagRfid:String)
  {
    
  }
  
  def addTemperatureInWebservice()
  {
    //val request = Http.post("http://smarking.azurewebsites.net/api/users").
  }
  
}