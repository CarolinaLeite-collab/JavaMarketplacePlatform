package TOPSECRET.persistence.mem;

import TOPSECRET.domain.edition.Edition;
import TOPSECRET.domain.edition.EditionBook;
import TOPSECRET.domain.edition.EditionFactory;
import TOPSECRET.domain.edition.EditionMagazine;
import TOPSECRET.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemoEditionRepoTest {

    private EditionFactory _editionFactoryDouble;
    private EditionId _editionIdDouble;
    private EditionBook _editionBookDouble;

    private ISBN _bookIdIsbnDouble;
    private NoIsbnBook _NoIsbnBookIdDouble;
    private PublicationId _publicationIdDouble;
    private PublishingCompanyId _publishingCompanyIdDouble;
    private Language _languageDouble;
    private Dimension _dimensionDouble;
    private Weight _weightDouble;
    private NumberOfPages _numberOfPagesDouble;
    private EditionNumber _editionNumberDouble;
    private Binding _bindingDouble;

    //Edition Magazine
    private EditionMagazine _editionMagazineDouble;

    private ISSN _issnDouble;
    private NoIssnMagazine _noIssnMagazineDouble;
    private IssueNumber _issueNumberDouble;
    private Periodicity _periodicityDouble;

    private static final String expectedMessageMagazineIssnAlreadyExists = "An Edition with this ISSN already exists!";
    private static final String expectedMessagePublicationIsbnAlreadyExists = "An Edition with this ISBN already exists!";
    private static final String expectedMessagePublicationNoIsbnAlreadyExists = "Edition already exists!";

    @BeforeEach
    void setUp () {
        //EditionBook
        _editionFactoryDouble = mock(EditionFactory.class);
        _editionIdDouble = mock(EditionId.class);

        _editionBookDouble = mock(EditionBook.class);
        when(_editionBookDouble.identity()).thenReturn(_editionIdDouble);

        _bookIdIsbnDouble= mock(ISBN.class);
        _NoIsbnBookIdDouble = mock(NoIsbnBook.class);

        _publicationIdDouble = mock(PublicationId.class);
        _publishingCompanyIdDouble = mock(PublishingCompanyId.class);
        _languageDouble = mock(Language.class);
        _dimensionDouble = mock(Dimension.class);
        _weightDouble = mock(Weight.class);
        _numberOfPagesDouble = mock(NumberOfPages.class);
        _editionNumberDouble = mock(EditionNumber.class);
        _bindingDouble = mock(Binding.class);

        //Edition Magazine
        _editionMagazineDouble = mock(EditionMagazine.class);
        when(_editionMagazineDouble.identity()).thenReturn(_editionIdDouble);

        _issnDouble = mock(ISSN.class);
        _noIssnMagazineDouble = mock(NoIssnMagazine.class);

        _issueNumberDouble = mock(IssueNumber.class);
        _periodicityDouble = mock(Periodicity.class);
    }

    @Test
    void saveShouldStoreEditionAndReturnIt() {
        //Arrange
        //SUT
        MemoEditionRepo memoRepo = new MemoEditionRepo(_editionFactoryDouble);

        //Act
        Edition result = memoRepo.save(_editionBookDouble);

        //Assert
        assertEquals(_editionBookDouble, result);
    }
    @Test
    void findAllShouldReturnSavedEditions() {
        //Arrange
        //SUT
        MemoEditionRepo memoRepo = new MemoEditionRepo(_editionFactoryDouble);

        EditionBook anotherEditionBookDouble = mock(EditionBook.class);
        EditionId anotherEditionIdDouble = mock(EditionId.class);

        when(anotherEditionBookDouble.identity()).thenReturn(anotherEditionIdDouble);

        memoRepo.save(_editionBookDouble);
        memoRepo.save(anotherEditionBookDouble);

        //Act
        Iterable<Edition> result = memoRepo.findAll();

        //Assert
        List<Edition> editions = new ArrayList<>();
        result.forEach(editions::add);

        assertEquals(2, editions.size());
        assertTrue(editions.contains(_editionBookDouble));
        assertTrue(editions.contains(anotherEditionBookDouble));
    }

    @Test
    void ofIdentityShouldReturnEditionWhenItExists() {
        //Arrange
        //SUT
        MemoEditionRepo memoRepo = new MemoEditionRepo(_editionFactoryDouble);
        memoRepo.save(_editionBookDouble);

        //Act
        Optional<Edition> result = memoRepo.ofIdentity(_editionIdDouble);

        //Assert
        assertTrue(result.isPresent());
        assertEquals(_editionBookDouble, result.get());
    }

    @Test
    void ofIdentityShouldReturnEmptyWhenEditionDoesNotExist() {
        //Arrange
        //SUT
        MemoEditionRepo memoRepo = new MemoEditionRepo(_editionFactoryDouble);

        //Act
        Optional<Edition> result = memoRepo.ofIdentity(_editionIdDouble);

        //Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void containsOfIdentityShouldReturnTrueWhenEditionExists() {
        //Arrange
        //SUT
        MemoEditionRepo memoRepo = new MemoEditionRepo(_editionFactoryDouble);
        memoRepo.save(_editionBookDouble);

        //Act
        boolean result = memoRepo.containsOfIdentity(_editionIdDouble);

        //Assert
        assertTrue(result);
    }

    @Test
    void containsOfIdentityShouldReturnFalseWhenEditionDoesNotExist() {
        //Arrange
        //SUT
        MemoEditionRepo memoRepo = new MemoEditionRepo(_editionFactoryDouble);

        //Act
        boolean result = memoRepo.containsOfIdentity(_editionIdDouble);

        //Assert
        assertFalse(result);
    }

    @Test
    void addEditionBookShouldCreateSaveAndReturnEditionWhenBookIdIsIsbnAndDoesNotExist() {
        //Arrange
        //SUT
        MemoEditionRepo memoRepo = new MemoEditionRepo(_editionFactoryDouble);

        ISBN isbnDouble = mock(ISBN.class);
        Year publishingYear = Year.of(2020);

        when(_editionFactoryDouble.createEditionBook(
                isbnDouble,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                publishingYear,
                _languageDouble,
                _dimensionDouble,
                _weightDouble,
                _numberOfPagesDouble,
                _editionNumberDouble,
                _bindingDouble
        )).thenReturn(_editionBookDouble);

        //Act
        Edition result = memoRepo.addEditionBook(
                isbnDouble,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                publishingYear,
                _languageDouble,
                _dimensionDouble,
                _weightDouble,
                _numberOfPagesDouble,
                _editionNumberDouble,
                _bindingDouble
        );

        //Assert
        assertEquals(_editionBookDouble, result);
    }

    @Test
    void addEditionBookShouldThrowExceptionWhenBookIdIsIsbnAndAlreadyExists() {
        //Arrange
        //SUT
        MemoEditionRepo memoRepo = new MemoEditionRepo(_editionFactoryDouble);

        ISBN isbnDouble = mock(ISBN.class);
        Year publishingYear = Year.of(2020);

        when(_editionFactoryDouble.createEditionBook(
                isbnDouble,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                publishingYear,
                _languageDouble,
                _dimensionDouble,
                _weightDouble,
                _numberOfPagesDouble,
                _editionNumberDouble,
                _bindingDouble
        )).thenReturn(_editionBookDouble);

        when(_editionBookDouble.identity()).thenReturn(isbnDouble);

        memoRepo.save(_editionBookDouble);

        //Act
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                memoRepo.addEditionBook(
                        isbnDouble,
                        _publicationIdDouble,
                        _publishingCompanyIdDouble,
                        publishingYear,
                        _languageDouble,
                        _dimensionDouble,
                        _weightDouble,
                        _numberOfPagesDouble,
                        _editionNumberDouble,
                        _bindingDouble
                )
        );

        //Assert
        assertEquals(expectedMessagePublicationIsbnAlreadyExists, exception.getMessage());
    }

    @Test
    void addEditionBookShouldThrowExceptionWhenBookIdIsNoIsbnBookAndEquivalentEditionAlreadyExists() {
        //Arrange
        //SUT
        MemoEditionRepo memoRepo = new MemoEditionRepo(_editionFactoryDouble);

        NoIsbnBook noIsbnBookDouble = mock(NoIsbnBook.class);
        Year publishingYear = Year.of(1960);

        EditionBook existingEditionBookDouble = mock(EditionBook.class);
        EditionId existingEditionIdDouble = mock(EditionId.class);

        when(existingEditionBookDouble.identity()).thenReturn(existingEditionIdDouble);
        when(existingEditionBookDouble.sameAs(_editionBookDouble)).thenReturn(true);

        when(_editionFactoryDouble.createEditionBook(
                noIsbnBookDouble,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                publishingYear,
                _languageDouble,
                _dimensionDouble,
                _weightDouble,
                _numberOfPagesDouble,
                _editionNumberDouble,
                _bindingDouble
        )).thenReturn(_editionBookDouble);

        memoRepo.save(existingEditionBookDouble);

        //Act
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                memoRepo.addEditionBook(
                        noIsbnBookDouble,
                        _publicationIdDouble,
                        _publishingCompanyIdDouble,
                        publishingYear,
                        _languageDouble,
                        _dimensionDouble,
                        _weightDouble,
                        _numberOfPagesDouble,
                        _editionNumberDouble,
                        _bindingDouble
                )
        );

        //Assert
        assertEquals(expectedMessagePublicationNoIsbnAlreadyExists, exception.getMessage());
    }

    @Test
    void addEditionBookShouldSaveAndReturnEditionWhenBookIdIsNoIsbnBookAndEquivalentEditionDoesNotExist() {
        //Arrange
        //SUT
        MemoEditionRepo memoRepo = new MemoEditionRepo(_editionFactoryDouble);

        NoIsbnBook noIsbnBookDouble = mock(NoIsbnBook.class);
        Year publishingYear = Year.of(1960);

        EditionBook existingEditionBookDouble = mock(EditionBook.class);
        EditionId existingEditionIdDouble = mock(EditionId.class);

        when(existingEditionBookDouble.identity()).thenReturn(existingEditionIdDouble);
        when(existingEditionBookDouble.sameAs(_editionBookDouble)).thenReturn(false);

        when(_editionFactoryDouble.createEditionBook(
                noIsbnBookDouble,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                publishingYear,
                _languageDouble,
                _dimensionDouble,
                _weightDouble,
                _numberOfPagesDouble,
                _editionNumberDouble,
                _bindingDouble
        )).thenReturn(_editionBookDouble);

        memoRepo.save(existingEditionBookDouble);

        //Act
        Edition result = memoRepo.addEditionBook(
                noIsbnBookDouble,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                publishingYear,
                _languageDouble,
                _dimensionDouble,
                _weightDouble,
                _numberOfPagesDouble,
                _editionNumberDouble,
                _bindingDouble
        );

        //Assert
        assertEquals(_editionBookDouble, result);
    }

    @Test
    void addEditionMagazineShouldCreateSaveAndReturnEditionWhenMagazineIdIsIssnAndDoesNotExist() {
        //Arrange
        MemoEditionRepo memoRepo = new MemoEditionRepo(_editionFactoryDouble);
        Year publishingYear = Year.of(2020);

        when(_editionFactoryDouble.createEditionMagazine(
                _issnDouble,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                publishingYear,
                _languageDouble,
                _dimensionDouble,
                _weightDouble,
                _issueNumberDouble,
                _periodicityDouble
        )).thenReturn(_editionMagazineDouble);

        //Act
        Edition result = memoRepo.addEditionMagazine(
                _issnDouble,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                publishingYear,
                _languageDouble,
                _dimensionDouble,
                _weightDouble,
                _issueNumberDouble,
                _periodicityDouble

        );

        //Assert
        assertEquals(_editionMagazineDouble, result);
    }

}

