package TOPSECRET.domain.item;

import TOPSECRET.domain.valueobject.Condition;
import TOPSECRET.domain.valueobject.Description;
import TOPSECRET.domain.valueobject.EditionId;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ItemFactoryTest {

    @Test
    void shouldSuccessfullyCreateItem() {
        //Arrange
        EditionId editionIdDouble = mock(EditionId.class);
        Description descriptionDouble = mock(Description.class);
        Condition condition = Condition.LIKE_NEW;

        //SUT
        ItemFactory factory = new ItemFactory();

        try (MockedConstruction<Item> mocked =
                     mockConstruction(Item.class,
                             (mock, context) -> {
                                 when(mock.getCondition())
                                         .thenReturn(condition);
                                 when(mock.getDescription())
                                         .thenReturn(descriptionDouble);
                             })) {
            //Act
            Item newItem = factory.createItem(editionIdDouble, condition, descriptionDouble);
            //Assert
            assertEquals(condition, newItem.getCondition());
        }
    }
}
