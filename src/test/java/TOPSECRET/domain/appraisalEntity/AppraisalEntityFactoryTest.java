package TOPSECRET.domain.appraisalEntity;

import TOPSECRET.domain.publicationtype.PublicationType;
import TOPSECRET.domain.genre.Genre;
import TOPSECRET.domain.valueobject.Name;
import TOPSECRET.domain.valueobject.PublicationTypeId;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class AppraisalEntityFactoryTest {
    @Test
    void shouldCreateAppraisalEntity(){

        //Arrange
        Name _nameDouble = mock(Name.class);
        when(_nameDouble.get_Name()).thenReturn("Helpingz TM");

        PublicationType _publicationTypeDouble = mock(PublicationType.class);
        PublicationTypeId _publicationTypeIdDouble = mock(PublicationTypeId.class);
        when(_publicationTypeIdDouble.toString()).thenReturn("Book");
        when(_publicationTypeDouble.identity()).thenReturn(_publicationTypeIdDouble);

        List<PublicationType> _publicationTypes = new ArrayList<>();
        _publicationTypes.add(_publicationTypeDouble);

        Genre _genreDouble = mock(Genre.class);
        when(_genreDouble.getGenre()).thenReturn("Self-Help");

        List<Genre> _genres = new ArrayList<>();
        _genres.add(_genreDouble);

        //SUT
        AppraisalEntityFactory factory = new AppraisalEntityFactory();

        try (MockedConstruction<AppraisalEntity> mockedConstruction = mockConstruction(AppraisalEntity.class)){

            //Act
            AppraisalEntity appraisalEntityResult = factory.createAppraisalEntity(_nameDouble, _publicationTypes, _genres);

            //Assert
            assertNotNull(appraisalEntityResult);
            assertEquals(1, mockedConstruction.constructed().size());

        }

    }

}
