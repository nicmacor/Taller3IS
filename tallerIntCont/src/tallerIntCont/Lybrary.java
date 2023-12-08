package tallerIntCont;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;





public class Lybrary {
	
	public int maxBooksCheckOut = 10;
	public double lateFee = 1.0;
	public int loanPeriod = 14;

	public List<Book> catalog;
	public List<Book> selectedBooks;
	public Scanner scanner;

	public Lybrary() {
		catalog = new ArrayList<>();
		selectedBooks = new ArrayList<>();
		scanner = new Scanner(System.in);
	}
	
	public void displayCatalog() {
		System.out.println("Catalog of Available Books:");
		for (Book book : catalog) {
			System.out.println("Title: " + book.title + " Author: " + book.author + " - Available: " + book.quantity);
		}
	}
	
	public void selectBooks() {
		int totalBooks = 0;
		while (totalBooks < maxBooksCheckOut) {
			displayCatalog();
			System.out.print("Enter the title of the book to checkout (or 'done' to finish): ");
			String title = scanner.nextLine();
			
			if (title.equalsIgnoreCase("done")) {
				break;
			}
			Book selectedBook = findBookByTitle(title); 
			
			//We validate that the book exist in the catalog and that is available
			if (selectedBook != null && selectedBook.available) {
				
				System.out.print("Enter the quantity to checkout: ");
				int quantity = getValidQuantityInput();
				
				if (quantity > 0 && totalBooks + quantity <= maxBooksCheckOut && quantity <= selectedBook.quantity ) {
					selectedBook.quantity = selectedBook.quantity - quantity;
					if (selectedBook.quantity == 0) {
						selectedBook.available = false;
					}
					selectedBooks.add(new Book (selectedBook.title, selectedBook.author, quantity));
					totalBooks += quantity;
					System.out.println(quantity + " copies of " + title + " added to the checkout.");
				} else {
					System.out.println("Invalid quantity. Please reenter quantity.");
	            }
			} else {
				System.out.println("Book not found or not available. Please re-select.");
	        }
		}
	}
	
	//Here we validate that it is a positive integer
	public int getValidQuantityInput() {
		while (true) {
			try {
				int quantity = Integer.parseInt(scanner.nextLine());
				if (quantity > 0) {
					return quantity;
	            } else {
	            	System.out.print("Please enter a positive integer: ");
	            }
			} catch (NumberFormatException e) {
				System.out.print("Invalid input. Please enter a positive integer: ");
			}
		}
	}
	
	//
	public void displayCheckoutSummary() {
		String checkout = "edit";
		while (checkout.equalsIgnoreCase("edit")){
			System.out.println("\nCheckout Summary:");
			for (Book book : selectedBooks) {
				System.out.println(book.quantity + " copies of " + book.title + " - Due Date: " + calculateDueDate());
			}
			System.out.println("Confirm checkout (confirm)or edit checkout(edit) or Cancel (cancel)");
			checkout = scanner.nextLine();
			if (checkout.equalsIgnoreCase("confirm")) {
				System.out.println("Total late fees for selected books: $" + calculateTotalLateFees()+ " per day");
				System.out.print("\nThank you");
			} else if(checkout.equalsIgnoreCase("edit")) {
				System.out.print("\nWhich book would you like to change (title):");
				String title = scanner.nextLine();
				Book selectedBook = findBookByTitleChange(title);
				if (selectedBook != null) {
					System.out.print("\nEnter new amount: ");
					int quantity = getValidQuantityInput();
					for (Book book : selectedBooks) {
						if (book.title.equalsIgnoreCase(title)) {
							book.quantity=quantity;
						}
					}
				}else {
					System.out.print("That book has not been selected");
				}
			} else {
				System.out.print("selection canceled");
			}
		}
	}
	
	//We calculate de date and we give it a 14 days loan period
	private String calculateDueDate() {
		return new Date(System.currentTimeMillis() + loanPeriod * 24 * 60 * 60 * 1000).toString();
	}
	
	public double calculateTotalLateFees() {
		double totalLateFees = 0.0;
		for (Book book : selectedBooks) {
			int daysOverdue = 1;
			totalLateFees += daysOverdue * lateFee * book.quantity;
		}
		return totalLateFees;
	}
	
	public Book findBookByTitle(String title) {
		for (Book book : catalog) {
			if (book.title.equalsIgnoreCase(title)) {
				return book;
			}
		}
		return null;
	}
	
	public Book findBookByTitleChange(String title) {
		for (Book book : selectedBooks) {
			if (book.title.equalsIgnoreCase(title)) {
				return book;
			}
		}
		return null;
	}
	public void returnBooks() {
		System.out.println("Enter the title of the book you want to return:");
		String title = scanner.nextLine();
		System.out.println("Enter the amount of the book you want to return:");
		int numberOfBooks = scanner.nextInt();
		System.out.println("Enter the borrowing date (YYYY-MM-DD) :");
		String date = scanner.nextLine();
		LocalDate borrowingDate = LocalDate.parse(date);
		LocalDate currentDate = LocalDate.now();
		for (Book book : catalog) {
			if (book.title.equalsIgnoreCase(title)) {
				book.quantity=book.quantity + numberOfBooks;
				book.available=true;
				long daysBorrowed = ChronoUnit.DAYS.between(borrowingDate, currentDate);
				long daysOverdue= Math.max(0, daysBorrowed -14);
				double lateFeeCharge=0.0;
				if(daysOverdue >0) {
					lateFeeCharge= daysOverdue * lateFee * numberOfBooks;
					System.out.println("Thank you for returning the books");
					System.out.println("You have a late fee charge of: "+lateFeeCharge);
				}
			}else {
				System.out.println("We don't have that book in our catalog:");
			}
		}
	}
	
	public static void main(String[] args) {
		
		Lybrary library = new Lybrary();
		
		// Add some sample books to the catalog
		library.catalog.add(new Book("The Catcher in the Rye", "J.D. Salinger", 5));
		library.catalog.add(new Book("To Kill a Mockingbird", "Harper Lee", 8));
		library.catalog.add(new Book("1984", "George Orwell", 10));
		
		// Book selection and checkout process
		library.selectBooks();
	    // Display checkout summary
		library.displayCheckoutSummary();
		
	}
	
}
