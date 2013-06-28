package clientgui;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

import bookmark.BookList;
import chatclient.Client;

public class BookmarkList {
	
		JDialog bkUtil = new JDialog();
		GridBagConstraints gc = new GridBagConstraints();
		BookList myBookmarks;
		Client client1;
		ChatPanel chatGui;
		int i;
		Container c = bkUtil.getContentPane();
		
	
	
		BookmarkList(ChatPanel panel){
		chatGui = panel;
		try {
			myBookmarks = BookList.deserializeFromFile("listan");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bkUtil.setVisible(true);
		bkUtil.setSize(300, myBookmarks.size()*75);
		bkUtil.setLayout(new GridBagLayout());
		JLabel label[] = new JLabel[myBookmarks.size()];
		JButton buttons[] = new JButton[myBookmarks.size()];
		JButton delButtons[] = new JButton[myBookmarks.size()];
		
		
		
		
		for(i=0; i < myBookmarks.size(); i++){
			label[i] = new JLabel("Name: " + myBookmarks.get(i).getServerName());
			buttons[i] = new JButton("Connect");
			delButtons[i] = new JButton("Delete");
			final int g = i;
			buttons[i].addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					chatGui.client1 = new Client(myBookmarks.get(g).getServerAdress(), myBookmarks.get(g).getPortNumber(), chatGui);
					bkUtil.dispose();
				}
			});
			
			delButtons[i].addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					myBookmarks.remove(g);
					myBookmarks.serializeToFile("listan");
					bkUtil.dispose();
				}
			});
			
			gc.anchor = gc.NORTHWEST;
			gc.weightx = 0.1;
			gc.weighty = 1d;
			gc.fill = gc.BOTH;
			gc.gridx = 0;
			gc.gridy = i;
			c.add(label[i], gc);
			gc.anchor = gc.NORTHEAST;
			gc.weightx = 0;
			gc.gridx = 1;
			gc.gridy = i;
			c.add(buttons[i], gc);
			gc.gridx = 2;
			gc.gridy = i;
			c.add(delButtons[i], gc);
		
		}
		

		
	
			

		
		
		
	}
		

}
