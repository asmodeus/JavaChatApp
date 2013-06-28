package bookmark;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

//Klassen är en subklass till ArrayList och sparar kan spara ned alla bokmärken i en lista som lätt kan editeras genom
//olika inbyggda funktioner i ArrayList.

public class BookList extends ArrayList<Bookmark> {

	//constructors with different parameters
	public BookList(){
		super();
	}

	//constructors with different parameters
	@SuppressWarnings("unchecked")
	BookList(int x){
		super();
		if (x<50)
			this.addAll(createSomeMarks(x));
	}
	
	public void serializeToFile(String filename) {
		System.out.println("Serializing...");
		try {
			FileOutputStream createFile = new FileOutputStream(filename+".ser");
			ObjectOutputStream out = new ObjectOutputStream(createFile);
			out.writeObject(this); //gör "detta" BookList objektet till en serialiserad fil 
			out.close();
			createFile.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
		System.out.println("Serialization successful.");
	}

	public static BookList deserializeFromFile(String filename) throws Exception{
		BookList savedList = null;
		System.out.println("Deserializing...");
			FileInputStream importFile = new FileInputStream(filename+".ser");
			ObjectInputStream inObj = new ObjectInputStream(importFile);
			savedList = (BookList) inObj.readObject(); //Filen som importeras blir explicit till en BookList
			inObj.close();
			importFile.close();
		
		System.out.println("Deserialization successful.");
		return savedList;
	}
//	public static BookList deserializeFromFile(String filename) {
//		BookList savedList = null;
//		System.out.println("Deserializing...");
//		try {
//			FileInputStream importFile = new FileInputStream(filename+".ser");
//			ObjectInputStream inObj = new ObjectInputStream(importFile);
//			savedList = (BookList) inObj.readObject(); //Filen som importeras blir explicit till en BookList
//			inObj.close();
//			importFile.close();
//		} catch (IOException i) {
//			i.printStackTrace();
//		} catch (ClassNotFoundException c) {
//			System.out.println("ArrayList not found");
//			c.printStackTrace();
//		}
//		System.out.println("Deserialization successful.");
//		return savedList;
//	}

	@SuppressWarnings("unchecked")
	private BookList createSomeMarks (int x){
		BookList markList = new BookList();
		for (int i = 0; i < x;i++){
			Bookmark m = new Bookmark();
			m.setPortNumber(m.getPortNumber()+i);
			markList.add(m);
		}
		return markList;
	}
	

	public void printMarks () {
		if (this.isEmpty())
			System.out.println("this BookList is empty");
		else {
			for (Bookmark m : this) {
				m.printValues();
			}
		}
	}
	
	
	

}
