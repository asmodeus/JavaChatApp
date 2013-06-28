package clientgui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import bookmark.BookList;
import bookmark.Bookmark;

public class AddingBookmarks {
	 JDialog bkAdding = new JDialog();
	 JLabel nameLabel = new JLabel("Server name:");
	 JLabel ipLabel = new JLabel("IP:");
	 JLabel portLabel = new JLabel("Port:");
	 JTextField ipInput = new JTextField(19);
	 JTextField portInput = new JTextField(19);
	 JTextField nameInput = new JTextField(19);
	 JButton add = new JButton("Add Bookmark");
	 BookList myBookmarks;
	
	AddingBookmarks(){
		GridBagConstraints gc1 = new GridBagConstraints();
		bkAdding.setVisible(true);
		bkAdding.setSize(370, 200);
		bkAdding.setResizable(false);
		bkAdding.setLayout(new GridBagLayout());
		gc1.anchor = gc1.WEST;
		gc1.weightx = 0.1;
		gc1.gridx = 0;
		gc1.gridy = 0;
		bkAdding.add(ipLabel, gc1);
		gc1.gridx = 1;
		gc1.gridy = 0;
		bkAdding.add(ipInput, gc1);
		gc1.gridx = 0;
		gc1.gridy = 1;
		bkAdding.add(portLabel, gc1);
		gc1.gridx = 1;
		gc1.gridy = 1;
		bkAdding.add(portInput, gc1);
		gc1.gridx = 0;
		gc1.gridy = 2;
		bkAdding.add(nameLabel, gc1);
		gc1.gridx = 1;
		gc1.gridy = 2;
		bkAdding.add(nameInput, gc1);
		gc1.gridx = 0;
		gc1.gridy = 3;
		bkAdding.add(add, gc1);
		
		add.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					myBookmarks = BookList.deserializeFromFile("listan");
				}catch (Exception e){
					myBookmarks = new BookList();
				}
				Bookmark newBookmark = new Bookmark(nameInput.getText(), Integer.parseInt(portInput.getText()), ipInput.getText());
				nameInput.setText(null);
				portInput.setText(null);
				ipInput.setText(null);
				bkAdding.dispose();
				myBookmarks.add(newBookmark);
				myBookmarks.serializeToFile("listan");

			}
			
		});
		
	}

}
