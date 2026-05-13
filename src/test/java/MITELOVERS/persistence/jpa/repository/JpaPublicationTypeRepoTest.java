package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.valueobject.PublicationTypeId;
import MITELOVERS.persistence.jpa.assembler.PublicationTypeAssembler;
import MITELOVERS.persistence.jpa.datamodel.PublicationTypeDataModel;
import MITELOVERS.persistence.springdata.IPublicationTypeSpringDataRepo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("jpa")
class JpaPublicationTypeRepoTest {

    // SUT
    @InjectMocks
    private JpaPublicationTypeRepo _jpaPublicationTypeRepo;

    @Mock
    private IPublicationTypeSpringDataRepo _springDataRepoDouble;

    @Mock
    private PublicationTypeAssembler _assemblerDouble;

    @Mock
    private PublicationType _publicationTypeDouble;

    @Mock
    private PublicationTypeId _publicationTypeIdDouble;

    @Mock
    private PublicationTypeDataModel _dataModelDouble;


    @Test
    void shouldSaveAndReturnPublicationType() {

        // Arrange
        when(_assemblerDouble.toDataModel(_publicationTypeDouble)).thenReturn(_dataModelDouble);
        when(_springDataRepoDouble.save(_dataModelDouble)).thenReturn(_dataModelDouble);
        when(_assemblerDouble.toDomain(_dataModelDouble)).thenReturn(_publicationTypeDouble);

        // Act
        PublicationType result = _jpaPublicationTypeRepo.save(_publicationTypeDouble);

        // Assert
        assertEquals(_publicationTypeDouble, result);

    }

    @Test
    void shouldReturnAllPublicationTypes() {

        // Arrange
        List<PublicationTypeDataModel> dataModels = List.of(_dataModelDouble);
        when(_springDataRepoDouble.findAll()).thenReturn(dataModels);
        when(_assemblerDouble.toDomain(_dataModelDouble)).thenReturn(_publicationTypeDouble);

        // Act
        List<PublicationType> result = new ArrayList<>();
        _jpaPublicationTypeRepo.findAll().forEach(result::add);

        // Assert
        assertEquals(1, result.size());
        assertEquals(_publicationTypeDouble, result.get(0));

    }

    @Test
    void shouldReturnAllKeys() {

        // Arrange
        when(_dataModelDouble.getPublicationTypeId()).thenReturn("BOOK");
        when(_springDataRepoDouble.findAll()).thenReturn(List.of(_dataModelDouble));

        // Act
        List<PublicationTypeId> result = new ArrayList<>();
        _jpaPublicationTypeRepo.findAllKeys().forEach(result::add);

        // Assert
        assertEquals(1, result.size());
        assertEquals("BOOK", result.get(0).toString());

    }

    @Test
    void shouldReturnPublicationTypeWhenIdPresent() {

        // Arrange
        when(_publicationTypeIdDouble.toString()).thenReturn("BOOK");
        when(_springDataRepoDouble.findById("BOOK")).thenReturn(Optional.of(_dataModelDouble));
        when(_assemblerDouble.toDomain(_dataModelDouble)).thenReturn(_publicationTypeDouble);

        // Act
        Optional<PublicationType> result = _jpaPublicationTypeRepo.ofIdentity(_publicationTypeIdDouble);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(_publicationTypeDouble, result.get());

    }

    @Test
    void shouldReturnEmptyListWhenNoPublicationTypes() {

        // Arrange
        when(_springDataRepoDouble.findAll()).thenReturn(List.of());

        // Act
        List<PublicationType> result = new ArrayList<>();
        _jpaPublicationTypeRepo.findAll().forEach(result::add);

        // Assert
        assertTrue(result.isEmpty());

    }

    @Test
    void shouldReturnTrueWhenIdPresent() {

        // Arrange
        when(_publicationTypeIdDouble.toString()).thenReturn("BOOK");
        when(_springDataRepoDouble.existsById("BOOK")).thenReturn(true);

        // Act
        boolean result = _jpaPublicationTypeRepo.containsOfIdentity(_publicationTypeIdDouble);

        // Assert
        assertTrue(result);

    }

    @Test
    void shouldReturnFalseWhenIdNotPresent() {

        // Arrange
        when(_publicationTypeIdDouble.toString()).thenReturn("BOOK");
        when(_springDataRepoDouble.existsById("BOOK")).thenReturn(false);

        // Act
        boolean result = _jpaPublicationTypeRepo.containsOfIdentity(_publicationTypeIdDouble);

        // Assert
        assertFalse(result);

    }

    @Test
    void shouldReturnEmptyOptionalWhenIdNotPresent() {
        // Arrange
        when(_publicationTypeIdDouble.toString()).thenReturn("BOOK");
        when(_springDataRepoDouble.findById("BOOK")).thenReturn(Optional.empty());

        // Act
        Optional<PublicationType> result = _jpaPublicationTypeRepo.ofIdentity(_publicationTypeIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

}