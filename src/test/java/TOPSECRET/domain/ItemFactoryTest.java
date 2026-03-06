package TOPSECRET.domain;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemFactoryTest {

    @Test
    void shouldSuccessfullyCreateItem() {
        //arrange
        Publication publication = mock(Publication.class);
        Condition condition = Condition.LIKE_NEW;
        try (MockedConstruction<Item> mocked =
                     mockConstruction(Item.class,
                             (mock, context) -> {
                                 when(mock.getCondition())
                                         .thenReturn(condition);
                             })) {

        }
        //SUT
        ItemFactory factory = new ItemFactory();

        //act
        Item newItem = factory.createItem(publication, condition);

        //assert
        assertEquals(condition, newItem.getCondition());
    }
}
