import java.util.HashMap;
import java.util.Map;

class book {
    String isbn;
    String title;
    int year;
    double price;
    int limited_year;

    public book(String i, String t, int y, double p, int l) {
        isbn = i;
        title = t;
        year = y;
        price = p;
        limited_year = l;
    }

    public String get_isbn() {
        return isbn;
    }

    public String get_title() {
        return title;
    }

    public double get_price() {
        return price;
    }

    public int get_year() {
        return year;
    }

    public void deliver(String email, String address) {
        System.out.println("delivering book to " + email + " at " + (address.isEmpty() ? "your email (e-book)" : address));
    }
}

class paper_book extends book {
    int stock;

    public paper_book(String i, String t, int y, double p, int l, int s) {
        super(i, t, y, p, l);
        stock = s;
    }

    void reduce_stock(int quantity) {
        if (quantity > stock) {
            throw new RuntimeException("the stock is not satisfying your request");
        }
        stock -= quantity;
    }
}

class Ebook extends book {
    String file_type;

    public Ebook(String i, String t, int y, double p, int l, String f) {
        super(i, t, y, p, l);
        file_type = f;
    }
}

class demo_book extends book {
    public demo_book(String i, String t, int y, double p, String author) {
        super(i, t, y, p, y);
    }
}

class inventory {
    Map<String, book> books = new HashMap<>();

    void add_book(book b) {
        books.put(b.get_isbn(), b);
        System.out.println("the book \"" + b.get_title() + "\" is added");
    }

    book remove_outdated(int limited_year, int current_year) {
        for (book b : books.values()) {
            if (current_year - b.get_year() > limited_year) {
                books.remove(b.get_isbn());
                System.out.println("the store removed: " + b.get_title());
                return b;
            }
        }
        return null;
    }

    public double buy_book(String isbn, int quantity, String email, String address) {
        if (!books.containsKey(isbn)) {
            System.out.println("Quantum book store: Book not found");
            return 0;
        }

        book b = books.get(isbn);

        if (b instanceof demo_book) {
            System.out.println("Quantum book store: Demo book is not for sale");
            return 0;
        }

        if (b instanceof paper_book) {
            paper_book pb = (paper_book) b;
            if (quantity > pb.stock) {
                System.out.println("Quantum book store: Not enough stock");
                return 0;
            }
            pb.reduce_stock(quantity);
        }

        b.deliver(email, address);
        double total = b.get_price() * quantity;
        System.out.println("Quantum book store: Paid amount: " + total);
        return total;
    }
}

public class App {
    public static void main(String[] args) {
        inventory store = new inventory();

        book paper = new paper_book("P001", "Java Basics", 2018, 100.0, 5, 10);
        book ebook = new Ebook("E001", "Python Guide", 2020, 50.0, 5, "PDF");
        book demo = new demo_book("D001", "Preview Book", 2010, 0.0, "Author C");

        store.add_book(paper);
        store.add_book(ebook);
        store.add_book(demo);

        store.buy_book("P001", 2, "user@example.com", "123 Street");
        store.buy_book("E001", 1, "user@example.com", "");
        store.remove_outdated(5, 2025);

        store.buy_book("D001", 1, "user@example.com", "");
    }
}