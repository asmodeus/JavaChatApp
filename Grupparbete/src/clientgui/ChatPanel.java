package clientgui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultCaret;

import chatclient.Client;

public class ChatPanel {
	
	JFrame chatFrame = new JFrame();
	JTextArea chatWindow = new JTextArea();
	public JTextArea onlineUsers = new JTextArea();
	JPanel actionPanel = new JPanel();
	JTextField chatInput = new JTextField(19);
	JButton sendButton = new JButton("Send");
	JScrollPane scroll = new JScrollPane(chatWindow);
	JDialog connectionFrame = new JDialog();
	JLabel ipLabel = new JLabel("IP:");
	JLabel portLabel = new JLabel("Port:");
	JButton connectButton = new JButton("Connect");
	JTextField ipInput = new JTextField(19);
	JTextField portInput = new JTextField(19);
	JTextField nameInput = new JTextField(19);
	JMenuBar bar = new JMenuBar();
	JMenu fileMenu = new JMenu("File");
	JMenuItem connect = new JMenuItem("Connect to server...");
	JMenuItem disco = new JMenuItem("Disconnnect");
	JMenuItem quit = new JMenuItem("Quit");
	JMenu bkMenu = new JMenu("Bookmarks");
	JMenuItem addBk = new JMenuItem("Add server");
	JMenuItem showBk = new JMenuItem("Bookmark list");

	Container c = chatFrame.getContentPane();
	Container c1 = connectionFrame.getContentPane();
	GridBagConstraints gc = new GridBagConstraints();


	ChatPanel thisPanel;
	volatile Client client1 = null;
	
	ChatPanel(){
		thisPanel = this;
		chatFrame.setSize(600, 700);
//		chatFrame.setResizable(false);
		chatFrame.setVisible(true);
		chatFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		chatFrame.addWindowListener(exitListener);
		chatFrame.setLayout(new BorderLayout());
		
		
		chatWindow.setSize(200, 300);
		chatWindow.setVisible(true);
		chatWindow.setEditable(false);
		
		onlineUsers.setSize(100, 600);
		onlineUsers.setVisible(true);
		onlineUsers.setEditable(false);
		onlineUsers.setText("Online users:                            ");
		
		
		actionPanel.setSize(200, 100);
		actionPanel.setVisible(true);
		actionPanel.setLayout(new GridBagLayout());
		
		chatInput.setSize(100, 50);
		gc.anchor = gc.WEST;
		gc.fill = gc.BOTH;
		gc.weightx = 0.5;
		gc.gridx=0;
		gc.gridy=0;
		actionPanel.add(chatInput, gc);
		gc.anchor = gc.EAST;
		gc.weightx = 0;
		gc.gridx = 1;
		gc.gridy = 0;
		actionPanel.add(sendButton, gc);
		
		c.add(actionPanel, BorderLayout.SOUTH);
		c.add(scroll, BorderLayout.CENTER);
		c.add(onlineUsers, BorderLayout.EAST);
		
		fileMenu.add(connect);
		fileMenu.add(disco);
		fileMenu.addSeparator();
		fileMenu.add(quit);
		bkMenu.add(addBk);
		bkMenu.add(showBk);
		bar.add(fileMenu);
		bar.add(bkMenu);
		chatFrame.setJMenuBar(bar);
		
//Hï¿½r bï¿½rjar funktionalitetet fï¿½r vissa elementer		
		
		fileMenu.setMnemonic(KeyEvent.VK_F);
		bkMenu.setMnemonic(KeyEvent.VK_B);
		quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		quit.setMnemonic(KeyEvent.VK_Q);
		showBk.setMnemonic(KeyEvent.VK_B);
		connect.setMnemonic(KeyEvent.VK_C);
		disco.setMnemonic(KeyEvent.VK_D);
		addBk.setMnemonic(KeyEvent.VK_A);
		
		disco.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try{
					client1.closing();
				}catch(Exception ee){
				}
			}
		});
		
		quit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					client1.closing();
				}catch (Exception x){
				}
				System.exit(0);
			}
		});
		
		connect.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				thisPanel.connectionMenu();
			}
		});
		
		
		sendButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					client1.writeFromFrame(chatInput.getText());
					//System.out.println(chatInput.getText());
					chatInput.setText("");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		
		
		connectButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					client1 = new Client(ipInput.getText(), Integer.parseInt(portInput.getText()), thisPanel);
					connectionFrame.dispose();
				}catch(Exception e1){
					System.out.println("WROOOONG");
				}
			}
			
		});
		
		
		chatInput.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					client1.writeFromFrame(chatInput.getText());
//					System.out.println(chatInput.getText());
					chatInput.setText("");
				} catch (IOException | NullPointerException e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
				}
				
			}
			
		});
		
		
		showBk.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				BookmarkList bkl = new BookmarkList(thisPanel);
			}
		});
		
		addBk.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
//				thisPanel.addBookmarkDialog();
				AddingBookmarks addBookMark = new AddingBookmarks();
			}
		});
		
		DefaultCaret caret = (DefaultCaret)chatWindow.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
	}
	
	
	
	//ovriga metoder
	
	public int closeConnection(Client client){
		Client clCon = client;
		clCon.closing();
		System.exit(0);
		return 0;
	}

	public void addToText(String msg){
		this.chatWindow.append(msg + "\n");
	}
	
	public void connectionMenu(){
		connectionFrame.setVisible(true);
		connectionFrame.setSize(270, 120);
		connectionFrame.setResizable(false);
		connectionFrame.setLayout(new GridBagLayout());
		gc.anchor = gc.WEST;
		gc.weightx = 0.1;
		gc.gridx = 0;
		gc.gridy = 0;
		c1.add(ipLabel, gc);
		gc.gridx = 1;
		gc.gridy = 0;
		c1.add(ipInput, gc);
		gc.gridx = 0;
		gc.gridy = 1;
		c1.add(portLabel, gc);
		gc.gridx = 1;
		gc.gridy = 1;
		c1.add(portInput, gc);
		gc.gridx = 0;
		gc.gridy = 2;
		gc.gridwidth = 2;
		c1.add(connectButton, gc);
		
	}
	
	
	
	
	
	WindowListener exitListener = new WindowAdapter() {

        @Override
        public void windowClosing(WindowEvent e) {
            int confirm = JOptionPane.showOptionDialog(null, "Are You Sure to Close Application?", "Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (confirm == 0) {
               try{
            	   client1.closing();
               }catch(Exception ee){
            	   System.out.println("No connection found");
               }
               System.exit(0);
            }
        }
    };
	
	

}
