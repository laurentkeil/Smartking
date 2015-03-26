package viewScala

import barrier.controller._
import model._
import viewJava._
import com.phidgets._
import com.phidgets.event._
import javax.swing._
import scala.util._
import interfaceKit.data._

object Main {
  
  def main(args: Array[String]) 
  {
    val panelWelcome = (new PanelWelcome {

      override def actionScalaForm() {
        if (_comboBox.getSelectedItem() == "Inscription") {
          RFID.action = "write"
          switchToWriteMode()
        } else
          switchToReadMode()
      }

      override def actionScalaIn() {
        RFID.in()
      }

      override def actionScalaOut() {
        RFID.out()
      }

      override def actionScalaOpen() {
        Barriere.ouverture()
      }

      override def actionScalaClose() {
        Barriere.fermeture()
      }

      override def actionScalaWrite() {
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

      override def actionScalaUpdate() {

      }

      RFID.rfid.addTagGainListener(new TagGainListener() {
        def tagGained(oe: TagGainEvent) {
          val tag = oe.getValue
          println("\nTag Gained: " + tag)
          _textFieldTagLu.setText(tag)

          if (RFID.action != "write") {
            val userOption = DataGet.found(tag)

            userOption match {
              case Some(user) => {
                updatePersonFields(user.lastName, user.firstName, user.mail)

                DataGet.searchTageUser(tag) match {
                  case true => {
                    RFID.ledGreenOn()
                    Barriere.ouverture
                    JOptionPane.showMessageDialog(null, "L'utilisateur est bien passé", "Passage", JOptionPane.INFORMATION_MESSAGE);
                    RFID.carPassed(tag) //TODO
                    Barriere.fermeture
                    RFID.ledGreenOff()
                  }
                  case false => {
                    RFID.ledRedOn() //rouge si le tag est présent en BD
                    JOptionPane.showMessageDialog(null, "L'utilisateur n'a pas pu passer", "Passage", JOptionPane.ERROR_MESSAGE);
                    RFID.ledRedOff()
                  }
                }
              }
              case None => {
                updatePersonFields("", "", "")
                
                RFID.ledRedOn() //rouge si le tag est présent en BD
                JOptionPane.showMessageDialog(null, "L'utilisateur n'existe pas", "Passage", JOptionPane.ERROR_MESSAGE);
                RFID.ledRedOff()
              }
            }
          }
        }
      })

      RFID.rfid.addDetachListener(new DetachListener() {
        def detached(ae: DetachEvent) {
          println("detachment  1 of " + ae)
          JOptionPane.showMessageDialog(null, "Veuillez rattacher le Phidget RFID", "RFID detached", JOptionPane.WARNING_MESSAGE);
        }
      })

      RFID.rfid.addErrorListener(new ErrorListener() {
        def error(ee: ErrorEvent) {
          println("error event for " + ee)
          JOptionPane.showMessageDialog(null, "Il y a eu une erreur pour l'événement " + ee, "RFID error", JOptionPane.ERROR_MESSAGE);
        }
      })
    })

    val MainJFrame = new MainJFrame(panelWelcome)
  }
}