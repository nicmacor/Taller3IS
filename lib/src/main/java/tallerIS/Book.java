package tallerIS;

public class Book {
    String title;
    String author;
    int quantity;
    boolean available;

    public Book(String title, String author, int quantity) {
        this.title = title;
        this.author = author;
        this.quantity = quantity;
        this.available = true;
    }
}