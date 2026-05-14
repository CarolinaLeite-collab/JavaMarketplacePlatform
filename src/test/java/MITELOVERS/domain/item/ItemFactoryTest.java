package MITELOVERS.domain.item;

import MITELOVERS.domain.valueobject.*;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemFactoryTest {

    @Test
    void shouldSuccessfullyCreateNewItem() {
        //Arrange
        EditionId editionIdDouble = mock(EditionId.class);
        Description descriptionDouble = mock(Description.class);
        Condition condition = Condition.LIKE_NEW;
        Name nameDouble = mock(Name.class);

        //SUT
        ItemFactory factory = new ItemFactory();

        try (MockedConstruction<Item> mocked =
                     mockConstruction(Item.class)) {
            //Act
            Item newItem = factory.createItem(editionIdDouble, condition, descriptionDouble, nameDouble);

            //Assert
            assertNotNull(newItem);
            assertEquals(1, mocked.constructed().size());
        }
    }

    @Test
    void shouldSuccessfullyReconstructExistingItem() {
        //Arrange
        ItemId itemIdDouble = mock(ItemId.class);
        EditionId editionIdDouble = mock(EditionId.class);
        Description descriptionDouble = mock(Description.class);
        Condition condition = Condition.POOR;
        SaleStatus saleStatus = SaleStatus.OnAuction;
        Name nameDouble = mock(Name.class);

        //SUT
        ItemFactory factory = new ItemFactory();

        try (MockedConstruction<Item> mocked =
                     mockConstruction(Item.class)) {

            Item reconstitutedItem = factory.createItem(itemIdDouble, editionIdDouble, condition, descriptionDouble, saleStatus, nameDouble);

            //Assert
            assertNotNull(reconstitutedItem);
            assertEquals(1, mocked.constructed().size());

        }

    }

}
