package MITELOVERS.domain.publishingcompany;

import MITELOVERS.domain.valueobject.PublishingCompanyId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PublishingCompanyTest {

    @Test
    void constructorShouldBuildPublishingCompanyFromString() {

        // Arrange
        String name = "Penguin Random House";

        //Act
        PublishingCompany publishingCompany = new PublishingCompany(name);

        // Assert
        assertNotNull(publishingCompany);
    }

    @Test
    void constructorShouldThrowExceptionWhenNameIsNull() {
        // Arrange
        String name = null;

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new PublishingCompany(name));
    }

    @Test
    void constructorShouldThrowExceptionWhenNameIsBlank() {
        // Arrange
        String name = "   ";

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new PublishingCompany(name));
    }

    @Test
    void constructorShouldThrowExceptionWhenNameHasInvalidCharacters() {
        // Arrange
        String name = "Porto Editora 123";

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new PublishingCompany(name));
    }

    @Test
    void constructorShouldReconstructPublishingCompanyFromId() {
        // Arrange
        PublishingCompanyId publishingCompanyId = new PublishingCompanyId("Test Company");

        // Act
        PublishingCompany publishingCompany = new PublishingCompany(publishingCompanyId);

        // Assert
        assertEquals(publishingCompanyId, publishingCompany.identity());
    }

    @Test
    void identityShouldReturnPublishingCompanyId() {

        //Arrange
        PublishingCompany publishingCompany = new PublishingCompany("Pendant Publishing");

        // Act
        PublishingCompanyId pubCoId = publishingCompany.identity();

        //Assert
        assertEquals("PENDANT PUBLISHING", pubCoId.toString());

    }

    @Test
    void sameAsShouldReturnTrueForEqualPublishingCompanies() {

        //Arrange
        PublishingCompany pubCo = new PublishingCompany("Porto Editora");
        PublishingCompany pubCo2 = new PublishingCompany("PORTO EDITORA");

        // Act
        boolean result = pubCo.sameAs(pubCo2);

        // Assert
        assertTrue(result);
    }

    @Test
    void sameAsShouldReturnFalseForDifferentPublishingCompanies() {

        //Arrange
        PublishingCompany pubCo = new PublishingCompany("Porto Editora");
        PublishingCompany pubCo2 = new PublishingCompany("PORTUGAL EDITORA");

        // Act
        boolean result = pubCo.sameAs(pubCo2);

        // Assert
        assertFalse(result);
    }

    @Test
    void samePublishingCompaniesAreEqual() {
        //Arrange
        PublishingCompany pubCo = new PublishingCompany("Porto editora");
        PublishingCompany pubCo2 = new PublishingCompany("PORTO EDITORA");

        //Act + Assert
        assertEquals(pubCo, pubCo2);
    }

    @Test
    void differentPublishingCompaniesAreNotEqual() {
        //Arrange
        PublishingCompany pubCo = new PublishingCompany("Porto Editora");
        PublishingCompany pubCo2 = new PublishingCompany("Tinta da China");

        //Act + Assert
        assertNotEquals(pubCo, pubCo2);
    }

    @Test
    void toStringShouldReturnPublishingCompanyName() {

        //Arrange
        PublishingCompany pubCo = new PublishingCompany("Pendant Publishing");

        //Act + Assert
        assertEquals("PENDANT PUBLISHING", pubCo.toString());

    }

    @Test
    void sameObjectShouldAssertEquals() {
        //Arrange
        PublishingCompany pubCo = new PublishingCompany("Porto Editora");

        //Assert
        assertEquals(pubCo, pubCo);
    }

    @Test
    void publisherNotEqualToNull() {
        //Arrange
        PublishingCompany pubCo = new PublishingCompany("Porto Editora");

        //Assert
        assertNotEquals(null, pubCo);
    }

    @Test
    void publisherNotEqualToDifferentType() {
        //Arrange
        PublishingCompany pubCo = new PublishingCompany("Porto Editora");
        String differentType = "Porto Editora";

        //Assert
        assertNotEquals(pubCo, differentType);
    }

    @Test
    void publishersWithSameNameHaveSameHashCode() {
        //Arrange
        PublishingCompany pubCo = new PublishingCompany("Porto editora");
        PublishingCompany pubCo2 = new PublishingCompany("PORTO EDITORA");

        //Act + Assert
        assertEquals(pubCo.hashCode(), pubCo2.hashCode());
    }

    @Test
    void publisherWithDifferentNameHaveDifferentHashCode() {
        //Arrange
        PublishingCompany pubCo = new PublishingCompany("Porto Editora");
        PublishingCompany pubCo2 = new PublishingCompany("Tinta da China");

        //Act + Assert
        assertNotEquals(pubCo.hashCode(), pubCo2.hashCode());
    }

}
