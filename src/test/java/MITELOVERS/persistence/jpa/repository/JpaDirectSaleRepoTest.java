package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.valueobject.AuthorId;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaDirectSaleRepoTest {

    @InjectMocks
    private JpaDirectSaleRepo _jpaDirectSaleRepo;

    @Mock
    private IDirectSaleSpringDataRepo _iDirectSaleSpringDataRepo;

    @Mock
    private DirectSaleAssembler _directSaleAssembler;

    @Mock
    private DirectSaleDataModel _directSaleDMDouble1;

    @Mock
    private DirectSaleDataModel _directSaleDMDouble2;

    @Mock
    private DirectSale _directSaleEntityDouble1;

    @Mock
    private DirectSale _directSaleEntityDouble2;

    @Mock
    private DirectSaleId _idDouble1;

    @Mock
    private ItemId _itemId1;

    @Mock
    private ItemId _itemId2;

    // ------------------------------------------------------------
    // save
    // ------------------------------------------------------------

    @Test
    void saveShouldReturnDomainDirectSale() {

        // Arrange
        when(_directSaleAssembler.toDataModel(_directSaleEntityDouble1)).thenReturn(_directSaleDMDouble1);
        when(_iDirectSaleSpringDataRepo.save(_directSaleDMDouble1)).thenReturn(_directSaleDMDouble1);
        when(_directSaleAssembler.toDomain(_directSaleDMDouble1)).thenReturn(_directSaleEntityDouble1);

        // Act
        DirectSale result = _jpaDirectSaleRepo.save(_directSaleEntityDouble1);

        // Assert
        assertSame(_directSaleEntityDouble1, result);
    }

    // ------------------------------------------------------------
    // findAllKeys
    // ------------------------------------------------------------

    @Test
    void findAllKeysShouldReturnIterableOfDirectSaleIds() {

        // Arrange
        when(_directSaleDMDouble1.getDirectSaleId()).thenReturn("DS-12345678");
        when(_iDirectSaleSpringDataRepo.findAll()).thenReturn(List.of(_directSaleDMDouble1));

        // Act
        Iterable<DirectSaleId> result = _jpaDirectSaleRepo.findAllKeys();

        // Assert
        assertIterableEquals(List.of(new DirectSaleId("DS-12345678")), result);
    }

    @Test
    void findAllKeysShouldReturnEmptyIterableWhenNoDirectSalesExist() {

        // Arrange
        when(_iDirectSaleSpringDataRepo.findAll()).thenReturn(List.of());

        // Act
        Iterable<DirectSaleId> result = _jpaDirectSaleRepo.findAllKeys();

        // Assert
        assertFalse(result.iterator().hasNext());
    }

    // ------------------------------------------------------------
    // findAll
    // ------------------------------------------------------------

    @Test
    void findAllShouldReturnIterableOfDomainDirectSales() {

        // Arrange
        when(_directSaleAssembler.toDomain(_directSaleDMDouble1)).thenReturn(_directSaleEntityDouble1);
        when(_directSaleAssembler.toDomain(_directSaleDMDouble2)).thenReturn(_directSaleEntityDouble2);
        when(_iDirectSaleSpringDataRepo.findAll()).thenReturn(List.of(_directSaleDMDouble1, _directSaleDMDouble2));

        // Act
        Iterable<DirectSale> result = _jpaDirectSaleRepo.findAll();

        // Assert
        assertIterableEquals(List.of(_directSaleEntityDouble1, _directSaleEntityDouble2), result);
    }

    @Test
    void findAllShouldReturnEmptyIterableWhenNoDirectSalesExist() {

        // Arrange
        when(_iDirectSaleSpringDataRepo.findAll()).thenReturn(List.of());

        // Act
        Iterable<DirectSale> result = _jpaDirectSaleRepo.findAll();

        // Assert
        assertFalse(result.iterator().hasNext());
    }

    // ------------------------------------------------------------
    // ofIdentity
    // ------------------------------------------------------------

    @Test
    void ofIdentityShouldReturnDirectSaleWhenIdExists() {

        // Arrange
        when(_idDouble1.toString()).thenReturn("DS-12345678");
        when(_iDirectSaleSpringDataRepo.findById("DS-12345678")).thenReturn(Optional.of(_directSaleDMDouble1));
        when(_directSaleAssembler.toDomain(_directSaleDMDouble1)).thenReturn(_directSaleEntityDouble1);

        // Act
        Optional<DirectSale> result = _jpaDirectSaleRepo.ofIdentity(_idDouble1);

        // Assert
        assertTrue(result.isPresent());
        assertSame(_directSaleEntityDouble1, result.get());
    }

    @Test
    void ofIdentityShouldReturnEmptyOptionalWhenIdDoesNotExist() {

        // Arrange
        when(_idDouble1.toString()).thenReturn("DS-12345678");
        when(_iDirectSaleSpringDataRepo.findById("DS-12345678")).thenReturn(Optional.empty());

        // Act
        Optional<DirectSale> result = _jpaDirectSaleRepo.ofIdentity(_idDouble1);

        // Assert
        assertTrue(result.isEmpty());
    }

    // ------------------------------------------------------------
    // containsOfIdentity
    // ------------------------------------------------------------

    @Test
    void containsOfIdentityShouldReturnTrueWhenDirectSaleExists() {

        // Arrange
        String stringId = "DS-12345678";
        when(_idDouble1.toString()).thenReturn(stringId);
        when(_iDirectSaleSpringDataRepo.existsById(stringId)).thenReturn(true);

        // Act
        boolean result = _jpaDirectSaleRepo.containsOfIdentity(_idDouble1);

        // Assert
        assertTrue(result);
    }

    @Test
    void containsOfIdentityShouldReturnFalseWhenDirectSaleDoesNotExist() {

        // Arrange
        String stringId = "DS-12345678";
        when(_idDouble1.toString()).thenReturn(stringId);
        when(_iDirectSaleSpringDataRepo.existsById(stringId)).thenReturn(false);

        // Act
        boolean result = _jpaDirectSaleRepo.containsOfIdentity(_idDouble1);

        // Assert
        assertFalse(result);
    }

    // ------------------------------------------------------------
    // findDirectSaleItemsByAuthorIdSortedByDescription
    // ------------------------------------------------------------

    @Test
    void findDirectSaleItemsByAuthorIdSortedByDescriptionShouldAlwaysReturnEmptyList() {

        // Arrange
        AuthorId authorIdDouble = mock(AuthorId.class);

        // Act
        List<ItemId> result = _jpaDirectSaleRepo.findDirectSaleItemsByAuthorIdSortedByDescription(authorIdDouble);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ------------------------------------------------------------
    // findByItemsIdSortedByPublicationDateAsc
    // ------------------------------------------------------------

    @Test
    void findByItemsIdSortedByPublicationDateAscShouldReturnMappedDirectSalesFilteredByItems() {

        // Arrange
        when(_itemId1.toString()).thenReturn("I1");
        when(_itemId2.toString()).thenReturn("I2");

        when(_iDirectSaleSpringDataRepo.findByItemsIdOrderByCreationDateAsc(List.of("I1", "I2")))
                .thenReturn(List.of(_directSaleDMDouble1, _directSaleDMDouble2));

        when(_directSaleAssembler.toDomain(_directSaleDMDouble1)).thenReturn(_directSaleEntityDouble1);
        when(_directSaleAssembler.toDomain(_directSaleDMDouble2)).thenReturn(_directSaleEntityDouble2);

        when(_directSaleEntityDouble1.getItemsId()).thenReturn(List.of(_itemId1));
        when(_directSaleEntityDouble2.getItemsId()).thenReturn(List.of(_itemId2));

        // Act
        List<DirectSale> result =
                _jpaDirectSaleRepo.findByItemsIdSortedByPublicationDateAsc(List.of(_itemId1, _itemId2));

        // Assert
        assertEquals(2, result.size());
        assertSame(_directSaleEntityDouble1, result.get(0));
        assertSame(_directSaleEntityDouble2, result.get(1));
    }

    @Test
    void findByItemsIdSortedByPublicationDateAscShouldReturnOnlyDirectSalesContainingRequestedItems() {

        // Arrange
        when(_itemId1.toString()).thenReturn("I1");

        when(_iDirectSaleSpringDataRepo.findByItemsIdOrderByCreationDateAsc(List.of("I1")))
                .thenReturn(List.of(_directSaleDMDouble1, _directSaleDMDouble2));

        when(_directSaleAssembler.toDomain(_directSaleDMDouble1)).thenReturn(_directSaleEntityDouble1);
        when(_directSaleAssembler.toDomain(_directSaleDMDouble2)).thenReturn(_directSaleEntityDouble2);

        when(_directSaleEntityDouble1.getItemsId()).thenReturn(List.of(_itemId1));
        when(_directSaleEntityDouble2.getItemsId()).thenReturn(List.of(_itemId2));

        // Act
        List<DirectSale> result =
                _jpaDirectSaleRepo.findByItemsIdSortedByPublicationDateAsc(List.of(_itemId1));

        // Assert
        assertEquals(1, result.size());
        assertSame(_directSaleEntityDouble1, result.get(0));
    }

    @Test
    void findByItemsIdSortedByPublicationDateAscShouldReturnEmptyListWhenNoMatches() {

        // Arrange
        when(_itemId1.toString()).thenReturn("I1");

        when(_iDirectSaleSpringDataRepo.findByItemsIdOrderByCreationDateAsc(List.of("I1")))
                .thenReturn(List.of());

        // Act
        List<DirectSale> result =
                _jpaDirectSaleRepo.findByItemsIdSortedByPublicationDateAsc(List.of(_itemId1));

        // Assert
        assertTrue(result.isEmpty());
    }

    // ------------------------------------------------------------
    // findByItemsIdSortedByPublicationDateDesc
    // ------------------------------------------------------------

    @Test
    void findByItemsIdSortedByPublicationDateDescShouldReturnMappedDirectSalesFilteredByItems() {

        // Arrange
        when(_itemId1.toString()).thenReturn("I1");
        when(_itemId2.toString()).thenReturn("I2");

        when(_iDirectSaleSpringDataRepo.findByItemsIdOrderByCreationDateDesc(List.of("I1", "I2")))
                .thenReturn(List.of(_directSaleDMDouble2, _directSaleDMDouble1)); // reversed order

        when(_directSaleAssembler.toDomain(_directSaleDMDouble1)).thenReturn(_directSaleEntityDouble1);
        when(_directSaleAssembler.toDomain(_directSaleDMDouble2)).thenReturn(_directSaleEntityDouble2);

        when(_directSaleEntityDouble1.getItemsId()).thenReturn(List.of(_itemId1));
        when(_directSaleEntityDouble2.getItemsId()).thenReturn(List.of(_itemId2));

        // Act
        List<DirectSale> result =
                _jpaDirectSaleRepo.findByItemsIdSortedByPublicationDateDesc(List.of(_itemId1, _itemId2));

        // Assert
        assertEquals(2, result.size());
        assertSame(_directSaleEntityDouble2, result.get(0));
        assertSame(_directSaleEntityDouble1, result.get(1));
    }

    @Test
    void findByItemsIdSortedByPublicationDateDescShouldReturnOnlyDirectSalesContainingRequestedItems() {

        // Arrange
        when(_itemId1.toString()).thenReturn("I1");

        when(_iDirectSaleSpringDataRepo.findByItemsIdOrderByCreationDateDesc(List.of("I1")))
                .thenReturn(List.of(_directSaleDMDouble1, _directSaleDMDouble2));

        when(_directSaleAssembler.toDomain(_directSaleDMDouble1)).thenReturn(_directSaleEntityDouble1);
        when(_directSaleAssembler.toDomain(_directSaleDMDouble2)).thenReturn(_directSaleEntityDouble2);

        when(_directSaleEntityDouble1.getItemsId()).thenReturn(List.of(_itemId1));
        when(_directSaleEntityDouble2.getItemsId()).thenReturn(List.of(_itemId2));

        // Act
        List<DirectSale> result =
                _jpaDirectSaleRepo.findByItemsIdSortedByPublicationDateDesc(List.of(_itemId1));

        // Assert
        assertEquals(1, result.size());
        assertSame(_directSaleEntityDouble1, result.get(0));
    }

    @Test
    void findByItemsIdSortedByPublicationDateDescShouldReturnEmptyListWhenNoMatches() {

        // Arrange
        when(_itemId1.toString()).thenReturn("I1");

        when(_iDirectSaleSpringDataRepo.findByItemsIdOrderByCreationDateDesc(List.of("I1")))
                .thenReturn(List.of());

        // Act
        List<DirectSale> result =
                _jpaDirectSaleRepo.findByItemsIdSortedByPublicationDateDesc(List.of(_itemId1));

        // Assert
        assertTrue(result.isEmpty());
    }

}