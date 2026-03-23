package TOPSECRET.controller;

import TOPSECRET.ddd.ValueObject;
import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterNewAppraisalEntityControllerTest {
    private AppraisalEntity _appraisalEntityDouble;
    private ValueObject.Name _nameDouble;
    private GenreRepo _genreRepoDouble;
    private List<Genre> _genres;
    private Genre _genreDouble;
    private PublicationTypeRepo _typeRepoDouble;
    private List<PublicationType> _publicationTypes;
    private PublicationType _publicationTypeDouble;
    private AppraisalEntityRepo _entityRepoDouble;

    @BeforeEach
    void setUp() throws InstantiationException{
        _nameDouble = mock(ValueObject.Name.class);

        _genreDouble = mock(Genre.class);
        _genres = new ArrayList<>();
        _genres.add(_genreDouble);

        _genreRepoDouble = mock(GenreRepo.class);
        when(_genreRepoDouble.getListOfOfficialGenres()).thenReturn(_genres);

        _publicationTypeDouble = mock(PublicationType.class);
        _publicationTypes = new ArrayList<>();
        _publicationTypes.add(_publicationTypeDouble);

        _typeRepoDouble = mock(PublicationTypeRepo.class);
        when(_typeRepoDouble.getAll()).thenReturn(_publicationTypes);

        _appraisalEntityDouble = mock(AppraisalEntity.class);

        _entityRepoDouble = mock (AppraisalEntityRepo.class);
        when(_entityRepoDouble.registerNewAppraisalEntity(_nameDouble, _publicationTypes, _genres)).thenReturn(_appraisalEntityDouble);

    }

    @Test
    void registerNewAppraisalEntityControllerTest(){

        // SUT
        RegisterNewAppraisalEntityController  controller = new RegisterNewAppraisalEntityController(_entityRepoDouble, _typeRepoDouble, _genreRepoDouble);

        assertNotNull(controller);
    }

    @Test
    void shouldGetPublicationTypesFromRepo() {

        // SUT
        RegisterNewAppraisalEntityController  controller = new RegisterNewAppraisalEntityController(_entityRepoDouble, _typeRepoDouble, _genreRepoDouble);

        // act
        List types = controller.getPublicationTypes();

        // assert
        assertEquals(_publicationTypes, types);
        verify(_typeRepoDouble).getAll();
    }

    @Test
    void shouldGetGenresFromRepo() {

        // SUT
        RegisterNewAppraisalEntityController  controller = new RegisterNewAppraisalEntityController(_entityRepoDouble, _typeRepoDouble, _genreRepoDouble);

        // act
        List genres = controller.getGenres();

        // assert
        assertEquals(_genres, genres);
        verify(_genreRepoDouble).getListOfOfficialGenres();
    }

    @Test
    void shouldSuccessfullyCallAppraisalEntityCreationMethod() {

        // SUT
        RegisterNewAppraisalEntityController  controller = new RegisterNewAppraisalEntityController(_entityRepoDouble, _typeRepoDouble, _genreRepoDouble);

        // act
        AppraisalEntity result = controller.registerNewAppraisalEntity(_nameDouble, _publicationTypes, _genres);

        // assert
        assertEquals(_appraisalEntityDouble, result);
        verify(_entityRepoDouble).registerNewAppraisalEntity(_nameDouble, _publicationTypes, _genres);
    }
}
