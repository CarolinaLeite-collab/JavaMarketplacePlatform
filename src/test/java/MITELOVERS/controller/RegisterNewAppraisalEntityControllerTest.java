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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@WebMvcTest(RegisterNewAppraisalEntityController.class)
@ActiveProfiles("jpa")
class RegisterNewAppraisalEntityControllerTest {

    @MockBean
    IAppraisalEntityRepo _iAppraisalEntityRepoDouble;

    @MockBean
    AppraisalEntityFactory _appraisalEntityFactoryDouble;

    @MockBean
    IPublicationTypeRepo _iPublicationTypeRepoDouble;

    @MockBean
    IGenreRepo _iGenreRepoDouble;

    @Autowired
    RegisterNewAppraisalEntityController _controller;

    private Name _nameDouble;
    private List<Genre> _genresIdDouble;
    private Genre _genreDouble;
    private List<GenreId> _genreIdsDouble;
    private GenreId _genreIdDouble;
    private List<PublicationType> _publicationTypesDouble;
    private List<PublicationTypeId> _publicationTypeIds;
    private PublicationTypeId _publicationTypeIdDouble;
    private AppraisalEntityId _appraisalEntityIdDouble;
    private AppraisalEntity _appraisalEntityDouble;
    private PublicationType _publicationTypeDouble;

    @BeforeEach
    void setUp() throws InstantiationException {

        MockitoAnnotations.openMocks(this);

        _nameDouble = mock(Name.class);

        // Genres
        _genreDouble = mock(Genre.class);
        _genreIdDouble = mock(GenreId.class);
        _genreIdsDouble = new ArrayList<>();
        _genreIdsDouble.add(_genreIdDouble);
        when(_iGenreRepoDouble.findAllKeys()).thenReturn(_genreIdsDouble);

        // Publication Types
        _publicationTypeDouble = mock(PublicationType.class);
        _publicationTypeIdDouble = mock(PublicationTypeId.class);
        _publicationTypeIds = new ArrayList<>();
        _publicationTypeIds.add(_publicationTypeIdDouble);
        when(_iPublicationTypeRepoDouble.findAllKeys()).thenReturn(_publicationTypeIds);

        // Appraisal Entity
        _appraisalEntityDouble = mock(AppraisalEntity.class);
        _appraisalEntityIdDouble = mock(AppraisalEntityId.class);
    }

    @Test
    void registerNewAppraisalEntityControllerTest(){
        // SUT
        _controller =
                new RegisterNewAppraisalEntityController(_iAppraisalEntityRepoDouble,
                        _iPublicationTypeRepoDouble, _appraisalEntityFactoryDouble, _iGenreRepoDouble);

    }

    @Test
    void shouldGetPublicationTypeIdsFromRepo() {
        // Arrange
        when(_publicationTypeDouble.identity()).thenReturn(_publicationTypeIdDouble);

        // Act
        Iterable<PublicationTypeId> types = _controller.getPublicationTypesId();

        // Assert
        assertEquals(_publicationTypeIds, types);
    }

    @Test
    void shouldGetEmptyPublicationTypeIdsWhenRepoIsEmpty() {
        // Arrange
        when(_iPublicationTypeRepoDouble.findAllKeys()).thenReturn(new ArrayList<>());

        // Act
        Iterable<PublicationTypeId> types = _controller.getPublicationTypesId();

        // Assert
        assertFalse(types.iterator().hasNext());
    }

    @Test
    void shouldGetGenreIdsFromRepo() {
        // Arrange
        when(_genreDouble.identity()).thenReturn(_genreIdDouble);

        // Act
        Iterable<GenreId> genres = _controller.getGenresId();

        // Assert
        assertNotNull(genres);
        assertTrue(genres.iterator().hasNext());
    }

    @Test
    void shouldGetEmptyGenreIdsWhenRepoIsEmpty() {
        // Arrange
        when(_iGenreRepoDouble.findAllKeys()).thenReturn(new ArrayList<>());

        // Act
        Iterable<GenreId> genres = _controller.getGenresId();

        // Assert
        assertFalse(genres.iterator().hasNext());
    }

    @Test
    void shouldSuccessfullyRegisterAppraisalEntity() {
        // Arrange
        when(_appraisalEntityFactoryDouble.createAppraisalEntity(_nameDouble, _publicationTypeIds, _genreIdsDouble))
                .thenReturn(_appraisalEntityDouble);
        when(_appraisalEntityDouble.identity()).thenReturn(_appraisalEntityIdDouble);
        when(_iAppraisalEntityRepoDouble.containsOfIdentity(_appraisalEntityIdDouble)).thenReturn(false);
        when(_iAppraisalEntityRepoDouble.save(_appraisalEntityDouble)).thenReturn(_appraisalEntityDouble);

        // Act
        AppraisalEntity result = _controller.registerNewAppraisalEntity(_nameDouble, _publicationTypeIds, _genreIdsDouble);

        // Assert
        assertEquals(_appraisalEntityDouble, result);

    }

    @Test
    void shouldThrowWhenAddingDuplicateAppraisalEntity() {
        // Arrange
        when(_appraisalEntityFactoryDouble.createAppraisalEntity(
                _nameDouble, _publicationTypeIds, _genreIdsDouble))
                .thenReturn(_appraisalEntityDouble);

        when(_appraisalEntityDouble.identity()).thenReturn(_appraisalEntityIdDouble);
        when(_iAppraisalEntityRepoDouble.containsOfIdentity(_appraisalEntityIdDouble)).thenReturn(true);

        // Act + Assert
        assertThrows(IllegalStateException.class, () ->
                _controller.registerNewAppraisalEntity(_nameDouble, _publicationTypeIds, _genreIdsDouble));

    }

}