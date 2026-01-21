package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GetDirectSaleItemsByAuthorControllerTest {

    private User _user;
    private DirectSaleRepo _dsr;
    private Author _author;
    private GetDirectSaleItemsByAuthorController _getDirectSaleItemsByAuthorController;

    @BeforeEach
    void setUp() {

        _user = new User(
                new Name("Zé Isep"),
                new Email("test@isep.pt"));
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



}