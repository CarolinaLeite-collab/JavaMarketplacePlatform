package TOPSECRET.domain.appraisalEntity;

import TOPSECRET.domain.valueobject.AppraisalEntityId;
import TOPSECRET.domain.valueobject.GenreId;
import TOPSECRET.domain.valueobject.Name;
import TOPSECRET.domain.valueobject.PublicationTypeId;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AppraisalEntityFactoryTest {


    @Test
    void testConstructor() {

        //SUT
        AppraisalEntityFactory factory = new AppraisalEntityFactory();

    }

    @Test
    void shouldCreateAppraisalEntity(){

        //Arrange
        Name _nameDouble = mock(Name.class);

        PublicationTypeId _publicationTypeIdDouble = mock(PublicationTypeId.class);
        List<PublicationTypeId> _publicationTypeIds = new ArrayList<>();
        _publicationTypeIds.add(_publicationTypeIdDouble);

        GenreId _genreIdDouble = mock(GenreId.class);
        List<GenreId> _genreIds = new ArrayList<>();
        _genreIds.add(_genreIdDouble);

        //SUT
        AppraisalEntityFactory factory = new AppraisalEntityFactory();

        try (MockedConstruction<AppraisalEntity> mockedConstruction = mockConstruction(AppraisalEntity.class)){

            //Act
            AppraisalEntity appraisalEntityResult = factory.createAppraisalEntity(_nameDouble, _publicationTypeIds, _genreIds);

            //Assert
            assertNotNull(appraisalEntityResult);
            assertEquals(1, mockedConstruction.constructed().size());

        }

    }

    @Test
    void shouldCreateAppraisalEntityWithId() {

        //Arrange
        AppraisalEntityId appraisalEntityIdDouble = mock(AppraisalEntityId.class);
        Name nameDouble = mock(Name.class);

        PublicationTypeId pubTypeDouble = mock(PublicationTypeId.class);
        List<PublicationTypeId> publicationTypeIds = new ArrayList<>();
        publicationTypeIds.add(pubTypeDouble);

        GenreId genreDouble = mock(GenreId.class);
        List<GenreId> genreIds = new ArrayList<>();
        genreIds.add(genreDouble);

        //SUT
        AppraisalEntityFactory factory = new AppraisalEntityFactory();

        try (MockedConstruction<AppraisalEntity> mockedConstruction = mockConstruction(AppraisalEntity.class)) {

            //Act
            AppraisalEntity result = factory.createAppraisalEntity(
                    appraisalEntityIdDouble, nameDouble, publicationTypeIds, genreIds);

            //Assert
            assertNotNull(result);
            assertEquals(1, mockedConstruction.constructed().size());

        }

    }

}
