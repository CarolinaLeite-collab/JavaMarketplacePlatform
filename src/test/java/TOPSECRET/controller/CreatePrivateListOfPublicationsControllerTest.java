package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CreatePrivateListOfPublicationsController}.
 *
 * <p>The following Mockito doubles are used:
 * <ul>
 *   <li>{@link IListOfPublicationsRepo} — mocked collaborator (persistence dependency)</li>
 *   <li>{@link IGenreRepo} — mocked collaborator (persistence dependency)</li>
 *   <li>{@link User} — mocked dummy (structural input, no behaviour required)</li>
 *   <li>{@link Genre} — mocked dummy (structural input, no behaviour required)</li>
 * </ul>
 */

class CreatePrivateListOfPublicationsControllerTest {

    private IListOfPublicationsRepo _repoDouble;
    private IGenreRepo _iGenreRepoDouble;
    private User _userDouble;
    private Genre _actionDouble;
    private Genre _poetryDouble;

    @BeforeEach
    void setUp() {

        _repoDouble = mock(IListOfPublicationsRepo.class);
        _iGenreRepoDouble = mock(IGenreRepo.class);
        _userDouble = mock(User.class);
        _actionDouble = mock(Genre.class);
        _poetryDouble = mock(Genre.class);

    }

    @Test
    void testCreatePrivateListOfPublicationsController(){

        // SUT & Act
        CreatePrivateListOfPublicationsController controller = new CreatePrivateListOfPublicationsController(_repoDouble, _iGenreRepoDouble, _userDouble);

    }

    @Test
    void shouldCreateListSuccessfully() {
        // Arrange
        ListOfPublications listDouble = mock(ListOfPublications.class);
        when(_repoDouble.addListOfPublications(_userDouble, "My List", _actionDouble)).thenReturn(listDouble);

        //SUT
        CreatePrivateListOfPublicationsController controller = new CreatePrivateListOfPublicationsController(_repoDouble, _iGenreRepoDouble, _userDouble);

        // Act
        ListOfPublications result = controller.createListOfPublications(_userDouble, "My List", _actionDouble);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(listDouble, result)
        );
        verify(_repoDouble).addListOfPublications(_userDouble, "My List", _actionDouble);
    }

    @Test
    void shouldNotCreateDuplicateList() {
        // Arrange
        when(_repoDouble.addListOfPublications(_userDouble, "My List", _actionDouble)).thenReturn(null);

        //SUT
        CreatePrivateListOfPublicationsController controller = new CreatePrivateListOfPublicationsController(_repoDouble, _iGenreRepoDouble, _userDouble);

        // Act
        ListOfPublications duplicate = controller.createListOfPublications(_userDouble, "My List", _actionDouble);

        // Assert
        assertNull(duplicate);
        verify(_repoDouble).addListOfPublications(_userDouble, "My List", _actionDouble);
    }

    @Test
    void getListOfOfficialGenresReturnsUnmodifiableList() {
        // Arrange
        when(_iGenreRepoDouble.getListOfOfficialGenres()).thenReturn(List.of(_actionDouble, _poetryDouble));

        //SUT
        CreatePrivateListOfPublicationsController controller = new CreatePrivateListOfPublicationsController(_repoDouble, _iGenreRepoDouble, _userDouble);

        // Act
        List<Genre> officialGenres = controller.getListOfOfficialGenres();

        // Assert
        assertThrows(UnsupportedOperationException.class,
                () -> officialGenres.add(mock(Genre.class)));
    }

    @Test
    void getListOfOfficialGenresReturnsCorrectList() {
        // Arrange
        when(_iGenreRepoDouble.getListOfOfficialGenres()).thenReturn(List.of(_actionDouble, _poetryDouble));

        //SUT
        CreatePrivateListOfPublicationsController controller = new CreatePrivateListOfPublicationsController(_repoDouble, _iGenreRepoDouble, _userDouble);

        // Act
        List<Genre> officialGenres = controller.getListOfOfficialGenres();

        // Assert
        assertAll(
                () -> assertEquals(2, officialGenres.size()),
                () -> assertTrue(officialGenres.contains(_actionDouble)),
                () -> assertTrue(officialGenres.contains(_poetryDouble))
        );
    }
}