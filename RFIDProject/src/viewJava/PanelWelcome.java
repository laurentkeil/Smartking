package viewJava;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public abstract class PanelWelcome extends JPanel 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3862594280332085599L;
	
	public PanelWelcome()
	{
		new JPanel();
		JPanel center = new JPanel();
		JPanel south = new JPanel();
		JPanel top = new JPanel();
		JPanel readPan = new JPanel();
		JPanel userPan = new JPanel();
		new JButton("Inscription");
		new JButton("Mise à jour");
		JButton in = new JButton("Entrée parking");
		JButton out = new JButton("Sortie Parking");
		JButton open = new JButton("Ouvrir barrière");
		JButton close = new JButton("Fermer barrière");
		JLabel choix = new JLabel("Choix de l'action");
		JComboBox<String> combo = new JComboBox<String>();
		JLabel tagLabel = new JLabel();
		JTextField tagLu = new JTextField();
		JLabel userNomLabel = new JLabel();
		JTextField userNom = new JTextField();
		JLabel userPrenomLabel = new JLabel();
		JTextField userPrenom = new JTextField();
		JLabel userMailLabel = new JLabel();
		JTextField userMail = new JTextField();
		new JLabel();
		new JTextField();
		JCheckBox client = new JCheckBox("Client");
      
	    setBackground(Color.GRAY);
	    setLayout(new BorderLayout());

	    combo.addItem("Scan");
	    combo.addItem("Inscription");
	    //combo.addActionListener(new FormListener())
	    top.add(choix);
	    top.add(combo);
	    add(top, BorderLayout.NORTH);

	    userNomLabel.setText("Nom : ");
	    userNom.setEditable(false);
	    userNom.setPreferredSize(new Dimension(300, 30));
	    userPrenomLabel.setText("Prénom : ");
	    userPrenom.setEditable(false);
	    userPrenom.setPreferredSize(new Dimension(300, 30));
	    userMailLabel.setText("E-mail : ");
	    userMail.setEditable(false);
	    userMail.setPreferredSize(new Dimension(300, 30));
	    userPan.setBorder(BorderFactory.createTitledBorder("Informations utilisateur"));
	    userPan.setBackground(Color.WHITE);
	    userPan.add(userNomLabel);
	    userPan.add(userNom);
	    userPan.add(userPrenomLabel);
	    userPan.add(userPrenom);
	    userPan.add(userMailLabel);
	    userPan.add(userMail);
	    client.setEnabled(false);
	    add(client);

	    tagLabel.setText("Tag : ");
	    tagLu.setEditable(false);
	    tagLu.setPreferredSize(new Dimension(300, 30));
	    readPan.setBorder(BorderFactory.createTitledBorder("Tag lu"));
	    readPan.setPreferredSize(new Dimension(300, 100));
	    readPan.setBackground(Color.WHITE);
	    readPan.add(tagLabel);
	    readPan.add(tagLu);

	    center.setLayout(new BorderLayout());
	    center.setBackground(Color.WHITE);
	    center.add(readPan, BorderLayout.NORTH);
	    center.add(userPan, BorderLayout.CENTER);
	    add(center, BorderLayout.CENTER);

	    south.setPreferredSize(new Dimension(300, 70));
	    in.addActionListener(inListener());
	    south.add(in);
	    out.addActionListener(outListener());
	    south.add(out);
	    open.addActionListener(openListener());
	    south.add(open);
	    close.addActionListener(closeListener());
	    south.add(close);
	    add(south, BorderLayout.SOUTH);
	}
	
	public ActionListener inListener()
	{
		return new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				actionScalaIn();
			}
		};
	}
	
	public ActionListener outListener()
	{
		return new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				actionScalaOut();
			}
		};
	}
	
	public ActionListener openListener()
	{
		return new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				actionScalaOpen();
			}
		};
	}
	
	public ActionListener closeListener()
	{
		return new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				actionScalaClose();
			}
		};
	}
	
	protected abstract void actionScalaIn();
	protected abstract void actionScalaOut();
	protected abstract void actionScalaOpen();
	protected abstract void actionScalaClose();
}
