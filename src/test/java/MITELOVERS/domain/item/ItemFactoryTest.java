package MITELOVERS.domain.item;

import MITELOVERS.domain.valueobject.*;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;

class ItemFactoryTest {

    @Test
    void shouldSuccessfullyCreateNewItemWithoutPicture() {
        //Arrange
        EditionId editionIdDouble = mock(EditionId.class);
        Description descriptionDouble = mock(Description.class);
        Condition condition = Condition.MINT;

        //SUT
        ItemFactory factory = new ItemFactory();

        try (MockedConstruction<Item> mocked =
                     mockConstruction(Item.class)) {
            //Act
            Item newItem = factory.createItem(editionIdDouble, condition, descriptionDouble);

            //Assert
            assertNotNull(newItem);
            assertEquals(1, mocked.constructed().size());
        }
    }

    @Test
    void shouldSuccessfullyCreateNewItemWithPicture() {

        // Arrange
        EditionId editionIdDouble = mock(EditionId.class);
        Description descriptionDouble = mock(Description.class);
        Condition condition = Condition.MINT;
        Picture pictureDouble = mock(Picture.class);

        // SUT
        ItemFactory factory = new ItemFactory();

        try (MockedConstruction<Item> mocked =
                     mockConstruction(Item.class)) {

            // Act
            Item newItem = factory.createItem(editionIdDouble, condition, descriptionDouble, pictureDouble);

            // Assert
            assertNotNull(newItem);
            assertEquals(1, mocked.constructed().size());
        }
    }

    @Test
    void shouldSuccessfullyReconstructExistingItemWithoutPicture() {
        //Arrange
        ItemId itemIdDouble = mock(ItemId.class);
        EditionId editionIdDouble = mock(EditionId.class);
        Description descriptionDouble = mock(Description.class);
        Condition condition = Condition.POOR;
        SaleStatus saleStatus = SaleStatus.OnAuction;

        //SUT
        ItemFactory factory = new ItemFactory();

        try (MockedConstruction<Item> mocked =
                     mockConstruction(Item.class)) {

            Item reconstitutedItem = factory.createItem(itemIdDouble, editionIdDouble, condition, descriptionDouble, saleStatus);

            //Assert
            assertNotNull(reconstitutedItem);
            assertEquals(1, mocked.constructed().size());

        }

    }

    @Test
    void shouldSuccessfullyReconstructExistingItemWithPicture() {

        // Arrange
        ItemId itemIdDouble = mock(ItemId.class);
        EditionId editionIdDouble = mock(EditionId.class);
        Description descriptionDouble = mock(Description.class);
        Condition condition = Condition.POOR;
        SaleStatus saleStatus = SaleStatus.OnAuction;
        Picture pictureDouble = mock(Picture.class);

        // SUT
        ItemFactory factory = new ItemFactory();

        try (MockedConstruction<Item> mocked =
                     mockConstruction(Item.class)) {

            // Act
            Item reconstitutedItem = factory.createItem(itemIdDouble, editionIdDouble, condition, descriptionDouble, saleStatus, pictureDouble);

            // Assert
            assertNotNull(reconstitutedItem);
            assertEquals(1, mocked.constructed().size());
        }
    }

}
