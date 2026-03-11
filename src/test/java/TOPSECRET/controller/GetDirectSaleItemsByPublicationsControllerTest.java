package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetDirectSaleItemsByPublicationsControllerTest {

    private User _buyerDouble;
    private DirectSaleRepo _dsrDouble;
    private Publication _publicationDouble;
    private GetDirectSaleItemsByPublicationsController _controller;

    @BeforeEach
    void setUp(){
            _buyerDouble = mock(User.class);
            _dsrDouble = mock(DirectSaleRepo.class);
            _publicationDouble = mock(Publication.class);

    }

    @Test
    void testDirectSaleItemsByPublicationControllerConstructor(){

        _controller = new GetDirectSaleItemsByPublicationsController(_dsrDouble, _buyerDouble);

    }

    @Test
    void getDirectSaleItemsByPublicationShouldReturnEmptyListWhenThereAreNoItems(){

        _controller = new GetDirectSaleItemsByPublicationsController(_dsrDouble, _buyerDouble);

        when(_dsrDouble.getDirectSaleItemsByPublication(_publicationDouble)).thenReturn(List.of());

        List<Item> resultList  = _controller.getDirectSaleItemsByPublication(_publicationDouble);

        assertNotNull(resultList);
        assertTrue(resultList.isEmpty());

    }

    @Test
    void getDirectSaleItemsByPublicationShouldReturnList() {

        _controller = new GetDirectSaleItemsByPublicationsController(_dsrDouble, _buyerDouble);

        Item itemDouble = mock(Item.class);
        Item itemDouble2 = mock(Item.class);

        when(_dsrDouble.getDirectSaleItemsByPublication(_publicationDouble)).thenReturn(List.of(itemDouble, itemDouble2));

        List<Item> resultList = _controller.getDirectSaleItemsByPublication(_publicationDouble);

        assertFalse(resultList.isEmpty());
        assertEquals(2, resultList.size());
        assertEquals(itemDouble, resultList.get(0));
        assertEquals(itemDouble2, resultList.get(1));
        assertTrue(resultList.containsAll(List.of(itemDouble, itemDouble2)));
    }

    @Test
    void getDirectSaleItemsByPublicationShouldCallDirectSaleRepo() {

        _controller = new GetDirectSaleItemsByPublicationsController(_dsrDouble, _buyerDouble);

        List<Item> listOfDirectSaleItemsByPublication = _controller.getDirectSaleItemsByPublication(_publicationDouble);

        verify(_dsrDouble,  times(1)).getDirectSaleItemsByPublication(_publicationDouble);
    }

}
