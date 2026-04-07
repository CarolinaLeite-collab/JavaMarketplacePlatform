package TOPSECRET.domain.appraisalEntity;

import TOPSECRET.domain.genre.Genre;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.valueobject.Name;
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
        when(_publicationTypeDouble.getPublicationType()).thenReturn("Book");

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
