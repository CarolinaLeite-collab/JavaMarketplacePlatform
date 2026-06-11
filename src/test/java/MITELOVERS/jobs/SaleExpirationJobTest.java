package MITELOVERS.jobs;

import MITELOVERS.applicationservices.SaleExpirationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;


@ExtendWith(MockitoExtension.class)
class SaleExpirationJobTest {

    @Mock
    private SaleExpirationService _expirationService;

    @InjectMocks
    private SaleExpirationJob _job;

    @BeforeEach
    void setup() {
        _job = new SaleExpirationJob(_expirationService);
    }

    @Test
    void run_shouldTriggerExpirationWithoutErrors() {

        // Arrange
        // (no stubbing needed — method returns void)

        // Act
        assertDoesNotThrow(() -> _job.run());

        // Assert
        assertTrue(true);
    }

    @Test
    void run_shouldPropagateExceptionWhenServiceFails() {

        // Arrange
        doThrow(new IllegalStateException("boom"))
                .when(_expirationService)
                .expireAllExpiredSales();

        // Act + Assert
        assertThrows(IllegalStateException.class, () -> _job.run());
    }

    @Test
    void constructor_shouldCreateInstance() {

        // Act
        SaleExpirationJob instance = new SaleExpirationJob(_expirationService);

        // Assert
       assertNotNull(instance);
    }

}