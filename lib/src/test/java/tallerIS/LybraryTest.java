package tallerIS;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;



class LybraryTest {
	
	
  
	@Test
	void testDisplayCatalog() {
		Lybrary library;
	    library = new Lybrary();
	    library.catalog.add(new Book("The Catcher in the Rye", "J.D. Salinger", 5));
	    library.catalog.add(new Book("To Kill a Mockingbird", "Harper Lee", 8));
	    library.catalog.add(new Book("1984", "George Orwell", 10));
		
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        library.displayCatalog();
        System.setOut(System.out);

        String expectedOutput = "Catalog of Available Books:\n" +
                                "Title: The Catcher in the Rye Author: J.D. Salinger - Available: 5\n" +
                                "Title: To Kill a Mockingbird Author: Harper Lee - Available: 8\n" +
                                "Title: 1984 Author: George Orwell - Available: 10\n";
        assertEquals(expectedOutput, outputStream.toString());
	}

	@Test
	void testSelectBooks() {
		Lybrary library;
	    library = new Lybrary();
	    library.catalog.add(new Book("The Catcher in the Rye", "J.D. Salinger", 5));
	    library.catalog.add(new Book("To Kill a Mockingbird", "Harper Lee", 8));
	    library.catalog.add(new Book("1984", "George Orwell", 10));
		List<String> userInput = Arrays.asList("The Catcher in the Rye", "2", "To Kill a Mockingbird", "8", "done");
        provideUserInput(userInput);

        library.selectBooks();

        assertEquals(2, library.selectedBooks.size());
        assertEquals(2, library.selectedBooks.get(0).quantity);
        assertEquals(3, library.selectedBooks.get(1).quantity);
        assertTrue(library.catalog.get(0).available);
        assertFalse(library.catalog.get(1).available);
        assertTrue(library.catalog.get(2).available);
	}
	
	
    private void provideUserInput(List<String> inputList) {
        String input = String.join(System.lineSeparator(), inputList);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
    }

	@Test
	void testGetValidQuantityInput() {
		Lybrary library;
	    library = new Lybrary();
	    library.catalog.add(new Book("The Catcher in the Rye", "J.D. Salinger", 5));
	    library.catalog.add(new Book("To Kill a Mockingbird", "Harper Lee", 8));
	    library.catalog.add(new Book("1984", "George Orwell", 10));
		 String input = "invalid\n3";
	        provideUserInput(input);

	        int result = library.getValidQuantityInput();

	        assertEquals(3, result);
	}
	
	
    private void provideUserInput(String input) {
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
    }

	@Test
	void testDisplayCheckoutSummary() {
		Lybrary library3 = new Lybrary();
        library3.selectedBooks.add(new Book("The Catcher in the Rye", "J.D. Salinger", 2));
        
        String input = "confirm";
        provideUserInput(input);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        
        library3.displayCheckoutSummary();

        System.setOut(System.out);

        assertTrue(outputStream.toString().contains("Total late fees for selected books: $2.0 per day"));
        assertTrue(outputStream.toString().contains("Thank you"));
	}

	@Test
	void testCalculateTotalLateFees() {
		
		Lybrary library2 = new Lybrary();
        library2.selectedBooks.add(new Book("The Catcher in the Rye", "J.D. Salinger", 2));
        library2.selectedBooks.add(new Book("To Kill a Mockingbird", "Harper Lee", 3));
        library2.lateFee=1.0;
        double totalLateFees = library2.calculateTotalLateFees();

        assertEquals(5.0, totalLateFees, 0.001);
	}

	@Test
	void testFindBookByTitle() {
		Lybrary library;
	    library = new Lybrary();
	    library.catalog.add(new Book("The Catcher in the Rye", "J.D. Salinger", 5));
	    library.catalog.add(new Book("To Kill a Mockingbird", "Harper Lee", 8));
	    library.catalog.add(new Book("1984", "George Orwell", 10));
		Book foundBook = library.findBookByTitle("To Kill a Mockingbird");
        assertEquals("To Kill a Mockingbird", foundBook.title);
        assertEquals("Harper Lee", foundBook.author);
        assertEquals(8, foundBook.quantity);
	}

	@Test
	void testFindBookByTitleChange() {
		Lybrary library;
	    library = new Lybrary();
	    library.catalog.add(new Book("The Catcher in the Rye", "J.D. Salinger", 5));
	    library.catalog.add(new Book("To Kill a Mockingbird", "Harper Lee", 8));
	    library.catalog.add(new Book("1984", "George Orwell", 10));
		library.selectedBooks.add(new Book("The Catcher in the Rye", "J.D. Salinger", 2));
        library.selectedBooks.add(new Book("To Kill a Mockingbird", "Harper Lee", 3));
        library.selectedBooks.add(new Book("1984", "George Orwell", 4));
		Book foundBook = library.findBookByTitleChange("To Kill a Mockingbird");
        assertEquals("To Kill a Mockingbird", foundBook.title);
        assertEquals("Harper Lee", foundBook.author);
        assertEquals(3, foundBook.quantity);
	}

}
