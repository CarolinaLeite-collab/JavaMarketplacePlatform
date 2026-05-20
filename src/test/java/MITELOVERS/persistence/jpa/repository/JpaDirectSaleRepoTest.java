package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.valueobject.DirectSaleId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.persistence.jpa.assembler.DirectSaleAssembler;
import MITELOVERS.persistence.jpa.datamodel.DirectSaleDataModel;
import MITELOVERS.persistence.springdata.IDirectSaleSpringDataRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaDirectSaleRepoTest {

    @InjectMocks
    private JpaDirectSaleRepo _repoDouble;

    @Mock
    private IDirectSaleSpringDataRepo _iDirectSaleSpringDataRepo;

    @Mock
    private DirectSaleDataModel _directSaleDMDouble1;

    @Mock
    private DirectSaleDataModel _directSaleDMDouble2;

    @Mock
    private DirectSale _directSaleEntityDouble1;

    @Mock
    private DirectSale _directSaleEntityDouble2;

    @Mock
    private DirectSaleAssembler _directSaleAssembler;

    @Mock
    private DirectSaleId _idDouble1;

    @Mock
    private ItemId _itemId1;

    @Mock
    private ItemId _itemId2;

    @Test
    void shouldSaveDirectSale() {
        //Arrange
        when(_directSaleAssembler.toDataModel(_directSaleEntityDouble1)).thenReturn(_directSaleDMDouble1);
        when(_iDirectSaleSpringDataRepo.save(_directSaleDMDouble1)).thenReturn(_directSaleDMDouble1);
        when(_directSaleAssembler.toDomain(_directSaleDMDouble1)).thenReturn(_directSaleEntityDouble1);

        //Act
        DirectSale result = _repoDouble.save(_directSaleEntityDouble1);

        //Assert
        assertSame(result, _directSaleEntityDouble1);
    }

    @Test
    void shouldFindAllKeys() {
        //Arrange
        when(_directSaleDMDouble1.getDirectSaleId()).thenReturn("DS-12345678");
        when(_iDirectSaleSpringDataRepo.findAll()).thenReturn(List.of(_directSaleDMDouble1));

        //Act
        Iterable<DirectSaleId> result = _repoDouble.findAllKeys();

        //Assert
        assertIterableEquals(List.of(new DirectSaleId("DS-12345678")), result);
    }

    @Test
    void shouldFindAll() {
        //Arrange
        when(_directSaleAssembler.toDomain(_directSaleDMDouble1)).thenReturn(_directSaleEntityDouble1);
        when(_directSaleAssembler.toDomain(_directSaleDMDouble2)).thenReturn(_directSaleEntityDouble2);
        when(_iDirectSaleSpringDataRepo.findAll()).thenReturn(List.of(_directSaleDMDouble1, _directSaleDMDouble2));

        //Act
        Iterable<DirectSale> result = _repoDouble.findAll();

        //Assert
        assertIterableEquals(List.of(_directSaleEntityDouble1, _directSaleEntityDouble2), result);
    }

    @Test
    void shouldReturnDirectSaleWhenOfIdentityExists() {
        //Arrange
        when(_idDouble1.toString()).thenReturn("DS-12345678");
        when(_iDirectSaleSpringDataRepo.findById(_idDouble1.toString())).thenReturn(Optional.of(_directSaleDMDouble1));
        when(_directSaleAssembler.toDomain(_directSaleDMDouble1)).thenReturn(_directSaleEntityDouble1);

        //Act
        Optional<DirectSale> result = _repoDouble.ofIdentity(_idDouble1);

        //Assert
        assertTrue(result.isPresent());
        assertSame(_directSaleEntityDouble1, result.get());
    }

    @Test
    void shouldReturnEmptyWhenOfIdentityDoesNotExist() {
        //Arrange
        when(_idDouble1.toString()).thenReturn("DS-12345678");
        when(_iDirectSaleSpringDataRepo.findById(_idDouble1.toString())).thenReturn(Optional.empty());

        //Act
        Optional<DirectSale> result = _repoDouble.ofIdentity(_idDouble1);

        //Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnTrueWhenContainsOfIdentityDoesNotExist() {
        //Arrange
        String stringId = "DS-12345678";
        when(_iDirectSaleSpringDataRepo.existsById(stringId)).thenReturn(true);
        when(_idDouble1.toString()).thenReturn(stringId);

        //Act
        boolean result = _repoDouble.containsOfIdentity(_idDouble1);

        //Assert
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenContainsOfIdentityDoesNotExist() {
        //Arrange
        String stringId = "DS-12345678";
        when(_iDirectSaleSpringDataRepo.existsById(stringId)).thenReturn(false);
        when(_idDouble1.toString()).thenReturn(stringId);

        //Act
        boolean result = _repoDouble.containsOfIdentity(_idDouble1);

        //Assert
        assertFalse(result);
    }

    @Test
    void shouldReturnItemsSortedByPublicationDateAsc() {

        when(_itemId1.toString()).thenReturn("I1");
        when(_itemId2.toString()).thenReturn("I2");

        when(_iDirectSaleSpringDataRepo.findByItemsIdOrderByCreationDateAsc(List.of("I1", "I2")))
                .thenReturn(List.of(_directSaleDMDouble1, _directSaleDMDouble2));

        when(_directSaleAssembler.toDomain(_directSaleDMDouble1)).thenReturn(_directSaleEntityDouble1);
        when(_directSaleAssembler.toDomain(_directSaleDMDouble2)).thenReturn(_directSaleEntityDouble2);

        when(_directSaleEntityDouble1.getItemsId()).thenReturn(List.of(_itemId1));
        when(_directSaleEntityDouble2.getItemsId()).thenReturn(List.of(_itemId2));

        List<ItemId> result = _repoDouble.findByItemsIdSortedByPublicationDateAsc(List.of(_itemId1, _itemId2));

        assertEquals(List.of(_itemId1, _itemId2), result);
    }

    @Test
    void shouldReturnItemsSortedByPublicationDateDesc() {

        when(_itemId1.toString()).thenReturn("I1");
        when(_itemId2.toString()).thenReturn("I2");

        when(_iDirectSaleSpringDataRepo.findByItemsIdOrderByCreationDateDesc(List.of("I1", "I2")))
                .thenReturn(List.of(_directSaleDMDouble2, _directSaleDMDouble1)); // reversed order

        when(_directSaleAssembler.toDomain(_directSaleDMDouble1)).thenReturn(_directSaleEntityDouble1);
        when(_directSaleAssembler.toDomain(_directSaleDMDouble2)).thenReturn(_directSaleEntityDouble2);

        when(_directSaleEntityDouble1.getItemsId()).thenReturn(List.of(_itemId1));
        when(_directSaleEntityDouble2.getItemsId()).thenReturn(List.of(_itemId2));

        List<ItemId> result = _repoDouble.findByItemsIdSortedByPublicationDateDesc(List.of(_itemId1, _itemId2));

        assertEquals(List.of(_itemId2, _itemId1), result);
    }

    @Test
    void shouldReturnOnlyMatchingItemsAsc() {

        when(_itemId1.toString()).thenReturn("I1");

        when(_iDirectSaleSpringDataRepo.findByItemsIdOrderByCreationDateAsc(List.of("I1")))
                .thenReturn(List.of(_directSaleDMDouble1, _directSaleDMDouble2));

        when(_directSaleAssembler.toDomain(_directSaleDMDouble1)).thenReturn(_directSaleEntityDouble1);
        when(_directSaleAssembler.toDomain(_directSaleDMDouble2)).thenReturn(_directSaleEntityDouble2);

        when(_directSaleEntityDouble1.getItemsId()).thenReturn(List.of(_itemId1));
        when(_directSaleEntityDouble2.getItemsId()).thenReturn(List.of(_itemId2));

        List<ItemId> result = _repoDouble.findByItemsIdSortedByPublicationDateAsc(List.of(_itemId1));

        assertEquals(List.of(_itemId1), result);
    }
}