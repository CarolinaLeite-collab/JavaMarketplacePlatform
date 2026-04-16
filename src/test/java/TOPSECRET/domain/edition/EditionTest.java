package TOPSECRET.domain.edition;

import TOPSECRET.domain.publication.Publication;
import TOPSECRET.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EditionTest {

    private PublicationTypeId _bookTypeId;
    private PublicationTypeId _magazineTypeId;

    private Identifier _bookIdentifier;
    private Identifier _magazineIdentifier;

    private PublicationId _publicationIdDouble;
    private PublishingCompanyId _publishingCompanyIdDouble;
    private Language _languageDouble;
    private Year _publishingYear;
    //Optional fields
    private Dimension _dimensionDouble;
    private Weight _weightDouble;
    private NumberOfPages _pagesDouble;
    private EditionNumber _editionNumberDouble;
    private Binding _bindingDouble;

    private static final String expectedMessageTypeId = "Publication Type Id is required";
    private static final String expectedMessagePublicationId = "Publication Id is required";
    private static final String expectedMessageCompanyId = "Publishing Company is required";
    private static final String expectedMessageYear = "Publishing Year is required";
    private static final String expectedMessageLanguage = "Language is required";
    private static final String expectedMessageInvalidIdentifier = "Invalid identifier for given type and year";

    @BeforeEach
    void setUp() {

        _bookTypeId = mock(PublicationTypeId.class);
        when(_bookTypeId.isBook()).thenReturn(true);
        _magazineTypeId = mock(PublicationTypeId.class);
        when(_magazineTypeId.isMagazine()).thenReturn(true);

        _bookIdentifier = mock(ISBN.class);
        _magazineIdentifier = mock(ISSN.class);

        _publicationIdDouble = mock(PublicationId.class);
        _publishingCompanyIdDouble = mock(PublishingCompanyId.class);
        _languageDouble = mock(Language.class);
        _publishingYear = Year.of(2020);
        //Optional fields
        _dimensionDouble = mock(Dimension.class);
        _weightDouble = mock(Weight.class);
        _pagesDouble = mock(NumberOfPages.class);
        _editionNumberDouble = mock(EditionNumber.class);
        _bindingDouble = mock(Binding.class);

    }

    @Test
    void testConstructorWithAllFields() {

        //SUT
        new Edition(
                _bookTypeId,
                _bookIdentifier,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                _publishingYear,
                _languageDouble,
                _dimensionDouble,
                _weightDouble,
                _pagesDouble,
                _editionNumberDouble,
                _bindingDouble
        );
    }

    @Test
    void testConstructorWithoutOptionalFields() {

        // SUT
        new Edition(
                _bookTypeId,
                _bookIdentifier,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                _publishingYear,
                _languageDouble
        );
    }

    @Test
    void testConstructorWithSomeNullOptionalFields() {

        //SUT
        new Edition(
                _bookTypeId,
                _bookIdentifier,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                _publishingYear,
                _languageDouble,
                _dimensionDouble,
                _weightDouble,
                _pagesDouble,
                _editionNumberDouble,
                null
        );
    }

    @Test
    void shouldThrowWhenTypeIdIsNull() {
        //Act
        //SUT
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () ->
                        new Edition(null,
                                _bookIdentifier,
                                _publicationIdDouble,
                                _publishingCompanyIdDouble,
                                _publishingYear,
                                _languageDouble,
                                _dimensionDouble,
                                _weightDouble,
                                _pagesDouble,
                                _editionNumberDouble,
                                _bindingDouble));

        //Assert
        assertEquals(expectedMessageTypeId, exception.getMessage());

    }

    @Test
    void shouldThrowWhenPublicationIdIsNull() {
        //Act
        //SUT
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () ->
                        new Edition(_bookTypeId,
                                _bookIdentifier,
                                null,
                                _publishingCompanyIdDouble,
                                _publishingYear,
                                _languageDouble,
                                _dimensionDouble,
                                _weightDouble,
                                _pagesDouble,
                                _editionNumberDouble,
                                _bindingDouble));

        //Assert
        assertEquals(expectedMessagePublicationId, exception.getMessage());
    }

    @Test
    void shouldThrowWhenPublishingCompanyIdIsNull() {
        //Act
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () ->
                        new Edition(
                                _bookTypeId,
                                _bookIdentifier,
                                _publicationIdDouble,
                                null,
                                _publishingYear,
                                _languageDouble,
                                _dimensionDouble,
                                _weightDouble,
                                _pagesDouble,
                                _editionNumberDouble,
                                _bindingDouble
                        )
                );

        //Assert
        assertEquals(expectedMessageCompanyId, exception.getMessage());
    }

    @Test
    void shouldThrowWhenPublishingYearIsNull() {
        //Act
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () ->
                        new Edition(
                                _bookTypeId,
                                _bookIdentifier,
                                _publicationIdDouble,
                                _publishingCompanyIdDouble,
                                null,
                                _languageDouble,
                                _dimensionDouble,
                                _weightDouble,
                                _pagesDouble,
                                _editionNumberDouble,
                                _bindingDouble
                        )
                );

        //Assert
        assertEquals(expectedMessageYear, exception.getMessage());
    }

    @Test
    void shouldThrowWhenLanguageIsNull() {
        //Act
        //SUT
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () ->
                        new Edition(
                                _bookTypeId,
                                _bookIdentifier,
                                _publicationIdDouble,
                                _publishingCompanyIdDouble,
                                _publishingYear,
                                null,
                                _dimensionDouble,
                                _weightDouble,
                                _pagesDouble,
                                _editionNumberDouble,
                                _bindingDouble
                        )
                );

        //Assert
        assertEquals(expectedMessageLanguage, exception.getMessage());
    }

    @Test
    void shouldThrowWhenIdentifierIsInvalid() {

        //Act
        //SUT
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () ->
                        new Edition(
                                _bookTypeId,
                                _magazineIdentifier,
                                _publicationIdDouble,
                                _publishingCompanyIdDouble,
                                _publishingYear,
                                _languageDouble,
                                _dimensionDouble,
                                _weightDouble,
                                _pagesDouble,
                                _editionNumberDouble,
                                _bindingDouble
                        )
                );

        // Assert
        assertEquals(expectedMessageInvalidIdentifier, exception.getMessage());
    }

    @Test
    void shouldCreateBookEditionWhenYearIsAfter1970AndIdentifierIsIsbn() {

        //SUT
        new Edition(
                _bookTypeId,
                _bookIdentifier,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                Year.of(2020),
                _languageDouble,
                _dimensionDouble,
                _weightDouble,
                _pagesDouble,
                _editionNumberDouble,
                _bindingDouble
        );
    }

    @Test
    void shouldThrowWhenBookYearIsAfter1970AndIdentifierIsNotIsbn() {

        //Act
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () ->
                        new Edition(
                                _bookTypeId,
                                _magazineIdentifier,
                                _publicationIdDouble,
                                _publishingCompanyIdDouble,
                                Year.of(2020),
                                _languageDouble,
                                _dimensionDouble,
                                _weightDouble,
                                _pagesDouble,
                                _editionNumberDouble,
                                _bindingDouble
                        )
                );

        // Assert
        assertEquals(expectedMessageInvalidIdentifier, exception.getMessage());
    }

    @Test
    void shouldCreateBookEditionWhenYearIsBefore1970AndIdentifierIsNull() {

        //SUT
        new Edition(
                _bookTypeId,
                null,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                Year.of(1969),
                _languageDouble,
                _dimensionDouble,
                _weightDouble,
                _pagesDouble,
                _editionNumberDouble,
                _bindingDouble
        );
    }

    @Test
    void shouldCreateBookEditionWhenYearIs1970AndIdentifierIsNotIsbn() {

        //SUT
        new Edition(
                _bookTypeId,
                _magazineIdentifier,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                Year.of(1970),
                _languageDouble,
                _dimensionDouble,
                _weightDouble,
                _pagesDouble,
                _editionNumberDouble,
                _bindingDouble
        );

    }

    @Test
    void shouldCreateMagazineEditionWhenYearIsAfter1975AndIdentifierIsIssn() {
        //SUT
        new Edition(
                _magazineTypeId,
                _magazineIdentifier,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                Year.of(2020),
                _languageDouble,
                _dimensionDouble,
                _weightDouble,
                _pagesDouble,
                _editionNumberDouble,
                _bindingDouble
        );
    }

    @Test
    void shouldThrowWhenMagazineYearIsAfter1975AndIdentifierIsNotIssn() {
        //SUT
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () ->
                        new Edition(
                                _magazineTypeId,
                                _bookIdentifier,
                                _publicationIdDouble,
                                _publishingCompanyIdDouble,
                                Year.of(2020),
                                _languageDouble,
                                _dimensionDouble,
                                _weightDouble,
                                _pagesDouble,
                                _editionNumberDouble,
                                _bindingDouble
                        )
                );

        assertEquals(expectedMessageInvalidIdentifier, exception.getMessage());
    }

    @Test
    void shouldCreateMagazineEditionWhenYearIsBefore1975AndIdentifierIsNull() {
        //SUT
        new Edition(
                _magazineTypeId,
                null,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                Year.of(1974),
                _languageDouble,
                _dimensionDouble,
                _weightDouble,
                _pagesDouble,
                _editionNumberDouble,
                _bindingDouble
        );
    }

    @Test
    void shouldCreateMagazineEditionWhenYearIs1975AndIdentifierIsNotIssn() {
        //SUT
        new Edition(
                _magazineTypeId,
                _bookIdentifier,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                Year.of(1975),
                _languageDouble,
                _dimensionDouble,
                _weightDouble,
                _pagesDouble,
                _editionNumberDouble,
                _bindingDouble
        );
    }

    @Test
    void shouldThrowWhenTypeIsNeitherBookNorMagazine() {
        //Arrange
        PublicationTypeId unknownType = mock(PublicationTypeId.class);
        //SUT
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () ->
                        new Edition(
                                unknownType,
                                _bookIdentifier,
                                _publicationIdDouble,
                                _publishingCompanyIdDouble,
                                Year.of(2020),
                                _languageDouble,
                                _dimensionDouble,
                                _weightDouble,
                                _pagesDouble,
                                _editionNumberDouble,
                                _bindingDouble
                        )
                );
        //Assert
        assertEquals(expectedMessageInvalidIdentifier, exception.getMessage());
    }

    @Test
    void toStringShouldReturnExpectedTextWithoutOptionalFields() {

        //Arrange
        Edition edition = new Edition(
                _bookTypeId,
                _bookIdentifier,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                _publishingYear,
                _languageDouble
        );

        //Act
        //SUT
        String result = edition.toString();

        //Assert
        assertEquals(
                "Id: " + edition.identity() +
                        "\nType: " + _bookTypeId +
                        "\nIdentifier: " + _bookIdentifier +
                        "\nPublication: " + _publicationIdDouble +
                        "\nPublishing Company: " + _publishingCompanyIdDouble +
                        "\nYear: " + _publishingYear +
                        "\nLanguage: " + _languageDouble,
                result
        );
    }

    @Test
    void toStringShouldReturnExpectedTextWithOptionalFields() {

        //Arrange
        Edition edition = new Edition(
                _bookTypeId,
                _bookIdentifier,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                _publishingYear,
                _languageDouble,
                _dimensionDouble,
                _weightDouble,
                _pagesDouble,
                _editionNumberDouble,
                _bindingDouble
        );

        //Act
        //SUT
        String result = edition.toString();

        //Assert
        assertEquals(
                "Id: " + edition.identity() +
                        "\nType: " + _bookTypeId +
                        "\nIdentifier: " + _bookIdentifier +
                        "\nPublication: " + _publicationIdDouble +
                        "\nPublishing Company: " + _publishingCompanyIdDouble +
                        "\nYear: " + _publishingYear +
                        "\nLanguage: " + _languageDouble +
                        "\nDimension: " + _dimensionDouble +
                        "\nWeight: " + _weightDouble +
                        "\nNumber of pages: " + _pagesDouble +
                        "\nEdition number: " + _editionNumberDouble +
                        "\nBinding: " + _bindingDouble,
                result
        );
    }

    @Test
    void sameAsShouldReturnFalseWhenObjectIsNull() {

        //Arrange
        Edition edition = new Edition(
                _bookTypeId,
                _bookIdentifier,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                _publishingYear,
                _languageDouble
        );

        //Act
        //SUT
        boolean result = edition.sameAs(null);

        //Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldReturnFalseWhenObjectIsNotEdition() {

        //Arrange
        Edition edition = new Edition(
                _bookTypeId,
                _bookIdentifier,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                _publishingYear,
                _languageDouble
        );

        //Act
        //SUT
        boolean result = edition.sameAs("not an edition");

        //Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldReturnTrueWhenTypeAndIdentifierAreEqual() {

        //Arrange
        //SUT
        Edition edition1 = new Edition(
                _bookTypeId,
                _bookIdentifier,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                _publishingYear,
                _languageDouble
        );

        Edition edition2 = new Edition(
                _bookTypeId,
                _bookIdentifier,
                mock(PublicationId.class),
                mock(PublishingCompanyId.class),
                Year.of(2022),
                mock(Language.class)
        );

        //Act
        boolean result = edition1.sameAs(edition2);

        //Assert
        assertTrue(result);
    }

    @Test
    void sameAsShouldReturnFalseWhenTypeIsEqualButIdentifierIsDifferent() {

        //Arrange
        Identifier otherBookIdentifier = mock(ISBN.class);

        // SUT
        Edition edition1 = new Edition(
                _bookTypeId,
                _bookIdentifier,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                _publishingYear,
                _languageDouble
        );

        Edition edition2 = new Edition(
                _bookTypeId,
                otherBookIdentifier,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                _publishingYear,
                _languageDouble
        );

        //Act
        boolean result = edition1.sameAs(edition2);

        //Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldReturnTrueWhenIdentifiersAreNullAndBusinessFieldsAreEqual() {

        //Arrange
        //SUT
        Year oldYear = Year.of(1970);

        Edition edition1 = new Edition(
                _bookTypeId,
                null,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                oldYear,
                _languageDouble
        );

        Edition edition2 = new Edition(
                _bookTypeId,
                null,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                oldYear,
                _languageDouble
        );

        //Act
        boolean result = edition1.sameAs(edition2);

        //Assert
        assertTrue(result);
    }

    @Test
    void sameAsShouldReturnFalseWhenIdentifiersAreNullAndPublicationIsDifferent() {

        //Arrange
        //SUT
        Year oldYear = Year.of(1970);
        PublicationId otherPublicationId = mock(PublicationId.class);

        Edition edition1 = new Edition(
                _bookTypeId,
                null,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                oldYear,
                _languageDouble
        );

        Edition edition2 = new Edition(
                _bookTypeId,
                null,
                otherPublicationId,
                _publishingCompanyIdDouble,
                oldYear,
                _languageDouble
        );

        //Act
        boolean result = edition1.sameAs(edition2);

        //Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldReturnFalseWhenTypesAreDifferentEvenIfBusinessFieldsMatch() {

        //Arrange
        //SUT
        Year oldBookYear = Year.of(1970);
        Year oldMagazineYear = Year.of(1975);

        Edition edition1 = new Edition(
                _bookTypeId,
                null,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                oldBookYear,
                _languageDouble
        );

        Edition edition2 = new Edition(
                _magazineTypeId,
                null,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                oldMagazineYear,
                _languageDouble
        );

        //Act
        boolean result = edition1.sameAs(edition2);

        //Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldReturnTrueWhenOneIdentifierIsNullAndBusinessFieldsAreEqual() {

        //Arrange
        Year oldYear = Year.of(1970);

        //SUT
        Edition edition1 = new Edition(
                _bookTypeId,
                _bookIdentifier,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                oldYear,
                _languageDouble
        );

        Edition edition2 = new Edition(
                _bookTypeId,
                null,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                oldYear,
                _languageDouble
        );

        //Act
        boolean result = edition1.sameAs(edition2);

        //Assert
        assertTrue(result);
    }

    @Test
    void sameAsShouldReturnFalseWhenOneIdentifierIsNullAndBusinessFieldsAreDifferent() {

        //Arrange
        //SUT
        Year oldYear = Year.of(1970);
        PublicationId otherPublicationId = mock(PublicationId.class);

        Edition edition1 = new Edition(
                _bookTypeId,
                _bookIdentifier,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                oldYear,
                _languageDouble
        );

        Edition edition2 = new Edition(
                _bookTypeId,
                null,
                otherPublicationId,
                _publishingCompanyIdDouble,
                oldYear,
                _languageDouble
        );

        //Act
        boolean result = edition1.sameAs(edition2);

        //Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldReturnTrueWhenBusinessFieldsMatch() {

        //Arrange
        //SUT
        Year oldYear = Year.of(1970);

        Edition edition1 = new Edition(
                _bookTypeId,
                null,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                oldYear,
                _languageDouble
        );

        Edition edition2 = new Edition(
                _bookTypeId,
                null,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                oldYear,
                _languageDouble
        );

        //Act
        boolean result = edition1.sameAs(edition2);

        //Assert
        assertTrue(result);
    }

    @Test
    void sameAsShouldReturnFalseWhenPublishingCompanyDiffers() {

        //Arrange
        Year oldYear = Year.of(1970);
        PublishingCompanyId otherCompany = mock(PublishingCompanyId.class);

        //SUT
        Edition edition1 = new Edition(
                _bookTypeId,
                null,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                oldYear,
                _languageDouble
        );

        Edition edition2 = new Edition(
                _bookTypeId,
                null,
                _publicationIdDouble,
                otherCompany,
                oldYear,
                _languageDouble
        );

        //Act
        boolean result = edition1.sameAs(edition2);

        //Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldReturnFalseWhenPublishingYearDiffers() {

        //Arrange
        Year oldYear = Year.of(1960);

        //SUT
        Edition edition1 = new Edition(
                _bookTypeId,
                null,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                oldYear,
                _languageDouble
        );

        Edition edition2 = new Edition(
                _bookTypeId,
                null,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                Year.of(1959),
                _languageDouble
        );

        //Act
        boolean result = edition1.sameAs(edition2);

        //Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldReturnFalseWhenLanguageDiffers() {

        //Arrange
        Year oldYear = Year.of(1970);
        Language otherLanguage = mock(Language.class);

        //SUT
        Edition edition1 = new Edition(
                _bookTypeId,
                null,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                oldYear,
                _languageDouble
        );

        Edition edition2 = new Edition(
                _bookTypeId,
                null,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                oldYear,
                otherLanguage
        );

        //Act
        boolean result = edition1.sameAs(edition2);

        //Assert
        assertFalse(result);
    }

    @Test
    void equalsShouldReturnTrueWhenSameInstance() {

        //Arrange
        //SUT
        Edition edition = new Edition(
                _bookTypeId,
                _bookIdentifier,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                _publishingYear,
                _languageDouble
        );

        //Act
        boolean result = edition.equals(edition);

        //Assert
        assertTrue(result);
    }

    @Test
    void equalsShouldReturnFalseWhenObjectIsNull() {

        //Arrange
        //SUT
        Edition edition = new Edition(
                _bookTypeId,
                _bookIdentifier,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                _publishingYear,
                _languageDouble
        );

        //Act
        boolean result = edition.equals(null);

        //Assert
        assertFalse(result);
    }

    @Test
    void equalsShouldReturnFalseWhenObjectIsDifferentType() {

        //Arrange
        Edition edition = new Edition(
                _bookTypeId,
                _bookIdentifier,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                _publishingYear,
                _languageDouble
        );

        //Act
        boolean result = edition.equals("not an edition");

        //Assert
        assertFalse(result);
    }

    @Test
    void equalsShouldReturnFalseWhenDifferentEditions() {

        //Arrange
        //SUT
        Edition edition1 = new Edition(
                _bookTypeId,
                _bookIdentifier,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                _publishingYear,
                _languageDouble
        );

        Edition edition2 = new Edition(
                _bookTypeId,
                _bookIdentifier,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                _publishingYear,
                _languageDouble
        );

        //Act
        boolean result = edition1.equals(edition2);

        //Assert
        assertFalse(result); // different generated IDs
    }

    @Test
    void gettersShouldReturnExpectedValues() {

        //Arrange
        //SUT
        Edition edition = new Edition(
                _bookTypeId,
                _bookIdentifier,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                _publishingYear,
                _languageDouble,
                _dimensionDouble,
                _weightDouble,
                _pagesDouble,
                _editionNumberDouble,
                _bindingDouble
        );

        //Assert
        assertEquals(_bookTypeId, edition.getPublicationTypeId());
        assertEquals(_bookIdentifier, edition.getIdentifier());
        assertEquals(_publicationIdDouble, edition.getPublicationId());
        assertEquals(_publishingCompanyIdDouble, edition.getPublishingCompanyId());
        assertEquals(_publishingYear, edition.getPublishingYear());
        assertEquals(_languageDouble, edition.getEditionLanguage());
        assertEquals(_pagesDouble, edition.getNumberOfPages());
        assertEquals(_editionNumberDouble, edition.getEditionNumber());
        assertEquals(_bindingDouble, edition.getBinding());
        assertEquals(_dimensionDouble, edition.getDimension());
        assertEquals(_weightDouble, edition.getWeight());
    }

    @Test
    void identityShouldBeDifferentForDifferentEditions() {

        //Arrange
        //SUT
        Edition edition1 = new Edition(
                _bookTypeId,
                _bookIdentifier,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                _publishingYear,
                _languageDouble
        );

        Edition edition2 = new Edition(
                _bookTypeId,
                _bookIdentifier,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                _publishingYear,
                _languageDouble
        );

        //Assert
        assertNotNull(edition1.identity());
        assertNotNull(edition2.identity());
        assertNotEquals(edition1.identity(), edition2.identity());
    }

    @Test
    void isByPublicationIdSamePublicationReturnsTrue() {
        // SUT
        Edition e = new Edition(_bookTypeId, _bookIdentifier, _publicationIdDouble, _publishingCompanyIdDouble, _publishingYear, _languageDouble);

        // Act
        boolean result = e.isByPublicationId(_publicationIdDouble);

        // Assert
        assertTrue(result);
    }
    @Test
    void isByPublicationIdDifferentPublicationReturnsFalse() {
        // Arrange
        PublicationId otherPublicationIdDouble = mock(PublicationId.class);

        // SUT
        Edition e = new Edition(_bookTypeId, _bookIdentifier, _publicationIdDouble, _publishingCompanyIdDouble, _publishingYear, _languageDouble);

        // Act
        boolean result = e.isByPublicationId(otherPublicationIdDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void isByPublicationIdNullReturnsFalse() {
        // SUT
        Edition e = new Edition(_bookTypeId, _bookIdentifier, _publicationIdDouble, _publishingCompanyIdDouble, _publishingYear, _languageDouble);

        // Act
        boolean result = e.isByPublicationId(null);

        // Assert
        assertFalse(result);
    }
    @Test
    void isByPublishingCompanyIdSamePublishingCompanyReturnsTrue() {
        // SUT
        Edition e = new Edition(_bookTypeId, _bookIdentifier, _publicationIdDouble, _publishingCompanyIdDouble, _publishingYear, _languageDouble);

        // Act
        boolean result = e.isByPublishingCompanyId(_publishingCompanyIdDouble);

        // Assert
        assertTrue(result);
    }
    @Test
    void isByPublishingCompanyIdDifferentPublishingCompanyReturnsFalse() {
        // Arrange
        PublishingCompanyId otherPublishingCompanyIdDouble = mock(PublishingCompanyId.class);

        // SUT
        Edition e = new Edition(_bookTypeId, _bookIdentifier, _publicationIdDouble, _publishingCompanyIdDouble, _publishingYear, _languageDouble);

        // Act
        boolean result = e.isByPublishingCompanyId(otherPublishingCompanyIdDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void isByPublishingCompanyIdNullReturnsFalse() {
        // SUT
        Edition e = new Edition(_bookTypeId, _bookIdentifier, _publicationIdDouble, _publishingCompanyIdDouble, _publishingYear, _languageDouble);

        // Act
        boolean result = e.isByPublishingCompanyId(null);

        // Assert
        assertFalse(result);
    }
}