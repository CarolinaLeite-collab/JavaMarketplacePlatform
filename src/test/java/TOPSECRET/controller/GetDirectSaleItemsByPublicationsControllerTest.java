package TOPSECRET.controller;

import TOPSECRET.domain.IDirectSaleRepo;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.Publication;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetDirectSaleItemsByPublicationsControllerTest {

    private UserId _buyerIdDouble;
    private IDirectSaleRepo _iDirectSaleRepoDouble;
    private Publication _publicationDouble;

    @BeforeEach
    void setUp(){
            _buyerIdDouble = mock(UserId.class);
            _iDirectSaleRepoDouble = mock(IDirectSaleRepo.class);
            _publicationDouble = mock(Publication.class);
    }

    @Test
    void testDirectSaleItemsByPublicationControllerConstructor(){

        //SUT
        new GetDirectSaleItemsByPublicationsController(_iDirectSaleRepoDouble, _buyerIdDouble);

    }

    @Test
    void getDirectSaleItemsByPublicationShouldReturnEmptyListWhenThereAreNoItems(){
        //Arrange
        when(_iDirectSaleRepoDouble.getDirectSaleItemsByPublication(_publicationDouble)).thenReturn(List.of());
        //SUT
        GetDirectSaleItemsByPublicationsController controller  = new GetDirectSaleItemsByPublicationsController(_iDirectSaleRepoDouble, _buyerIdDouble);
        //Act
        List<Item> resultList  = controller.getDirectSaleItemsByPublication(_publicationDouble);
        //Assert
        assertNotNull(resultList);
        assertTrue(resultList.isEmpty());
    }

    @Test
    void getDirectSaleItemsByPublicationShouldReturnList() {
        //Arrange
        Item itemDouble = mock(Item.class);
        Item itemDouble2 = mock(Item.class);

        when(_iDirectSaleRepoDouble.getDirectSaleItemsByPublication(_publicationDouble)).thenReturn(List.of(itemDouble, itemDouble2));
        // SUT
        GetDirectSaleItemsByPublicationsController controller = new GetDirectSaleItemsByPublicationsController(_iDirectSaleRepoDouble, _buyerIdDouble);
        //Act
        List<Item> resultList = controller.getDirectSaleItemsByPublication(_publicationDouble);
        //Assert
        assertFalse(resultList.isEmpty());
        assertEquals(2, resultList.size());
        assertEquals(itemDouble, resultList.get(0));
        assertEquals(itemDouble2, resultList.get(1));
        assertTrue(resultList.containsAll(List.of(itemDouble, itemDouble2)));
    }

    @Test
    void getDirectSaleItemsByPublicationShouldCallDirectSaleRepo() {
        // SUT
        GetDirectSaleItemsByPublicationsController controller = new GetDirectSaleItemsByPublicationsController(_iDirectSaleRepoDouble, _buyerIdDouble);
        //Act
        List<Item> listOfDirectSaleItemsByPublication = controller.getDirectSaleItemsByPublication(_publicationDouble);
        //Assert
        verify(_iDirectSaleRepoDouble,  times(1)).getDirectSaleItemsByPublication(_publicationDouble);
    }
}
