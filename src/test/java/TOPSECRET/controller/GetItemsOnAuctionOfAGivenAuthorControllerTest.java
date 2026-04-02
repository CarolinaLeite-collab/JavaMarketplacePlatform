package TOPSECRET.controller;

import TOPSECRET.domain.IAuctionRepo;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.User;
import TOPSECRET.domain.Author.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class GetItemsOnAuctionOfAGivenAuthorControllerTest {

    private User _buyerDouble;
    private IAuctionRepo _iAuctionRepoDouble;
    private Author _authorDouble;

    @BeforeEach
    void setUp() {
        _buyerDouble = mock(User.class);
        _iAuctionRepoDouble = mock(IAuctionRepo.class);
        _authorDouble = mock(Author.class);
    }

    @Test
    void testAConstructor() {

        //SUT
        new GetItemsOnAuctionOfAGivenAuthorController(_iAuctionRepoDouble, _buyerDouble);

    }

    @Test
    void getAuctionItemsByAuthorShouldReturnEmptyListWhenThereAreNoItems() {
        //Arrange
        when(_iAuctionRepoDouble.getAuctionItemsByAuthor(_authorDouble)).thenReturn(List.of());

        //SUT
        GetItemsOnAuctionOfAGivenAuthorController ctl = new GetItemsOnAuctionOfAGivenAuthorController(_iAuctionRepoDouble, _buyerDouble);

        //Act
        List<Item> result = ctl.getAuctionItemsByAuthor(_authorDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getAuctionItemsByAuthorReturnsListWithCorrectSize() {
        // Arrange

        Item _item1 = mock(Item.class);
        Item _item2 = mock(Item.class);

        when(_iAuctionRepoDouble.getAuctionItemsByAuthor(_authorDouble)).thenReturn(List.of(_item1, _item2));

        //SUT
        GetItemsOnAuctionOfAGivenAuthorController ctl = new GetItemsOnAuctionOfAGivenAuthorController(_iAuctionRepoDouble, _buyerDouble);

        // Act
        List<Item> result = ctl.getAuctionItemsByAuthor(_authorDouble);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void getAuctionItemsByAuthorReturnsListContainingCorrectItems() {
        // Arrange

        Item _item1 = mock(Item.class);
        Item _item2 = mock(Item.class);
        Item _item3 = mock(Item.class);

        when(_iAuctionRepoDouble.getAuctionItemsByAuthor(_authorDouble)).thenReturn(List.of(_item1, _item2, _item3));

        //SUT
        GetItemsOnAuctionOfAGivenAuthorController ctl = new GetItemsOnAuctionOfAGivenAuthorController(_iAuctionRepoDouble, _buyerDouble);

        // Act
        List<Item> result = ctl.getAuctionItemsByAuthor(_authorDouble);

        // Assert
        assertTrue(result.containsAll(List.of(_item1, _item2, _item3)));
    }

    @Test
    void getAuctionItemsByAuthorShouldCallRepoWithCorrectAuthor() {

        //SUT / Arrange
        GetItemsOnAuctionOfAGivenAuthorController ctl = new GetItemsOnAuctionOfAGivenAuthorController(_iAuctionRepoDouble, _buyerDouble);

        //Act
        List<Item> result = ctl.getAuctionItemsByAuthor(_authorDouble);

        //Assert
        verify(_iAuctionRepoDouble, times(1)).getAuctionItemsByAuthor(_authorDouble);

    }

}
