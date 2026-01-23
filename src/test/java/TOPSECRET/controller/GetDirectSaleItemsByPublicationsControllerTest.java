package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GetDirectSaleItemsByPublicationsControllerTest {
    private User _buyer;
    private DirectSaleRepo _dsr;
    private Publication _publication;
    private Publication _publication2;
    private GetDirectSaleItemsByPublicationsController _controller;

    @BeforeEach
    void setUp(){
            _buyer = new User(
                new Name("Maria Francisca"),
                new Email("test@gmail.com"));

        _publication = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new Publisher("Penguin"))
                .build();

        _publication2 = Publication.builder()
                .type(new PublicationType("MAGAZINE"))
                .identifier(new ISSN("2316-9133"))
                .year(Year.of(2022))
                .title(new Title("Science Weekly"))
                .publisher(new Publisher("Nature"))
                .build();

        _dsr = new DirectSaleRepo();

        _controller = new GetDirectSaleItemsByPublicationsController(_dsr, _buyer);

    }

    @Test
    void testDirectSaleItemsByPublicationController(){

        new GetDirectSaleItemsByPublicationsController(_dsr, _buyer);

    }

    @Test
    void testShouldReturnDirectSaleItemsByPublication() {
        Item item = new  Item(_publication, Condition.GOOD);

        _dsr.createDirectSale(item, new Price(20.0, Currency.EUR), null);

        List<Item> result = _controller.getDirectSaleItemsByPublication(_publication);

        assertFalse(result.isEmpty());
        assertEquals(item, result.get(0));  // get the first value from the list that is equal to item
    }

    @Test
    void testGetDirectSaleItemsByPublicationsBookWithNoDirectSalesShouldReturnEmptyList() {

        List<Item> listOfDirectSaleItemsByPublication = _controller.getDirectSaleItemsByPublication(_publication);

        assertNotNull(listOfDirectSaleItemsByPublication);
        assertTrue(listOfDirectSaleItemsByPublication.isEmpty());
    }

    @Test
    void testGetDirectSaleItemsByPublicationsMagazineWithNoDirectSalesShouldReturnEmptyList() {

        List<Item> listOfDirectSaleItemsByPublication = _controller.getDirectSaleItemsByPublication(_publication2);

        assertNotNull(listOfDirectSaleItemsByPublication);
        assertTrue(listOfDirectSaleItemsByPublication.isEmpty());
    }

}
