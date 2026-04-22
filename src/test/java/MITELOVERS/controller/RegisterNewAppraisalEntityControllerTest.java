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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterNewAppraisalEntityControllerTest {

    private AppraisalEntity _appraisalEntityDouble;
    private AppraisalEntityFactory _appraisalEntityFactoryDouble;
    private Name _nameDouble;
    private IGenreRepo _iGenreRepoDouble;
    private List<Genre> _genres;
    private Genre _genreDouble;
    private List<GenreId> _genreIds;
    private GenreId _genreIdDouble;
    private IPublicationTypeRepo _iPublicationTypeRepoDouble;
    private List<PublicationType> _publicationTypes;
    private PublicationType _publicationTypeDouble;
    private List<PublicationTypeId> _publicationTypeIds;
    private PublicationTypeId _publicationTypeIdDouble;
    private IAppraisalEntityRepo _iAppraisalEntityRepoDouble;
    private AppraisalEntityId _appraisalEntityIdDouble;
    private UserId _adminIdDouble;

    @BeforeEach
    void setUp() throws InstantiationException{

        _appraisalEntityFactoryDouble = mock(AppraisalEntityFactory.class);

        _nameDouble = mock(Name.class);
        _adminIdDouble = mock (UserId.class);

        _genreDouble = mock(Genre.class);
        _genres = new ArrayList<>();
        _genres.add(_genreDouble);

        _iGenreRepoDouble = mock(IGenreRepo.class);
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

        _iPublicationTypeRepoDouble = mock(IPublicationTypeRepo.class);
        when(_iPublicationTypeRepoDouble.findAll()).thenReturn(_publicationTypes);

        _appraisalEntityDouble = mock(AppraisalEntity.class);

        _iAppraisalEntityRepoDouble = mock (IAppraisalEntityRepo.class);

        _appraisalEntityIdDouble = mock(AppraisalEntityId.class);

    }

    @Test
    void registerNewAppraisalEntityControllerTest(){

        // SUT
        RegisterNewAppraisalEntityController  controller =
                new RegisterNewAppraisalEntityController(_iAppraisalEntityRepoDouble,
                        _iPublicationTypeRepoDouble, _appraisalEntityFactoryDouble, _iGenreRepoDouble, _adminIdDouble);

    }

    @Test
    void shouldGetPublicationTypesFromRepo() {

        // arrange & SUT
        RegisterNewAppraisalEntityController  controller =
                new RegisterNewAppraisalEntityController(_iAppraisalEntityRepoDouble,
                        _iPublicationTypeRepoDouble, _appraisalEntityFactoryDouble, _iGenreRepoDouble, _adminIdDouble);

        // act
        Iterable <PublicationType> types = controller.getPublicationTypes();

        // assert
        assertEquals(_publicationTypes, types);
        verify(_iPublicationTypeRepoDouble).findAll();

    }

    @Test
    void shouldGetGenresFromRepo() {

        // ararnge & SUT
        RegisterNewAppraisalEntityController  controller =
                new RegisterNewAppraisalEntityController(_iAppraisalEntityRepoDouble,
                        _iPublicationTypeRepoDouble, _appraisalEntityFactoryDouble, _iGenreRepoDouble, _adminIdDouble);

        // act
        Iterable <Genre> genres = controller.getGenres();

        // assert
        assertNotNull(genres);
        assertTrue(genres.iterator().hasNext());

    }

    @Test

    void shouldSuccessfullyRegisterAppraisalEntity() {

        // arrange
        when(_appraisalEntityFactoryDouble.createAppraisalEntity(_nameDouble, _publicationTypeIds, _genreIds))
                .thenReturn(_appraisalEntityDouble);
        when(_appraisalEntityDouble.identity()).thenReturn(_appraisalEntityIdDouble);
        when(_iAppraisalEntityRepoDouble.containsOfIdentity(_appraisalEntityIdDouble)).thenReturn(false);
        when(_iAppraisalEntityRepoDouble.save(_appraisalEntityDouble)).thenReturn(_appraisalEntityDouble);

        // SUT
        RegisterNewAppraisalEntityController controller = new RegisterNewAppraisalEntityController(
                _iAppraisalEntityRepoDouble, _iPublicationTypeRepoDouble, _appraisalEntityFactoryDouble,
                _iGenreRepoDouble, _adminIdDouble);

        // act
        AppraisalEntity result = controller.registerNewAppraisalEntity(_nameDouble, _publicationTypeIds, _genreIds);

        // assert
        assertEquals(_appraisalEntityDouble, result);

    }

    @Test
    void shouldAddAppraisalEntity() {

        // arrange
        when(_appraisalEntityDouble.getName()).thenReturn(_nameDouble);
        when(_appraisalEntityDouble.getPublicationTypeIds()).thenReturn(_publicationTypeIds);
        when(_appraisalEntityDouble.getGenreIds()).thenReturn(_genreIds);

        when(_appraisalEntityFactoryDouble
                .createAppraisalEntity(_nameDouble, _publicationTypeIds, _genreIds)).thenReturn(_appraisalEntityDouble);

        // SUT
        RegisterNewAppraisalEntityController  controller =
                new RegisterNewAppraisalEntityController(_iAppraisalEntityRepoDouble,
                        _iPublicationTypeRepoDouble, _appraisalEntityFactoryDouble, _iGenreRepoDouble, _adminIdDouble);
        when(controller.addAppraisalEntity(_nameDouble, _publicationTypeIds, _genreIds)).thenReturn(_appraisalEntityDouble);

        // act
        AppraisalEntity appraisalEntity = controller.addAppraisalEntity(_nameDouble, _publicationTypeIds, _genreIds);

        // assert
        assertEquals(_appraisalEntityDouble, appraisalEntity);

    }

    @Test
    void shouldThrowWhenAddingDuplicateAppraisalEntity() {

        // arrange
        when(_appraisalEntityFactoryDouble.createAppraisalEntity(
                _nameDouble, _publicationTypeIds, _genreIds))
                .thenReturn(_appraisalEntityDouble);

        when(_appraisalEntityDouble.identity()).thenReturn(_appraisalEntityIdDouble);
        when(_iAppraisalEntityRepoDouble.containsOfIdentity(_appraisalEntityIdDouble)).thenReturn(true);

        // SUT
        RegisterNewAppraisalEntityController controller =
                new RegisterNewAppraisalEntityController(_iAppraisalEntityRepoDouble, _iPublicationTypeRepoDouble,
                        _appraisalEntityFactoryDouble, _iGenreRepoDouble, _adminIdDouble);

        // act + assert
        assertThrows(IllegalStateException.class, () ->
                controller.addAppraisalEntity(_nameDouble, _publicationTypeIds, _genreIds));

    }

}
