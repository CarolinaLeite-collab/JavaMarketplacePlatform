package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GetItemsOnDirectSaleOfAGivenPublishingCompanyControllerTest {

    private User _buyer;
    private DirectSaleRepo _dsr;
    private Author _author;
    private PublishingCompany _publisher;
    private Publication _publication;
    private Item _item;
    private GetItemsOnDirectSaleOfAGivenPublishingCompanyController _getItemsOnDirectSaleOfAGivenPublishingCompanyController;

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
                .publisher(new PublishingCompany("Penguin"))
                .build();

        _item = new Item(_publication, Condition.GOOD);

        _dsr = new DirectSaleRepo();
        _publisher = new PublishingCompany("Penguin");
        _getItemsOnDirectSaleOfAGivenPublishingCompanyController = new GetItemsOnDirectSaleOfAGivenPublishingCompanyController(_dsr, _buyer);

    }

    @Test
    void test_a_direct_sale_items_by_publisher_controller(){

        //act
        new GetItemsOnDirectSaleOfAGivenPublishingCompanyController(_dsr, _buyer);

    }

    @Test
    void test_get_direct_sale_items_by_publisher_with_no_direct_sales_should_return_empty_list(){

        //act
        List<Item> listOfDirectSaleItemsByPublisher = _getItemsOnDirectSaleOfAGivenPublishingCompanyController.getDirectSaleItemByPublisher(_publisher);

        //assert
        assertNotNull(listOfDirectSaleItemsByPublisher);
        assertTrue(listOfDirectSaleItemsByPublisher.isEmpty());

    }

    @Test
    void test_get_direct_sale_items_by_publisher_with_direct_sales_should_return_non_empty_list() throws InstantiationException {

        //arrange
        _dsr.createDirectSale(_item, new Price(25.0,Currency.EUR), null);
        _dsr.createDirectSale(_item, new Price(35.0,Currency.EUR), null);

        //act
        List<Item> listOfDirectSaleItemsByPublisher = _getItemsOnDirectSaleOfAGivenPublishingCompanyController.getDirectSaleItemByPublisher(_publisher);

        //assert
        assertNotNull(listOfDirectSaleItemsByPublisher);
        assertFalse(listOfDirectSaleItemsByPublisher.isEmpty());


    }
}