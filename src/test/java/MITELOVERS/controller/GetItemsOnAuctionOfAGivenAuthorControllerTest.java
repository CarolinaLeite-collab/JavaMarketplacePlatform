package MITELOVERS.controller;

import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.repository.*;
import MITELOVERS.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetItemsOnAuctionOfAGivenAuthorControllerTest {

    private UserId _userIdDouble;
    private IAuctionRepo _iAuctionRepoDouble;
    private IItemRepo _iItemRepoDouble;
    private IEditionRepo _iEditionRepoDouble;
    private IPublicationRepo _iPublicationRepoDouble;
    private IAuthorRepo _iAuthorRepoDouble;
    private AuthorId _authorIdDouble;
    private ItemId _itemIdDouble;
    private Item _itemDouble;
    private Auction _auctionDouble;
    private EditionId _editionIdDouble;
    private Edition _editionDouble;
    private PublicationId _publicationIdDouble;
    private Publication _publicationDouble;


    @BeforeEach
    void setUp() {

        _userIdDouble = mock(UserId.class);
        _iAuctionRepoDouble = mock(IAuctionRepo.class);
        _iItemRepoDouble = mock(IItemRepo.class);
        _iEditionRepoDouble = mock(IEditionRepo.class);
        _iPublicationRepoDouble = mock(IPublicationRepo.class);
        _authorIdDouble = mock(AuthorId.class);
        _itemIdDouble = mock(ItemId.class);
        _itemDouble = mock(Item.class);
        _auctionDouble = mock(Auction.class);
        _editionIdDouble = mock(EditionId.class);
        _publicationIdDouble = mock(PublicationId.class);
        _editionDouble = mock(Edition.class);
        _publicationDouble = mock(Publication.class);
        _iAuthorRepoDouble = mock(IAuthorRepo.class);

    }

    @Test
    void testAConstructor(){

        //act / SUT
        new GetItemsOnAuctionOfAGivenAuthorController(_iAuthorRepoDouble, _iItemRepoDouble, _iEditionRepoDouble, _iPublicationRepoDouble, _iAuctionRepoDouble, _userIdDouble);

    }

    @Test
    void findAllKeysShouldReturnAuthorIdsFromRepo() {
        //Arrange
        AuthorId authorIdDouble2 = mock(AuthorId.class);

        List<AuthorId> expected = List.of(_authorIdDouble, authorIdDouble2);

        when(_iAuthorRepoDouble.findAllKeys()).thenReturn(expected);

        //SUT
        GetItemsOnAuctionOfAGivenAuthorController controller =  new GetItemsOnAuctionOfAGivenAuthorController(_iAuthorRepoDouble, _iItemRepoDouble, _iEditionRepoDouble, _iPublicationRepoDouble, _iAuctionRepoDouble, _userIdDouble);

        //Act
        Iterable<AuthorId> result = controller.findAllKeys();

        //Assert
        assertEquals(expected, result);
    }

    @Test
    void shouldReturnItemsMatchingAuthorIds() {
        //Arrange
        when(_iAuctionRepoDouble.findAll()).thenReturn(List.of(_auctionDouble));
        when(_auctionDouble.getItemsId()).thenReturn(List.of(_itemIdDouble));
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
        when(_itemDouble.getEditionId()).thenReturn(_editionIdDouble);
        when(_iEditionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(_editionDouble));
        when(_editionDouble.getPublicationId()).thenReturn(_publicationIdDouble);
        when(_iPublicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(_publicationDouble));
        when(_publicationDouble.isByAuthorId(_authorIdDouble)).thenReturn(true);

        //SUT
        GetItemsOnAuctionOfAGivenAuthorController controller =  new GetItemsOnAuctionOfAGivenAuthorController(_iAuthorRepoDouble, _iItemRepoDouble, _iEditionRepoDouble, _iPublicationRepoDouble, _iAuctionRepoDouble, _userIdDouble);

        //Act
        List<ItemId> result = controller.getAuctionItemsByAuthorId(_authorIdDouble);

        //Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(_itemIdDouble));
    }

    @Test
    void shouldReturnEmptyListWhenNoItemsMatchAuthorId() {
        // Arrange
        when(_iAuctionRepoDouble.findAll()).thenReturn(List.of(_auctionDouble));
        when(_auctionDouble.getItemsId()).thenReturn(List.of(_itemIdDouble));
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
        when(_iEditionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(_editionDouble));
        when(_iPublicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(_publicationDouble));
        when(_publicationDouble.isByAuthorId(_authorIdDouble)).thenReturn(false);

        //SUT
        GetItemsOnAuctionOfAGivenAuthorController controller =  new GetItemsOnAuctionOfAGivenAuthorController(_iAuthorRepoDouble, _iItemRepoDouble, _iEditionRepoDouble, _iPublicationRepoDouble, _iAuctionRepoDouble, _userIdDouble);

        // Act
        List<ItemId> result = controller.getAuctionItemsByAuthorId(_authorIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldAggregateItemsFromMultipleAuctions() {
        // Arrange
        Auction auctionDouble2 = mock(Auction.class);
        ItemId itemIdDouble2 = mock(ItemId.class);

        when(_iAuctionRepoDouble.findAll()).thenReturn(List.of(_auctionDouble, auctionDouble2));
        when(_auctionDouble.getItemsId()).thenReturn(List.of(_itemIdDouble));
        when(auctionDouble2.getItemsId()).thenReturn(List.of(itemIdDouble2));
        when(_iItemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(_itemDouble));
        when(_iEditionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(_editionDouble));
        when(_iPublicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(_publicationDouble));
        when(_publicationDouble.isByAuthorId(_authorIdDouble)).thenReturn(true);

        //SUT
        GetItemsOnAuctionOfAGivenAuthorController controller =  new GetItemsOnAuctionOfAGivenAuthorController(_iAuthorRepoDouble, _iItemRepoDouble, _iEditionRepoDouble, _iPublicationRepoDouble, _iAuctionRepoDouble, _userIdDouble);

        // Act
        List<ItemId> result = controller.getAuctionItemsByAuthorId(_authorIdDouble);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void shouldThrowExceptionWhenItemNotFound() {
        // Arrange
        when(_iAuctionRepoDouble.findAll()).thenReturn(List.of(_auctionDouble));
        when(_auctionDouble.getItemsId()).thenReturn(List.of(_itemIdDouble));
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.empty());

        //SUT
        GetItemsOnAuctionOfAGivenAuthorController controller =  new GetItemsOnAuctionOfAGivenAuthorController(_iAuthorRepoDouble, _iItemRepoDouble, _iEditionRepoDouble, _iPublicationRepoDouble, _iAuctionRepoDouble, _userIdDouble);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> controller.getAuctionItemsByAuthorId(_authorIdDouble));
    }

    @Test
    void shouldThrowExceptionWhenEditionNotFound() {
        // Arrange
        when(_iAuctionRepoDouble.findAll()).thenReturn(List.of(_auctionDouble));
        when(_auctionDouble.getItemsId()).thenReturn(List.of(_itemIdDouble));
        when(_iEditionRepoDouble.ofIdentity(_editionIdDouble)).thenReturn(Optional.empty());
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
        when(_itemDouble.getEditionId()).thenReturn(_editionIdDouble);

        //SUT
        GetItemsOnAuctionOfAGivenAuthorController controller =  new GetItemsOnAuctionOfAGivenAuthorController(_iAuthorRepoDouble, _iItemRepoDouble, _iEditionRepoDouble, _iPublicationRepoDouble, _iAuctionRepoDouble, _userIdDouble);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> controller.getAuctionItemsByAuthorId(_authorIdDouble));
    }

    @Test
    void shouldThrowExceptionWhenPublicationNotFound() {
        // Arrange
        when(_iAuctionRepoDouble.findAll()).thenReturn(List.of(_auctionDouble));
        when(_auctionDouble.getItemsId()).thenReturn(List.of(_itemIdDouble));
        when(_iPublicationRepoDouble.ofIdentity(_publicationIdDouble)).thenReturn(Optional.empty());
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
        when(_itemDouble.getEditionId()).thenReturn(_editionIdDouble);
        when(_iEditionRepoDouble.ofIdentity(_editionIdDouble)).thenReturn(Optional.of(_editionDouble));
        when(_editionDouble.getPublicationId()).thenReturn(_publicationIdDouble);

        //SUT
        GetItemsOnAuctionOfAGivenAuthorController controller =  new GetItemsOnAuctionOfAGivenAuthorController(_iAuthorRepoDouble, _iItemRepoDouble, _iEditionRepoDouble, _iPublicationRepoDouble, _iAuctionRepoDouble, _userIdDouble);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> controller.getAuctionItemsByAuthorId(_authorIdDouble));
    }


}

