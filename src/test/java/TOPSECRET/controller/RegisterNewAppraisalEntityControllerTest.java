package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.valueobject.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class RegisterNewAppraisalEntityControllerTest {
    private AppraisalEntity _appraisalEntityDouble;
    private Name _nameDouble;
    private IGenreRepo _iGenreRepoDouble;
    private List<Genre> _genres;
    private Genre _genreDouble;
    private IPublicationTypeRepo _iTypeRepo;
    private List<PublicationType> _publicationTypes;
    private PublicationType _publicationTypeDouble;
    private IAppraisalEntityRepo _entityRepoDouble;

    @BeforeEach
    void setUp() throws InstantiationException{
        _nameDouble = mock(Name.class);

        _genreDouble = mock(Genre.class);
        _genres = new ArrayList<>();
        _genres.add(_genreDouble);

        _iGenreRepoDouble = mock(IGenreRepo.class);
        when(_iGenreRepoDouble.getListOfOfficialGenres()).thenReturn(_genres);

        _publicationTypeDouble = mock(PublicationType.class);
        _publicationTypes = new ArrayList<>();
        _publicationTypes.add(_publicationTypeDouble);

        _iTypeRepo = mock(IPublicationTypeRepo.class);
        when(_iTypeRepo.getAll()).thenReturn(_publicationTypes);

        _appraisalEntityDouble = mock(AppraisalEntity.class);

        _entityRepoDouble = mock (IAppraisalEntityRepo.class);
        when(_entityRepoDouble.registerNewAppraisalEntity(_nameDouble, _publicationTypes, _genres)).thenReturn(_appraisalEntityDouble);

    }

    @Test
    void registerNewAppraisalEntityControllerTest(){

        // SUT
        RegisterNewAppraisalEntityController  controller = new RegisterNewAppraisalEntityController(_entityRepoDouble, _iTypeRepo, _iGenreRepoDouble);

        assertNotNull(controller);
    }

    @Test
    void shouldGetPublicationTypesFromRepo() {

        // SUT
        RegisterNewAppraisalEntityController  controller = new RegisterNewAppraisalEntityController(_entityRepoDouble, _iTypeRepo, _iGenreRepoDouble);

        // act
        List types = controller.getPublicationTypes();

        // assert
        assertEquals(_publicationTypes, types);
        verify(_iTypeRepo).getAll();
    }

    @Test
    void shouldGetGenresFromRepo() {

        // SUT
        RegisterNewAppraisalEntityController  controller = new RegisterNewAppraisalEntityController(_entityRepoDouble, _iTypeRepo, _iGenreRepoDouble);

        // act
        List genres = controller.getGenres();

        // assert
        assertEquals(_genres, genres);
        verify(_iGenreRepoDouble).getListOfOfficialGenres();
    }

    @Test
    void shouldSuccessfullyCallAppraisalEntityCreationMethod() {

        // SUT
        RegisterNewAppraisalEntityController  controller = new RegisterNewAppraisalEntityController(_entityRepoDouble, _iTypeRepo, _iGenreRepoDouble);

        // act
        AppraisalEntity result = controller.registerNewAppraisalEntity(_nameDouble, _publicationTypes, _genres);

        // assert
        assertEquals(_appraisalEntityDouble, result);
        verify(_entityRepoDouble).registerNewAppraisalEntity(_nameDouble, _publicationTypes, _genres);
    }
}
