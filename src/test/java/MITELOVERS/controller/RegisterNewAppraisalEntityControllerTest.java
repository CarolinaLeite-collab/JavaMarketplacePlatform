package MITELOVERS.controller;

import MITELOVERS.domain.appraisalentity.AppraisalEntity;
import MITELOVERS.domain.appraisalentity.AppraisalEntityFactory;
import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.repository.IAppraisalEntityRepo;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.repository.IPublicationTypeRepo;
import MITELOVERS.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class RegisterNewAppraisalEntityControllerTest {

    @MockBean
    IAppraisalEntityRepo _iAppraisalEntityRepoDouble;

    @MockBean
    AppraisalEntityFactory _appraisalEntityFactoryDouble;

    @MockBean
    IPublicationTypeRepo _iPublicationTypeRepoDouble;

    @MockBean
    IGenreRepo _iGenreRepoDouble;

    @MockBean
    UserId _adminIdDouble;

    @InjectMocks
    RegisterNewAppraisalEntityController _controller;

    private Name _nameDouble;
    private List<Genre> _genres;
    private Genre _genreDouble;
    private List<GenreId> _genreIds;
    private GenreId _genreIdDouble;
    private List<PublicationType> _publicationTypes;
    private List<PublicationTypeId> _publicationTypeIds;
    private PublicationTypeId _publicationTypeIdDouble;
    private AppraisalEntityId _appraisalEntityIdDouble;
    private AppraisalEntity _appraisalEntityDouble;
    private PublicationType _publicationTypeDouble;

    @BeforeEach
    void setUp() throws InstantiationException{

        MockitoAnnotations.openMocks(this);

        _nameDouble = mock(Name.class);

        _genreDouble = mock(Genre.class);
        _genres = new ArrayList<>();
        _genres.add(_genreDouble);

        when(_iGenreRepoDouble.findAll()).thenReturn(_genres);

        _genreIdDouble = mock(GenreId.class);
        _genreIds = new ArrayList<>();
        _genreIds.add(_genreIdDouble);

        _publicationTypeDouble = mock(PublicationType.class);
        _publicationTypes = new ArrayList<>();
        _publicationTypes.add(_publicationTypeDouble);

        _publicationTypeIdDouble = mock(PublicationTypeId.class);
        _publicationTypeIds = new ArrayList<>();
        _publicationTypeIds.add(_publicationTypeIdDouble);

        when(_iPublicationTypeRepoDouble.findAll()).thenReturn(_publicationTypes);

        _appraisalEntityDouble = mock(AppraisalEntity.class);

        _appraisalEntityIdDouble = mock(AppraisalEntityId.class);

    }

    @Test
    void registerNewAppraisalEntityControllerTest(){
        // SUT
          _controller =
                new RegisterNewAppraisalEntityController(_iAppraisalEntityRepoDouble,
                        _iPublicationTypeRepoDouble, _appraisalEntityFactoryDouble, _iGenreRepoDouble, _adminIdDouble);

    }

    @Test
    void shouldGetPublicationTypesFromRepo() {
        // Act
        Iterable <PublicationType> types = _controller.getPublicationTypes();

        // Assert
        assertEquals(_publicationTypes, types);

    }

    @Test
    void shouldGetEmptyPublicationTypesWhenRepoIsEmpty() {
        // Arrange
        when(_iPublicationTypeRepoDouble.findAll()).thenReturn(new ArrayList<>());

        // Act
        Iterable<PublicationType> types = _controller.getPublicationTypes();

        // Assert
        assertFalse(types.iterator().hasNext());
    }

    @Test
    void shouldGetGenresFromRepo() {
        // Act
        Iterable <Genre> genres = _controller.getGenres();

        // Assert
        assertNotNull(genres);
        assertTrue(genres.iterator().hasNext());

    }

    @Test
    void shouldGetEmptyGenresWhenRepoIsEmpty() {
        // Arrange
        when(_iGenreRepoDouble.findAll()).thenReturn(new ArrayList<>());

        // Act
        Iterable<Genre> genres = _controller.getGenres();

        // Assert
        assertFalse(genres.iterator().hasNext());
    }

    @Test
    void shouldSuccessfullyRegisterAppraisalEntity() {
        // Arrange
        when(_appraisalEntityFactoryDouble.createAppraisalEntity(_nameDouble, _publicationTypeIds, _genreIds))
                .thenReturn(_appraisalEntityDouble);
        when(_appraisalEntityDouble.identity()).thenReturn(_appraisalEntityIdDouble);
        when(_iAppraisalEntityRepoDouble.containsOfIdentity(_appraisalEntityIdDouble)).thenReturn(false);
        when(_iAppraisalEntityRepoDouble.save(_appraisalEntityDouble)).thenReturn(_appraisalEntityDouble);

        // Act
        AppraisalEntity result = _controller.registerNewAppraisalEntity(_nameDouble, _publicationTypeIds, _genreIds);

        // Assert
        assertEquals(_appraisalEntityDouble, result);

    }

    @Test
    void shouldThrowWhenAddingDuplicateAppraisalEntity() {
        // Arrange
        when(_appraisalEntityFactoryDouble.createAppraisalEntity(
                _nameDouble, _publicationTypeIds, _genreIds))
                .thenReturn(_appraisalEntityDouble);

        when(_appraisalEntityDouble.identity()).thenReturn(_appraisalEntityIdDouble);
        when(_iAppraisalEntityRepoDouble.containsOfIdentity(_appraisalEntityIdDouble)).thenReturn(true);

        // Act + Assert
        assertThrows(IllegalStateException.class, () ->
                _controller.registerNewAppraisalEntity(_nameDouble, _publicationTypeIds, _genreIds));

    }

}
