package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.publishingcompany.PublishingCompany;
import MITELOVERS.domain.publishingcompany.PublishingCompanyFactory;
import MITELOVERS.domain.valueobject.PublishingCompanyId;
import MITELOVERS.persistence.jpa.datamodel.PublishingCompanyDataModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PublishingCompanyAssemblerTest {

    @Test
    void toDataModelShouldReturnDataModelWithPublishingCompanyId() {

        // Arrange
        PublishingCompany publishingCompanyDouble =
                mock(PublishingCompany.class);
        PublishingCompanyId publishingCompanyIdDouble =
                mock(PublishingCompanyId.class);
        PublishingCompanyFactory publishingCompanyFactoryDouble =
                mock(PublishingCompanyFactory.class);

        when(publishingCompanyDouble.identity()).thenReturn(publishingCompanyIdDouble);
        when(publishingCompanyIdDouble.toString()).thenReturn("PORTO EDITORA");

        // SUT
        PublishingCompanyAssembler assembler =
                new PublishingCompanyAssembler(publishingCompanyFactoryDouble);

        // Act
        PublishingCompanyDataModel result =
                assembler.toDataModel(publishingCompanyDouble);

        // Assert
        assertEquals("PORTO EDITORA", result.getPublishingCompanyId());
    }

    @Test
    void toDomainShouldReturnPublishingCompanyWithSameIdentity() {

        // Arrange
        PublishingCompanyDataModel dataModelDouble =
                mock(PublishingCompanyDataModel.class);
        PublishingCompanyFactory publishingCompanyFactoryDouble =
                mock(PublishingCompanyFactory.class);

        PublishingCompany publishingCompanyDouble =
                mock(PublishingCompany.class);
        PublishingCompanyId publishingCompanyIdDouble =
                mock(PublishingCompanyId.class);

        when(dataModelDouble.getPublishingCompanyId())
                .thenReturn("PORTO EDITORA");

        when(publishingCompanyFactoryDouble.createPublishingCompany(any(PublishingCompanyId.class)))
                .thenReturn(publishingCompanyDouble);

        when(publishingCompanyDouble.identity())
                .thenReturn(publishingCompanyIdDouble);

        when(publishingCompanyIdDouble.toString())
                .thenReturn("PORTO EDITORA");

        // SUT
        PublishingCompanyAssembler assembler =
                new PublishingCompanyAssembler(publishingCompanyFactoryDouble);

        // Act
        PublishingCompany result = assembler.toDomain(dataModelDouble);

        // Assert
        assertEquals("PORTO EDITORA", result.identity().toString());
    }

    @Test
    void toDataModelAndBackShouldKeepSameIdentity() {

        // Arrange
        PublishingCompany publishingCompanyDouble =
                mock(PublishingCompany.class);
        PublishingCompanyId publishingCompanyIdDouble =
                mock(PublishingCompanyId.class);
        PublishingCompanyFactory publishingCompanyFactoryDouble =
                mock(PublishingCompanyFactory.class);

        PublishingCompany reconstructedPublishingCompanyDouble =
                mock(PublishingCompany.class);
        PublishingCompanyId reconstructedPublishingCompanyIdDouble =
                mock(PublishingCompanyId.class);

        when(publishingCompanyDouble.identity())
                .thenReturn(publishingCompanyIdDouble);
        when(publishingCompanyIdDouble.toString())
                .thenReturn("PORTO EDITORA");

        when(reconstructedPublishingCompanyDouble.identity())
                .thenReturn(reconstructedPublishingCompanyIdDouble);
        when(reconstructedPublishingCompanyIdDouble.toString())
                .thenReturn("PORTO EDITORA");

        when(publishingCompanyFactoryDouble.createPublishingCompany(any(PublishingCompanyId.class)))
                .thenReturn(reconstructedPublishingCompanyDouble);

        // SUT
        PublishingCompanyAssembler assembler =
                new PublishingCompanyAssembler(publishingCompanyFactoryDouble);

        // Act
        PublishingCompanyDataModel dm =
                assembler.toDataModel(publishingCompanyDouble);

        PublishingCompany result =
                assembler.toDomain(dm);

        // Assert
        assertEquals("PORTO EDITORA", result.identity().toString());
    }

}