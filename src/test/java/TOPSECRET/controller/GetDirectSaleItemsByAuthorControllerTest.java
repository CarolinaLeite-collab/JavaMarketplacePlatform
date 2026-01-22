package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GetDirectSaleItemsByAuthorControllerTest {

    private User _user;
    private DirectSaleRepo _dsr;
    private Author _author;
    private Publication _publication;
    private Item _item;
    private GetDirectSaleItemsByAuthorController _getDirectSaleItemsByAuthorController;

    @BeforeEach
    void setUp() {

        _user = new User(
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
        _author = new Author("Seneca");
        _getDirectSaleItemsByAuthorController = new GetDirectSaleItemsByAuthorController(_dsr, _user);

    }

    @Test
    void test_a_direct_sale_items_by_author_controller(){

        //act
        new GetDirectSaleItemsByAuthorController(_dsr, _user);

    }

    @Test
    void test_get_direct_sale_items_by_author_with_direct_sales_should_return_not_empty_list(){

        //needs createDirectSale()

    }

    @Test
    void test_get_direct_sale_items_by_author_with_no_direct_sales_should_return_empty_list(){

        //arrange and act
        List<Item> listOfDirectSaleItemsByAuthor = _getDirectSaleItemsByAuthorController.getDirectSaleItemsByAuthor(_author);

        //assert
        assertNotNull(listOfDirectSaleItemsByAuthor);
        assertTrue(listOfDirectSaleItemsByAuthor.isEmpty());

    }

    @Test
    void test_get_direct_sale_items_by_author_with_no_direct_sales_should_return_non_empty_list() {

        //act
        _dsr.createDirectSale(_item, new Price(20.0,Currency.EUR), null);
        _dsr.createDirectSale(_item, new Price(25.0,Currency.EUR), null);

        //arrange
        List<Item> listOfDirectSaleItemsByAuthor = _getDirectSaleItemsByAuthorController.getDirectSaleItemsByAuthor(_author);

        //assert
        assertNotNull(listOfDirectSaleItemsByAuthor);
        assertFalse(listOfDirectSaleItemsByAuthor.isEmpty());


    }



}