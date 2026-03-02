package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DirectSaleRepoTest {

    private User _buyer;
    private DirectSaleRepo _directSaleRepo;
    private Publication _publication;
    private Publication _publication2;
    private Item _item;
    private Item _item2;
    private Author _author;
    private DirectSale _directSale1;
    private DirectSale _directSale2;
    private Genre _genre;
    private PublishingCompany _publisher;


    @BeforeEach
    void setUp() {

        _buyer = new User(
                new Name("Zé Isep"),
                new Email("ze@isep.pt")
        );

        _author = new Author("Seneca");
        _genre = new Genre("History");
        _publisher = new PublishingCompany("Penguin");

        _publication = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new PublishingCompany("Penguin"))
                .genre(_genre)
                .build();

        _publication2 = Publication.builder()
                .type(new PublicationType("MAGAZINE"))
                .identifier(new ISSN("2316-9133"))
                .year(Year.of(2022))
                .title(new Title("Science Weekly"))
                .publisher(new PublishingCompany("Nature"))
                .build();

        _item = new Item(_publication, Condition.GOOD);
        _item2 = new Item(_publication2, Condition.GOOD);

        _directSale1 = new DirectSale(_item, new Price(20.0, Currency.EUR), null);
        _directSale2 = new DirectSale(_item, new Price(25.0, Currency.EUR), null);

        _directSaleRepo = new DirectSaleRepo();

    }

    @Test
    void testADirectSaleRepoConstructor() {

        new DirectSaleRepo();

    }

    @Test
    void testAddDirectSale() {

        DirectSale ds = _directSaleRepo.createDirectSale(_item, new Price(20.0, Currency.EUR), null);

        assertNotNull(ds);

    }

    @Test
    void testGetDirectSaleItemsByAuthorNoDirectSalesShouldReturnEmptyList() {

        //act
        List<Item> emptyList = _directSaleRepo.getDirectSaleItemsByAuthor(_author);

        //assert
        assertNotNull(emptyList);
        assertTrue(emptyList.isEmpty());

    }

    @Test
    void testGetDirectSaleItemsByPublicationBookNoDirectSalesShouldReturnEmptyList() {

        List<Item> emptyList = _directSaleRepo.getDirectSaleItemsByPublication(_publication);

        assertNotNull(emptyList);
        assertTrue(emptyList.isEmpty());

    }

    @Test
    void testGetDirectSaleItemsByPublicationMagazineNoDirectSalesShouldReturnEmptyList() {

        List<Item> emptyList = _directSaleRepo.getDirectSaleItemsByPublication(_publication2);

        assertNotNull(emptyList);
        assertTrue(emptyList.isEmpty());

    }

    @Test
    void testGetDirectSaleItemsByPublicationBookWithDirectSalesShouldReturnNonEmptyList() {

        _directSaleRepo.createDirectSale(_item, new Price(20.0, Currency.EUR), null);
        _directSaleRepo.createDirectSale(_item, new Price(25.0, Currency.EUR), null);

        //act
        List<Item> list = _directSaleRepo.getDirectSaleItemsByPublication(_publication);

        //assert
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }

    @Test
    void testGetDirectSaleItemsByPublicationMagazineWithDirectSalesShouldReturnNonEmptyList() {

        _directSaleRepo.createDirectSale(_item2, new Price(20.0, Currency.EUR), null);
        _directSaleRepo.createDirectSale(_item2, new Price(25.0, Currency.EUR), null);

        List<Item> list = _directSaleRepo.getDirectSaleItemsByPublication(_publication2);

        assertNotNull(list);
        assertFalse(list.isEmpty());
    }

    @Test
    void testGetDirectSaleItemsByAuthorWithDirectSalesShouldReturnNonEmptyList() {

        //arrange
        _directSaleRepo.createDirectSale(_item, new Price(20.0, Currency.EUR), null);
        _directSaleRepo.createDirectSale(_item, new Price(25.0, Currency.EUR), null);

        //act
        List<Item> list = _directSaleRepo.getDirectSaleItemsByAuthor(_author);

        //assert
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }

    @Test
    void testGetDirectSaleItemsByGenreWithDirectSalesShouldReturnNonEmptyList() {


        _directSaleRepo.createDirectSale(_item, new Price(20.0, Currency.EUR), null);
        _directSaleRepo.createDirectSale(_item, new Price(25.0, Currency.EUR), null);
        _directSaleRepo.createDirectSale(_item, new Price(30.0, Currency.EUR), null);

        List<Item> list = _directSaleRepo.getDirectSaleItemsByGenre(_genre);

        assertNotNull(list);
        assertEquals(3, list.size());
    }

    @Test
    void testGetDirectSaleItemsByGenreWithNoDirectSalesShouldReturnEmptyList() {

        //act
        List<Item> emptyList = _directSaleRepo.getDirectSaleItemsByGenre(_genre);

        //assert
        assertNotNull(emptyList);
        assertTrue(emptyList.isEmpty());

    }

    @Test void createDirectSale_CreateAndStoreDirectSale() {

        // act
        DirectSale created = _directSaleRepo.createDirectSale(
                _item,
                new Price(20.0, Currency.EUR),
                null
        );

        // assert: returned object is correct
        assertNotNull(created);
        assertEquals(_item, created.getItem());
        assertEquals(20.0, created.getPrice().getValue());
        assertEquals(Currency.EUR, created.getPrice().getCurrency());

        // assert: repo actually stored it
        List<Item> itemsByAuthor = _directSaleRepo.getDirectSaleItemsByAuthor(_author);

        assertEquals(1, itemsByAuthor.size());
        assertEquals(_item, itemsByAuthor.get(0));
    }

    @Test
    void testGetDirectSaleItemsByPublisherWithDirectSalesShouldReturnNonEmptyList() {

        //Arrange
        _directSaleRepo.createDirectSale(_item, new Price(10.0, Currency.EUR), null);
        _directSaleRepo.createDirectSale(_item, new Price(15.0, Currency.EUR), null);
        _directSaleRepo.createDirectSale(_item, new Price(25.0, Currency.EUR), null);
        _directSaleRepo.createDirectSale(_item, new Price(5.0, Currency.EUR), null);

        //Act
        List<Item> list = _directSaleRepo.getDirectSaleItemByPublisher(_publisher);

        //Assert
        assertNotNull(list);
        assertEquals(4, list.size());
    }

    @Test
    void shouldReturnEmptyListWhenNoDirectSaleItemsForPublisher() {

        //act
        List<Item> emptyList = _directSaleRepo.getDirectSaleItemByPublisher(_publisher);

        //assert
        assertNotNull(emptyList);
        assertTrue(emptyList.isEmpty());

    }

}