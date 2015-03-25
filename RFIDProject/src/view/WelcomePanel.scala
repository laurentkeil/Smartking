package view;

import javax.swing.JPanel;
import javax.swing._
import java.awt.event._
import java.awt._

object PanelWelcome extends JPanel 
{
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
	  def PanelWelcome() {
	      initComponents()
	  }

	  def initComponents() {

	    /*//Définit un titre pour notre fenêtre
	    setTitle("RFID SmartKing")
	    //Définit sa taille : 400 pixels de large et 100 pixels de haut
	    setSize(350, 550)
	    //Nous demandons maintenant à notre objet de se positionner au centre
	    setLocationRelativeTo(null)
	    //Termine le processus lorsqu'on clique sur la croix rouge
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
	    setResizable(false)*/
      
      val frame = new MainJFrame()

	    setBackground(Color.GRAY)
	    setLayout(new BorderLayout())

	    combo.addItem("Scan")
	    combo.addItem("Inscription")
	    //combo.addActionListener(new FormListener())
	    top.add(choix)
	    top.add(combo)
	    add(top, BorderLayout.NORTH)

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
	    add(client)

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
	    add(center, BorderLayout.CENTER)

	    south.setPreferredSize(new Dimension(300, 70))
	    //in.addActionListener(new inListener())
	    south.add(in)
	    //out.addActionListener(new outListener())
	    south.add(out)
	   // open.addActionListener(new openListener())
	    south.add(open)
	    //close.addActionListener(new closeListener())
	    south.add(close)
	    add(south, BorderLayout.SOUTH)

	    frame.setContentPane(this)

	  }
    
    def main(args: Array[String]) {
        PanelWelcome()
    }
}
