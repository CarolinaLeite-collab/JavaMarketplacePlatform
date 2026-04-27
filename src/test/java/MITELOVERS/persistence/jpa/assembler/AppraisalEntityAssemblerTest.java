package persistence.jpa.assembler;

import MITELOVERS.domain.appraisalentity.AppraisalEntity;
import MITELOVERS.domain.appraisalentity.AppraisalEntityFactory;
import MITELOVERS.domain.valueobject.AppraisalEntityId;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.Name;
import MITELOVERS.domain.valueobject.PublicationTypeId;
import MITELOVERS.persistence.jpa.assembler.AppraisalEntityAssembler;
import MITELOVERS.persistence.jpa.datamodel.AppraisalEntityDataModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AppraisalEntityAssemblerTest {

    private AppraisalEntityFactory _appraisalEntityFactoryDouble;
    private AppraisalEntityId _appraisalEntityIdDouble;

    @BeforeEach
    void setUp() {

        _appraisalEntityFactoryDouble = mock(AppraisalEntityFactory.class);
        _appraisalEntityIdDouble = mock(AppraisalEntityId.class);

    }

    @Test
    void testAContructor() {

        // Act
        new AppraisalEntityAssembler(_appraisalEntityFactoryDouble);

    }

    @Test
    void testDomain2DMShouldReturnCorrectDataModelWhenFedWithAAppraisalEntity() {

        // Arrange
        AppraisalEntity appraisalEntityDouble = mock(AppraisalEntity.class);
        when(appraisalEntityDouble.identity()).thenReturn(_appraisalEntityIdDouble);
        when(_appraisalEntityIdDouble.toString()).thenReturn("id");

        PublicationTypeId publicationTypeId1Double = mock(PublicationTypeId.class);
        when(publicationTypeId1Double.toString()).thenReturn("ptId1Double");
        PublicationTypeId publicationTypeId2Double = mock(PublicationTypeId.class);
        when(publicationTypeId2Double.toString()).thenReturn("ptId2Double");

        GenreId genreId1Double = mock(GenreId.class);
        when(genreId1Double.toString()).thenReturn("genreId1Double");
        GenreId genreId2Double = mock(GenreId.class);
        when(genreId2Double.toString()).thenReturn("genreId2Double");

        Name nameDouble = mock(Name.class);
        when(nameDouble.getName()).thenReturn("nameDouble");

        when(appraisalEntityDouble.getName()).thenReturn(nameDouble);
        when(appraisalEntityDouble.getGenreIds()).thenReturn(List.of(genreId1Double,genreId2Double));
        when(appraisalEntityDouble.getPublicationTypeIds()).thenReturn(List.of(publicationTypeId1Double,publicationTypeId2Double));

        // SUT
        AppraisalEntityAssembler assembler = new AppraisalEntityAssembler(_appraisalEntityFactoryDouble);

        // Act
        AppraisalEntityDataModel result = assembler.domain2DM(appraisalEntityDouble);

        // Assert
        assertEquals("id", result.getId());
        assertEquals("nameDouble", result.getName());
        assertEquals(List.of("genreId1Double", "genreId2Double"), result.getGenresIds());
        assertEquals(List.of("ptId1Double", "ptId2Double"), result.getPublicationTypeIds());

    }

    @Test
    void testDM2DomainShouldReturnCorrectAppraisalEntityObjectWhenFedWithAAppraisalEntityDataModel() {

        // Arrange
        AppraisalEntityDataModel appraisalEntityDataModelDouble = mock(AppraisalEntityDataModel.class);

        when(appraisalEntityDataModelDouble.getId()).thenReturn("id");
        when(appraisalEntityDataModelDouble.getName()).thenReturn("nameDouble");
        when(appraisalEntityDataModelDouble.getGenresIds()).thenReturn(List.of("genreId1Double", "genreId2Double"));
        when(appraisalEntityDataModelDouble.getPublicationTypeIds()).thenReturn(List.of("ptId1Double", "ptId2Double"));

        AppraisalEntity appraisalEntityDouble = mock(AppraisalEntity.class);

        AppraisalEntityId appraisalEntityIdDouble = mock(AppraisalEntityId.class);
        when(appraisalEntityIdDouble.toString()).thenReturn("id");

        Name nameDouble = mock(Name.class);
        when(nameDouble.getName()).thenReturn("nameDouble");

        GenreId genreId1Double = mock(GenreId.class);
        when(genreId1Double.toString()).thenReturn("genreId1Double");

        GenreId genreId2Double = mock(GenreId.class);
        when(genreId2Double.toString()).thenReturn("genreId2Double");

        PublicationTypeId publicationTypeId1Double = mock(PublicationTypeId.class);
        when(publicationTypeId1Double.toString()).thenReturn("ptId1Double");

        PublicationTypeId publicationTypeId2Double = mock(PublicationTypeId.class);
        when(publicationTypeId2Double.toString()).thenReturn("ptId2Double");

        when(appraisalEntityDouble.identity()).thenReturn(appraisalEntityIdDouble);
        when(appraisalEntityDouble.getName()).thenReturn(nameDouble);
        when(appraisalEntityDouble.getGenreIds()).thenReturn(List.of(genreId1Double, genreId2Double));
        when(appraisalEntityDouble.getPublicationTypeIds()).thenReturn(List.of(publicationTypeId1Double, publicationTypeId2Double));

        when(_appraisalEntityFactoryDouble.createAppraisalEntity(any(AppraisalEntityId.class), any(Name.class), anyList(), anyList()))
                .thenReturn(appraisalEntityDouble);

        // SUT
        AppraisalEntityAssembler assembler = new AppraisalEntityAssembler(_appraisalEntityFactoryDouble);

        // Act
        AppraisalEntity result = assembler.DM2domain(appraisalEntityDataModelDouble);

        List<String> expectedGenreIds = new ArrayList<>();
        List<GenreId> genreIds = result.getGenreIds();

        for (GenreId genreId : genreIds) {
            expectedGenreIds.add(genreId.toString());
        }

        List<String> expectedPublicationTypeIds = new ArrayList<>();
        List<PublicationTypeId> publicationTypeIds = result.getPublicationTypeIds();

        for (PublicationTypeId publicationTypeId : publicationTypeIds) {
            expectedPublicationTypeIds.add(publicationTypeId.toString());
        }

        // Assert
        assertEquals("id", result.identity().toString());
        assertEquals("nameDouble", result.getName().getName());
        assertEquals(List.of("genreId1Double", "genreId2Double"), expectedGenreIds);
        assertEquals(List.of("ptId1Double", "ptId2Double"), expectedPublicationTypeIds);
    }
}