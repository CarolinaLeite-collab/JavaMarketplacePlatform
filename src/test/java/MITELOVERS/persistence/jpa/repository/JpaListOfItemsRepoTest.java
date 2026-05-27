package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.valueobject.ListOfItemsId;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.persistence.jpa.assembler.ListOfItemsAssembler;
import MITELOVERS.persistence.jpa.datamodel.ListOfItemsDataModel;
import MITELOVERS.persistence.springdata.IListOfItemsSpringDataRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JpaListOfItemsRepoTest {

    @Mock
    private IListOfItemsSpringDataRepo _springDataRepoDouble;

    @Mock
    private ListOfItemsAssembler _assemblerDouble;

    @InjectMocks
    private JpaListOfItemsRepo _repo;

    private ListOfItems _listDouble;
    private ListOfItems _listResultDouble;
    private ListOfItemsDataModel _dmDouble;
    private ListOfItemsDataModel _savedDmDouble;
    private ListOfItemsId _listIdDouble;

    @BeforeEach
    void setUp() {
        _listDouble = mock(ListOfItems.class);
        _listResultDouble = mock(ListOfItems.class);
        _dmDouble = mock(ListOfItemsDataModel.class);
        _savedDmDouble = mock(ListOfItemsDataModel.class);
        _listIdDouble = mock(ListOfItemsId.class);

    }

    @Test
    void saveShouldDelegateToAssemblerAndReturnReconstructedDomain() {
        // Arrange
        when(_assemblerDouble.toDataModel(_listDouble)).thenReturn(_dmDouble);
        when(_springDataRepoDouble.save(_dmDouble)).thenReturn(_savedDmDouble);
        when(_assemblerDouble.toDomain(_savedDmDouble)).thenReturn(_listResultDouble);

        // Act
        ListOfItems result = _repo.save(_listDouble);

        // Assert
        assertSame(_listResultDouble, result);
        verify(_assemblerDouble).toDataModel(_listDouble);
        verify(_springDataRepoDouble).save(_dmDouble);
        verify(_assemblerDouble).toDomain(_savedDmDouble);
    }

    @Test
    void findAllShouldReturnMappedDomainObjects() {
        // Arrange
        when(_springDataRepoDouble.findAll()).thenReturn(List.of(_dmDouble));
        when(_assemblerDouble.toDomain(_dmDouble)).thenReturn(_listDouble);

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
        when(_listIdDouble.toString()).thenReturn("LOI-ABC123");
        when(_springDataRepoDouble.findById("LOI-ABC123")).thenReturn(Optional.of(_dmDouble));
        when(_assemblerDouble.toDomain(_dmDouble)).thenReturn(_listDouble);

        // Act
        Optional<ListOfItems> result = _repo.ofIdentity(_listIdDouble);

        // Assert
        assertTrue(result.isPresent());
        assertSame(_listDouble, result.get());
    }

    @Test
    void ofIdentityShouldThrowWhenNotExists() {
        // Arrange
        when(_listIdDouble.toString()).thenReturn("LOI-ABC123");
        when(_springDataRepoDouble.findById("LOI-ABC123")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> _repo.ofIdentity(_listIdDouble));
    }

    @Test
    void containsOfIdentityShouldReturnTrueWhenExists() {
        // Arrange
        when(_springDataRepoDouble.existsById("LOI-ABC123")).thenReturn(true);
        when(_listIdDouble.toString()).thenReturn("LOI-ABC123");

        // Act & Assert
        assertTrue(_repo.containsOfIdentity(_listIdDouble));
    }

    @Test
    void containsOfIdentityShouldReturnFalseWhenNotExists() {
        // Arrange
        when(_listIdDouble.toString()).thenReturn("LOI-ABC123");
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

    @Test
    void findByUserIdShouldReturnListsOfGivenUserId(){
        // Arrange
        ListOfItemsDataModel listDmDouble1 = mock(ListOfItemsDataModel.class);
        ListOfItemsDataModel listDmDouble2 = mock(ListOfItemsDataModel.class);
        UserId userIdDouble = mock(UserId.class);

        List<ListOfItemsDataModel> dms = List.of(listDmDouble1, listDmDouble2);

        when(_springDataRepoDouble.findListOfItemsByUserId(userIdDouble)).thenReturn(dms);
        when(_assemblerDouble.toDomain(listDmDouble1)).thenReturn(_listDouble);
        when(_assemblerDouble.toDomain(listDmDouble2)).thenReturn(_listResultDouble);

        List<ListOfItems> expectedList = new ArrayList<>();

        // Act
        expectedList.add(_listDouble);
        expectedList.add(_listResultDouble);

        List<ListOfItems> result = _repo.findListOfItemsByUserId(userIdDouble);

        // Assert
        assertEquals(expectedList, result);
    }

}