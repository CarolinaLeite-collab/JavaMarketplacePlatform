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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("jpa")
class RegisterNewAppraisalEntityControllerTest {

    @Mock
    IAppraisalEntityRepo _iAppraisalEntityRepoDouble;

    @Mock
    AppraisalEntityFactory _appraisalEntityFactoryDouble;

    @Mock
    IPublicationTypeRepo _iPublicationTypeRepoDouble;

    @Mock
    IGenreRepo _iGenreRepoDouble;

    @InjectMocks
    RegisterNewAppraisalEntityController _controller;

    private Name _nameDouble;
    private List<Genre> _genresIdDouble;
    private List<GenreId> _genreIdsDouble;
    private GenreId _genreIdDouble;
    private List<PublicationTypeId> _publicationTypeIds;
    private PublicationTypeId _publicationTypeIdDouble;
    private AppraisalEntityId _appraisalEntityIdDouble;
    private AppraisalEntity _appraisalEntityDouble;

    @BeforeEach
    void setUp() throws InstantiationException {

        _nameDouble = mock(Name.class);

        // Genres
        _genreIdDouble = mock(GenreId.class);
        _genreIdsDouble = new ArrayList<>();
        _genreIdsDouble.add(_genreIdDouble);

        // Publication Types
        _publicationTypeIdDouble = mock(PublicationTypeId.class);
        _publicationTypeIds = new ArrayList<>();
        _publicationTypeIds.add(_publicationTypeIdDouble);

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
        when(_iPublicationTypeRepoDouble.findAllKeys()).thenReturn(_publicationTypeIds);

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
        when(_iGenreRepoDouble.findAllKeys()).thenReturn(_genreIdsDouble);

        // Act
        Iterable<GenreId> genres = _controller.getGenresId();

        // Assert
        assertEquals(_genreIdsDouble, genres);
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