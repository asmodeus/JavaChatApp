package bookmark;

public class TestBookmarks {
	public static void main(String[] args) {
		BookList myBookmarks = new BookList(3); // skapar en random booklist som
												// fylls med några genererade objekt
												// enbart för testning
		
		
		myBookmarks.printMarks();
		
		myBookmarks.serializeToFile("listan");
		
		myBookmarks.clear(); log("List has been cleared.");
		
		myBookmarks.printMarks();
		
		try {
			myBookmarks = BookList.deserializeFromFile("listan");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		myBookmarks.printMarks();
	}

	private static void log(String string) {
		System.out.println(string);
	}
}
