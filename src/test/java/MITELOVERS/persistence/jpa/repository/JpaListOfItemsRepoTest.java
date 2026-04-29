package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.valueobject.ListOfItemsId;
import MITELOVERS.persistence.jpa.assembler.ListOfItemsAssembler;
import MITELOVERS.persistence.jpa.datamodel.ListOfItemsDataModel;
import MITELOVERS.persistence.jpa.springdata.IListOfItemsSpringDataRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JpaListOfItemsRepoTest {

    private IListOfItemsSpringDataRepo _springDataRepoDouble;
    private ListOfItemsAssembler _assemblerDouble;
    private ListOfItems _listDouble;
    private ListOfItems _listResultDouble;
    private ListOfItemsDataModel _dmDouble;
    private ListOfItemsDataModel _savedDmDouble;
    private ListOfItemsId _listIdDouble;
    private JpaListOfItemsRepo _repo;

    @BeforeEach
    void setUp() {
        _springDataRepoDouble = mock(IListOfItemsSpringDataRepo.class);
        _assemblerDouble = mock(ListOfItemsAssembler.class);
        _listDouble = mock(ListOfItems.class);
        _listResultDouble = mock(ListOfItems.class);
        _dmDouble = mock(ListOfItemsDataModel.class);
        _savedDmDouble = mock(ListOfItemsDataModel.class);
        _listIdDouble = mock(ListOfItemsId.class);

        when(_listDouble.identity()).thenReturn(_listIdDouble);
        when(_listIdDouble.toString()).thenReturn("LOI-ABC123");

        _repo = new JpaListOfItemsRepo(_springDataRepoDouble, _assemblerDouble);
    }

    @Test
    void saveShouldDelegateToAssemblerAndReturnReconstructedDomain() {
        // Arrange
        when(_assemblerDouble.domain2DM(_listDouble)).thenReturn(_dmDouble);
        when(_springDataRepoDouble.save(_dmDouble)).thenReturn(_savedDmDouble);
        when(_assemblerDouble.DM2Domain(_savedDmDouble)).thenReturn(_listResultDouble);

        // Act
        ListOfItems result = _repo.save(_listDouble);

        // Assert
        assertSame(_listResultDouble, result);
        verify(_assemblerDouble).domain2DM(_listDouble);
        verify(_springDataRepoDouble).save(_dmDouble);
        verify(_assemblerDouble).DM2Domain(_savedDmDouble);
    }

    @Test
    void findAllShouldReturnMappedDomainObjects() {
        // Arrange
        when(_springDataRepoDouble.findAll()).thenReturn(List.of(_dmDouble));
        when(_assemblerDouble.DM2Domain(_dmDouble)).thenReturn(_listDouble);

        // Act
        Iterable<ListOfItems> result = _repo.findAll();

        // Assert
        int count = 0;
        for (ListOfItems ignored : result) count++;
        assertEquals(1, count);
    }

    @Test
    void ofIdentityShouldReturnMappedDomainObjectWhenExists() {
        // Arrange
        when(_springDataRepoDouble.findById("LOI-ABC123")).thenReturn(Optional.of(_dmDouble));
        when(_assemblerDouble.DM2Domain(_dmDouble)).thenReturn(_listDouble);

        // Act
        Optional<ListOfItems> result = _repo.ofIdentity(_listIdDouble);

        // Assert
        assertTrue(result.isPresent());
        assertSame(_listDouble, result.get());
    }

    @Test
    void ofIdentityShouldThrowWhenNotExists() {
        // Arrange
        when(_springDataRepoDouble.findById("LOI-ABC123")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> _repo.ofIdentity(_listIdDouble));
    }

    @Test
    void containsOfIdentityShouldReturnTrueWhenExists() {
        // Arrange
        when(_springDataRepoDouble.existsById("LOI-ABC123")).thenReturn(true);

        // Act & Assert
        assertTrue(_repo.containsOfIdentity(_listIdDouble));
    }

    @Test
    void containsOfIdentityShouldReturnFalseWhenNotExists() {
        // Arrange
        when(_springDataRepoDouble.existsById("LOI-ABC123")).thenReturn(false);

        // Act & Assert
        assertFalse(_repo.containsOfIdentity(_listIdDouble));
    }

    @Test
    void findAllKeysShouldReturnAllIds() {
        // Arrange
        ListOfItemsDataModel dm = mock(ListOfItemsDataModel.class);
        when(dm.getListOfItemsId()).thenReturn("LOI-ABC123");
        when(_springDataRepoDouble.findAll()).thenReturn(List.of(dm));

        // Act
        List<ListOfItemsId> keys = _repo.findAllKeys();

        // Assert
        assertEquals(1, keys.size());
        assertEquals("LOI-ABC123", keys.get(0).toString());
    }

}