package MITELOVERS.persistence.mem;

import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemEditionRepoTest {
    
    private Edition _editionDouble;
    private EditionId _editionIdDouble;

    private PublicationTypeId _typeIdDouble;
    private Identifier _identifierDouble;
    private PublicationId _publicationIdDouble;
    private PublishingCompanyId _publishingCompanyIdDouble;
    private Language _languageDouble;
    private Dimension _dimensionDouble;
    private Weight _weightDouble;
    private NumberOfPages _numberOfPagesDouble;
    private EditionNumber _editionNumberDouble;
    private Binding _bindingDouble;

    private static final String expectedMessageIdentifierAlreadyExists =
            "An Edition with this identifier already exists!";
    private static final String expectedMessageEditionAlreadyExists =
            "Edition already exists!";

    @BeforeEach
    void setUp() {

        _editionDouble = mock(Edition.class);
        _editionIdDouble = mock(EditionId.class);
        when(_editionDouble.identity()).thenReturn(_editionIdDouble);

        _typeIdDouble = mock(PublicationTypeId.class);
        _identifierDouble = mock(Identifier.class);
        _publicationIdDouble = mock(PublicationId.class);
        _publishingCompanyIdDouble = mock(PublishingCompanyId.class);
        _languageDouble = mock(Language.class);
        _dimensionDouble = mock(Dimension.class);
        _weightDouble = mock(Weight.class);
        _numberOfPagesDouble = mock(NumberOfPages.class);
        _editionNumberDouble = mock(EditionNumber.class);
        _bindingDouble = mock(Binding.class);
    }

    @Test
    void saveShouldStoreEditionAndReturnIt() {
        // SUT
        MemEditionRepo memoRepo = new MemEditionRepo();

        // Act
        Edition result = memoRepo.save(_editionDouble);

        // Assert
        assertEquals(_editionDouble, result);
    }

    @Test
    void findAllShouldReturnSavedEditions() {
        // SUT
        MemEditionRepo memoRepo = new MemEditionRepo();

        Edition anotherEditionDouble = mock(Edition.class);
        EditionId anotherEditionIdDouble = mock(EditionId.class);
        when(anotherEditionDouble.identity()).thenReturn(anotherEditionIdDouble);

        memoRepo.save(_editionDouble);
        memoRepo.save(anotherEditionDouble);

        // Act
        Iterable<Edition> result = memoRepo.findAll();

        // Assert
        List<Edition> editions = new ArrayList<>();
        result.forEach(editions::add);

        assertEquals(2, editions.size());
        assertTrue(editions.contains(_editionDouble));
        assertTrue(editions.contains(anotherEditionDouble));
    }

    @Test
    void ofIdentityShouldReturnEditionWhenItExists() {
        // SUT
        MemEditionRepo memoRepo = new MemEditionRepo();
        memoRepo.save(_editionDouble);

        // Act
        Optional<Edition> result = memoRepo.ofIdentity(_editionIdDouble);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(_editionDouble, result.get());
    }

    @Test
    void ofIdentityShouldReturnEmptyWhenEditionDoesNotExist() {
        // SUT
        MemEditionRepo memoRepo = new MemEditionRepo();

        // Act
        Optional<Edition> result = memoRepo.ofIdentity(_editionIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void containsOfIdentityShouldReturnTrueWhenEditionExists() {
        // SUT
        MemEditionRepo memoRepo = new MemEditionRepo();
        memoRepo.save(_editionDouble);

        // Act
        boolean result = memoRepo.containsOfIdentity(_editionIdDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void containsOfIdentityShouldReturnFalseWhenEditionDoesNotExist() {
        // SUT
        MemEditionRepo memoRepo = new MemEditionRepo();

        // Act
        boolean result = memoRepo.containsOfIdentity(_editionIdDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void findAllKeysShouldReturnAllEditionIds() {
        // SUT
        MemEditionRepo memoRepo = new MemEditionRepo();

        Edition edition1 = mock(Edition.class);
        EditionId id1 = mock(EditionId.class);
        when(edition1.identity()).thenReturn(id1);

        Edition edition2 = mock(Edition.class);
        EditionId id2 = mock(EditionId.class);
        when(edition2.identity()).thenReturn(id2);

        memoRepo.save(edition1);
        memoRepo.save(edition2);

        // Act
        List<EditionId> result = memoRepo.findAllKeys();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(id1));
        assertTrue(result.contains(id2));
    }

}
