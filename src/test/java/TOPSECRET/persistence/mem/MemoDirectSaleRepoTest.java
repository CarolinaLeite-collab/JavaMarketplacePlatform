package TOPSECRET.persistence.mem;

import TOPSECRET.domain.item.Item;
import TOPSECRET.domain.directsale.DirectSale;
import TOPSECRET.domain.directsale.DirectSaleFactory;
import TOPSECRET.domain.valueobject.DirectSaleId;
import TOPSECRET.domain.valueobject.ItemId;
import TOPSECRET.domain.valueobject.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

class MemoDirectSaleRepoTest {

    private DirectSaleFactory _factoryDouble;
    private DirectSale _ds1Double;
    private DirectSale _ds2Double;
    private DirectSaleId _dsIdDouble;
    private List<ItemId> _itemsId1;
    private List<ItemId> _itemsId2;
    private ItemId _itemIdDouble1;
    private ItemId _itemIdDouble2;
    private Period _periodDouble;

    @BeforeEach
    void setUp() {
        _factoryDouble = mock(DirectSaleFactory.class);
        _ds1Double = mock(DirectSale.class);
        _ds2Double = mock(DirectSale.class);
        _dsIdDouble = mock(DirectSaleId.class);
        _itemsId1 = new ArrayList<>();
        _itemsId2 = new ArrayList<>();
        _itemIdDouble1 = mock(ItemId.class);
        _itemIdDouble2 = mock(ItemId.class);
        _itemsId1.add(_itemIdDouble1);
        _itemsId2.add(_itemIdDouble2);

        _periodDouble = mock(Period.class);


    }

    @Test
    void constructorShouldBuildDirectSaleRepo() {

        // Act & SUT
        MemoDirectSaleRepo dsr = new MemoDirectSaleRepo(_factoryDouble);

        // Assert
        assertNotNull(dsr);
    }

    @Test
    void addDirectSaleShouldReturnDirectSaleFromFactory() {

        // Arrange
        _itemsId1.add(_itemIdDouble1);
        Price priceDouble = mock(Price.class);
        when(_factoryDouble.createDirectSale(_itemsId1, priceDouble, _periodDouble)).thenReturn(_ds1Double);

        // SUT
        MemoDirectSaleRepo dsr = new MemoDirectSaleRepo(_factoryDouble);

        // Act
        DirectSale created = dsr.addDirectSale(_itemsId1, priceDouble, _periodDouble);

        // Assert
        assertSame(_ds1Double, created);
        verify(_factoryDouble, times(1)).createDirectSale(_itemsId1, priceDouble, _periodDouble);

    }

    @Test
    void addDirectSaleShouldPropagateExceptionFromFactory() {

        // Arrange
        Price priceDouble = mock(Price.class);
        when(_factoryDouble.createDirectSale(eq(_itemsId1), eq(priceDouble), isNull()))
                .thenThrow(new IllegalStateException("boom"));

        // SUT
        MemoDirectSaleRepo dsr = new MemoDirectSaleRepo(_factoryDouble);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> dsr.addDirectSale(_itemsId1, priceDouble, null));
    }


    @Test
    void shouldSaveDirectSale(){
        // SUT
        MemoDirectSaleRepo dsr = new MemoDirectSaleRepo(_factoryDouble);

        //act
        DirectSale ds = dsr.save(_ds1Double);

        //assert
        assertSame(ds, _ds1Double);
    }

    @Test
    void shouldReturnOptionalWhenDSIsPresent(){
        //arrange
        when(_ds1Double.identity()).thenReturn(_dsIdDouble);

        //SUT
        MemoDirectSaleRepo dsr = new MemoDirectSaleRepo(_factoryDouble);

        //act
        dsr.save(_ds1Double);

        Optional<DirectSale> result = dsr.ofIdentity(_dsIdDouble);

        //assert
        assertTrue(result.isPresent());
        assertSame(_ds1Double, result.get());
    }

    @Test
    void shouldReturnEmptyWhenDSNotPresent(){
        //SUT
        MemoDirectSaleRepo dsr = new MemoDirectSaleRepo(_factoryDouble);

        //act
        Optional<DirectSale> result = dsr.ofIdentity(_dsIdDouble);

        //assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnTrueWhenContainsId(){
        //arrange
        when(_ds1Double.identity()).thenReturn(_dsIdDouble);

        //SUT
        MemoDirectSaleRepo dsr = new MemoDirectSaleRepo(_factoryDouble);

        //act
        dsr.save(_ds1Double);
        DirectSaleId dsId = mock(DirectSaleId.class);

        //assert
        assertTrue(dsr.containsOfIdentity(_dsIdDouble));
    }

    @Test
    void shouldReturnFalseWhenDoesNotContainId(){
        //arrange
        when(_ds1Double.identity()).thenReturn(_dsIdDouble);

        //SUT
        MemoDirectSaleRepo dsr = new MemoDirectSaleRepo(_factoryDouble);

        //act
        dsr.save(_ds1Double);
        DirectSaleId dsId = mock(DirectSaleId.class);

        //assert
        assertFalse(dsr.containsOfIdentity(dsId));
    }

    @Test
    void shouldFindAll(){
        //arrange
        DirectSaleId dsIdDouble = mock(DirectSaleId.class);
        when(_ds1Double.identity()).thenReturn(_dsIdDouble);
        when(_ds2Double.identity()).thenReturn(dsIdDouble);

        //SUT
        MemoDirectSaleRepo dsr = new MemoDirectSaleRepo(_factoryDouble);

        //act
        dsr.save(_ds1Double);
        dsr.save(_ds2Double);

        //act
        Iterable<DirectSale> result = dsr.findAll();

        //assert
        List<DirectSale> list = new ArrayList<>();
        result.forEach(list::add);

        assertEquals(2, list.size());
        assertTrue(list.contains(_ds1Double));
        assertTrue(list.contains(_ds2Double));
    }

}