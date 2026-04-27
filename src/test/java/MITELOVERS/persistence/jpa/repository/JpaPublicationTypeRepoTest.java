package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.appraisalentity.AppraisalEntity;
import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.valueobject.PublicationTypeId;
import MITELOVERS.persistence.jpa.assembler.PublicationTypeAssembler;
import MITELOVERS.persistence.jpa.datamodel.PublicationTypeDataModel;
import MITELOVERS.persistence.springdata.IPublicationTypeSpringDataRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JpaPublicationTypeRepoTest {

    private IPublicationTypeSpringDataRepo _springDataRepoDouble;
    private PublicationTypeAssembler _assemblerDouble;
    private PublicationType _publicationTypeDouble;
    private PublicationTypeId _publicationTypeIdDouble;
    private PublicationTypeDataModel _dataModelDouble;

    @BeforeEach
    void setUp() {

        _springDataRepoDouble = mock(IPublicationTypeSpringDataRepo.class);
        _assemblerDouble = mock(PublicationTypeAssembler.class);
        _publicationTypeDouble = mock(PublicationType.class);
        _publicationTypeIdDouble = mock(PublicationTypeId.class);
        _dataModelDouble = mock(PublicationTypeDataModel.class);

    }

    @Test
    void shouldConstructPublicationTypeRepo() {

        // SUT & Act
        JpaPublicationTypeRepo jpaRepo = new JpaPublicationTypeRepo(_springDataRepoDouble, _assemblerDouble);

    }

    @Test
    void shouldSaveAndReturnPublicationType() {

        // Arrange
        when(_assemblerDouble.domain2DM(_publicationTypeDouble)).thenReturn(_dataModelDouble);
        when(_springDataRepoDouble.save(_dataModelDouble)).thenReturn(_dataModelDouble);
        when(_assemblerDouble.DM2Domain(_dataModelDouble)).thenReturn(_publicationTypeDouble);

        // SUT
        JpaPublicationTypeRepo sut = new JpaPublicationTypeRepo(_springDataRepoDouble, _assemblerDouble);

        // Act
        PublicationType result = sut.save(_publicationTypeDouble);

        // Assert
        assertEquals(_publicationTypeDouble, result);

    }

    @Test
    void shouldReturnAllPublicationTypes() {

        // Arrange
        List<PublicationTypeDataModel> dataModels = List.of(_dataModelDouble);
        when(_springDataRepoDouble.findAll()).thenReturn(dataModels);
        when(_assemblerDouble.DM2Domain(_dataModelDouble)).thenReturn(_publicationTypeDouble);

        // SUT
        JpaPublicationTypeRepo sut = new JpaPublicationTypeRepo(_springDataRepoDouble, _assemblerDouble);

        // Act
        List<PublicationType> result = new ArrayList<>();
        sut.findAll().forEach(result::add);

        // Assert
        assertEquals(1, result.size());
        assertEquals(_publicationTypeDouble, result.get(0));

    }

    @Test
    void shouldReturnAllKeys() {

        // Arrange
        when(_dataModelDouble.getPublicationTypeId()).thenReturn("BOOK");
        when(_springDataRepoDouble.findAll()).thenReturn(List.of(_dataModelDouble));

        // SUT
        JpaPublicationTypeRepo sut = new JpaPublicationTypeRepo(_springDataRepoDouble, _assemblerDouble);

        // Act
        List<PublicationTypeId> result = new ArrayList<>();
        sut.findAllKeys().forEach(result::add);

        // Assert
        assertEquals(1, result.size());

    }

    @Test
    void shouldReturnPublicationTypeWhenIdPresent() {

        // Arrange
        when(_publicationTypeIdDouble.toString()).thenReturn("BOOK");
        when(_springDataRepoDouble.findById("BOOK")).thenReturn(Optional.of(_dataModelDouble));
        when(_assemblerDouble.DM2Domain(_dataModelDouble)).thenReturn(_publicationTypeDouble);

        // SUT
        JpaPublicationTypeRepo sut = new JpaPublicationTypeRepo(_springDataRepoDouble, _assemblerDouble);

        // Act
        Optional<PublicationType> result = sut.ofIdentity(_publicationTypeIdDouble);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(_publicationTypeDouble, result.get());

    }

    @Test
    void shouldReturnEmptyOptionalWhenIdNotPresent() {

        // Arrange
        when(_publicationTypeIdDouble.toString()).thenReturn("BOOK");
        when(_springDataRepoDouble.findById("BOOK")).thenReturn(Optional.of(_dataModelDouble));
        when(_assemblerDouble.DM2Domain(_dataModelDouble)).thenReturn(_publicationTypeDouble);

        // SUT
        JpaPublicationTypeRepo sut = new JpaPublicationTypeRepo(_springDataRepoDouble, _assemblerDouble);

        // Act
        Optional<PublicationType> result = sut.ofIdentity(_publicationTypeIdDouble);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(_publicationTypeDouble, result.get());

    }

    @Test
    void shouldReturnTrueWhenIdPresent() {

        // Arrange
        when(_publicationTypeIdDouble.toString()).thenReturn("BOOK");
        when(_springDataRepoDouble.existsById("BOOK")).thenReturn(true);

        // SUT
        JpaPublicationTypeRepo sut = new JpaPublicationTypeRepo(_springDataRepoDouble, _assemblerDouble);

        // Act
        boolean result = sut.containsOfIdentity(_publicationTypeIdDouble);

        // Assert
        assertTrue(result);

    }

    @Test
    void shouldReturnFalseWhenIdNotPresent() {

        // Arrange
        when(_publicationTypeIdDouble.toString()).thenReturn("BOOK");
        when(_springDataRepoDouble.existsById("BOOK")).thenReturn(false);

        // SUT
        JpaPublicationTypeRepo sut = new JpaPublicationTypeRepo(_springDataRepoDouble, _assemblerDouble);

        // Act
        boolean result = sut.containsOfIdentity(_publicationTypeIdDouble);

        // Assert
        assertFalse(result);

    }

}