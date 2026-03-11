package TOPSECRET.domain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Period;
import java.time.Year;
import java.time.ZonedDateTime;

class ItemTest {

    private ZonedDateTime auctionStartDate = ZonedDateTime.now().plusDays(1);
    private ZonedDateTime auctionEndDate = ZonedDateTime.now().plusDays(2);
    private Publication _publicationDouble;
    private Condition _conditionDouble;


    @BeforeEach
    void setUp() {

        _publicationDouble = mock(Publication.class);
        _conditionDouble = mock(Condition.class);

    }

    @Test
    void itemIsCreatedWithPublicationAndCondition() {
        Publication publication = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new PublishingCompany("Penguin"))
                .build();
        Item item = new Item(publication, Condition.GOOD);

        assertEquals(Condition.GOOD, item.getCondition());
    }

    @Test
    void canSetDirectSaleWhenNoAuctionExists() {
        Publication publication = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new PublishingCompany("Penguin"))
                .build();
        Item item = new Item(publication, Condition.LIKE_NEW);

        DirectSale directSale =
                new DirectSale(item, new Price(10.0, Currency.EUR), Period.ofMonths(3));

        assertDoesNotThrow(() -> item.setDirectSale(directSale));
    }

    @Test
    void canSetAuctionWhenNoDirectSaleExists() {
        Publication publication = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new PublishingCompany("Penguin"))
                .build();
        Item item = new Item(publication, Condition.FAIR);

        AuctionRepo repo = new AuctionRepo();
        Auction auction = repo.createAuction(
                item,
                new Price(5.0, Currency.EUR),
                auctionStartDate,
                auctionEndDate
        );

        assertDoesNotThrow(() -> item.setAuction(auction));
    }

    @Test
    void cannotSetDirectSaleIfAuctionAlreadyExists() {
        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new PublishingCompany("Penguin"))
                .build();
        Item item = new Item(pub, Condition.GOOD);

        AuctionRepo repo = new AuctionRepo();
        Auction auction = repo.createAuction(
                item,
                new Price(5.0, Currency.EUR),
                auctionStartDate,
                auctionEndDate
        );
        item.setAuction(auction);

        DirectSale directSale =
                new DirectSale(item, new Price(10.0, Currency.EUR), Period.ofMonths(3));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> item.setDirectSale(directSale)
        );

        assertEquals("Item is already in an auction.", exception.getMessage());
    }

    @Test
    void cannotSetAuctionIfDirectSaleAlreadyExists() {
        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new PublishingCompany("Penguin"))
                .build();
        Item item = new Item(pub, Condition.GOOD);

        DirectSale ds =
                new DirectSale(item, new Price(10.0, Currency.EUR), Period.ofMonths(3));
        item.setDirectSale(ds);

        AuctionRepo repo = new AuctionRepo();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> repo.createAuction(
                        item,
                        new Price(5.0, Currency.EUR),
                        auctionStartDate,
                        auctionEndDate
                )
        );

        assertTrue(exception.getMessage().contains("Item is already in a direct sale."));
    }

    @Test
    void settingDirectSaleDoesNotOverwriteCondition() {
        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new PublishingCompany("Penguin"))
                .build();
        Item item = new Item(pub, Condition.POOR);

        DirectSale ds =
                new DirectSale(item, new Price(10.0, Currency.EUR), Period.ofMonths(3));
        item.setDirectSale(ds);

        assertEquals(Condition.POOR, item.getCondition());
    }

    @Test
    void settingAuctionDoesNotOverwriteCondition() {
        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new PublishingCompany("Penguin"))
                .build();
        Item item = new Item(pub, Condition.LIKE_NEW);

        AuctionRepo repo = new AuctionRepo();
        Auction auction = repo.createAuction(
                item,
                new Price(5.0, Currency.EUR),
                auctionStartDate,
                auctionEndDate
        );
        item.setAuction(auction);

        assertEquals(Condition.LIKE_NEW, item.getCondition());
    }

    @Test
    void puttingPublicationOnAuctionWrongAuctionItem(){

        Publication testPub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780141036144"))
                .year(Year.of(2012))
                .title(new Title("1984"))
                .author(new Author("George Orwell"))
                .publisher(new PublishingCompany("Penguin"))
                .build();

        Item item = new Item(testPub, Condition.GOOD);
        Auction wrongAuctionItem = new Auction(new Item(testPub, Condition.POOR), new Price(10, Currency.EUR), ZonedDateTime.now().plusDays(1), ZonedDateTime.now().plusDays(8));

        assertThrows(IllegalArgumentException.class, () -> item.setAuction(wrongAuctionItem),
                "This Auction does not belong to this Item.");

    }

    @Test
    void puttingPublicationOnDirectSaleWrongDirectSaleItem(){

        Publication testPub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780141036144"))
                .year(Year.of(2012))
                .title(new Title("1984"))
                .author(new Author("George Orwell"))
                .publisher(new PublishingCompany("Penguin"))
                .build();

        Item item = new Item(testPub, Condition.GOOD);
        DirectSale wrongDirectSaleItem = new DirectSale(new Item(testPub, Condition.POOR), new Price(10.0, Currency.EUR), Period.ofMonths(3));

        assertThrows(IllegalArgumentException.class, () -> item.setDirectSale(wrongDirectSaleItem),
                "This DirectSale does not belong to this Item.");

    }

    @Test
    void testGetDirectSale_WhenDirectSaleIsSet() {
        // Arrange
        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780141036144"))
                .year(Year.of(2012))
                .title(new Title("1984"))
                .author(new Author("George Orwell"))
                .publisher(new PublishingCompany("Penguin"))
                .build();

        Item item = new Item(pub, Condition.GOOD);
        DirectSale sale = new DirectSale(item, new Price(10, Currency.EUR), Period.ofDays(30));

        // Act
        item.setDirectSale(sale);

        // Assert
        assertEquals(sale, item.getDirectSale(),
                "Getter must return the DirectSale previously assigned");
    }

    @Test
    void testGetDirectSale_WhenNoDirectSaleWasAssigned() {
        // Arrange
        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780141036144"))
                .year(Year.of(2012))
                .title(new Title("1984"))
                .author(new Author("George Orwell"))
                .publisher(new PublishingCompany("Penguin"))
                .build();

        Item item = new Item(pub, Condition.GOOD);

        // Act + Assert
        assertNull(item.getDirectSale(),
                "Getter must return null when no DirectSale is assigned");
    }

    // Isolated test of isByAuthor method
    @Test
    void isByAuthorShouldReturnTrueWhenAuthorMatches() {

        //Arrange
        Author _author = mock(Author.class);
        when(_publicationDouble.isByAuthor(_author)).thenReturn(true);

        // SUT
        Item item = new Item(_publicationDouble, _conditionDouble);

        //Act
        boolean result = item.isByAuthor(_author);

        //Assert
        assertTrue(result);

    }

    @Test
    void isByAuthorShouldReturnFalseWhenAuthorIsDifferent() {

        //Arrange
        Author _author2 = mock(Author.class);
        when(_publicationDouble.isByAuthor(_author2)).thenReturn(false);

        //SUT
        Item item = new Item(_publicationDouble, _conditionDouble);

        //Act
        boolean result = item.isByAuthor(_author2);

        //Assert
        assertFalse(result);

    }

    @Test
    void isByAuthorShouldDelegateToPublication() {
        //Arrange
        Author _author = mock(Author.class);

        //SUT
        Item item = new Item(_publicationDouble, _conditionDouble);

        //Act
        item.isByAuthor(_author);

        //Assert
        verify(_publicationDouble, times(1)).isByAuthor(_author);
    }

    // Isolated test of isByGenre method

    @Test
    void isByGenreShouldReturnTrueWhenGenreMatches() {

        //Arrange
        Genre _genre = mock(Genre.class);
        when(_publicationDouble.isByGenre(_genre)).thenReturn(true);

        // SUT
        Item item = new Item(_publicationDouble, _conditionDouble);

        //Act
        boolean result = item.isByGenre(_genre);

        //Assert
        assertTrue(result);

    }

    @Test
    void isByGenreShouldReturnFalseWhenGenreIsDifferent() {

        //Arrange
        Genre _genre2 = mock(Genre.class);
        when(_publicationDouble.isByGenre(_genre2)).thenReturn(false);

        //SUT
        Item item = new Item(_publicationDouble, _conditionDouble);

        //Act
        boolean result = item.isByGenre(_genre2);

        //Assert
        assertFalse(result);
    }

    @Test
    void isByGenreShouldDelegateToPublication() {
        //Arrange
        Genre _genre = mock(Genre.class);

        //SUT
        Item item = new Item(_publicationDouble, _conditionDouble);

        //Act
        item.isByGenre(_genre);

        //Assert
        verify(_publicationDouble, times(1)).isByGenre(_genre);
    }

    // Isolated test of isByPublication method

    @Test
    void isByPublicationShouldReturnTrueWhenPublicationMatches() {

        Item item = new Item(_publicationDouble, _conditionDouble);

        boolean result = item.isByPublication(_publicationDouble);

        assertTrue(result);


    }

    @Test
    void isByPublicationShouldReturnFalseWhenPublicationIsDifferent() {

        Publication _publicationDouble2 = mock(Publication.class);

        Item item = new Item(_publicationDouble, _conditionDouble);

        boolean result = item.isByPublication(_publicationDouble2);

        assertFalse(result);
    }


}