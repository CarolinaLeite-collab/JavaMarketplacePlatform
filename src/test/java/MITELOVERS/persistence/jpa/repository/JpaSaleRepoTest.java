package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.sale.Sale;
import MITELOVERS.domain.valueobject.SaleId;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.persistence.jpa.assembler.SaleAssembler;
import MITELOVERS.persistence.jpa.datamodel.SaleDataModel;
import MITELOVERS.persistence.springdata.ISaleSpringDataRepo;
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
class JpaSaleRepoTest {

    @Mock
    private ISaleSpringDataRepo _saleSpringDataRepoDouble;

    @Mock
    private SaleAssembler _saleAssemblerDouble;

    @InjectMocks
    private JpaSaleRepo _jpaSaleRepo;

    @Test
    void saveShouldSaveDataModelAndReturnSale() {

        // Arrange
        Sale saleDouble = mock(Sale.class);
        SaleDataModel dmDouble = mock(SaleDataModel.class);
        SaleDataModel savedDmDouble = mock(SaleDataModel.class);

        when(_saleAssemblerDouble.toDataModel(saleDouble)).thenReturn(dmDouble);
        when(_saleSpringDataRepoDouble.save(dmDouble)).thenReturn(savedDmDouble);
        when(_saleAssemblerDouble.toDomain(savedDmDouble)).thenReturn(saleDouble);

        // Act
        Sale result = _jpaSaleRepo.save(saleDouble);

        // Assert
        assertEquals(saleDouble, result);
    }

    @Test
    void testFindAllKeysReturnSaleIds() {

        // Arrange
        SaleDataModel dm1Double = mock(SaleDataModel.class);
        SaleDataModel dm2Double = mock(SaleDataModel.class);
        List<SaleDataModel> dmList = List.of(dm1Double, dm2Double);

        when(dm1Double.getSaleId()).thenReturn("SA-1234ABCD");
        when(dm2Double.getSaleId()).thenReturn("SA-5678EFGH");
        when(_saleSpringDataRepoDouble.findAll()).thenReturn(dmList);

        // Act
        Iterable<SaleId> result = _jpaSaleRepo.findAllKeys();
        List<SaleId> resultList = (List<SaleId>) result;

        // Assert
        assertEquals(2, resultList.size());
        assertEquals("SA-1234ABCD", resultList.get(0).toString());
        assertEquals("SA-5678EFGH", resultList.get(1).toString());
    }

    @Test
    void testFindAllReturnIterableOfSales() {

        // Arrange
        SaleDataModel dm1Double = mock(SaleDataModel.class);
        SaleDataModel dm2Double = mock(SaleDataModel.class);
        List<SaleDataModel> dmList = List.of(dm1Double, dm2Double);

        Sale sale1Double = mock(Sale.class);
        Sale sale2Double = mock(Sale.class);
        List<Sale> saleList = List.of(sale1Double, sale2Double);

        when(_saleSpringDataRepoDouble.findAll()).thenReturn(dmList);
        when(_saleAssemblerDouble.toDomain(dm1Double)).thenReturn(sale1Double);
        when(_saleAssemblerDouble.toDomain(dm2Double)).thenReturn(sale2Double);

        // Act
        Iterable<Sale> result = _jpaSaleRepo.findAll();

        // Assert
        assertEquals(saleList, result);
    }

    @Test
    void testOfIdentityReturnsSale() {

        // Arrange
        SaleId saleIdDouble = mock(SaleId.class);
        SaleDataModel dmDouble = mock(SaleDataModel.class);
        Sale saleDouble = mock(Sale.class);

        when(saleIdDouble.toString()).thenReturn("SA-1234ABCD");
        when(_saleSpringDataRepoDouble.findById("SA-1234ABCD")).thenReturn(Optional.of(dmDouble));
        when(_saleAssemblerDouble.toDomain(dmDouble)).thenReturn(saleDouble);

        // Act
        Optional<Sale> result = _jpaSaleRepo.ofIdentity(saleIdDouble);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(saleDouble, result.get());
    }

    @Test
    void testOfIdentityReturnsEmptyWhenNotFound() {

        // Arrange
        SaleId saleIdDouble = mock(SaleId.class);

        when(saleIdDouble.toString()).thenReturn("SA-1234ABCD");
        when(_saleSpringDataRepoDouble.findById("SA-1234ABCD")).thenReturn(Optional.empty());

        // Act
        Optional<Sale> result = _jpaSaleRepo.ofIdentity(saleIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void testContainsOfIdentityReturnsTrueWhenSaleExists() {

        // Arrange
        SaleId saleIdDouble = mock(SaleId.class);

        when(saleIdDouble.toString()).thenReturn("SA-1234ABCD");
        when(_saleSpringDataRepoDouble.existsById("SA-1234ABCD")).thenReturn(true);

        // Act
        boolean result = _jpaSaleRepo.containsOfIdentity(saleIdDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void testContainsOfIdentityReturnsFalseWhenSaleDoesNotExist() {

        // Arrange
        SaleId saleIdDouble = mock(SaleId.class);

        when(saleIdDouble.toString()).thenReturn("SA-1234ABCD");
        when(_saleSpringDataRepoDouble.existsById("SA-1234ABCD")).thenReturn(false);

        // Act
        boolean result = _jpaSaleRepo.containsOfIdentity(saleIdDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void findByUserIdReturnsListOfSalesWhenFound() {
        // Arrange
        SaleDataModel dm1Double = mock(SaleDataModel.class);
        SaleDataModel dm2Double = mock(SaleDataModel.class);

        Sale sale1Double = mock(Sale.class);
        Sale sale2Double = mock(Sale.class);

        UserId userIdDouble = mock(UserId.class);
        when(userIdDouble.toString()).thenReturn("pedro@aeiou.com");

        when(_saleSpringDataRepoDouble.findByUserId("pedro@aeiou.com"))
                .thenReturn(List.of(dm1Double, dm2Double));
        when(_saleAssemblerDouble.toDomain(dm1Double)).thenReturn(sale1Double);
        when(_saleAssemblerDouble.toDomain(dm2Double)).thenReturn(sale2Double);

        // Act
        List<Sale> result = _jpaSaleRepo.findByUserId(userIdDouble);

        // Assert
        assertEquals(2, result.size());
        assertSame(sale1Double, result.get(0));
        assertSame(sale2Double, result.get(1));
    }

    @Test
    void findByUserIdReturnsEmptyListWhenNoSalesFound() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);
        when(userIdDouble.toString()).thenReturn("pedro@aeiou.com");

        when(_saleSpringDataRepoDouble.findByUserId("pedro@aeiou.com"))
                .thenReturn(List.of());

        // Act
        List<Sale> result = _jpaSaleRepo.findByUserId(userIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }
}