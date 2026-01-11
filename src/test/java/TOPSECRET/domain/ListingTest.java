package TOPSECRET.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListingTest {

    //Reusable instances
    PublicationInfo publicationInfoBook = new PublicationInfo(
            new Title ("Louis I. Kahn: The Idea of Orde"),
            Genre.ACTION,
            new Author ("Klaus-Peter Gast"),
            new Edition(
                    new ISBN(9789720048758L),
                    198,
                    3,
                    LocalDate.of(2001, 4, 23),
                    Binding.SADDLE_STITCH,
                    new Description("Sinopse:A comprehensive study of Louis I. Kahn's architecture"),
                    new Dimension(21, 29.7, 1, DimensionUnit.CENTIMETERS),
                    new Weight(224.7, Weight.WeightUnit.GRAMS),
                    Language.of("en", "English", "English")),
            new Publisher("Birkhäuser"));

    PublicationInfo publicationInfoMagazine = new PublicationInfo(
            new Title ("title"),
            Genre.ACTION,
            new Author ("Eça de Queirós"),
            new Edition(
                    new ISSN("1018-4783"),
                    30,
                    3,
                    LocalDate.of(2001, 4, 23),
                    Binding.SADDLE_STITCH,
                    new Description("Amazing Magazine"),
                    new Dimension(21, 29.7, 1, DimensionUnit.CENTIMETERS),
                    new Weight(224.7, Weight.WeightUnit.GRAMS),
                    Language.of("pt", "Portuguese", "Português")),
            new Publisher("My Publisher")
    );

    Name name = new Name("Marcelo Rocha");

    Address adress = new Address ("Rua Dr.Amilcar de Castro", "24", Address.BuildingType.HOUSE, "Barcelos", "Braga", Address.Country.PORTUGAL, "4775-105", null );

    Phone phone = new Phone(new PhonePrefix("+351"), " 962064343 ");

    Email email = new Email ("1251995@isep.ipp.pt");


    @Test
    void shouldCreateListingWithBook() {

        //Arrange
        Book book = new Book(publicationInfoBook, Condition.LIKE_NEW);
        Price priceBook = new Price(150, Currency.EUR);
        User seller = new User(name,adress,email,phone);
        SKU sku = SKU.generate();
        Description description = new Description("Sinopse");
        LocalDate date = LocalDate.now();
        List<String> urls = new ArrayList<>();

        //Act
        Listing listing = new Listing(book, priceBook, seller, sku, description, date, urls);

        //Assert
        assertEquals(book, listing.getBook());
        assertEquals(priceBook, listing.getPrice());
        assertEquals(seller, listing.getSeller());
        assertEquals(sku, listing.getSku());
        assertEquals(description, listing.getDescription());
        assertEquals(date, listing.getCreatedDate());
        assertNull(listing.getMagazine(), "Magazine should be null when a book is used");
    }

    @Test
    void shouldCreateListingWithMagazine() {
        // Arrange
        Magazine magazine = new Magazine(publicationInfoMagazine, Condition.FAIR);
        Price priceMagazine = new Price(70, Currency.EUR);
        User seller = new User(name,adress,email,phone);
        SKU sku = SKU.generate();
        Description description = new Description("Sinopse");
        LocalDate date = LocalDate.now();
        List<String> urls = new ArrayList<>();

        // Act
        Listing listing = new Listing(magazine, priceMagazine, seller, sku, description, date, urls);

        // Assert
        assertNotNull(listing);
        assertEquals(magazine, listing.getMagazine());
        assertEquals(priceMagazine, listing.getPrice());
        assertEquals(seller, listing.getSeller());
        assertEquals(sku, listing.getSku());
        assertEquals(description, listing.getDescription());
        assertEquals(date, listing.getCreatedDate());
        assertNull(listing.getBook(), "Book should be null when a magazine is used");

    }

    @Test
    void shouldThrowExceptionWhenBookIsNull() {
        // Arrange
        Price priceBook = new Price(150, Currency.EUR);
        User seller = new User(name,adress,email,phone);
        SKU sku = SKU.generate();
        Description description = new Description("Sinopse");
        LocalDate date = LocalDate.now();
        List<String> urls = new ArrayList<>();

        // Act
        Executable action = () ->
                new Listing((Book) null, priceBook, seller, sku, description, date, urls);

        // Assert
        assertThrows(IllegalArgumentException.class, action);
    }

    @Test
    void shouldThrowExceptionWhenMagazineIsNull() {
        // Arrange
        Price priceMagazine = new Price(70, Currency.EUR);
        User seller = new User(name,adress,email,phone);
        SKU sku = SKU.generate();
        Description description = new Description("Sinopse");
        LocalDate date = LocalDate.now();
        List<String> urls = new ArrayList<>();

        // Act
        Executable action = () ->
                new Listing((Magazine) null, priceMagazine, seller, sku, description, date, urls);

        // Assert
        assertThrows(IllegalArgumentException.class, action);
    }

    @Test
    void shouldThrowExceptionWhenUrlsIsNull() {
        // Arrange
        Book book = new Book(publicationInfoBook, Condition.LIKE_NEW);
        Price price = new Price(70, Currency.EUR);
        User seller = new User(name, adress, email, phone);
        SKU sku = SKU.generate();
        Description description = new Description("Sinopse");
        LocalDate date = LocalDate.now();

        // Act
        Executable action = () ->
                new Listing(book, price, seller, sku, description, date, null);

        // Assert
        assertThrows(IllegalArgumentException.class, action);
    }

    @Test
    void shouldReturnCopyOfUrls() {
        // Arrange
        Book book = new Book(publicationInfoBook, Condition.LIKE_NEW);
        Price price = new Price(70, Currency.EUR);
        User seller = new User(name, adress, email, phone);
        SKU sku = SKU.generate();
        Description description = new Description("Sinopse");
        LocalDate date = LocalDate.now();

        List<String> urls = new ArrayList<>();
        urls.add("http://image1.com");
        urls.add("http://image2.com");

        Listing listing = new Listing(book, price, seller, sku, description, date, urls);

        // Act
        List<String> result = listing.getUrls();

        // Assert
        assertEquals(2, result.size());
        assertEquals("http://image1.com", result.get(0));
        assertThrows(UnsupportedOperationException.class,
                () -> result.add("http://image3.com"));
    }

}