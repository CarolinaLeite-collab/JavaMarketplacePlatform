package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.valueobject.EditionId;
import MITELOVERS.persistence.jpa.assembler.EditionAssembler;
import MITELOVERS.persistence.jpa.datamodel.EditionDataModel;
import MITELOVERS.persistence.springdata.IEditionSpringDataRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaEditionRepoTest {

    @Mock
    private IEditionSpringDataRepo _springDataRepoDouble;

    @Mock
    private EditionAssembler _assemblerDouble;

    @InjectMocks
    private JpaEditionRepo _jpaRepoDouble;

    @Test
    void shouldSaveEdition() {
        // Arrange
        Edition editionDouble = mock(Edition.class);
        EditionDataModel dataModelDouble = mock(EditionDataModel.class);
        EditionDataModel savedDataModelDouble = mock(EditionDataModel.class);
        Edition savedEditionDouble = mock(Edition.class);

        when(_assemblerDouble.toDataModel(editionDouble)).thenReturn(dataModelDouble);
        when(_springDataRepoDouble.save(dataModelDouble)).thenReturn(savedDataModelDouble);
        when(_assemblerDouble.toDomain(savedDataModelDouble)).thenReturn(savedEditionDouble);

        // Act
        Edition result = _jpaRepoDouble.save(editionDouble);

        // Assert
        assertEquals(savedEditionDouble, result);
    }

    @Test
    void shouldReturnAllKeys() {

        //Arrange
        EditionDataModel dataModelDouble1 = mock(EditionDataModel.class);
        EditionDataModel dataModelDouble2 = mock(EditionDataModel.class);

        when(dataModelDouble1.getId()).thenReturn("1");
        when(dataModelDouble2.getId()).thenReturn("2");

        List<EditionDataModel> dataModels = new ArrayList<>();
        dataModels.add(dataModelDouble1);
        dataModels.add(dataModelDouble2);

        when(_springDataRepoDouble.findAll()).thenReturn(dataModels);

        // Act
        Iterable<EditionId> result = _jpaRepoDouble.findAllKeys();

        List<EditionId> ids = new ArrayList<>();

        for (EditionId editionId : result) {
            ids.add(editionId);
        }

        // Assert
        assertEquals(2, ids.size());
        assertEquals("1", ids.get(0).toString());
        assertEquals("2", ids.get(1).toString());
    }

    @Test
    void shouldReturnAllEditions() {

        // Arrange
        EditionDataModel dataModelDouble1 = mock(EditionDataModel.class);
        EditionDataModel dataModelDouble2 = mock(EditionDataModel.class);

        Edition editionDouble1 = mock(Edition.class);
        Edition editionDouble2 = mock(Edition.class);

        List<EditionDataModel> dataModels = new ArrayList<>();
        dataModels.add(dataModelDouble1);
        dataModels.add(dataModelDouble2);

        when(_springDataRepoDouble.findAll()).thenReturn(dataModels);
        when(_assemblerDouble.toDomain(dataModelDouble1)).thenReturn(editionDouble1);
        when(_assemblerDouble.toDomain(dataModelDouble2)).thenReturn(editionDouble2);

        // Act
        Iterable<Edition> result = _jpaRepoDouble.findAll();

        List<Edition> editions = new ArrayList<>();
        for (Edition edition : result) {
            editions.add(edition);
        }

        // Assert
        assertEquals(2, editions.size());
        assertTrue(editions.contains(editionDouble1));
        assertTrue(editions.contains(editionDouble2));

    }

    @Test
    void shouldReturnEditionById() {

        // Arrange
        EditionId idDouble = mock(EditionId.class);
        EditionDataModel dataModelDouble = mock(EditionDataModel.class);
        Edition editionDouble = mock(Edition.class);

        when(idDouble.toString()).thenReturn("1");
        when(_springDataRepoDouble.findById("1")).thenReturn(Optional.of(dataModelDouble));
        when(_assemblerDouble.toDomain(dataModelDouble)).thenReturn(editionDouble);

        // Act
        Optional<Edition> result = _jpaRepoDouble.ofIdentity(idDouble);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(editionDouble, result.get());
    }

    @Test
    void shouldThrowExceptionWhenEditionNotFound() {

        // Arrange
        EditionId idDouble = mock(EditionId.class);

        when(idDouble.toString()).thenReturn("1");
        when(_springDataRepoDouble.findById("1")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> _jpaRepoDouble.ofIdentity(idDouble));
    }

    @Test
    void shouldReturnTrueWhenEditionExists() {

        // Arrange
        EditionId idDouble = mock(EditionId.class);

        when(idDouble.toString()).thenReturn("1");
        when(_springDataRepoDouble.existsById("1")).thenReturn(true);

        // Act
        boolean result = _jpaRepoDouble.containsOfIdentity(idDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenEditionNotExists() {

        // Arrange
        EditionId idDouble = mock(EditionId.class);

        when(idDouble.toString()).thenReturn("1");
        when(_springDataRepoDouble.existsById("1")).thenReturn(false);

        // Act
        boolean result = _jpaRepoDouble.containsOfIdentity(idDouble);

        // Assert
        assertFalse(result);
    }
}