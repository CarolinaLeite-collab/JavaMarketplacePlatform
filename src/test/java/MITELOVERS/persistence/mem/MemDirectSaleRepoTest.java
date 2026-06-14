package MITELOVERS.persistence.mem;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.valueobject.DirectSaleId;
import MITELOVERS.domain.valueobject.ItemId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemDirectSaleRepoTest {

    @Mock
    private DirectSale _ds1Double;

    @Mock
    private DirectSale _ds2Double;

    @Mock
    private DirectSaleId _dsIdDouble1;

    @Mock
    private DirectSaleId _dsIdDouble2;

    @Mock
    private ItemId _itemIdDouble1;

    @Mock
    private ItemId _itemIdDouble2;


    @Test
    void constructorShouldBuildDirectSaleRepo() {

        // Act & SUT
        MemDirectSaleRepo dsr = new MemDirectSaleRepo();

        // Assert
        assertNotNull(dsr);
    }

    @Test
    void findAllKeysShouldCorrectlyReturnIds() {
        MemDirectSaleRepo repo = new MemDirectSaleRepo();

        when(_ds1Double.identity()).thenReturn(_dsIdDouble1);
        when(_ds2Double.identity()).thenReturn(_dsIdDouble2);

        repo.save(_ds1Double);
        repo.save(_ds2Double);

        List<DirectSaleId> ids = repo.findAllKeys();

        assertEquals(2, ids.size());
        assertTrue(ids.contains(_dsIdDouble1));
        assertTrue(ids.contains(_dsIdDouble2));
    }

    @Test
    void shouldSaveDirectSale(){
        // SUT
        MemDirectSaleRepo dsr = new MemDirectSaleRepo();

        //act
        when(_ds1Double.identity()).thenReturn(_dsIdDouble1);

        DirectSale ds = dsr.save(_ds1Double);

        //assert
        assertSame(_ds1Double, ds);
    }

    @Test
    void shouldReturnOptionalWhenPresent() {

        MemDirectSaleRepo repo = new MemDirectSaleRepo();

        when(_ds1Double.identity()).thenReturn(_dsIdDouble1);

        repo.save(_ds1Double);

        Optional<DirectSale> result = repo.ofIdentity(_dsIdDouble1);

        assertTrue(result.isPresent());
        assertSame(_ds1Double, result.get());
    }

    @Test
    void shouldReturnEmptyWhenDSNotPresent(){
        //SUT
        MemDirectSaleRepo dsr = new MemDirectSaleRepo();

        //act
        Optional<DirectSale> result = dsr.ofIdentity(_dsIdDouble1);

        //assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnTrueWhenContainsId() {
        // SUT
        MemDirectSaleRepo repo = new MemDirectSaleRepo();

        //Act
        when(_ds1Double.identity()).thenReturn(_dsIdDouble1);

        repo.save(_ds1Double);

        //Assert
        assertTrue(repo.containsOfIdentity(_dsIdDouble1));
    }

    @Test
    void shouldReturnFalseWhenDoesNotContainId() {
        MemDirectSaleRepo repo = new MemDirectSaleRepo();

        when(_ds1Double.identity()).thenReturn(_dsIdDouble1);

        repo.save(_ds1Double);

        assertFalse(repo.containsOfIdentity(_dsIdDouble2));
    }

    @Test
    void shouldFindAll(){
        //arrange
        DirectSaleId dsIdDouble = mock(DirectSaleId.class);
        when(_ds1Double.identity()).thenReturn(_dsIdDouble1);
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

    @Test
    void shouldReturnDirectSalesSortedByPublicationDateAsc() {

        MemDirectSaleRepo repo = new MemDirectSaleRepo();

        when(_ds1Double.identity()).thenReturn(_dsIdDouble1);
        when(_ds2Double.identity()).thenReturn(_dsIdDouble2);

        when(_ds1Double.getCreationDate()).thenReturn(Instant.parse("2020-01-01T00:00:00Z"));
        when(_ds2Double.getCreationDate()).thenReturn(Instant.parse("2020-01-02T00:00:00Z"));

        when(_ds1Double.getItemsId()).thenReturn(List.of(_itemIdDouble1));
        when(_ds2Double.getItemsId()).thenReturn(List.of(_itemIdDouble2));

        repo.save(_ds1Double);
        repo.save(_ds2Double);

        List<DirectSale> result =
                repo.findByItemsIdSortedByPublicationDateAsc(List.of(_itemIdDouble1, _itemIdDouble2));

        assertEquals(List.of(_ds1Double, _ds2Double), result);
    }

    @Test
    void shouldReturnDirectSalesSortedByPublicationDateDesc() {

        MemDirectSaleRepo repo = new MemDirectSaleRepo();

        when(_ds1Double.identity()).thenReturn(_dsIdDouble1);
        when(_ds2Double.identity()).thenReturn(_dsIdDouble2);

        when(_ds1Double.getCreationDate()).thenReturn(Instant.parse("2020-01-01T00:00:00Z"));
        when(_ds2Double.getCreationDate()).thenReturn(Instant.parse("2020-01-02T00:00:00Z"));

        when(_ds1Double.getItemsId()).thenReturn(List.of(_itemIdDouble1));
        when(_ds2Double.getItemsId()).thenReturn(List.of(_itemIdDouble2));

        repo.save(_ds1Double);
        repo.save(_ds2Double);

        List<DirectSale> result =
                repo.findByItemsIdSortedByPublicationDateDesc(List.of(_itemIdDouble1, _itemIdDouble2));

        assertEquals(List.of(_ds2Double, _ds1Double), result);
    }

    @Test
    void shouldReturnOnlyMatchingDirectSales() {

        MemDirectSaleRepo repo = new MemDirectSaleRepo();

        when(_ds1Double.identity()).thenReturn(_dsIdDouble1);
        when(_ds2Double.identity()).thenReturn(_dsIdDouble2);

        when(_ds1Double.getItemsId()).thenReturn(List.of(_itemIdDouble1));
        when(_ds2Double.getItemsId()).thenReturn(List.of(_itemIdDouble2));

        repo.save(_ds1Double);
        repo.save(_ds2Double);

        List<DirectSale> result =
                repo.findByItemsIdSortedByPublicationDateAsc(List.of(_itemIdDouble2));

        assertEquals(List.of(_ds2Double), result);
    }

    @Test
    void deleteDirectSale_shouldThrowUnsupportedOperation() {

        // Arrange
        MemDirectSaleRepo repo = new MemDirectSaleRepo();

        // Act + Assert
        assertThrows(UnsupportedOperationException.class,
                () -> repo.deleteDirectSale(_dsIdDouble1));
    }

    @Test
    void findExpired_shouldReturnEmptyWhenNoSales() {

        // Arrange
        MemDirectSaleRepo repo = new MemDirectSaleRepo();

        // Act
        List<DirectSaleId> result = repo.findExpired();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findExpired_shouldReturnOnlyExpiredSales() {

        // Arrange
        MemDirectSaleRepo repo = new MemDirectSaleRepo();

        when(_ds1Double.identity()).thenReturn(_dsIdDouble1);
        when(_ds2Double.identity()).thenReturn(_dsIdDouble2);

        // expired sale
        when(_ds1Double.getCreationDate()).thenReturn(Instant.now().minusSeconds(5000));
        when(_ds1Double.getTimeLimit()).thenReturn(Duration.ofSeconds(1000));

        // not expired sale
        when(_ds2Double.getCreationDate()).thenReturn(Instant.now());
        when(_ds2Double.getTimeLimit()).thenReturn(Duration.ofSeconds(999999));

        repo.save(_ds1Double);
        repo.save(_ds2Double);

        // Act
        List<DirectSaleId> result = repo.findExpired();

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(_dsIdDouble1));
    }

}
