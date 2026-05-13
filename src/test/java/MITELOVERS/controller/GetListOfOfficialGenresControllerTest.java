//package MITELOVERS.controller;
//
//import MITELOVERS.domain.listofitems.ListOfItems;
//import MITELOVERS.domain.repository.IGenreRepo;
//import MITELOVERS.domain.repository.IListOfItemsRepo;
//import MITELOVERS.domain.valueobject.GenreId;
//import MITELOVERS.domain.valueobject.ItemId;
//import MITELOVERS.domain.valueobject.UserId;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//class GetListOfOfficialGenresControllerTest {
//
//    private IGenreRepo _iGenreRepoDouble;
//    private IListOfItemsRepo _iListOfItemsRepo;
//    private ListOfItems _listOfItemsDouble;
//    private ItemId _itemIdDouble;
//    private GenreId _genreIdDouble;
//    private UserId _userIdDouble;
//
//    @BeforeEach
//    void setUp() {
//        _iGenreRepoDouble = mock(IGenreRepo.class);
//        _iListOfItemsRepo = mock(IListOfItemsRepo.class);
//        _listOfItemsDouble = mock(ListOfItems.class);
//        _itemIdDouble = mock(ItemId.class);
//        _genreIdDouble = mock(GenreId.class);
//        _userIdDouble = mock(UserId.class);
//    }
//
//    @Test
//    void testConstructorGetOfficialGenresController() {
//
//        //SUT
//        GetListOfOfficialGenresController controller = new GetListOfOfficialGenresController(_iGenreRepoDouble,
//                _iListOfItemsRepo, _userIdDouble);
//
//    }
//
//    @Test
//    void findAllKeysShouldReturnGenreIdsFromRepo() {
//        //Arrange
//        GenreId genreIdDouble2 = mock(GenreId.class);
//
//        List<GenreId> expected = List.of(_genreIdDouble, genreIdDouble2);
//
//        when(_iGenreRepoDouble.findAllKeys()).thenReturn(expected);
//
//        //SUT
//        GetListOfOfficialGenresController controller = new GetListOfOfficialGenresController(_iGenreRepoDouble,
//                _iListOfItemsRepo, _userIdDouble);
//
//        //Act
//        Iterable<GenreId> result = controller.findAllKeys();
//
//        //Assert
//        assertEquals(expected, result);
//    }
//
//    @Test
//    void shouldReturnItemIdsFromPublicListsWithGivenGenre() {
//        // Arrange
//        ListOfItems listOfItemsDouble2 = mock(ListOfItems.class);
//        ItemId itemIdDouble2 = mock(ItemId.class);
//
//        when(_listOfItemsDouble.isPrivate()).thenReturn(false);
//        when(_listOfItemsDouble.getGenreId()).thenReturn(_genreIdDouble);
//        when(_listOfItemsDouble.getItemIds()).thenReturn(List.of(_itemIdDouble));
//
//        when(listOfItemsDouble2.isPrivate()).thenReturn(false);
//        when(listOfItemsDouble2.getGenreId()).thenReturn(_genreIdDouble);
//        when(listOfItemsDouble2.getItemIds()).thenReturn(List.of(itemIdDouble2));
//
//        when(_iListOfItemsRepo.findAll()).thenReturn(List.of(_listOfItemsDouble, listOfItemsDouble2));
//
//        //SUT
//        GetListOfOfficialGenresController controller = new GetListOfOfficialGenresController(_iGenreRepoDouble,
//                _iListOfItemsRepo, _userIdDouble);
//
//        // Act
//        List<ItemId> result = controller.getPublicListsByGenreId(_genreIdDouble);
//
//        // Assert
//        assertEquals(2, result.size());
//        assertTrue(result.contains(_itemIdDouble));
//        assertTrue(result.contains(_itemIdDouble));
//    }
//
//    @Test
//    void shouldIgnorePrivateLists() {
//        // Assert
//        ListOfItems privateListDouble = mock(ListOfItems.class);
//
//        when(privateListDouble.isPrivate()).thenReturn(true);
//        when(privateListDouble.getGenreId()).thenReturn(_genreIdDouble);
//
//        when(_iListOfItemsRepo.findAll()).thenReturn(List.of(privateListDouble));
//
//        //SUT
//        GetListOfOfficialGenresController controller = new GetListOfOfficialGenresController(_iGenreRepoDouble,
//                _iListOfItemsRepo, _userIdDouble);
//
//        // Act
//        List<ItemId> result = controller.getPublicListsByGenreId(_genreIdDouble);
//
//        // Assert
//        assertTrue(result.isEmpty());
//    }
//
//    @Test
//    void shouldIgnoreListsWithDifferentGenre() {
//        // Arrange
//        GenreId genreIdDouble2 = mock(GenreId.class);
//        when(_listOfItemsDouble.isPrivate()).thenReturn(false);
//        when(_listOfItemsDouble.getGenreId()).thenReturn(genreIdDouble2);
//
//        when(_iListOfItemsRepo.findAll()).thenReturn(List.of(_listOfItemsDouble));
//
//        //SUT
//        GetListOfOfficialGenresController controller = new GetListOfOfficialGenresController(_iGenreRepoDouble,
//                _iListOfItemsRepo, _userIdDouble);
//
//        // Act
//        List<ItemId> result = controller.getPublicListsByGenreId(_genreIdDouble);
//
//        // Assert
//        assertTrue(result.isEmpty());
//    }
//
//    @Test
//    void shouldReturnEmptyListWhenRepoIsEmpty() {
//        // Arrange
//        when(_iListOfItemsRepo.findAll()).thenReturn(List.of());
//
//        //SUT
//        GetListOfOfficialGenresController controller = new GetListOfOfficialGenresController(_iGenreRepoDouble,
//                _iListOfItemsRepo, _userIdDouble);
//
//        // Act
//        List<ItemId> result = controller.getPublicListsByGenreId(_genreIdDouble);
//
//        // Assert
//        assertNotNull(result);
//        assertTrue(result.isEmpty());
//    }
//
//}
