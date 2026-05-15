package MITELOVERS.persistence.jpa.datamodel;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PublicationTypeDataModelTest {

    @Test
    void shouldCreateEmptyDataModelWithNoArgsConstructor() {

        // SUT & Act
        PublicationTypeDataModel dataModel = new PublicationTypeDataModel();
        String result = dataModel.getPublicationTypeId();

        // Assert
        assertNull(result);

    }

    @Test
    void shouldCreateDataModelWithAllArgsConstructor() {

        // SUT & Act
        PublicationTypeDataModel dataModel = new PublicationTypeDataModel("BOOK");
        String result = dataModel.getPublicationTypeId();

        // Assert
        assertEquals("BOOK", result);

    }

}