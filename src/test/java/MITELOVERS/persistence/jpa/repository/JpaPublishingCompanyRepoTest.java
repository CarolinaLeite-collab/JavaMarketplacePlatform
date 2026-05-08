package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.publishingcompany.PublishingCompany;
import MITELOVERS.domain.valueobject.PublishingCompanyId;
import MITELOVERS.persistence.jpa.assembler.PublishingCompanyAssembler;
import MITELOVERS.persistence.jpa.datamodel.PublishingCompanyDataModel;
import MITELOVERS.persistence.springdata.IPublishingCompanySpringDataRepo;
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

@ExtendWith(MockitoExtension .class)
class JpaPublishingCompanyRepoTest {

    @Mock
    private IPublishingCompanySpringDataRepo _publishingCompanySpringDataRepoDouble;

    @Mock
    private PublishingCompanyAssembler _publishingCompanyAssemblerDouble;

    @InjectMocks
    private JpaPublishingCompanyRepo _jpaPublishingCompanyRepoDouble;

    @Test
    void saveShouldSaveDataModelAndReturnDomainObject() {
        // Arrange

        PublishingCompany publishingCompanyDouble =
                mock(PublishingCompany.class);

        PublishingCompanyDataModel dmToSaveDouble =
                mock(PublishingCompanyDataModel.class);

        PublishingCompanyDataModel savedDmDouble =
                mock(PublishingCompanyDataModel.class);

        PublishingCompany savedPublishingCompanyDouble =
                mock(PublishingCompany.class);

        when(_publishingCompanyAssemblerDouble.toDataModel(publishingCompanyDouble))
                .thenReturn(dmToSaveDouble);

        when(_publishingCompanySpringDataRepoDouble.save(dmToSaveDouble))
                .thenReturn(savedDmDouble);

        when(_publishingCompanyAssemblerDouble.toDomain(savedDmDouble))
                .thenReturn(savedPublishingCompanyDouble);

        // Act
        PublishingCompany result = _jpaPublishingCompanyRepoDouble.save(publishingCompanyDouble);

        // Assert
        assertEquals(savedPublishingCompanyDouble, result);
    }

    @Test
    void findAllKeysShouldReturnAllPublishingCompanyIds() {
        // Arrange

        PublishingCompanyDataModel dm1 =
                new PublishingCompanyDataModel("PORTO EDITORA");
        PublishingCompanyDataModel dm2 =
                new PublishingCompanyDataModel("LEYA");

        when(_publishingCompanySpringDataRepoDouble.findAll())
                .thenReturn(List.of(dm1, dm2));

        // Act
        Iterable<PublishingCompanyId> result = _jpaPublishingCompanyRepoDouble.findAllKeys();

        // Assert
        List<PublishingCompanyId> ids = new ArrayList<>();
        result.forEach(ids::add);

        assertEquals(2, ids.size());
        assertEquals("PORTO EDITORA", ids.get(0).toString());
        assertEquals("LEYA", ids.get(1).toString());
    }

    @Test
    void findAllShouldReturnAllPublishingCompanies() {
        // Arrange

        PublishingCompanyDataModel dm1 =
                mock(PublishingCompanyDataModel.class);
        PublishingCompanyDataModel dm2 =
                mock(PublishingCompanyDataModel.class);

        PublishingCompany pc1 =
                mock(PublishingCompany.class);
        PublishingCompany pc2 =
                mock(PublishingCompany.class);

        when(_publishingCompanySpringDataRepoDouble.findAll())
                .thenReturn(List.of(dm1, dm2));
        when(_publishingCompanyAssemblerDouble.toDomain(dm1))
                .thenReturn(pc1);
        when(_publishingCompanyAssemblerDouble.toDomain(dm2))
                .thenReturn(pc2);

        // Act
        Iterable<PublishingCompany> result = _jpaPublishingCompanyRepoDouble.findAll();

        // Assert
        List<PublishingCompany> publishingCompanies = new ArrayList<>();
        result.forEach(publishingCompanies::add);

        assertEquals(2, publishingCompanies.size());
        assertEquals(pc1, publishingCompanies.get(0));
        assertEquals(pc2, publishingCompanies.get(1));
    }

    @Test
    void ofIdentityShouldReturnPublishingCompanyWhenFound() {
        // Arrange

        PublishingCompanyId id =
                new PublishingCompanyId("PORTO EDITORA");
        PublishingCompanyDataModel dmDouble =
                mock(PublishingCompanyDataModel.class);
        PublishingCompany publishingCompanyDouble =
                mock(PublishingCompany.class);

        when(_publishingCompanySpringDataRepoDouble.findById("PORTO EDITORA"))
                .thenReturn(Optional.of(dmDouble));
        when(_publishingCompanyAssemblerDouble.toDomain(dmDouble))
                .thenReturn(publishingCompanyDouble);

        // Act
        Optional<PublishingCompany> result = _jpaPublishingCompanyRepoDouble.ofIdentity(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(publishingCompanyDouble, result.get());
    }

    @Test
    void ofIdentityShouldThrowExceptionWhenNotFound() {
        // Arrange

        PublishingCompanyId id =
                new PublishingCompanyId("PORTO EDITORA");

        when(_publishingCompanySpringDataRepoDouble.findById("PORTO EDITORA"))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> _jpaPublishingCompanyRepoDouble.ofIdentity(id)
        );
    }

    @Test
    void containsOfIdentityShouldReturnTrueWhenExists() {
        // Arrange

        PublishingCompanyId id =
                new PublishingCompanyId("PORTO EDITORA");

        when(_publishingCompanySpringDataRepoDouble.existsById("PORTO EDITORA"))
                .thenReturn(true);

        // Act
        boolean result = _jpaPublishingCompanyRepoDouble.containsOfIdentity(id);

        // Assert
        assertTrue(result);
    }

    @Test
    void containsOfIdentityShouldReturnFalseWhenDoesNotExist() {
        // Arrange

        PublishingCompanyId id =
                new PublishingCompanyId("PORTO EDITORA");

        when(_publishingCompanySpringDataRepoDouble.existsById("PORTO EDITORA"))
                .thenReturn(false);

        // Act
        boolean result = _jpaPublishingCompanyRepoDouble.containsOfIdentity(id);

        // Assert
        assertFalse(result);
    }
}