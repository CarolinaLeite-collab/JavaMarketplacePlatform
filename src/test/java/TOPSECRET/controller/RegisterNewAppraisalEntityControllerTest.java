package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.AppraisalEntity.AppraisalEntity;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.valueobject.Name;
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
    private IPublicationTypeRepo _iPublicationTypeRepoDouble;
    private List<PublicationType> _publicationTypes;
    private PublicationType _publicationTypeDouble;
    private IAppraisalEntityRepo _iAppraisalEntityRepoDouble;
    private User _userDouble;

    @BeforeEach
    void setUp() throws InstantiationException{
        _nameDouble = mock(Name.class);
        _userDouble = mock(User.class);

        _genreDouble = mock(Genre.class);
        _genres = new ArrayList<>();
        _genres.add(_genreDouble);

        _iGenreRepoDouble = mock(IGenreRepo.class);
        when(_iGenreRepoDouble.getListOfOfficialGenres()).thenReturn(_genres);

        _publicationTypeDouble = mock(PublicationType.class);
        _publicationTypes = new ArrayList<>();
        _publicationTypes.add(_publicationTypeDouble);

        _iPublicationTypeRepoDouble = mock(IPublicationTypeRepo.class);
        when(_iPublicationTypeRepoDouble.getAll()).thenReturn(_publicationTypes);

        _appraisalEntityDouble = mock(AppraisalEntity.class);

        _iAppraisalEntityRepoDouble = mock (IAppraisalEntityRepo.class);
        when(_iAppraisalEntityRepoDouble.registerNewAppraisalEntity(_nameDouble, _publicationTypes, _genres)).thenReturn(_appraisalEntityDouble);

    }

    @Test
    void registerNewAppraisalEntityControllerTest(){

        // SUT
        RegisterNewAppraisalEntityController  controller = new RegisterNewAppraisalEntityController(_iAppraisalEntityRepoDouble, _iPublicationTypeRepoDouble, _iGenreRepoDouble);

        assertNotNull(controller);
    }

    @Test
    void shouldGetPublicationTypesFromRepo() {

        // SUT
        RegisterNewAppraisalEntityController  controller = new RegisterNewAppraisalEntityController(_iAppraisalEntityRepoDouble, _iPublicationTypeRepoDouble, _iGenreRepoDouble);

        // act
        List types = controller.getPublicationTypes();

        // assert
        assertEquals(_publicationTypes, types);
        verify(_iPublicationTypeRepoDouble).getAll();
    }

    @Test
    void shouldGetGenresFromRepo() {

        // SUT
        RegisterNewAppraisalEntityController  controller = new RegisterNewAppraisalEntityController(_iAppraisalEntityRepoDouble, _iPublicationTypeRepoDouble, _iGenreRepoDouble);

        // act
        List genres = controller.getGenres();

        // assert
        assertEquals(_genres, genres);
        verify(_iGenreRepoDouble).getListOfOfficialGenres();
    }

    @Test
    void shouldSuccessfullyCallAppraisalEntityCreationMethodIfUserIsAdmin() {

        // arrange
        User adminDouble = mock (User.class);
        when(adminDouble.hasRole(Role.ADMIN)).thenReturn(true);
        when(_iAppraisalEntityRepoDouble.registerNewAppraisalEntity(
                _nameDouble, _publicationTypes, _genres))
                .thenReturn(_appraisalEntityDouble);

        // SUT
        RegisterNewAppraisalEntityController  controller = new RegisterNewAppraisalEntityController(_iAppraisalEntityRepoDouble, _iPublicationTypeRepoDouble, _iGenreRepoDouble);

        // act
        AppraisalEntity result = controller.registerNewAppraisalEntity(_nameDouble, _publicationTypes, _genres, adminDouble);

        // assert
        assertEquals(_appraisalEntityDouble, result);
    }
    @Test
    void shouldThrowExceptionWhenUserIsNotAdmin() {

        // arrange
        when(_userDouble.hasRole(Role.ADMIN)).thenReturn(false);

        // SUT
        RegisterNewAppraisalEntityController  controller = new RegisterNewAppraisalEntityController(_iAppraisalEntityRepoDouble, _iPublicationTypeRepoDouble, _iGenreRepoDouble);

        //Act
        SecurityException exception = assertThrows(SecurityException.class, () -> controller.registerNewAppraisalEntity(_nameDouble, _publicationTypes, _genres, _userDouble));

        //Assert
        assertEquals("User is not authorized to register appraisal entities", exception.getMessage());
    }
}
