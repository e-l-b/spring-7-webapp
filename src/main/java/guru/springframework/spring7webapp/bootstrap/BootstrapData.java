package guru.springframework.spring7webapp.bootstrap;

import guru.springframework.spring7webapp.domain.Author;
import guru.springframework.spring7webapp.domain.Book;
import guru.springframework.spring7webapp.domain.Publisher;
import guru.springframework.spring7webapp.repositories.AuthorRepository;
import guru.springframework.spring7webapp.repositories.BookRepository;
import guru.springframework.spring7webapp.repositories.PublisherRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// Annotates this class as a Spring-managed component, making it a Spring Bean.
// It will automatically be detected and registered in the application context.
@Component
public class BootstrapData implements CommandLineRunner {

    // Declares final fields for dependencies to ensure immutability.
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;

    // Constructor-based dependency injection for the repositories.
    // These repositories are automatically injected by Spring, as they are also Spring-managed components.
    public BootstrapData(AuthorRepository authorRepository, BookRepository bookRepository, PublisherRepository publisherRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
    }

    // The run method is executed automatically at application startup because this class implements
    // the CommandLineRunner interface. It serves as a way to execute initialization logic.
    @Override
    public void run(String... args) throws Exception {

        // Step 1: Create the first Author object and set its attributes.
        Author eric = new Author();
        eric.setFirstname ("Eric");
        eric.setLastname("Evans");

        // Step 2: Create the first Book object and set its attributes.
        Book ddd = new Book();
        ddd.setTitle("Domain Driven Design");
        ddd.setIsbn("123456");

        // Step 3: Save the Author and Book objects to the database using their respective repositories.
        // The save method returns the saved entity, which now includes its generated ID.
        Author ericSaved = authorRepository.save(eric);
        Book dddSaved = bookRepository.save(ddd);

        // Step 4: Create the second Author object and set its attributes.
        Author rod = new Author();
        rod.setFirstname("Rod");
        rod.setLastname("Johnson");

        // Step 5: Create the second Book object and set its attributes.
        Book noEJB = new Book();
        noEJB.setTitle("J2EE Development without EJB");
        noEJB.setIsbn("54757585");

        // Step 6: Save the second Author and Book objects to the database.
        Author rodSaved = authorRepository.save(rod);
        Book noEJBSaved = bookRepository.save(noEJB);

        // Step 7: Establish relationships between authors and books.
        // Each author is assigned a book by adding the book to their collection of books.
        ericSaved.getBooks().add(dddSaved);
        rodSaved.getBooks().add(noEJBSaved);
        dddSaved.getAuthors().add(ericSaved);
        noEJBSaved.getAuthors().add(rodSaved);

        Publisher penguin = new Publisher();
        penguin.setPublisherName("Penguin");
        penguin.setAdress("Main Street 5");
        penguin.setCity("North Pole");
        penguin.setState("Fishing");
        penguin.setZip("176F");

        Publisher penguinSaved = publisherRepository.save(penguin);

        dddSaved.setPublisher(penguinSaved);
        noEJBSaved.setPublisher(penguinSaved);

        // Step 8: Persist the updated Author entities to save the relationships in the database.
        // Without this, the changes to the `books` collection would not be saved.
        authorRepository.save(ericSaved);
        authorRepository.save(rodSaved);
        bookRepository.save(dddSaved);
        bookRepository.save(noEJBSaved);

        // Step 9: Print a message to the console to indicate the bootstrap process is complete.
        System.out.println("In Bootstrap");
        System.out.println("Author Count: " + authorRepository.count());
        System.out.println("Book Count: " + bookRepository.count());
        System.out.println("Publisher Count: " + publisherRepository.count());
    }
}
