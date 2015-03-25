package view;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Steven on 14-03-15.
 */
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;


public class MainJFrame extends JFrame
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 6618848859440615955L;
	private Container container;
    private JMenuBar menuBarOfApp;
    private JMenu menuFile;
    private JMenuItem menuItemHome, menuItemQuit;

    public MainJFrame()
    {
		/*Création de la fenêtre*/
        super("RFID");
        container =this.getContentPane();
        container.setLayout(new BorderLayout());
        this.setSize(900,700);
        this.setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);
        addWindowListener(eventCloseWindow());

		/*Ajout d'une menuBarOfApp à la fenêtre*/
        menuBarOfApp = new JMenuBar( );
        this.setJMenuBar(menuBarOfApp);

		/*Création des JMenu*/
        menuFile= new JMenu("File");
        menuFile.setMnemonic('F');

		/*Ajout des menus à la menuBarOfApp*/
        menuBarOfApp.add(menuFile);

		/*Création du gestionnaire d'action*/
        MonGestionnaireAction g= new MonGestionnaireAction();

		/*Ajout du JMenuItem "menuItemQuit" au menuFile*/
        menuItemHome =new JMenuItem("Home");
        menuItemHome.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_MASK));
        menuItemHome.addActionListener(g);
        menuFile.add(menuItemHome);

        menuFile.addSeparator( );

        menuItemQuit =new JMenuItem("Exit");
        menuItemQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
        menuItemQuit.addActionListener(g);
        menuFile.add(menuItemQuit);;
        
        this.add(new PanelWelcome());
        this.setVisible(true);
    }

    public WindowAdapter eventCloseWindow()
    {
        return new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent we)
            {
                closeWindow();
            }
        };
    }

    public void closeWindow()
    {
        String buttons[] = {"Yes", "No"};

        int PromptResult = JOptionPane.showOptionDialog(null, "Are you sure you want to exit?", "Warning",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
                buttons, buttons[1]);

        if (PromptResult == 0)
            System.exit(0);
    }

    private class MonGestionnaireAction implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if (e.getSource() == menuItemQuit)
                closeWindow();

            if (e.getSource() == menuItemHome)
            {
                container.removeAll();
                container.add(new PanelWelcome());
                container.repaint();
                container.validate();
            }
        }
    }
}
