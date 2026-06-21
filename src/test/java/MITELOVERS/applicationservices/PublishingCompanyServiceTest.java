package MITELOVERS.applicationservices;

import MITELOVERS.domain.publishingcompany.PublishingCompany;
import MITELOVERS.domain.publishingcompany.PublishingCompanyFactory;
import MITELOVERS.domain.repository.IPublishingCompanyRepo;
import MITELOVERS.domain.valueobject.PublishingCompanyId;
import MITELOVERS.dto.request.PublishingCompanyRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PublishingCompanyServiceTest {

    private PublishingCompanyFactory _publishingCompanyFactoryDouble;
    private IPublishingCompanyRepo _iPublishingCompanyRepoDouble;

    private PublishingCompany _publishingCompanyDouble;
    private PublishingCompanyId _publishingCompanyIdDouble;

    @BeforeEach
    void setUp() {
        _publishingCompanyFactoryDouble = mock(PublishingCompanyFactory.class);
        _iPublishingCompanyRepoDouble = mock(IPublishingCompanyRepo.class);

        _publishingCompanyDouble = mock(PublishingCompany.class);
        _publishingCompanyIdDouble = mock(PublishingCompanyId.class);

        when(_publishingCompanyDouble.identity()).thenReturn(_publishingCompanyIdDouble);
    }

    @Test
    void registerPublishingCompanyNewReturnsPublishingCompany() {
        // Arrange
        PublishingCompanyRequestDTO dtoDouble = mock(PublishingCompanyRequestDTO.class);

        when(dtoDouble.getPublishingCompanyName()).thenReturn("Porto Editora");
        when(_publishingCompanyFactoryDouble.createPublishingCompany("Porto Editora"))
                .thenReturn(_publishingCompanyDouble);
        when(_iPublishingCompanyRepoDouble.containsOfIdentity(_publishingCompanyIdDouble)).thenReturn(false);
        when(_iPublishingCompanyRepoDouble.save(_publishingCompanyDouble)).thenReturn(_publishingCompanyDouble);

        // SUT
        PublishingCompanyService service = new PublishingCompanyService(
                _publishingCompanyFactoryDouble,
                _iPublishingCompanyRepoDouble);

        // Act
        PublishingCompany result = service.registerPublishingCompany(dtoDouble);

        // Assert
        assertNotNull(result);
        assertEquals(_publishingCompanyDouble, result);
    }

    @Test
    void registerPublishingCompanyAlreadyExistsThrowsException() {
        // Arrange
        PublishingCompanyRequestDTO dtoDouble = mock(PublishingCompanyRequestDTO.class);

        when(dtoDouble.getPublishingCompanyName()).thenReturn("Porto Editora");
        when(_publishingCompanyFactoryDouble.createPublishingCompany("Porto Editora"))
                .thenReturn(_publishingCompanyDouble);
        when(_iPublishingCompanyRepoDouble.containsOfIdentity(_publishingCompanyIdDouble)).thenReturn(true);

        // SUT
        PublishingCompanyService service = new PublishingCompanyService(
                _publishingCompanyFactoryDouble,
                _iPublishingCompanyRepoDouble);

        // Act & Assert
        assertThrows(IllegalStateException.class, () ->
                service.registerPublishingCompany(dtoDouble));
    }

    @Test
    void getAllPublishingCompaniesReturnsListOfPublishingCompanies() {
        // Arrange
        when(_iPublishingCompanyRepoDouble.findAll()).thenReturn(List.of(_publishingCompanyDouble));

        // SUT
        PublishingCompanyService service = new PublishingCompanyService(
                _publishingCompanyFactoryDouble,
                _iPublishingCompanyRepoDouble);

        // Act
        List<PublishingCompany> result = service.getAllPublishingCompanies();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getAllPublishingCompaniesEmptyReturnsEmptyList() {
        // Arrange
        when(_iPublishingCompanyRepoDouble.findAll()).thenReturn(List.of());

        // SUT
        PublishingCompanyService service = new PublishingCompanyService(
                _publishingCompanyFactoryDouble,
                _iPublishingCompanyRepoDouble);

        // Act
        List<PublishingCompany> result = service.getAllPublishingCompanies();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getPublishingCompanyByIdReturns() {
        // Arrange
        when(_iPublishingCompanyRepoDouble.ofIdentity(any())).thenReturn(Optional.of(_publishingCompanyDouble));

        // SUT
        PublishingCompanyService service = new PublishingCompanyService(
                _publishingCompanyFactoryDouble,
                _iPublishingCompanyRepoDouble);

        // Act
        PublishingCompany result = service.getPublishingCompanyById("PORTO EDITORA");

        // Assert
        assertNotNull(result);
    }

    @Test
    void getPublishingCompanyByIdNotFoundThrowsException() {
        // Arrange
        when(_iPublishingCompanyRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // SUT
        PublishingCompanyService service = new PublishingCompanyService(
                _publishingCompanyFactoryDouble,
                _iPublishingCompanyRepoDouble);

        // Act & Assert
        assertThrows(NoSuchElementException.class, () ->
                service.getPublishingCompanyById("UNKNOWN"));
    }
}
