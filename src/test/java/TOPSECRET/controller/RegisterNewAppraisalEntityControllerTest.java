package TOPSECRET.controller;

import TOPSECRET.domain.appraisalEntity.AppraisalEntity;
import TOPSECRET.domain.publicationtype.PublicationType;
import TOPSECRET.domain.repository.IAppraisalEntityRepo;
import TOPSECRET.domain.user.User;
import TOPSECRET.domain.genre.Genre;
import TOPSECRET.domain.repository.IGenreRepo;
import TOPSECRET.domain.repository.IPublicationTypeRepo;
import TOPSECRET.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterNewAppraisalEntityControllerTest {
    private AppraisalEntity _appraisalEntityDouble;
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
    private User _userDouble;
    private UserId _adminIdDouble;

    @BeforeEach
    void setUp() throws InstantiationException{
        _nameDouble = mock(Name.class);
        _userDouble = mock(User.class);
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
        when(_iPublicationTypeRepoDouble.getAll()).thenReturn(_publicationTypes);

        _appraisalEntityDouble = mock(AppraisalEntity.class);

        _iAppraisalEntityRepoDouble = mock (IAppraisalEntityRepo.class);
        when(_iAppraisalEntityRepoDouble.addAppraisalEntity(_nameDouble, _publicationTypeIds, _genreIds)).thenReturn(_appraisalEntityDouble);

    }

    @Test
    void registerNewAppraisalEntityControllerTest(){

        // SUT
        RegisterNewAppraisalEntityController  controller = new RegisterNewAppraisalEntityController(_iAppraisalEntityRepoDouble, _iPublicationTypeRepoDouble, _iGenreRepoDouble, _adminIdDouble);

    }

    @Test
    void shouldGetPublicationTypesFromRepo() {

        // SUT
        RegisterNewAppraisalEntityController  controller = new RegisterNewAppraisalEntityController(_iAppraisalEntityRepoDouble, _iPublicationTypeRepoDouble, _iGenreRepoDouble, _adminIdDouble);

        // act
        List types = controller.getPublicationTypes();

        // assert
        assertEquals(_publicationTypes, types);
        verify(_iPublicationTypeRepoDouble).getAll();

    }

    @Test
    void shouldGetGenresFromRepo() {

        // SUT
        RegisterNewAppraisalEntityController  controller = new RegisterNewAppraisalEntityController(_iAppraisalEntityRepoDouble, _iPublicationTypeRepoDouble, _iGenreRepoDouble, _adminIdDouble);

        // act
        Iterable <Genre> genres = controller.getGenres();

        // assert
        assertNotNull(genres);
        assertTrue(genres.iterator().hasNext());
        verify(_iGenreRepoDouble).findAll();

    }

    @Test
    void shouldSuccessfullyCallAppraisalEntityCreationMethodIfUserIsAdmin() {

        // arrange
        User adminDouble = mock (User.class);
        when(adminDouble.hasRole(Role.ADMIN)).thenReturn(true);
        when(_iAppraisalEntityRepoDouble.addAppraisalEntity(
                _nameDouble, _publicationTypeIds, _genreIds))
                .thenReturn(_appraisalEntityDouble);

        // SUT
        RegisterNewAppraisalEntityController  controller = new RegisterNewAppraisalEntityController(_iAppraisalEntityRepoDouble, _iPublicationTypeRepoDouble, _iGenreRepoDouble, _adminIdDouble);

        // act
        AppraisalEntity result = controller.registerNewAppraisalEntity(_nameDouble, _publicationTypeIds, _genreIds, adminDouble);

        // assert
        assertEquals(_appraisalEntityDouble, result);

    }

    @Test
    void shouldThrowExceptionWhenUserIsNotAdmin() {

        // arrange
        when(_userDouble.hasRole(Role.ADMIN)).thenReturn(false);

        // SUT
        RegisterNewAppraisalEntityController  controller = new RegisterNewAppraisalEntityController(_iAppraisalEntityRepoDouble, _iPublicationTypeRepoDouble, _iGenreRepoDouble, _adminIdDouble);

        //Act
        SecurityException exception = assertThrows(SecurityException.class, () -> controller.registerNewAppraisalEntity(_nameDouble, _publicationTypeIds, _genreIds, _userDouble));

        //Assert
        assertEquals("User is not authorized to register appraisal entities", exception.getMessage());

    }

}
