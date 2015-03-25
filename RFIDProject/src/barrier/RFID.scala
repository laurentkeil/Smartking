package barrier

import java.nio.ByteBuffer
import java.util.UUID
import com.phidgets._
import com.phidgets.event._
import scalaj.http._
import java.io.InputStreamReader
import org.json._
import javax.swing._
import java.awt.event._
import java.awt._
  
/** 
 * @author laurent 
 */ 
object RFID extends JFrame {
  
        val rfid = new RFIDPhidget()
        val barriere = new Barriere()    
        var action = "no"
        var inscription = false
  
        val container = new JPanel()
        val center = new JPanel()
        val south = new JPanel()
        val top = new JPanel();
        val readPan = new JPanel();
        val userPan = new JPanel();
        val write = new JButton("Inscription")
        val update = new JButton("Mise à jour")
        val in = new JButton("Entrée parking")
        val out = new JButton("Sortie Parking")
        val open = new JButton("Ouvrir barrière")
        val close = new JButton("Fermer barrière")
        val choix = new JLabel("Choix de l'action")
        val combo = new JComboBox(new Array[String](0))
        val tagLabel = new JLabel();
        val tagLu = new JTextField();
        val userNomLabel = new JLabel();
        val userNom = new JTextField();
        val userPrenomLabel = new JLabel();
        val userPrenom = new JTextField();
        val userMailLabel = new JLabel();
        val userMail = new JTextField();
        val userMdpLabel = new JLabel();
        val userMdp = new JTextField();
        val client = new JCheckBox("Client");
      
    /** Creates new form RFID */
    def RFID() {
        initComponents()
    }
    
    def initComponents() {
      
        //Définit un titre pour notre fenêtre
        setTitle("RFID SmartKing")
        //Définit sa taille : 400 pixels de large et 100 pixels de haut
        setSize(350, 550)
        //Nous demandons maintenant à notre objet de se positionner au centre
        setLocationRelativeTo(null)
        //Termine le processus lorsqu'on clique sur la croix rouge
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
        setResizable(false)

        container.setBackground(Color.GRAY)
        container.setLayout(new BorderLayout())
        
        combo.addItem("Scan")
        combo.addItem("Inscription")  
        combo.addActionListener(new FormListener())
        top.add(choix)
        top.add(combo)    
        container.add(top, BorderLayout.NORTH)
         
        userNomLabel.setText("Nom : ")
        userNom.setEditable(false)
        userNom.setPreferredSize(new Dimension(300, 30))
        userPrenomLabel.setText("Prénom : ")
        userPrenom.setEditable(false)
        userPrenom.setPreferredSize(new Dimension(300, 30))
        userMailLabel.setText("E-mail : ")
        userMail.setEditable(false)
        userMail.setPreferredSize(new Dimension(300, 30))
        userPan.setBorder(BorderFactory.createTitledBorder("Informations utilisateur"))
        userPan.setBackground(Color.WHITE)
        userPan.add(userNomLabel)
        userPan.add(userNom)
        userPan.add(userPrenomLabel)
        userPan.add(userPrenom)
        userPan.add(userMailLabel)
        userPan.add(userMail)
        client.setEnabled(false)
        userPan.add(client)
        
        tagLabel.setText("Tag : ")
        tagLu.setEditable(false)
        tagLu.setPreferredSize(new Dimension(300, 30))
        readPan.setBorder(BorderFactory.createTitledBorder("Tag lu"))
        readPan.setPreferredSize(new Dimension(300, 100))
        readPan.setBackground(Color.WHITE)
        readPan.add(tagLabel)
        readPan.add(tagLu)
        
        center.setLayout(new BorderLayout())
        center.setBackground(Color.WHITE)
        center.add(readPan, BorderLayout.NORTH)
        center.add(userPan, BorderLayout.CENTER)
        container.add(center, BorderLayout.CENTER)
        
        south.setPreferredSize(new Dimension(300, 70))
        in.addActionListener(new inListener())
        south.add(in)
        out.addActionListener(new outListener())  
        south.add(out)
        open.addActionListener(new openListener()) 
        south.add(open)
        close.addActionListener(new closeListener())
        south.add(close)
        container.add(south, BorderLayout.SOUTH)
    
        setContentPane(container)  
        
        //Et enfin, la rendre visible        
        setVisible(true)
    }
    
     //Classe écoutant notre bouton
    class openListener extends ActionListener {
       def actionPerformed(arg0 : ActionEvent) {
         barriere.ouverture()
      }
    }
     //Classe écoutant notre bouton
    class closeListener extends ActionListener {
       def actionPerformed(arg0 : ActionEvent) {
         barriere.fermeture()
      }
    }
     //Classe écoutant notre bouton
    class inListener extends ActionListener {
       def actionPerformed(arg0 : ActionEvent) {
         action = "in"
      }
    }
     //Classe écoutant notre bouton
    class outListener extends ActionListener {
       def actionPerformed(arg0 : ActionEvent) {
         action = "out"
      }
    }
     //Classe écoutant notre bouton
    class updateListener extends ActionListener {
       def actionPerformed(arg0 : ActionEvent) {
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
                    case exc : PhidgetException => println(exc)
                    rfid.setOutputState(0, true)
                    JOptionPane.showMessageDialog(null, "Les informations n'ont pas pu être mise à jour", "Mis à jour", JOptionPane.ERROR_MESSAGE);                   
                    Thread.sleep(1000)
                    rfid.setOutputState(0, false)
                }
      }
    }
     //Classe écoutant notre bouton
    class writeListener extends ActionListener {
       def actionPerformed(arg0 : ActionEvent) {
         action = "write"
          //write a tag:
                try {
                    val uuid = UUID.randomUUID();
                    val time = System.currentTimeMillis()/1000;
                    val tag = (time.toString() ++ uuid.toString.replace("-", "").substring(9,23))
                    rfid.write(tag, RFIDPhidget.PHIDGET_RFID_PROTOCOL_PHIDGETS, false);  //écrit sur la carte
                    println("\nWrite Tag : " + tag);
                    //entre le tag du user en BD TODO vide
                    val responsePost = Http.post("http://smarking.azurewebsites.net/api/users").params(Map(("idTag", tag), ("lastname", userNom.getText), ("firstname", userPrenom.getText), ("mail", userMail.getText))).asString        
                    println(responsePost)
                    rfid.setOutputState(1, true) //ecriture : vert si tag a bien été écrit en BD
                    //Boîte du message d'information
                    JOptionPane.showMessageDialog(null, "L'utilisateur est bien inscrit", "Inscription", JOptionPane.INFORMATION_MESSAGE);
                    Thread.sleep(1000)
                    rfid.setOutputState(1, false)
                } catch {
                    case exc : PhidgetException => println(exc) 
                    rfid.setOutputState(0, true)
                    JOptionPane.showMessageDialog(null, "L'utilisateur n'a pas été inscrit", "Inscription", JOptionPane.ERROR_MESSAGE);                      
                    Thread.sleep(1000)
                    rfid.setOutputState(0, false)
                }
      }
    }
     
    // Changement de formulaire
    class FormListener extends ActionListener {
      def actionPerformed(e : ActionEvent) {
        if(combo.getSelectedItem() == "Inscription") {
          inscription = true
          south.removeAll()
          south.revalidate()
          south.repaint()
          write.addActionListener(new writeListener())
          south.add(write)
          update.addActionListener(new updateListener())
          south.add(update)
          userNom.setEditable(true)
          userPrenom.setEditable(true)
          userMail.setEditable(true)
        } else {
          action = "no"
          inscription = false
          south.removeAll()
          south.revalidate()
          south.repaint()
          in.addActionListener(new inListener())
          south.add(in)
          out.addActionListener(new outListener())  
          south.add(out)
          open.addActionListener(new openListener()) 
          south.add(open)
          close.addActionListener(new closeListener())
          south.add(close)
          userNom.setEditable(false)
          userPrenom.setEditable(false)
          userMail.setEditable(false)
        }
      }  
    } 
    
    def main(args:Array[String]) {
        barriere.Barriere()
        
        RFID()
        
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
        rfid.addOutputChangeListener(new OutputChangeListener() {
                  def outputChanged(oe : OutputChangeEvent)
                  {
                    println(oe.getIndex + " change to " + oe.getState);
                  }
        });
        
        rfid.openAny();
        println("waiting for RFID attachment...");
        rfid.waitForAttachment(1000);
        
        rfid.addTagGainListener(new TagGainListener()
                { 
                  def tagGained(oe : TagGainEvent)
                  {
                    val tag = oe.getValue
                    println("\nTag Gained: " + tag);
                    tagLu.setText(tag)
                    if(!inscription) {
                      val responseGet = Http.get("http://smarking.azurewebsites.net/api/users/" + tag).asString
                      if(responseGet != "\"NotFound\"") {
                        val json = new JSONObject(responseGet)
                        userNom.setText(json.get("lastname").toString)
                        userPrenom.setText(json.get("firstname").toString)
                        userMail.setText(json.get("mail").toString)
                        client.setSelected(true)
                      } else {
                        userNom.setText("")
                        userPrenom.setText("")
                        userMail.setText("")
                        client.setSelected(false)
                      }
                      //recherche le tag du user en BD
                      if(action == "in" || action == "out") {
                        val responsePost = Http.post("http://smarking.azurewebsites.net/api/FlowUsers").params(Map(("action", action),("idTag", tag))).asString        
                        println(responsePost)
                        if (responsePost != "\"NotFound\"" && responsePost != "\"AccessDenied\"") {   
                           rfid.setOutputState(1, true) //vert si le tag est présent en BD
                           barriere.ouverture()
                           //Boîte du message d'information
                           JOptionPane.showMessageDialog(null, "L'utilisateur est bien passé", "Passage", JOptionPane.INFORMATION_MESSAGE);
                           Thread.sleep(2000)
                           barriere.fermeture()
                        } else {  
                           rfid.setOutputState(0, true) //rouge si le tag est présent en BD
                           JOptionPane.showMessageDialog(null, "L'utilisateur n'a pas pu passer", "Passage", JOptionPane.ERROR_MESSAGE);
                        }
                      }
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
     
        println("Outputting events.  Input to stop.");
        System.in.read();
        rfid.close();
    }
}