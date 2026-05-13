//package MITELOVERS.controller;
//
//import MITELOVERS.domain.auction.Auction;
//import MITELOVERS.domain.edition.Edition;
//import MITELOVERS.domain.item.Item;
//import MITELOVERS.domain.publication.Publication;
//import MITELOVERS.domain.repository.*;
//import MITELOVERS.domain.valueobject.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class GetAuctionItemsByGenreControllerTest {
//
//    private UserId _buyerIdDouble;
//    private IAuctionRepo _iAuctionRepoDouble;
//    private IItemRepo _iItemRepoDouble;
//    private IEditionRepo _iEditionRepoDouble;
//    private IPublicationRepo _iPublicationRepoDouble;
//    private Auction _auctionDouble;
//    private Item _itemDouble;
//    private Edition _editionDouble;
//    private Publication _publicationDouble;
//    private PublicationId _publicationIdDouble;
//    private EditionId _editionIdDouble;
//    private ItemId _itemIdDouble;
//    private GenreId _genreIdDouble1;
//    private IGenreRepo _iGenreRepoDouble;
//
//    @BeforeEach
//    void setUp() {
//
//        _buyerIdDouble = mock(UserId.class);
//        _iAuctionRepoDouble = mock(IAuctionRepo.class);
//        _iItemRepoDouble = mock(IItemRepo.class);
//        _iEditionRepoDouble = mock(IEditionRepo.class);
//        _iPublicationRepoDouble = mock(IPublicationRepo.class);
//        _auctionDouble = mock(Auction.class);
//        _itemDouble = mock(Item.class);
//        _editionDouble = mock(Edition.class);
//        _publicationDouble = mock(Publication.class);
//        _publicationIdDouble = mock(PublicationId.class);
//        _editionIdDouble = mock(EditionId.class);
//        _itemIdDouble = mock(ItemId.class);
//        _genreIdDouble1 = mock(GenreId.class);
//        _iGenreRepoDouble = mock(IGenreRepo.class);
//    }
//
//    @Test
//    void testAuctionItemsByGenreController(){
//
//        //SUT
//        new GetAuctionItemsByGenreController (_iAuctionRepoDouble, _iItemRepoDouble, _iPublicationRepoDouble,
//                _iEditionRepoDouble, _iGenreRepoDouble, _buyerIdDouble);
//    }
//
//    @Test
//    void findAllKeysShouldReturnGenreIdsFromRepo() {
//        //Arrange
//        GenreId genreIdDouble2 = mock(GenreId.class);
//
//        List<GenreId> expected = List.of(_genreIdDouble1, genreIdDouble2);
//
//        when(_iGenreRepoDouble.findAllKeys()).thenReturn(expected);
//
//        //SUT
//        GetAuctionItemsByGenreController controller = new GetAuctionItemsByGenreController (_iAuctionRepoDouble,
//                _iItemRepoDouble, _iPublicationRepoDouble, _iEditionRepoDouble, _iGenreRepoDouble, _buyerIdDouble);
//
//        //Act
//        Iterable<GenreId> result = controller.findAllKeys();
//
//        //Assert
//        assertEquals(expected, result);
//    }
//
//    @Test
//    void shouldReturnsItemsMatchingGenreIds() {
//        //Arrange
//        when(_iAuctionRepoDouble.findAll()).thenReturn(List.of(_auctionDouble));
//        when(_auctionDouble.getItemsId()).thenReturn(List.of(_itemIdDouble));
//        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
//        when(_itemDouble.getEditionId()).thenReturn(_editionIdDouble);
//        when(_iEditionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(_editionDouble));
//        when(_editionDouble.getPublicationId()).thenReturn(_publicationIdDouble);
//        when(_iPublicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(_publicationDouble));
//        when(_publicationDouble.isByGenreId(_genreIdDouble1)).thenReturn(true);
//
//        //SUT
//        GetAuctionItemsByGenreController controller = new GetAuctionItemsByGenreController (_iAuctionRepoDouble,
//                _iItemRepoDouble, _iPublicationRepoDouble, _iEditionRepoDouble, _iGenreRepoDouble, _buyerIdDouble);
//
//        //Act
//        List<ItemId> result = controller.getAuctionItemsByGenreId(_genreIdDouble1);
//
//        //Assert
//        assertEquals(1, result.size());
//        assertTrue(result.contains(_itemIdDouble));
//    }
//
//    @Test
//    void shouldReturnsEmptyListWhenNoItemsMatchGenre() {
//        // Arrange
//        when(_iAuctionRepoDouble.findAll()).thenReturn(List.of(_auctionDouble));
//        when(_auctionDouble.getItemsId()).thenReturn(List.of(_itemIdDouble));
//        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
//        when(_iEditionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(_editionDouble));
//        when(_iPublicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(_publicationDouble));
//        when(_publicationDouble.isByGenreId(_genreIdDouble1)).thenReturn(false);
//
//        //SUT
//        GetAuctionItemsByGenreController controller = new GetAuctionItemsByGenreController (_iAuctionRepoDouble,
//                _iItemRepoDouble, _iPublicationRepoDouble, _iEditionRepoDouble, _iGenreRepoDouble, _buyerIdDouble);
//
//        // Act
//        List<ItemId> result = controller.getAuctionItemsByGenreId(_genreIdDouble1);
//
//        // Assert
//        assertTrue(result.isEmpty());
//    }
//
//    @Test
//    void shouldAggregatesItemsFromMultipleAuctions() {
//        // Arrange
//        Auction auctionDouble2 = mock(Auction.class);
//        ItemId itemIdDouble2 = mock(ItemId.class);
//
//        when(_iAuctionRepoDouble.findAll()).thenReturn(List.of(_auctionDouble, auctionDouble2));
//        when(_auctionDouble.getItemsId()).thenReturn(List.of(_itemIdDouble));
//        when(auctionDouble2.getItemsId()).thenReturn(List.of(itemIdDouble2));
//        when(_iItemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(_itemDouble));
//        when(_iEditionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(_editionDouble));
//        when(_iPublicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(_publicationDouble));
//        when(_publicationDouble.isByGenreId(_genreIdDouble1)).thenReturn(true);
//
//        //SUT
//        GetAuctionItemsByGenreController controller = new GetAuctionItemsByGenreController (_iAuctionRepoDouble,
//                _iItemRepoDouble, _iPublicationRepoDouble, _iEditionRepoDouble, _iGenreRepoDouble, _buyerIdDouble);
//
//        // Act
//        List<ItemId> result = controller.getAuctionItemsByGenreId(_genreIdDouble1);
//
//        // Assert
//        assertEquals(2, result.size());
//    }
//
//    @Test
//    void shouldThrowsExceptionWhenItemNotFound() {
//        // Arrange
//        when(_iAuctionRepoDouble.findAll()).thenReturn(List.of(_auctionDouble));
//        when(_auctionDouble.getItemsId()).thenReturn(List.of(_itemIdDouble));
//        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.empty());
//
//        //SUT
//        GetAuctionItemsByGenreController controller = new GetAuctionItemsByGenreController (_iAuctionRepoDouble,
//                _iItemRepoDouble, _iPublicationRepoDouble, _iEditionRepoDouble, _iGenreRepoDouble, _buyerIdDouble);
//
//        // Act & Assert
//        assertThrows(IllegalStateException.class,
//                () -> controller.getAuctionItemsByGenreId(_genreIdDouble1));
//    }
//
//    @Test
//    void shouldThrowsExceptionWhenEditionNotFound() {
//        // Arrange
//        when(_iAuctionRepoDouble.findAll()).thenReturn(List.of(_auctionDouble));
//        when(_auctionDouble.getItemsId()).thenReturn(List.of(_itemIdDouble));
//        when(_iEditionRepoDouble.ofIdentity(_editionIdDouble)).thenReturn(Optional.empty());
//        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
//        when(_itemDouble.getEditionId()).thenReturn(_editionIdDouble);
//
//        //SUT
//        GetAuctionItemsByGenreController controller = new GetAuctionItemsByGenreController (_iAuctionRepoDouble,
//                _iItemRepoDouble, _iPublicationRepoDouble, _iEditionRepoDouble, _iGenreRepoDouble, _buyerIdDouble);
//
//        // Act & Assert
//        assertThrows(IllegalStateException.class,
//                () -> controller.getAuctionItemsByGenreId(_genreIdDouble1));
//    }
//
//    @Test
//    void shouldThrowsExceptionWhenPublicationNotFound() {
//        // Arrange
//        when(_iAuctionRepoDouble.findAll()).thenReturn(List.of(_auctionDouble));
//        when(_auctionDouble.getItemsId()).thenReturn(List.of(_itemIdDouble));
//        when(_iPublicationRepoDouble.ofIdentity(_publicationIdDouble)).thenReturn(Optional.empty());
//        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
//        when(_itemDouble.getEditionId()).thenReturn(_editionIdDouble);
//        when(_iEditionRepoDouble.ofIdentity(_editionIdDouble)).thenReturn(Optional.of(_editionDouble));
//        when(_editionDouble.getPublicationId()).thenReturn(_publicationIdDouble);
//
//        //SUT
//        GetAuctionItemsByGenreController controller = new GetAuctionItemsByGenreController (_iAuctionRepoDouble,
//                _iItemRepoDouble, _iPublicationRepoDouble, _iEditionRepoDouble, _iGenreRepoDouble, _buyerIdDouble);
//
//        // Act & Assert
//        assertThrows(IllegalStateException.class,
//                () -> controller.getAuctionItemsByGenreId(_genreIdDouble1));
//    }
//}
