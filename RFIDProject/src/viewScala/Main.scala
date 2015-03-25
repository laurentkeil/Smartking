package viewScala

import barrier.Barriere
import viewJava._

object Main 
{
  def main(args: Array[String]) 
  {
    val barriere = new Barriere()
    barriere.Barriere()
    
    val panelWelcome = (new PanelWelcome {
      
      override def actionScalaIn() 
      {
        barriere.ouverture()
      }

      override def actionScalaOut() 
      {
        barriere.fermeture()
      }

      override def actionScalaOpen() 
      {

      }

      override def actionScalaClose() 
      {

      }
      
      override def actionScalaForm()
      {
        
      }
    })
    
    val MainJFrame = new MainJFrame(panelWelcome)
  }  
}