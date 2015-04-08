package viewScala

import barrier.controller._
import model._
import viewJava._
import com.phidgets._
import com.phidgets.event._
import javax.swing._
import scala.util._
import interfaceKit.data._
import interfaceKit.controller.InterfaceKit

object Main {
  
  def main(args: Array[String]) 
  {
    InterfaceKit.openAny
    val interfaceGraphique = true
    val barriere = new Barriere()
    barriere.Barriere()
    
    if(interfaceGraphique){
      
        val panelWelcome = new PanelWelcome
        {

          override def actionScalaForm()
          {
            if (_comboBox.getSelectedItem() == "Inscription")
            {
              RFID.action = "write"
              switchToWriteMode()
            }
            else
              switchToReadMode()
          }

          override def actionScalaIn()
          {
            RFID.in()
          }

          override def actionScalaOut()
          {
            RFID.out()
          }

          override def actionScalaOpen()
          {
            barriere.ouverture()
          }

          override def actionScalaClose()
          {
            barriere.fermeture()
          }
<<<<<<< HEAD:RFIDProject/src/main/scala/viewScala/Main.scala

          override def actionScalaWrite()
          {
            verifChamps match
            {
              case None =>
              {
                val person = new Person(_textFieldUserPrenom.getText, _textFieldUserNom.getText, _textFieldUserMail.getText)

                if (RFID.inscriptionTag(person))
                {
                  RFID.ledGreenOn()
                  JOptionPane.showMessageDialog(null, "L'utilisateur est bien inscrit", "Inscription", JOptionPane.INFORMATION_MESSAGE);
                  RFID.ledGreenOff()
                }
                else
                {
                  RFID.ledRedOn()
                  JOptionPane.showMessageDialog(null, "L'utilisateur n'a pas été inscrit", "Inscription", JOptionPane.ERROR_MESSAGE);
                  RFID.ledRedOff()
                }
              }
              case Some(exc) =>
              {
                JOptionPane.showMessageDialog(null, exc.toString(), "Inscription", JOptionPane.ERROR_MESSAGE);
              }
            }
          }

          override def actionScalaUpdate()
          {

          }

          def verifChamps(): Option[String] =
          {
            if (_textFieldUserPrenom.getText.isEmpty() || _textFieldUserNom.getText.isEmpty() || _textFieldUserMail.getText.isEmpty())
              Some("Veuillez remplir les champs.")
            else if (!_textFieldUserMail.getText.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"))
            {
              Some("Veuillez encoder une adresse e-mail correcte.")
            }
            else
            {
              None
            }
=======
    
          override def actionScalaWrite() {
              verifChamps match {
                case None => {
                  val person = new Person(_textFieldUserPrenom.getText, _textFieldUserNom.getText, _textFieldUserMail.getText)
          
                  if (RFID.inscriptionTag(person)) {
                    RFID.ledGreenOn()
                    JOptionPane.showMessageDialog(null, "L'utilisateur est bien inscrit", "Inscription", JOptionPane.INFORMATION_MESSAGE);
                    RFID.ledGreenOff()
                  } else {
                    RFID.ledRedOn()
                    JOptionPane.showMessageDialog(null, "L'utilisateur n'a pas été inscrit", "Inscription", JOptionPane.ERROR_MESSAGE);
                    RFID.ledRedOff()
                  }
                }
                case Some(exc) => {
                 JOptionPane.showMessageDialog(null, exc.toString(), "Inscription", JOptionPane.ERROR_MESSAGE);
                }
              }
          }
    
          override def actionScalaUpdate() {
              val tag = _textFieldTagLu.getText
              if(tag.isEmpty()) {
                RFID.ledRedOn()
                JOptionPane.showMessageDialog(null, "Veuillez scanner le tag RFID", "Inscription", JOptionPane.ERROR_MESSAGE);
                RFID.ledRedOff()
              } else {
                verifChamps match {
                  case None => {
                    val person = new Person(_textFieldUserPrenom.getText, _textFieldUserNom.getText, _textFieldUserMail.getText)
            
                    if (RFID.updateUser(tag, person)) {
                      RFID.ledGreenOn()
                      JOptionPane.showMessageDialog(null, "L'utilisateur a bien été mis à jour", "MAJ", JOptionPane.INFORMATION_MESSAGE);
                      RFID.ledGreenOff()
                    } else {
                      RFID.ledRedOn()
                      JOptionPane.showMessageDialog(null, "L'utilisateur n'a pas été mis à jour", "MAJ", JOptionPane.ERROR_MESSAGE);
                      RFID.ledRedOff()
                    }
                  }
                  case Some(exc) => {
                    JOptionPane.showMessageDialog(null, exc.toString(), "MAJ", JOptionPane.ERROR_MESSAGE);
                  }
                }
              }
          }
          
          def verifChamps () : Option[String] = {
              if(_textFieldUserPrenom.getText.isEmpty() || _textFieldUserNom.getText.isEmpty() || _textFieldUserMail.getText.isEmpty())
                Some("Veuillez remplir les champs.")
              else if (!_textFieldUserMail.getText.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
                Some("Veuillez encoder une adresse e-mail correcte.")
              } else {
                None
              }
>>>>>>> origin/master:RFIDProject/src/viewScala/Main.scala
          }

          RFID.rfid.addTagGainListener(new TagGainListener()
          {
            def tagGained(oe: TagGainEvent)
            {
              val tag = oe.getValue
              println("\nTag Gained: " + tag)
              _textFieldTagLu.setText(tag)

              if (RFID.action != "write")
              {
                val userOption = DataGet.found(tag)

                userOption match
                {
                  case Some(user) =>
                  {
                    updatePersonFields(user.lastName, user.firstName, user.mail)

                    if (RFID.action == "in" || RFID.action == "out")
                    {
                      DataGet.searchTagUser(tag, RFID.action) match
                      {
                        case true =>
                        {
                          RFID.ledGreenOn()
                          barriere.ouverture
                          JOptionPane.showMessageDialog(null, "L'utilisateur est bien passé", "Passage", JOptionPane.INFORMATION_MESSAGE);
                          RFID.carPassed(tag) match
                          {
                            case true => DataAdd.addFlowParking(tag, RFID.action)
                            case false => JOptionPane.showMessageDialog(null, "La voiture n'est pas passée", "Passage", JOptionPane.ERROR_MESSAGE);
                          }
                          barriere.fermeture
                          RFID.ledGreenOff()
                        }
                        case false =>
                        {
                          RFID.ledRedOn() //rouge si le tag est présent en BD
                          JOptionPane.showMessageDialog(null, "L'utilisateur n'a pas pu passer", "Passage", JOptionPane.ERROR_MESSAGE);
                          RFID.ledRedOff()
                        }
                      }
                    }

                  }
                  case None =>
                  {
                    updatePersonFields("", "", "")

                    RFID.ledRedOn() //rouge si le tag est présent en BD
                    JOptionPane.showMessageDialog(null, "L'utilisateur n'existe pas", "Passage", JOptionPane.ERROR_MESSAGE);
                    RFID.ledRedOff()
                  }
                }
              }
            }
          })

          RFID.rfid.addDetachListener(new DetachListener()
          {
            def detached(ae: DetachEvent)
            {
              println("detachment  1 of " + ae)
              JOptionPane.showMessageDialog(null, "Veuillez rattacher le Phidget RFID", "RFID detached", JOptionPane.WARNING_MESSAGE);
            }
          })

          RFID.rfid.addErrorListener(new ErrorListener()
          {
            def error(ee: ErrorEvent)
            {
              println("error event for " + ee)
              JOptionPane.showMessageDialog(null, "Il y a eu une erreur pour l'événement " + ee, "RFID error", JOptionPane.ERROR_MESSAGE);
            }
          })
        }
    
        val MainJFrame = new MainJFrame(panelWelcome)
        
    } else {
      
        RFID.rfid.addTagGainListener(new TagGainListener() {
            def tagGained(oe: TagGainEvent) {
              val tag = oe.getValue
              println("\nTag Gained: " + tag)
              val userOption = DataGet.found(tag)

              println("test1")

                userOption match {
                  case Some(user) => {
                    val action = DataGet.foundAction()
                    action match {
                      case ("in" | "out") => {
                        DataGet.searchTagUser(tag, action) match {
                          case true => {
                            RFID.ledGreenOn()
                            barriere.ouverture
                            RFID.carPassed(tag) match {
                              case true => DataAdd.addFlowParking(tag, action)
                              case false => println("Car not passed")
                            }
                            Thread.sleep(1000)
                            barriere.fermeture
                            RFID.ledGreenOff()
                          }
                          case false => {
                            RFID.ledRedOn() //rouge si le tag est présent en BD   
                            Thread.sleep(1000)
                            RFID.ledRedOff()
                          }
                        }
                      }
                      case _ => {
                            println("Pas la bonne action")
                            RFID.ledRedOn() 
                            Thread.sleep(1000)
                            RFID.ledRedOff()
                      }
                    }
                  }
                  case None => {
                    RFID.ledRedOn() //rouge si le tag est présent en BD
                    Thread.sleep(1000)
                    RFID.ledRedOff()
                  }
                }
            }
         })
            
     }
  }
}