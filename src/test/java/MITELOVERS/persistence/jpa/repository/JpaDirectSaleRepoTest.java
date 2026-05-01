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
    private JpaDirectSaleRepo repo;

    @Mock
    private IDirectSaleSpringDataRepo iDirectSaleSpringDataRepo;

    @Mock
    private DirectSaleDataModel directSaleDataModel;

    @Mock
    private DirectSaleDataModel dmOther;

    @Mock
    private DirectSale directSaleEntity;

    @Mock
    private DirectSale directSaleEntityOther;
    @Mock
    private DirectSaleAssembler assembler;

    @Mock
    private DirectSaleId id;

    @Test
    void shouldSaveDirectSale() {
        //Arrange
        when(assembler.domain2DM(directSaleEntity)).thenReturn(directSaleDataModel);
        when(iDirectSaleSpringDataRepo.save(directSaleDataModel)).thenReturn(directSaleDataModel);
        when(assembler.DM2Domain(directSaleDataModel)).thenReturn(directSaleEntity);

        //Act
        DirectSale result = repo.save(directSaleEntity);

        //Assert
        assertSame(result,directSaleEntity);

    }

    @Test
    void shouldFindAllKeys() {
        //Arrange
        when(directSaleDataModel.getDirectSaleId()).thenReturn("DS-12345678");
        when(iDirectSaleSpringDataRepo.findAll()).thenReturn(List.of(directSaleDataModel));

        //Act
        Iterable<DirectSaleId> result = repo.findAllKeys();

        //Assert
        assertIterableEquals(List.of(new DirectSaleId("DS-12345678")), result);
    }

    @Test
    void shouldFindAll() {
        //Arrange
        when(assembler.DM2Domain(directSaleDataModel)).thenReturn(directSaleEntity);
        when(assembler.DM2Domain(dmOther)).thenReturn(directSaleEntityOther);
        when(iDirectSaleSpringDataRepo.findAll()).thenReturn(List.of(directSaleDataModel,dmOther));

        //Act
        Iterable<DirectSale> result = repo.findAll();

        //Assert
        assertIterableEquals(List.of(directSaleEntity, directSaleEntityOther), result);
    }

    @Test
    void shouldReturnDirectSaleWhenOfIdentityExists() {
        //Arrange
        when(id.toString()).thenReturn("DS-12345678");
        when(iDirectSaleSpringDataRepo.findById(id.toString())).thenReturn(Optional.of(directSaleDataModel));
        when(assembler.DM2Domain(directSaleDataModel)).thenReturn(directSaleEntity);

        //Act
        Optional<DirectSale> result = repo.ofIdentity(id);

        //Assert
        assertTrue(result.isPresent());
    }

    @Test
    void shouldReturnEmptyWhenOfIdentityDoesNotExist(){
        //Arrange
        when(id.toString()).thenReturn("DS-12345678");
        when(iDirectSaleSpringDataRepo.findById(id.toString())).thenReturn(Optional.empty());

        //Act
        Optional<DirectSale> result = repo.ofIdentity(id);

        //Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnTrueWhenContainsOfIdentityDoesNotExist() {
        //Arrange
        String stringId = "DS-12345678";
        when(iDirectSaleSpringDataRepo.existsById(stringId)).thenReturn(true);
        when(id.toString()).thenReturn(stringId);

        //Act
        boolean result = repo.containsOfIdentity(id);

        //Assert
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenContainsOfIdentityDoesNotExist() {
        //Arrange
        String stringId = "DS-12345678";
        when(iDirectSaleSpringDataRepo.existsById(stringId)).thenReturn(false);
        when(id.toString()).thenReturn(stringId);

        //Act
        boolean result = repo.containsOfIdentity(id);

        //Assert
        assertFalse(result);
    }
}