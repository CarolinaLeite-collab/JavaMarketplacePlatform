package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MemoAppraisalEntityRepoTest {

    private AppraisalEntity _entityDouble;
    private AppraisalEntityFactory _factoryDouble;
    private List<Genre> _genres;
    private Genre _genreDouble;
    private List<PublicationType> _publicationTypes;
    private PublicationType _publicationTypeDouble;
    private Name _nameDouble;

    @BeforeEach
    void setUp() {

        _genreDouble = mock(Genre.class);

        _genres = new ArrayList<>();
        _genres.add(_genreDouble);

        _publicationTypeDouble = mock(PublicationType.class);

        _publicationTypes = new ArrayList<>();
        _publicationTypes.add(_publicationTypeDouble);

        _nameDouble = mock(Name.class);

        _entityDouble = mock(AppraisalEntity.class);
        when(_entityDouble.getGenres()).thenReturn(_genres);
        when(_entityDouble.getPublicationTypes()).thenReturn(_publicationTypes);
        when(_entityDouble.getName()).thenReturn(_nameDouble);

        _factoryDouble = mock(AppraisalEntityFactory.class);

    }

    @Test
    void shouldAddNewAppraisalEntity() {

        // arrange
        when(_factoryDouble.createAppraisalEntity(_nameDouble, _publicationTypes, _genres)).thenReturn(_entityDouble);

        // SUT
        MemoAppraisalEntityRepo repo = new MemoAppraisalEntityRepo(_factoryDouble);

        // act
        AppraisalEntity entity = repo.registerNewAppraisalEntity(_nameDouble, _publicationTypes, _genres);

        // assert
        assertEquals(_nameDouble, entity.getName());
        assertEquals(_publicationTypes, entity.getPublicationTypes());
        assertEquals(_genres, entity.getGenres());
    }

    @Test
    void shouldReturnFalseWhenNameIsDifferent() {

        //arrange
        Name _otherNameDouble = mock(Name.class);
        when(_otherNameDouble.get_Name()).thenReturn("DifferentName");

        AppraisalEntity _otherEntityDouble = mock(AppraisalEntity.class);
        when(_otherEntityDouble.getName()).thenReturn(_otherNameDouble);

        when(_factoryDouble.createAppraisalEntity(_nameDouble, _publicationTypes, _genres)).thenReturn(_entityDouble);

        when(_factoryDouble.createAppraisalEntity(_otherNameDouble, _publicationTypes, _genres)).thenReturn(_otherEntityDouble);

        // SUT
        MemoAppraisalEntityRepo repo = new MemoAppraisalEntityRepo(_factoryDouble);

        // act
        repo.registerNewAppraisalEntity(_nameDouble, _publicationTypes, _genres);

        AppraisalEntity entity = repo.registerNewAppraisalEntity(_otherNameDouble, _publicationTypes, _genres);

        // assert
        assertNotNull(entity);

    }

    @Test
    void shouldThrowExceptionWhenDuplicateName() throws IllegalArgumentException {

        // arrange
        when(_factoryDouble.createAppraisalEntity(_nameDouble, _publicationTypes, _genres)).thenReturn(_entityDouble);

        //SUT
        MemoAppraisalEntityRepo repo = new MemoAppraisalEntityRepo(_factoryDouble);

        // act
        repo.registerNewAppraisalEntity(_nameDouble, _publicationTypes, _genres);

        // assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.registerNewAppraisalEntity(_nameDouble, _publicationTypes, _genres));
    }

    @Test
    void shouldAvoidExternalListsAlterations() {

        // arrange
        AppraisalEntity _entityDouble1= mock(AppraisalEntity.class);

        List<Genre> genresCopy = new ArrayList<>(_genres);
        List<PublicationType> typesCopy = new ArrayList<>(_publicationTypes);

        when(_entityDouble1.getGenres()).thenReturn(genresCopy);
        when(_entityDouble1.getPublicationTypes()).thenReturn(typesCopy);
        when(_entityDouble1.getName()).thenReturn(_nameDouble);
        when(_factoryDouble.createAppraisalEntity(_nameDouble, typesCopy, genresCopy)).thenReturn(_entityDouble1);

        // SUT
        MemoAppraisalEntityRepo repo = new MemoAppraisalEntityRepo(_factoryDouble);

        // act
        AppraisalEntity entity = repo.registerNewAppraisalEntity(_nameDouble, _publicationTypes, _genres);

        _genres.clear();
        _publicationTypes.clear();

        assertFalse(entity.getGenres().isEmpty());
        assertFalse(entity.getPublicationTypes().isEmpty());
    }
}
