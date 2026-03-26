package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Condition;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ItemFactoryTest {

    @Test
    void shouldSuccessfullyCreateItem() {
        //Arrange
        Publication publication = mock(Publication.class);
        Condition condition = Condition.LIKE_NEW;
        try (MockedConstruction<Item> mocked =
                     mockConstruction(Item.class,
                             (mock, context) -> {
                                 when(mock.getCondition())
                                         .thenReturn(condition);
                             })) {
            //SUT
            ItemFactory factory = new ItemFactory();
            //Act
            Item newItem = factory.createItem(publication, condition);
            //Assert
            assertEquals(condition, newItem.getCondition());
        }
    }
}
