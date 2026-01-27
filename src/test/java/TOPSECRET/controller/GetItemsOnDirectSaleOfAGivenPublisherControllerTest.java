package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GetItemsOnDirectSaleOfAGivenPublisherControllerTest {

    private User _buyer;
    private DirectSaleRepo _dsr;
    private Author _author;
    private Publisher _publisher;
    private Publication _publication;
    private Item _item;
    private GetItemsOnDirectSaleOfAGivenPublisherController _getItemsOnDirectSaleOfAGivenPublisherController;

    @BeforeEach
    void setUp() {

        _buyer = new User(
                new Name("Zé Isep"),
                new Email("test@isep.pt"));

        _author = new Author("Seneca");

        _publication = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new Publisher("Penguin"))
                .build();

        _item = new Item(_publication, Condition.GOOD);

        _dsr = new DirectSaleRepo();
        _publisher = new Publisher("Penguin");
        _getItemsOnDirectSaleOfAGivenPublisherController = new GetItemsOnDirectSaleOfAGivenPublisherController(_dsr, _buyer);

    }

    @Test
    void test_a_direct_sale_items_by_publisher_controller(){

        //act
        new GetItemsOnDirectSaleOfAGivenPublisherController(_dsr, _buyer);

    }

    @Test
    void test_get_direct_sale_items_by_publisher_with_no_direct_sales_should_return_empty_list(){

        //act
        List<Item> listOfDirectSaleItemsByPublisher = _getItemsOnDirectSaleOfAGivenPublisherController.getDirectSaleItemByPublisher(_publisher);

        //assert
        assertNotNull(listOfDirectSaleItemsByPublisher);
        assertTrue(listOfDirectSaleItemsByPublisher.isEmpty());

    }

    @Test
    void test_get_direct_sale_items_by_publisher_with_direct_sales_should_return_non_empty_list() {

        //arrange
        _dsr.createDirectSale(_item, new Price(25.0,Currency.EUR), null);
        _dsr.createDirectSale(_item, new Price(35.0,Currency.EUR), null);

        //act
        List<Item> listOfDirectSaleItemsByPublisher = _getItemsOnDirectSaleOfAGivenPublisherController.getDirectSaleItemByPublisher(_publisher);

        //assert
        assertNotNull(listOfDirectSaleItemsByPublisher);
        assertFalse(listOfDirectSaleItemsByPublisher.isEmpty());


    }
}