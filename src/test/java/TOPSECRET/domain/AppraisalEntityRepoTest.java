package TOPSECRET.domain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AppraisalEntityRepoTest {

    private AppraisalEntity _entity;
    private AppraisalEntityFactory _factoryDouble;
    private List<Genre> _genres;
    private Genre _genreDouble;
    private List<PublicationType> _publicationTypes;
    private PublicationType _publicationTypeDouble;
    private Name _nameDouble;

    @BeforeEach
    void setUp() {

        _genreDouble = mock(Genre.class);
        when(_genreDouble.getGenre()).thenReturn("Self-Help");

        _genres = new ArrayList<>();
        _genres.add(_genreDouble);

        _publicationTypeDouble = mock(PublicationType.class);
        when(_publicationTypeDouble.getPublicationType()).thenReturn("Book");

        _publicationTypes = new ArrayList<>();
        _publicationTypes.add(_publicationTypeDouble);

        _nameDouble = mock(Name.class);
        when(_nameDouble.get_Name()).thenReturn("Livraria Alfarrabista");

        _entity = new AppraisalEntity(_nameDouble, _publicationTypes, _genres);

        _factoryDouble = mock(AppraisalEntityFactory.class);
        when(_factoryDouble.createAppraisalEntity(_nameDouble, _publicationTypes, _genres)).thenReturn(_entity);

    }

    @Test
    void should_add_new_appraisalEntity() {

        // SUT
        AppraisalEntityRepo repo = new AppraisalEntityRepo(_factoryDouble);

        // act
        AppraisalEntity entity = repo.registerNewAppraisalEntity(_nameDouble, _publicationTypes, _genres);

        // assert
        assertEquals(_nameDouble, entity.getName());
        assertEquals(_publicationTypes, entity.getPublicationTypes());
        assertEquals(_genres, entity.getGenres());
    }

    @Test
    void should_throw_exception_when_duplicate_name() {

        //SUT
        AppraisalEntityRepo repo = new AppraisalEntityRepo(_factoryDouble);

        // act
        repo.registerNewAppraisalEntity(_nameDouble, _publicationTypes, _genres);

        // assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.registerNewAppraisalEntity(_nameDouble, _publicationTypes, _genres));
    }

    @Test
    void should_avoid_external_lists_alterations() {

        // SUT
        AppraisalEntityRepo repo = new AppraisalEntityRepo(_factoryDouble);

        // act
        AppraisalEntity entity = repo.registerNewAppraisalEntity(_nameDouble, _publicationTypes, _genres);

        _genres.clear();
        _publicationTypes.clear();

        assertFalse(entity.getGenres().isEmpty());
        assertFalse(entity.getPublicationTypes().isEmpty());
    }
}
