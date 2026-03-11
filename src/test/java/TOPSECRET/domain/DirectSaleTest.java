package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Period;
import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class DirectSaleTest {

    private Item _itemDouble;
    private Price _priceDouble;
    private Period _period;

    @BeforeEach
    void setUp() {

        _itemDouble = mock(Item.class);
        _priceDouble = mock(Price.class);
        _period = Period.ofMonths(3);
    }

    @Test
    void createsDirectSaleWithPriceAndTimeLimit() {
        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new PublishingCompany("Penguin"))
                .build();
        Item item = new Item(pub, Condition.GOOD);

        Price price = new Price(10.0, Currency.EUR);
        Period limit = Period.ofMonths(3);

        DirectSale sale = new DirectSale(item, price, limit);

        assertEquals(item, sale.getItem());
        assertEquals(price, sale.getPrice());
        assertEquals(limit, sale.getTimeLimit());
    }

    @Test
    void createsDirectSaleWithoutTimeLimit() {
        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new PublishingCompany("Penguin"))
                .build();
        Item item = new Item(pub, Condition.GOOD);

        Price price = new Price(15.0, Currency.USD);

        DirectSale sale = new DirectSale(item, price, null);

        assertEquals(item, sale.getItem());
        assertEquals(price, sale.getPrice());
        assertNull(sale.getTimeLimit());
    }

    @Test
    void throwsExceptionWhenPriceIsNull() {
        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new PublishingCompany("Penguin"))
                .build();
        Item item = new Item(pub, Condition.GOOD);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new DirectSale(item, null, Period.ofDays(10))
        );

        assertEquals("Price is required for a direct sale", ex.getMessage());
    }

    @Test
    void throwsExceptionWhenItemIsNull() {
        Price price = new Price(10.0, Currency.EUR);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new DirectSale(null, price, Period.ofDays(10))
        );

        assertEquals("Item is required for a direct sale", ex.getMessage());
    }

    @Test
    void throwsExceptionWhenTimeLimitIsNegative() {
        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new PublishingCompany("Penguin"))
                .build();
        Item item = new Item(pub, Condition.GOOD);

        Price price = new Price(10.0, Currency.EUR);
        Period negative = Period.ofMonths(-3);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new DirectSale(item, price, negative)
        );

        assertEquals("Time limit cannot be negative", ex.getMessage());
    }

    // Isolated IsByAuthor Test
    @Test
    void isByAuthorShouldReturnTrueWhenAuthorMatches() {

        //Arrange
        Author _authorDouble = mock(Author.class);
        when(_itemDouble.isByAuthor(_authorDouble)).thenReturn(true);

        // SUT
        DirectSale ds = new DirectSale(_itemDouble,  _priceDouble, _period);

        //Act
        boolean result = ds.isByAuthor(_authorDouble);

        //Assert
        assertTrue(result);

    }

    @Test
    void isByAuthorShouldReturnFalseWhenAuthorIsDifferent() {

        //Arrange
        Author _author2 = mock(Author.class);
        when(_itemDouble.isByAuthor(_author2)).thenReturn(false);

        // SUT
        DirectSale ds = new DirectSale(_itemDouble,  _priceDouble, _period);

        //Act
        boolean result = ds.isByAuthor(_author2);

        //Assert
        assertFalse(result);

    }

    @Test
    void isByAuthorShouldDelegateToItem() {
        //Arrange
        Author _author = mock(Author.class);

        //SUT
        DirectSale ds = new DirectSale(_itemDouble,  _priceDouble, _period);

        //Act
        ds.isByAuthor(_author);

        //Assert
        verify(_itemDouble, times(1)).isByAuthor(_author);
    }

    @Test
    void test_isByPublisher_should_return_true_when_Publisher_matches() {
        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new PublishingCompany("Penguin"))
                .build();
        Item item = new Item(pub, Condition.GOOD);

        Price price = new Price(15.0, Currency.USD);

        DirectSale sale = new DirectSale(item, price, null);

        PublishingCompany publisher = new PublishingCompany("Penguin");

        assertTrue(sale.isByPublisher(publisher));

    }

    @Test
    void test_is_by_publisher_should_return_false_when_publisher_does_not_match() {
        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new PublishingCompany("Penguin"))
                .build();
        Item item = new Item(pub, Condition.GOOD);

        Price price = new Price(15.0, Currency.USD);

        DirectSale sale = new DirectSale(item, price, null);

        PublishingCompany publisher = new PublishingCompany("Porto Editora");

        assertFalse(sale.isByPublisher(publisher));

    }

    @Test
    void testIsByPublicationShouldReturnTrueWhenPublicationBookWithIsbnMatches() {
        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new PublishingCompany("Penguin"))
                .build();
        Item item = new Item(pub, Condition.GOOD);

        Price price = new Price(15.0, Currency.USD);

        DirectSale sale = new DirectSale(item, price, null); // create a directSale of an item

        Publication pub1 = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new PublishingCompany("Penguin"))
                .build();

        assertTrue(sale.isByPublication(pub1));
    }

    @Test
    void testIsByPublicationShouldReturnFalseWhenPublicationBookWithIsbnDoesntMatches() {
        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9781408736401"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new PublishingCompany("Penguin"))
                .build();
        Item item = new Item(pub, Condition.GOOD);

        Price price = new Price(15.0, Currency.USD);

        DirectSale sale = new DirectSale(item, price, null); // create a directSale of an item

        Publication pub1 = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new PublishingCompany("Penguin"))
                .build();

        assertFalse(sale.isByPublication(pub1));
    }

    @Test
    void testIsByPublicationShouldReturnTrueWhenPublicationBookWithoutIsbnMatches() {
        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new NoIdentifier())
                .year(Year.of(1950))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new PublishingCompany("Penguin"))
                .build();
        Item item = new Item(pub, Condition.GOOD);

        Price price = new Price(15.0, Currency.USD);

        DirectSale sale = new DirectSale(item, price, null); // create a directSale of an item

        Publication pub1 = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new NoIdentifier())
                .year(Year.of(1950))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new PublishingCompany("Penguin"))
                .build();

        assertTrue(sale.isByPublication(pub1));
    }

    @Test
    void testIsByPublicationShouldReturnFalseWhenPublicationBookWithoutIsbnDoesntMatches() {
        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new NoIdentifier())
                .year(Year.of(1940))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new PublishingCompany("Penguin"))
                .build();
        Item item = new Item(pub, Condition.GOOD);

        Price price = new Price(15.0, Currency.USD);

        DirectSale sale = new DirectSale(item, price, null); // create a directSale of an item

        Publication pub1 = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new NoIdentifier())
                .year(Year.of(1950))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new PublishingCompany("Penguin"))
                .build();

        assertFalse(sale.isByPublication(pub1));
    }

    @Test
    void testIsByPublicationShouldReturnTrueWhenPublicationMagazineWithIssnMatches() {
        Publication pub = Publication.builder()
                .type(new PublicationType("MAGAZINE"))
                .identifier(new ISSN("1234-5678"))
                .year(Year.of(2022))
                .title(new Title("Science Weekly"))
                .publisher(new PublishingCompany("Nature"))
                .build();
        Item item = new Item(pub, Condition.GOOD);

        Price price = new Price(15.0, Currency.USD);

        DirectSale sale = new DirectSale(item, price, null); // create a directSale of an item

        Publication pub1 = Publication.builder()
                .type(new PublicationType("MAGAZINE"))
                .identifier(new ISSN("1234-5678"))
                .year(Year.of(2022))
                .title(new Title("Science Weekly"))
                .publisher(new PublishingCompany("Nature"))
                .build();

        assertTrue(sale.isByPublication(pub1));
    }

    @Test
    void testIsByPublicationShouldReturnFalseWhenPublicationMagazineWithIssnDoesntMatches() {
        Publication pub = Publication.builder()
                .type(new PublicationType("MAGAZINE"))
                .identifier(new ISSN("2316-9133"))
                .year(Year.of(2022))
                .title(new Title("Science Weekly"))
                .publisher(new PublishingCompany("Nature"))
                .build();
        Item item = new Item(pub, Condition.GOOD);

        Price price = new Price(15.0, Currency.USD);

        DirectSale sale = new DirectSale(item, price, null); // create a directSale of an item

        Publication pub1 = Publication.builder()
                .type(new PublicationType("MAGAZINE"))
                .identifier(new ISSN("1234-5678"))
                .year(Year.of(2022))
                .title(new Title("Science Weekly"))
                .publisher(new PublishingCompany("Nature"))
                .build();

        assertFalse(sale.isByPublication(pub1));
    }

    @Test
    void testIsByPublicationShouldReturnTrueWhenPublicationMagazineWithoutIssnMatches() {
        Publication pub = Publication.builder()
                .type(new PublicationType("MAGAZINE"))
                .identifier(new NoIdentifier())
                .year(Year.of(1950))
                .title(new Title("Science Weekly"))
                .publisher(new PublishingCompany("Nature"))
                .build();
        Item item = new Item(pub, Condition.GOOD);

        Price price = new Price(15.0, Currency.USD);

        DirectSale sale = new DirectSale(item, price, null); // create a directSale of an item

        Publication pub1 = Publication.builder()
                .type(new PublicationType("MAGAZINE"))
                .identifier(new NoIdentifier())
                .year(Year.of(1950))
                .title(new Title("Science Weekly"))
                .publisher(new PublishingCompany("Nature"))
                .build();

        assertTrue(sale.isByPublication(pub1));
    }

    @Test
    void testIsByPublicationShouldReturnFalseWhenPublicationMagazineWithoutIssnDoesntMatches() {
        Publication pub = Publication.builder()
                .type(new PublicationType("MAGAZINE"))
                .identifier(new NoIdentifier())
                .year(Year.of(1930))
                .title(new Title("Science Weekly"))
                .publisher(new PublishingCompany("Nature"))
                .build();
        Item item = new Item(pub, Condition.GOOD);

        Price price = new Price(15.0, Currency.USD);

        DirectSale sale = new DirectSale(item, price, null); // create a directSale of an item

        Publication pub1 = Publication.builder()
                .type(new PublicationType("MAGAZINE"))
                .identifier(new NoIdentifier())
                .year(Year.of(1950))
                .title(new Title("Science Weekly"))
                .publisher(new PublishingCompany("Nature"))
                .build();

        assertFalse(sale.isByPublication(pub1));
    }

    @Test
    void isByPublicationShouldReturnTrueWhenPublicationBookMatchesCaseInsensitive() {
        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new PublishingCompany("Penguin"))
                .build();
        Item item = new Item(pub, Condition.GOOD);

        Price price = new Price(15.0, Currency.USD);

        DirectSale sale = new DirectSale(item, price, null);

        Publication pub1 = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new PublishingCompany("PEngUiN"))
                .build();

        assertTrue(sale.isByPublication(pub1));

    }

    @Test
    void isByPublicationShouldReturnTrueWhenPublicationMagazineMatchesCaseInsensitive() {
        Publication pub = Publication.builder()
                .type(new PublicationType("MAGAZINE"))
                .identifier(new NoIdentifier())
                .year(Year.of(1950))
                .title(new Title("Science Weekly"))
                .publisher(new PublishingCompany("NatURE"))
                .build();
        Item item = new Item(pub, Condition.GOOD);

        Price price = new Price(15.0, Currency.USD);

        DirectSale sale = new DirectSale(item, price, null); // create a directSale of an item

        Publication pub1 = Publication.builder()
                .type(new PublicationType("MAGAZINE"))
                .identifier(new NoIdentifier())
                .year(Year.of(1950))
                .title(new Title("Science Weekly"))
                .publisher(new PublishingCompany("Nature"))
                .build();

        assertTrue(sale.isByPublication(pub1));

    }

    @Test
    void isByPublicationShouldReturnTrueWhenPublicationMatchesCaseInsensitive() {
        Publication pub = Publication.builder()
                .type(new PublicationType("MAGAZINE"))
                .identifier(new NoIdentifier())
                .year(Year.of(1950))
                .title(new Title("Science Weekly"))
                .publisher(new PublishingCompany("NatURE"))
                .build();
        Item item = new Item(pub, Condition.GOOD);

        Price price = new Price(15.0, Currency.USD);

        DirectSale sale = new DirectSale(item, price, null); // create a directSale of an item

        Publication pub1 = Publication.builder()
                .type(new PublicationType("MAGAZINE"))
                .identifier(new NoIdentifier())
                .year(Year.of(1950))
                .title(new Title("Science Weekly"))
                .publisher(new PublishingCompany("Nature"))
                .build();

        assertTrue(sale.isByPublication(pub1));

    }

    @Test
    void isByGenre_should_return_true_when_Genre_matches() {

        //Arrange
        Genre genre = mock(Genre.class);
        when(_itemDouble.isByGenre(genre)).thenReturn(true);

        // SUT
        DirectSale directSale = new DirectSale(_itemDouble,_priceDouble, _period);

        //Act
        boolean result = directSale.isByGenre(genre);

        //Assert
        assertTrue(result);
    }

    @Test
    void isByGenre_should_return_false_when_Genre_is_different() {

        //Arrange
        Genre genre = mock(Genre.class);
        when(_itemDouble.isByGenre(genre)).thenReturn(false);

        // SUT
        DirectSale directSale = new DirectSale(_itemDouble,_priceDouble, _period);

        //Act
        boolean result = directSale.isByGenre(genre);

        //Assert
        assertFalse(result);
    }

    @Test
    void isByGenre_should_delegate_to_Item() {
        //Arrange
        Genre genre = mock(Genre.class);

        //SUT
        DirectSale directSale = new DirectSale(_itemDouble,_priceDouble, _period);

        //Act
        directSale.isByGenre(genre);

        //Assert
        verify(_itemDouble, times(1)).isByGenre(genre);
    }

}