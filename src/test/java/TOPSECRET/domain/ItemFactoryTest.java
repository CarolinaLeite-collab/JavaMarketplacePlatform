package TOPSECRET.domain;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemFactoryTest {

    @Test
    void shouldSuccessfullyCreateItem() throws InstantiationException {
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
        Item newItem = factory.create(publication, condition);

        //assert
        assertEquals(condition, newItem.getCondition());
    }

    @Test
    void shouldThrowExceptionWhenCreateItem() throws InstantiationException {
        //arrange
        Publication publication = mock(Publication.class);
        Condition condition = Condition.LIKE_NEW;
        try (MockedConstruction<Item> mocked =
                     mockConstruction(Item.class, (mock, context) -> {
                         throw new RuntimeException("WRONG");
                     })) {

            //SUT
            ItemFactory factory = new ItemFactory();

            // act and assert
            assertThrows(InstantiationException.class, () -> factory.create(publication, condition));
        }
    }
}
