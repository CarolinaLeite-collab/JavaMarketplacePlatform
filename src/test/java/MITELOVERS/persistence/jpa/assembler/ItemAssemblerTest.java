package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.item.ItemFactory;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.persistence.jpa.datamodel.ItemDataModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItemAssemblerTest {

    @Test
    void toDataModelShouldMapIdFromItemIdentity() {
        // Arrange
        ItemFactory factoryDouble = mock(ItemFactory.class);

        Item itemDouble = mock(Item.class);
        ItemId itemId = new ItemId("ABCDEF1234");
        when(itemDouble.identity()).thenReturn(itemId);
        when(itemDouble.getEditionId()).thenReturn(new EditionId("E-TEST1234"));
        when(itemDouble.getCondition()).thenReturn(Condition.POOR);
        when(itemDouble.getDescription()).thenReturn(new Description("Nice book"));
        when(itemDouble.getSaleStatus()).thenReturn(SaleStatus.OnDirectSale);
        when(itemDouble.getName()).thenReturn(new Name("Best book"));

        // SUT
        ItemAssembler assembler = new ItemAssembler(factoryDouble);

        // Act
        ItemDataModel result = assembler.toDataModel(itemDouble);

        // Assert
        assertEquals("ABCDEF1234", result.getId());
    }

    @Test
    void toDataModelShouldMapEditionIdCorrectly() {
        // Arrange
        ItemFactory factoryDouble = mock(ItemFactory.class);

        Item itemDouble = mock(Item.class);
        ItemId itemId = new ItemId("ABCDEF1234");
        when(itemDouble.identity()).thenReturn(itemId);
        when(itemDouble.getEditionId()).thenReturn(new EditionId("E-TEST1234"));
        when(itemDouble.getCondition()).thenReturn(Condition.POOR);
        when(itemDouble.getDescription()).thenReturn(new Description("Nice book"));
        when(itemDouble.getSaleStatus()).thenReturn(SaleStatus.OnDirectSale);
        when(itemDouble.getName()).thenReturn(new Name("Best book"));

        // SUT
        ItemAssembler assembler = new ItemAssembler(factoryDouble);

        // Act
        ItemDataModel result = assembler.toDataModel(itemDouble);

        // Assert
        assertEquals("E-TEST1234", result.getEditionId());
    }

    @Test
    void toDataModelShouldMapConditionCorrectly() {
        // Arrange
        ItemFactory factoryDouble = mock(ItemFactory.class);

        Item itemDouble = mock(Item.class);
        when(itemDouble.identity()).thenReturn(new ItemId("ABCDEF1234"));
        when(itemDouble.getEditionId()).thenReturn(new EditionId("E-ED123456"));
        when(itemDouble.getCondition()).thenReturn(Condition.LIKE_NEW);
        when(itemDouble.getDescription()).thenReturn(new Description("Nice book"));
        when(itemDouble.getSaleStatus()).thenReturn(SaleStatus.OnAuction);
        when(itemDouble.getName()).thenReturn(new Name("Best book"));

        //SUT
        ItemAssembler assembler = new ItemAssembler(factoryDouble);

        // Act
        ItemDataModel result = assembler.toDataModel(itemDouble);

        // Assert
        assertEquals("LIKE_NEW", result.getCondition());
    }

    @Test
    void toDataModelShouldMapDescriptionCorrectly() {
        // Arrange
        ItemFactory factoryDouble = mock(ItemFactory.class);

        Item itemDouble = mock(Item.class);
        when(itemDouble.identity()).thenReturn(new ItemId("ABCDEF1234"));
        when(itemDouble.getEditionId()).thenReturn(new EditionId("E-ED123456"));
        when(itemDouble.getCondition()).thenReturn(Condition.LIKE_NEW);
        when(itemDouble.getDescription()).thenReturn(new Description("Nice book."));
        when(itemDouble.getSaleStatus()).thenReturn(SaleStatus.OnAuction);
        when(itemDouble.getName()).thenReturn(new Name("Best book"));

        //SUT
        ItemAssembler assembler = new ItemAssembler(factoryDouble);

        // Act
        ItemDataModel result = assembler.toDataModel(itemDouble);

        // Assert
        assertEquals("Nice book.", result.getDescription());
    }

    @Test
    void toDataModelShouldMapSaleStatusCorrectly() {
        // Arrange
        ItemFactory factoryDouble = mock(ItemFactory.class);

        Item itemDouble = mock(Item.class);
        when(itemDouble.identity()).thenReturn(new ItemId("ABCDEF1234"));
        when(itemDouble.getEditionId()).thenReturn(new EditionId("E-ED123456"));
        when(itemDouble.getCondition()).thenReturn(Condition.LIKE_NEW);
        when(itemDouble.getDescription()).thenReturn(new Description("Nice book"));
        when(itemDouble.getSaleStatus()).thenReturn(SaleStatus.OnAuction);
        when(itemDouble.getName()).thenReturn(new Name("Best book"));

        //SUT
        ItemAssembler assembler = new ItemAssembler(factoryDouble);

        // Act
        ItemDataModel result = assembler.toDataModel(itemDouble);

        // Assert
        assertEquals("OnAuction", result.getSaleStatus());
    }

    @Test
    void toDataModelShouldMapNameCorrectly() {
        // Arrange
        ItemFactory factoryDouble = mock(ItemFactory.class);

        Item itemDouble = mock(Item.class);
        when(itemDouble.identity()).thenReturn(new ItemId("ABCDEF1234"));
        when(itemDouble.getEditionId()).thenReturn(new EditionId("E-ED123456"));
        when(itemDouble.getCondition()).thenReturn(Condition.LIKE_NEW);
        when(itemDouble.getDescription()).thenReturn(new Description("Nice book"));
        when(itemDouble.getSaleStatus()).thenReturn(SaleStatus.OnAuction);
        when(itemDouble.getName()).thenReturn(new Name("Best book"));

        //SUT
        ItemAssembler assembler = new ItemAssembler(factoryDouble);

        // Act
        ItemDataModel result = assembler.toDataModel(itemDouble);

        // Assert
        assertEquals("Best Book", result.getName());
    }

    @Test
    void toDomainShouldDelegateToFactory() {
        // Arrange
        ItemFactory factoryDouble = mock(ItemFactory.class);

        ItemDataModel dmDouble = mock(ItemDataModel.class);
        Item itemDouble = mock(Item.class);

        when(dmDouble.getId()).thenReturn("ABCDEF1234");
        when(dmDouble.getEditionId()).thenReturn("E-ED123456");
        when(dmDouble.getCondition()).thenReturn("POOR");
        when(dmDouble.getDescription()).thenReturn("Not a great book");
        when(dmDouble.getSaleStatus()).thenReturn("NotOnSale");
        when(dmDouble.getName()).thenReturn("Best book");

        when(factoryDouble.createItem(any(), any(), any(), any(), any(), any())).thenReturn(itemDouble);

        //SUT
        ItemAssembler assembler = new ItemAssembler(factoryDouble);

        // Act
        Item result = assembler.toDomain(dmDouble);

        // Assert
        assertEquals(itemDouble, result);
    }

    @Test
    void toDataModelShouldThrowWhenItemIsNull() {
        // Arrange
        ItemFactory factoryDouble = mock(ItemFactory.class);
        ItemAssembler assembler = new ItemAssembler(factoryDouble);

        // Act + Assert
        NullPointerException ex = assertThrows(NullPointerException.class, () -> assembler.toDataModel(null));
        assertEquals("Item cannot be null", ex.getMessage());
    }

    @Test
    void toDomainShouldThrowWhenDataModelIsNull() {
        // Arrange
        ItemFactory factoryDouble = mock(ItemFactory.class);
        ItemAssembler assembler = new ItemAssembler(factoryDouble);

        // Act + Assert
        NullPointerException ex = assertThrows(NullPointerException.class, () -> assembler.toDomain(null));
        assertEquals("ItemDataModel cannot be null", ex.getMessage());
    }

}