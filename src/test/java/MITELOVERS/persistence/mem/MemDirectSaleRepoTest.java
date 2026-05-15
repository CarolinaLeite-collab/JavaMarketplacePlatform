package MITELOVERS.persistence.mem;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.valueobject.DirectSaleId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MemDirectSaleRepoTest {

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
        MemDirectSaleRepo dsr = new MemDirectSaleRepo();

        // Assert
        assertNotNull(dsr);
    }


    @Test
    void findAllKeysShouldCorrectlyReturnIds(){
        //arrange
        Price priceDouble = mock(Price.class);
        when(_ds1Double.identity()).thenReturn(_dsIdDouble);
        DirectSaleId dsIdDouble2 = mock(DirectSaleId.class);
        when(_ds2Double.identity()).thenReturn(dsIdDouble2);

        //SUT
        MemDirectSaleRepo dsr = new MemDirectSaleRepo();
        //act
        dsr.save(_ds1Double);
        dsr.save(_ds2Double);
        List<DirectSaleId> ids = dsr.findAllKeys();

        //assert
        assertEquals(2, ids.size());
        assertTrue(ids.contains(_dsIdDouble));
        assertTrue(ids.contains(dsIdDouble2));
    }

    @Test
    void shouldSaveDirectSale(){
        // SUT
        MemDirectSaleRepo dsr = new MemDirectSaleRepo();

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
        MemDirectSaleRepo dsr = new MemDirectSaleRepo();

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
        MemDirectSaleRepo dsr = new MemDirectSaleRepo();

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
        MemDirectSaleRepo dsr = new MemDirectSaleRepo();

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
        MemDirectSaleRepo dsr = new MemDirectSaleRepo();

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
        MemDirectSaleRepo dsr = new MemDirectSaleRepo();

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
