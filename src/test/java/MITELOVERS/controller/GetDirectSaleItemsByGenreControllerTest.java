//package MITELOVERS.controller;
//
//import MITELOVERS.domain.directsale.DirectSale;
//import MITELOVERS.domain.edition.Edition;
//import MITELOVERS.domain.item.Item;
//import MITELOVERS.domain.publication.Publication;
//import MITELOVERS.domain.repository.IDirectSaleRepo;
//import MITELOVERS.domain.repository.IEditionRepo;
//import MITELOVERS.domain.repository.IItemRepo;
//import MITELOVERS.domain.repository.IPublicationRepo;
//import MITELOVERS.domain.valueobject.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//class GetDirectSaleItemsByGenreControllerTest {
//
//    private UserId _buyerIdDouble;
//    private IDirectSaleRepo _iDirectSaleRepoDouble;
//    private IItemRepo _iItemRepoDouble;
//    private IEditionRepo _iEditionRepoDouble;
//    private IPublicationRepo _iPublicationRepoDouble;
//
//    @BeforeEach
//    void setUp() {
//        _buyerIdDouble = mock(UserId.class);
//        _iDirectSaleRepoDouble = mock(IDirectSaleRepo.class);
//        _iItemRepoDouble = mock(IItemRepo.class);
//        _iEditionRepoDouble = mock(IEditionRepo.class);
//        _iPublicationRepoDouble = mock(IPublicationRepo.class);
//
//    }
//
//    @Test
//    void testDirectSaleItemsByGenreController(){
//
//        // SUT
//        new GetDirectSaleItemsByGenreController(_iDirectSaleRepoDouble,
//                _iItemRepoDouble,
//                _iEditionRepoDouble,
//                _iPublicationRepoDouble,
//                _buyerIdDouble);
//
//    }
//
//    @Test
//    void GetDirectSaleItemsByGenreControllerShouldReturnDirectSaleItemsOfAGivenAuthor() {
//        //Arrange
//        DirectSale directSaleDouble = mock(DirectSale.class);
//        ItemId itemIdDouble = mock(ItemId.class);
//        Item itemDouble = mock(Item.class);
//        EditionId editionIdDouble = mock(EditionId.class);
//        Edition editionDouble = mock(Edition.class);
//        PublicationId publicationIdDouble = mock(PublicationId.class);
//        Publication publicationDouble = mock(Publication.class);
//        GenreId genreIdDouble = mock(GenreId.class);
//
//        when(_iDirectSaleRepoDouble.findAll()).thenReturn(Arrays.asList(directSaleDouble));
//        when(directSaleDouble.getItemsId()).thenReturn(Arrays.asList(itemIdDouble));
//
//        when(_iItemRepoDouble.ofIdentity(itemIdDouble)).thenReturn(Optional.ofNullable(itemDouble));
//        when(itemDouble.getEditionId()).thenReturn(editionIdDouble);
//
//        when(_iEditionRepoDouble.ofIdentity(editionIdDouble)).thenReturn(Optional.ofNullable(editionDouble));
//        when(editionDouble.getPublicationId()).thenReturn(publicationIdDouble);
//
//        when(_iPublicationRepoDouble.ofIdentity(publicationIdDouble)).thenReturn(Optional.ofNullable(publicationDouble));
//        when(publicationDouble.isByGenreId(genreIdDouble)).thenReturn(true);
//
//        //SUT
//        GetDirectSaleItemsByGenreController ctl = new GetDirectSaleItemsByGenreController(_iDirectSaleRepoDouble,
//                _iItemRepoDouble,
//                _iEditionRepoDouble,
//                _iPublicationRepoDouble,
//                _buyerIdDouble);
//
//
//        //Act
//        List<ItemId> result = ctl.getDirectSaleItemsByGenre(genreIdDouble);
//
//        //Assert
//        assertEquals(1, result.size());
//    }
//
//    @Test
//    void GetDirectSaleItemsByGenreControllerShouldThrowWhenPublicationNotFound() {
//        //Arrange
//        DirectSale directSaleDouble = mock(DirectSale.class);
//        ItemId itemIdDouble = mock(ItemId.class);
//        Item itemDouble = mock(Item.class);
//        EditionId editionIdDouble = mock(EditionId.class);
//        Edition editionDouble = mock(Edition.class);
//        PublicationId publicationIdDouble = mock(PublicationId.class);
//        Publication publicationDouble = mock(Publication.class);
//        GenreId genreIdDouble = mock(GenreId.class);
//
//        when(_iDirectSaleRepoDouble.findAll()).thenReturn(Arrays.asList(directSaleDouble));
//        when(directSaleDouble.getItemsId()).thenReturn(Arrays.asList(itemIdDouble));
//
//        when(_iItemRepoDouble.ofIdentity(itemIdDouble)).thenReturn(Optional.ofNullable(itemDouble));
//        when(itemDouble.getEditionId()).thenReturn(editionIdDouble);
//
//        when(_iEditionRepoDouble.ofIdentity(editionIdDouble)).thenReturn(Optional.ofNullable(editionDouble));
//        when(editionDouble.getPublicationId()).thenReturn(publicationIdDouble);
//
//        //SUT
//        GetDirectSaleItemsByGenreController ctl = new GetDirectSaleItemsByGenreController(_iDirectSaleRepoDouble,
//                _iItemRepoDouble,
//                _iEditionRepoDouble,
//                _iPublicationRepoDouble,
//                _buyerIdDouble);
//
//
//        //Act + Assert
//        assertThrows(IllegalStateException.class,
//                () -> ctl.getDirectSaleItemsByGenre(genreIdDouble));
//    }
//
//    @Test
//    void GetDirectSaleItemsByGenreControllerShouldThrowWhenEditionNotFound() {
//        //Arrange
//        DirectSale directSaleDouble = mock(DirectSale.class);
//        ItemId itemIdDouble = mock(ItemId.class);
//        Item itemDouble = mock(Item.class);
//        EditionId editionIdDouble = mock(EditionId.class);
//        Edition editionDouble = mock(Edition.class);
//        PublicationId publicationIdDouble = mock(PublicationId.class);
//        Publication publicationDouble = mock(Publication.class);
//        GenreId genreIdDouble = mock(GenreId.class);
//
//        when(_iDirectSaleRepoDouble.findAll()).thenReturn(Arrays.asList(directSaleDouble));
//        when(directSaleDouble.getItemsId()).thenReturn(Arrays.asList(itemIdDouble));
//
//        when(_iItemRepoDouble.ofIdentity(itemIdDouble)).thenReturn(Optional.ofNullable(itemDouble));
//        when(itemDouble.getEditionId()).thenReturn(editionIdDouble);
//
//        //SUT
//        GetDirectSaleItemsByGenreController ctl = new GetDirectSaleItemsByGenreController(_iDirectSaleRepoDouble,
//                _iItemRepoDouble,
//                _iEditionRepoDouble,
//                _iPublicationRepoDouble,
//                _buyerIdDouble);
//
//
//        // Act + Assert
//        assertThrows(IllegalStateException.class,
//                () -> ctl.getDirectSaleItemsByGenre(genreIdDouble));
//    }
//
//    @Test
//    void GetDirectSaleItemsByGenreControllerShouldThrowWhenItemNotFound() {
//        //Arrange
//        DirectSale directSaleDouble = mock(DirectSale.class);
//        ItemId itemIdDouble = mock(ItemId.class);
//        Item itemDouble = mock(Item.class);
//        EditionId editionIdDouble = mock(EditionId.class);
//        Edition editionDouble = mock(Edition.class);
//        PublicationId publicationIdDouble = mock(PublicationId.class);
//        Publication publicationDouble = mock(Publication.class);
//        GenreId genreIdDouble = mock(GenreId.class);
//
//        when(_iDirectSaleRepoDouble.findAll()).thenReturn(Arrays.asList(directSaleDouble));
//        when(directSaleDouble.getItemsId()).thenReturn(Arrays.asList(itemIdDouble));
//
//        //SUT
//        GetDirectSaleItemsByGenreController ctl = new GetDirectSaleItemsByGenreController(_iDirectSaleRepoDouble,
//                _iItemRepoDouble,
//                _iEditionRepoDouble,
//                _iPublicationRepoDouble,
//                _buyerIdDouble);
//
//
//        // Act + Assert
//        assertThrows(IllegalStateException.class,
//                () -> ctl.getDirectSaleItemsByGenre(genreIdDouble));
//    }
//
//
//}
