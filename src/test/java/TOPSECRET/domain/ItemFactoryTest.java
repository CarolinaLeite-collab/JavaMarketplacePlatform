package TOPSECRET.domain;

import TOPSECRET.domain.publication.Publication;
import TOPSECRET.domain.valueobject.Condition;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ItemFactoryTest {

    @Test
    void shouldSuccessfullyCreateItem() {
        //Arrange
        Publication publicationDouble = mock(Publication.class);
        Condition condition = Condition.LIKE_NEW;

        //SUT
        ItemFactory factory = new ItemFactory();

        try (MockedConstruction<Item> mocked =
                     mockConstruction(Item.class,
                             (mock, context) -> {
                                 when(mock.get_condition())
                                         .thenReturn(condition);
                             })) {
            //Act
            Item newItem = factory.createItem(publicationDouble, condition);
            //Assert
            assertEquals(condition, newItem.get_condition());
        }
    }
}
