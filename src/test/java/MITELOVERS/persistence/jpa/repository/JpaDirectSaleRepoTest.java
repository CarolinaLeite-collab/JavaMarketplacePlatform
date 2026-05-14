package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.valueobject.DirectSaleId;
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

    //SUT
    @InjectMocks
    private JpaDirectSaleRepo _repoDouble;

    @Mock
    private IDirectSaleSpringDataRepo _iDirectSaleSpringDataRepoDouble;

    @Mock
    private DirectSaleDataModel _directSaleDataModelDouble;

    @Mock
    private DirectSaleDataModel dmOther;

    @Mock
    private DirectSale _directSaleEntityDouble;

    @Mock
    private DirectSale _directSaleEntityOtherDouble;
    @Mock
    private DirectSaleAssembler _assemblerDouble;

    @Mock
    private DirectSaleId _idDouble;

    @Test
    void shouldSaveDirectSale() {
        //Arrange
        when(_assemblerDouble.toDataModel(_directSaleEntityDouble)).thenReturn(_directSaleDataModelDouble);
        when(_iDirectSaleSpringDataRepoDouble.save(_directSaleDataModelDouble)).thenReturn(_directSaleDataModelDouble);
        when(_assemblerDouble.toDomain(_directSaleDataModelDouble)).thenReturn(_directSaleEntityDouble);

        //Act
        DirectSale result = _repoDouble.save(_directSaleEntityDouble);

        //Assert
        assertSame(result,_directSaleEntityDouble);

    }

    @Test
    void shouldFindAllKeys() {
        //Arrange
        when(_directSaleDataModelDouble.getDirectSaleId()).thenReturn("DS-12345678");
        when(_iDirectSaleSpringDataRepoDouble.findAll()).thenReturn(List.of(_directSaleDataModelDouble));

        //Act
        Iterable<DirectSaleId> result = _repoDouble.findAllKeys();

        //Assert
        assertIterableEquals(List.of(new DirectSaleId("DS-12345678")), result);
    }

    @Test
    void shouldFindAll() {
        //Arrange
        when(_assemblerDouble.toDomain(_directSaleDataModelDouble)).thenReturn(_directSaleEntityDouble);
        when(_assemblerDouble.toDomain(dmOther)).thenReturn(_directSaleEntityOtherDouble);
        when(_iDirectSaleSpringDataRepoDouble.findAll()).thenReturn(List.of(_directSaleDataModelDouble,dmOther));

        //Act
        Iterable<DirectSale> result = _repoDouble.findAll();

        //Assert
        assertIterableEquals(List.of(_directSaleEntityDouble, _directSaleEntityOtherDouble), result);
    }

    @Test
    void shouldReturnDirectSaleWhenOfIdentityExists() {
        //Arrange
        when(_idDouble.toString()).thenReturn("DS-12345678");
        when(_iDirectSaleSpringDataRepoDouble.findById(_idDouble.toString())).thenReturn(Optional.of(_directSaleDataModelDouble));
        when(_assemblerDouble.toDomain(_directSaleDataModelDouble)).thenReturn(_directSaleEntityDouble);

        //Act
        Optional<DirectSale> result = _repoDouble.ofIdentity(_idDouble);

        //Assert
        assertTrue(result.isPresent());
    }

    @Test
    void shouldReturnEmptyWhenOfIdentityDoesNotExist(){
        //Arrange
        when(_idDouble.toString()).thenReturn("DS-12345678");
        when(_iDirectSaleSpringDataRepoDouble.findById(_idDouble.toString())).thenReturn(Optional.empty());

        //Act
        Optional<DirectSale> result = _repoDouble.ofIdentity(_idDouble);

        //Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnTrueWhenContainsOfIdentityDoesNotExist() {
        //Arrange
        String stringId = "DS-12345678";
        when(_iDirectSaleSpringDataRepoDouble.existsById(stringId)).thenReturn(true);
        when(_idDouble.toString()).thenReturn(stringId);

        //Act
        boolean result = _repoDouble.containsOfIdentity(_idDouble);

        //Assert
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenContainsOfIdentityDoesNotExist() {
        //Arrange
        String stringId = "DS-12345678";
        when(_iDirectSaleSpringDataRepoDouble.existsById(stringId)).thenReturn(false);
        when(_idDouble.toString()).thenReturn(stringId);

        //Act
        boolean result = _repoDouble.containsOfIdentity(_idDouble);

        //Assert
        assertFalse(result);
    }
}