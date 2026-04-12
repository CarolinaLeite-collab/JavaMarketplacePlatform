import TOPSECRET.domain.edition.EditionMagazine;
import TOPSECRET.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class EditionMagazineTest {

    private MagazineId _magazineIdDouble;
    private PublicationId _publicationIdDouble;
    private PublishingCompanyId _companyIdDouble;
    private Year _publishingYearDouble;
    private Language _languageDouble;
    private IssueNumber _issueNumberDouble;
    private Periodicity _periodicityDouble;
    private Dimension _dimensionDouble;
    private Weight _weightDouble;

    private static final String expectedMessagePublicationId = "PublicationId is required";
    private static final String expectedMessageCompanyId = "PublishingCompanyId is required";
    private static final String expectedMessageYear = "PublishingYear is required";
    private static final String expectedMessageLanguage = "Language is required";
    private static final String expectedMessageIssueNumber = "IssueNumber is required";
    private static final String expectedMessagePeriodicity = "Periodicity is required";

    @BeforeEach
    void setUp() {
        _magazineIdDouble = mock(MagazineId.class);
        _publicationIdDouble = mock(PublicationId.class);
        _companyIdDouble = mock(PublishingCompanyId.class);
        _publishingYearDouble = Year.of(2020);
        _languageDouble = mock(Language.class);
        _issueNumberDouble = mock(IssueNumber.class);
        _periodicityDouble = mock(Periodicity.class);
        _dimensionDouble = mock(Dimension.class);
        _weightDouble = mock(Weight.class);
    }

    @Test
    void shouldBuildEditionMagazineSuccessfully() {
        //Act
        //SUT
        EditionMagazine magazine = new EditionMagazine.Builder(
                _magazineIdDouble,
                _publicationIdDouble,
                _companyIdDouble,
                _publishingYearDouble,
                _languageDouble,
                _issueNumberDouble,
                _periodicityDouble
        ).build();

        //Assert
        assertNotNull(magazine);
        assertSame(_magazineIdDouble, magazine.identity());
        assertSame(_publicationIdDouble, magazine.getPublicationId());
        assertSame(_companyIdDouble, magazine.getPublishingCompanyId());
        assertSame(_publishingYearDouble, magazine.getPublishingYear());
        assertSame(_languageDouble, magazine.getEditionLanguage());
        assertSame(_issueNumberDouble, magazine.getIssueNumber());
        assertSame(_periodicityDouble, magazine.getPeriodicity());
    }

    @Test
    void shouldBuildEditionMagazineWithOptionalFields() {
        //Act
        //SUT
        EditionMagazine magazine = new EditionMagazine.Builder(
                _magazineIdDouble,
                _publicationIdDouble,
                _companyIdDouble,
                _publishingYearDouble,
                _languageDouble,
                _issueNumberDouble,
                _periodicityDouble
        )
                .withDimension(_dimensionDouble)
                .withWeight(_weightDouble)
                .build();

        //Assert
        assertSame(_dimensionDouble, magazine.getDimension());
        assertSame(_weightDouble, magazine.getWeight());
    }

    @Test
    void shouldThrowWhenPublicationIdIsNull() {
        //Act
        //SUT
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new EditionMagazine.Builder(
                        _magazineIdDouble,
                        null,
                        _companyIdDouble,
                        _publishingYearDouble,
                        _languageDouble,
                        _issueNumberDouble,
                        _periodicityDouble
                ).build()
        );

        //Assert
        assertEquals(expectedMessagePublicationId, exception.getMessage());
    }

    @Test
    void shouldThrowWhenPublishingCompanyIsNull() {
        //Act
        //SUT
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new EditionMagazine.Builder(
                        _magazineIdDouble,
                        _publicationIdDouble,
                        null,
                        _publishingYearDouble,
                        _languageDouble,
                        _issueNumberDouble,
                        _periodicityDouble
                ).build()
        );

        //Assert
        assertEquals(expectedMessageCompanyId, exception.getMessage());
    }

    @Test
    void shouldThrowWhenPublishingYearIsNull() {
        //Act
        //SUT
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new EditionMagazine.Builder(
                        _magazineIdDouble,
                        _publicationIdDouble,
                        _companyIdDouble,
                        null,
                        _languageDouble,
                        _issueNumberDouble,
                        _periodicityDouble
                ).build()
        );

        //Assert
        assertEquals(expectedMessageYear, exception.getMessage());
    }

    @Test
    void shouldThrowWhenLanguageIsNull() {
        //Act
        //SUT
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new EditionMagazine.Builder(
                        _magazineIdDouble,
                        _publicationIdDouble,
                        _companyIdDouble,
                        _publishingYearDouble,
                        null,
                        _issueNumberDouble,
                        _periodicityDouble
                ).build()
        );

        //Assert
        assertEquals(expectedMessageLanguage, exception.getMessage());
    }

    @Test
    void shouldThrowWhenIssueNumberIsNull() {
        //Act
        //SUT
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new EditionMagazine.Builder(
                        _magazineIdDouble,
                        _publicationIdDouble,
                        _companyIdDouble,
                        _publishingYearDouble,
                        _languageDouble,
                        null,
                        _periodicityDouble
                ).build()
        );

        //Assert
        assertEquals(expectedMessageIssueNumber, exception.getMessage());
    }

    @Test
    void shouldThrowWhenPeriodicityIsNull() {
        //Act
        //SUT
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new EditionMagazine.Builder(
                        _magazineIdDouble,
                        _publicationIdDouble,
                        _companyIdDouble,
                        _publishingYearDouble,
                        _languageDouble,
                        _issueNumberDouble,
                        null
                ).build()
        );

        //Assert
        assertEquals(expectedMessagePeriodicity, exception.getMessage());
    }

    @Test
    void shouldGenerateNoIssnMagazineWhenMagazineIdIsNullAndYearIs1976OrBefore() {
        //Act
        //SUT
        EditionMagazine magazine = new EditionMagazine.Builder(
                null,
                _publicationIdDouble,
                _companyIdDouble,
                Year.of(1960),
                _languageDouble,
                _issueNumberDouble,
                _periodicityDouble
        ).build();

        //Assert
        assertInstanceOf(NoIssnMagazine.class, magazine.identity());
    }

    @Test
    void shouldGenerateNoIssnMagazineWhenMagazineIdIsNullAndYearIs1976() {
        // Act
        EditionMagazine magazine = new EditionMagazine.Builder(
                null,
                _publicationIdDouble,
                _companyIdDouble,
                Year.of(1976),
                _languageDouble,
                _issueNumberDouble,
                _periodicityDouble
        ).build();

        // Assert
        assertInstanceOf(NoIssnMagazine.class, magazine.identity());
    }

    @Test
    void shouldThrowWhenNoIssnMagazineIsUsedAfter1976() {
        //Arrange
        NoIssnMagazine noIssnMagazine = new NoIssnMagazine("MAG-1");

        //Act
        //SUT
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new EditionMagazine.Builder(
                        null,
                        _publicationIdDouble,
                        _companyIdDouble,
                        Year.of(1977),
                        _languageDouble,
                        _issueNumberDouble,
                        _periodicityDouble
                ).build()
        );

        //Assert
        assertEquals("Magazines published after 1976 must have a valid ISSN", exception.getMessage());
    }

    @Test
    void shouldReturnStringRepresentation() {
        //Arrange
        EditionMagazine magazine = new EditionMagazine.Builder(
                _magazineIdDouble,
                _publicationIdDouble,
                _companyIdDouble,
                _publishingYearDouble,
                _languageDouble,
                _issueNumberDouble,
                _periodicityDouble
        ).build();

        String expected = "Id: " + _magazineIdDouble +
                "\nPublication: " + _publicationIdDouble +
                "\nPublishing Company: " + _companyIdDouble +
                "\nYear: " + _publishingYearDouble +
                "\nLanguage: " + _languageDouble +
                "\nIssue Number: " + _issueNumberDouble +
                "\nPeriodicity: " + _periodicityDouble;

        //Act
        //SUT
        String result = magazine.toString();

        //Assert
        assertEquals(expected, result);
    }

    @Test
    void shouldReturnStringRepresentationWithOptionalFields() {
        //Arrange
        EditionMagazine magazine = new EditionMagazine.Builder(
                _magazineIdDouble,
                _publicationIdDouble,
                _companyIdDouble,
                _publishingYearDouble,
                _languageDouble,
                _issueNumberDouble,
                _periodicityDouble
        )
                .withDimension(_dimensionDouble)
                .withWeight(_weightDouble)
                .build();

        String expected = "Id: " + _magazineIdDouble +
                "\nPublication: " + _publicationIdDouble +
                "\nPublishing Company: " + _companyIdDouble +
                "\nYear: " + _publishingYearDouble +
                "\nLanguage: " + _languageDouble +
                "\nIssue Number: " + _issueNumberDouble +
                "\nPeriodicity: " + _periodicityDouble +
                "\nDimension: " + _dimensionDouble +
                "\nWeight: " + _weightDouble;

        //Act
        //SUT
        String result = magazine.toString();

        //Assert
        assertEquals(expected, result);
    }

    @Test
    void equalsShouldBeTrueWhenSameInstance() {
        //Arrange
        EditionMagazine magazine = new EditionMagazine.Builder(
                _magazineIdDouble,
                _publicationIdDouble,
                _companyIdDouble,
                _publishingYearDouble,
                _languageDouble,
                _issueNumberDouble,
                _periodicityDouble
        ).build();

        //Act
        //SUT
        boolean result = magazine.equals(magazine);

        //Assert
        assertTrue(result);
    }

    @Test
    void equalsShouldBeTrueWhenSameMagazineId() {
        //Arrange
        EditionMagazine magazine1 = new EditionMagazine.Builder(
                _magazineIdDouble,
                _publicationIdDouble,
                _companyIdDouble,
                _publishingYearDouble,
                _languageDouble,
                _issueNumberDouble,
                _periodicityDouble
        ).build();

        EditionMagazine magazine2 = new EditionMagazine.Builder(
                _magazineIdDouble,
                _publicationIdDouble,
                _companyIdDouble,
                _publishingYearDouble,
                _languageDouble,
                _issueNumberDouble,
                _periodicityDouble
        ).build();

        //Act
        //SUT
        boolean result = magazine1.equals(magazine2);

        //Assert
        assertTrue(result);
    }

    @Test
    void equalsShouldBeFalseWhenDifferentMagazineId() {
        //Arrange
        MagazineId otherMagazineId = mock(MagazineId.class);

        EditionMagazine magazine1 = new EditionMagazine.Builder(
                _magazineIdDouble,
                _publicationIdDouble,
                _companyIdDouble,
                _publishingYearDouble,
                _languageDouble,
                _issueNumberDouble,
                _periodicityDouble
        ).build();

        EditionMagazine magazine2 = new EditionMagazine.Builder(
                otherMagazineId,
                _publicationIdDouble,
                _companyIdDouble,
                _publishingYearDouble,
                _languageDouble,
                _issueNumberDouble,
                _periodicityDouble
        ).build();

        //Act
        //SUT
        boolean result = magazine1.equals(magazine2);

        //Assert
        assertFalse(result);
    }

    @Test
    void equalsShouldBeFalseWhenDifferentType() {
        //Arrange
        EditionMagazine magazine = new EditionMagazine.Builder(
                _magazineIdDouble,
                _publicationIdDouble,
                _companyIdDouble,
                _publishingYearDouble,
                _languageDouble,
                _issueNumberDouble,
                _periodicityDouble
        ).build();

        Object other = new Object();

        //Act
        //SUT
        boolean result = magazine.equals(other);

        //Assert
        assertFalse(result);
    }


    //rever
    @Test
    void sameAsShouldBeTrueWhenSameIssn() {
        //Arrange
        MagazineId issn = mock(ISSN.class);

        EditionMagazine magazine1 = new EditionMagazine.Builder(
                issn,
                _publicationIdDouble,
                _companyIdDouble,
                _publishingYearDouble,
                _languageDouble,
                _issueNumberDouble,
                _periodicityDouble
        ).build();

        EditionMagazine magazine2 = new EditionMagazine.Builder(
                issn,
                mock(PublicationId.class),
                mock(PublishingCompanyId.class),
                Year.of(1999),
                mock(Language.class),
                mock(IssueNumber.class),
                mock(Periodicity.class)
        ).build();

        //Act
        //SUT
        boolean result = magazine1.sameAs(magazine2);

        //Assert
        assertTrue(result);
    }

    @Test
    void sameAsShouldBeFalseWhenDifferentIssn() {
        //Arrange
        MagazineId issn1 = mock(ISSN.class);
        MagazineId issn2 = mock(ISSN.class);

        EditionMagazine magazine1 = new EditionMagazine.Builder(
                issn1,
                _publicationIdDouble,
                _companyIdDouble,
                _publishingYearDouble,
                _languageDouble,
                _issueNumberDouble,
                _periodicityDouble
        ).build();

        EditionMagazine magazine2 = new EditionMagazine.Builder(
                issn2,
                _publicationIdDouble,
                _companyIdDouble,
                _publishingYearDouble,
                _languageDouble,
                _issueNumberDouble,
                _periodicityDouble
        ).build();

        //Act
        //SUT
        boolean result = magazine1.sameAs(magazine2);

        //Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldBeTrueWhenNonIssnAndSameBusinessAttributes() {
        //Arrange
        Year publishingYear = Year.of(1970);

        MagazineId internalId1 = new NoIssnMagazine("MAG-1");
        MagazineId internalId2 = new NoIssnMagazine("MAG-2");

        EditionMagazine magazine1 = new EditionMagazine.Builder(
                internalId1,
                _publicationIdDouble,
                _companyIdDouble,
                publishingYear,
                _languageDouble,
                _issueNumberDouble,
                _periodicityDouble
        ).build();

        EditionMagazine magazine2 = new EditionMagazine.Builder(
                internalId2,
                _publicationIdDouble,
                _companyIdDouble,
                publishingYear,
                _languageDouble,
                _issueNumberDouble,
                _periodicityDouble
        ).build();

        //Act
        //SUT
        boolean result = magazine1.sameAs(magazine2);

        //Assert
        assertTrue(result);
    }

    @Test
    void sameAsShouldBeFalseWhenNonIssnAndDifferentPublicationId() {
        //Arrange
        Year publishingYear = Year.of(1970);

        PublicationId otherPublicationId = mock(PublicationId.class);
        MagazineId internalId1 = new NoIssnMagazine("MAG-1");
        MagazineId internalId2 = new NoIssnMagazine("MAG-2");

        EditionMagazine magazine1 = new EditionMagazine.Builder(
                internalId1,
                _publicationIdDouble,
                _companyIdDouble,
                publishingYear,
                _languageDouble,
                _issueNumberDouble,
                _periodicityDouble
        ).build();

        EditionMagazine magazine2 = new EditionMagazine.Builder(
                internalId2,
                otherPublicationId,
                _companyIdDouble,
                publishingYear,
                _languageDouble,
                _issueNumberDouble,
                _periodicityDouble
        ).build();

        //Act
        //SUT
        boolean result = magazine1.sameAs(magazine2);

        //Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldBeFalseWhenNonIssnAndDifferentPublishingCompanyId() {
        //Arrange
        Year publishingYear = Year.of(1970);

        PublishingCompanyId otherCompanyId = mock(PublishingCompanyId.class);
        MagazineId internalId1 = new NoIssnMagazine("MAG-1");
        MagazineId internalId2 = new NoIssnMagazine("MAG-2");

        EditionMagazine magazine1 = new EditionMagazine.Builder(
                internalId1,
                _publicationIdDouble,
                _companyIdDouble,
                publishingYear,
                _languageDouble,
                _issueNumberDouble,
                _periodicityDouble
        ).build();

        EditionMagazine magazine2 = new EditionMagazine.Builder(
                internalId2,
                _publicationIdDouble,
                otherCompanyId,
                publishingYear,
                _languageDouble,
                _issueNumberDouble,
                _periodicityDouble
        ).build();

        //Act
        //SUT
        boolean result = magazine1.sameAs(magazine2);

        //Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldBeFalseWhenNonIssnAndDifferentPublishingYear() {
        //Arrange
        MagazineId internalId1 = new NoIssnMagazine("MAG-1");
        MagazineId internalId2 = new NoIssnMagazine("MAG-2");

        EditionMagazine magazine1 = new EditionMagazine.Builder(
                internalId1,
                _publicationIdDouble,
                _companyIdDouble,
                Year.of(1970),
                _languageDouble,
                _issueNumberDouble,
                _periodicityDouble
        ).build();

        EditionMagazine magazine2 = new EditionMagazine.Builder(
                internalId2,
                _publicationIdDouble,
                _companyIdDouble,
                Year.of(1971),
                _languageDouble,
                _issueNumberDouble,
                _periodicityDouble
        ).build();

        //Act
        //SUT
        boolean result = magazine1.sameAs(magazine2);

        //Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldBeFalseWhenNonIssnAndDifferentLanguage() {
        //Arrange
        Year publishingYear = Year.of(1970);

        Language otherLanguage = mock(Language.class);
        MagazineId internalId1 = new NoIssnMagazine("MAG-1");
        MagazineId internalId2 = new NoIssnMagazine("MAG-2");

        EditionMagazine magazine1 = new EditionMagazine.Builder(
                internalId1,
                _publicationIdDouble,
                _companyIdDouble,
                publishingYear,
                _languageDouble,
                _issueNumberDouble,
                _periodicityDouble
        ).build();

        EditionMagazine magazine2 = new EditionMagazine.Builder(
                internalId2,
                _publicationIdDouble,
                _companyIdDouble,
                publishingYear,
                otherLanguage,
                _issueNumberDouble,
                _periodicityDouble
        ).build();

        //Act
        //SUT
        boolean result = magazine1.sameAs(magazine2);

        //Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldBeFalseWhenNonIssnAndDifferentIssueNumber() {
        //Arrange
        Year publishingYear = Year.of(1970);

        IssueNumber otherIssueNumber = mock(IssueNumber.class);
        MagazineId internalId1 = new NoIssnMagazine("MAG-1");
        MagazineId internalId2 = new NoIssnMagazine("MAG-2");

        EditionMagazine magazine1 = new EditionMagazine.Builder(
                internalId1,
                _publicationIdDouble,
                _companyIdDouble,
                publishingYear,
                _languageDouble,
                _issueNumberDouble,
                _periodicityDouble
        ).build();

        EditionMagazine magazine2 = new EditionMagazine.Builder(
                internalId2,
                _publicationIdDouble,
                _companyIdDouble,
                publishingYear,
                _languageDouble,
                otherIssueNumber,
                _periodicityDouble
        ).build();

        //Act
        //SUT
        boolean result = magazine1.sameAs(magazine2);

        //Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldBeFalseWhenNonIssnAndDifferentPeriodicity() {
        //Arrange
        Year publishingYear = Year.of(1970);

        Periodicity otherPeriodicity = mock(Periodicity.class);
        MagazineId internalId1 = new NoIssnMagazine("MAG-1");
        MagazineId internalId2 = new NoIssnMagazine("MAG-2");

        EditionMagazine magazine1 = new EditionMagazine.Builder(
                internalId1,
                _publicationIdDouble,
                _companyIdDouble,
                publishingYear,
                _languageDouble,
                _issueNumberDouble,
                _periodicityDouble
        ).build();

        EditionMagazine magazine2 = new EditionMagazine.Builder(
                internalId2,
                _publicationIdDouble,
                _companyIdDouble,
                publishingYear,
                _languageDouble,
                _issueNumberDouble,
                otherPeriodicity
        ).build();

        //Act
        //SUT
        boolean result = magazine1.sameAs(magazine2);

        //Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldBeFalseWhenObjectIsNull() {
        //Arrange
        EditionMagazine magazine = new EditionMagazine.Builder(
                new NoIssnMagazine("MAG-1"),
                _publicationIdDouble,
                _companyIdDouble,
                Year.of(1970),
                _languageDouble,
                _issueNumberDouble,
                _periodicityDouble
        ).build();

        //Act
        //SUT
        boolean result = magazine.sameAs(null);

        //Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldBeFalseWhenObjectIsDifferentType() {
        //Arrange
        EditionMagazine magazine = new EditionMagazine.Builder(
                new NoIssnMagazine("MAG-1"),
                _publicationIdDouble,
                _companyIdDouble,
                Year.of(1970),
                _languageDouble,
                _issueNumberDouble,
                _periodicityDouble
        ).build();

        Object other = new Object();

        //Act
        //SUT
        boolean result = magazine.sameAs(other);

        //Assert
        assertFalse(result);
    }
}