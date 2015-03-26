package interfaceKit.data

import org.json._
import scalaj.http._
import model._

object DataGet 
{
  def found(tag : String) : Option[Person] = 
  {
    val responseGet = Http.get("http://smarking.azurewebsites.net/api/users/" + tag).asString
    
    if (responseGet != "\"TagNotFound\"") 
    {
      val temp = new JSONObject(responseGet)
      Some(new Person(temp.get("id").toString, temp.get("firstname").toString, temp.get("lastname").toString, temp.get("mail").toString))
    } 
    else None
  }
  
  def searchTageUser (tag : String):Boolean = 
  {
    val responseGet = Http.get("http://smarking.azurewebsites.net/api/Tags/in/" + tag).asString
    
    if (responseGet == "\"Ok\"") 
        true
     else 
        false
  }
}