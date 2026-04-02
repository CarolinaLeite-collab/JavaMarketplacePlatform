package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.genre.Genre;
import TOPSECRET.domain.repository.IGenreRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class CreatePrivateListOfPublicationsControllerTest {

    private IListOfPublicationsRepo _iListOfPublicationsRepoDouble;
    private IGenreRepo _iGenreRepoDouble;
    private User _userDouble;
    private Genre _actionDouble;
    private Genre _poetryDouble;

    @BeforeEach
    void setUp() {

        _iListOfPublicationsRepoDouble = mock(IListOfPublicationsRepo.class);
        _iGenreRepoDouble = mock(IGenreRepo.class);
        _userDouble = mock(User.class);
        _actionDouble = mock(Genre.class);
        _poetryDouble = mock(Genre.class);

    }

    @Test
    void createPrivateListOfPublicationsController(){

        // SUT & Act
        CreatePrivateListOfPublicationsController controller = new CreatePrivateListOfPublicationsController(_iListOfPublicationsRepoDouble, _iGenreRepoDouble);

        // Assert
        assertNotNull(controller);
    }

    @Test
    void shouldCreateListSuccessfully() {
        // Arrange
        ListOfPublications listDouble = mock(ListOfPublications.class);
        when(_iListOfPublicationsRepoDouble.addListOfPublications(_userDouble, "My List", _actionDouble)).thenReturn(listDouble);

        //SUT
        CreatePrivateListOfPublicationsController controller = new CreatePrivateListOfPublicationsController(_iListOfPublicationsRepoDouble, _iGenreRepoDouble);

        // Act
        ListOfPublications result = controller.createListOfPublications(_userDouble, "My List", _actionDouble);

        // Assert
        assertNotNull(result);
        assertEquals(listDouble, result);
    }

    @Test
    void getListOfOfficialGenresDelegatesToRepo() {
        // Arrange
        when(_iGenreRepoDouble.findAll()).thenReturn(List.of(_actionDouble, _poetryDouble));
        CreatePrivateListOfPublicationsController controller =
                new CreatePrivateListOfPublicationsController(
                        _iListOfPublicationsRepoDouble, _iGenreRepoDouble);

        // Act
        Iterable<Genre> result = controller.getListOfOfficialGenres(); // SUT

        // Assert
        assertNotNull(result);
        verify(_iGenreRepoDouble).findAll();
    }

    @Test
    void shouldNotCreateDuplicateList() {
        // Arrange
        when(_iListOfPublicationsRepoDouble.addListOfPublications(_userDouble, "My List", _actionDouble))
                .thenThrow(new IllegalArgumentException("List already exists"));

        //SUT
        CreatePrivateListOfPublicationsController controller = new CreatePrivateListOfPublicationsController(_iListOfPublicationsRepoDouble, _iGenreRepoDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.createListOfPublications(
                        _userDouble, "My List", _actionDouble));
    }
}