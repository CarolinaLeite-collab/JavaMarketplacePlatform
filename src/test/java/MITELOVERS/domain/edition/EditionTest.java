package MITELOVERS.domain.edition;

import MITELOVERS.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EditionTest {

    private EditionId _editionIdDouble;

    private PublicationTypeId _bookTypeIdDouble;
    private PublicationTypeId _magazineTypeIdDouble;

    private Identifier _bookIdentifierDouble;
    private Identifier _magazineIdentifierDouble;
    private Identifier _noIdentifierDouble;

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

    private static final String expectedMessageEditionId = "EditionId is required";
    private static final String expectedMessageTypeId = "Publication Type Id is required";
    private static final String expectedMessagePublicationId = "Publication Id is required";
    private static final String expectedMessageCompanyId = "Publishing Company is required";
    private static final String expectedMessageYear = "Publishing Year is required";
    private static final String expectedMessageLanguage = "Language is required";
    private static final String expectedMessageInvalidIdentifier = "Invalid identifier for given type and year";
    private static final String expectedMessageIdentifier = "Identifier is required";

    @BeforeEach
    void setUp() {

        _editionIdDouble = mock(EditionId.class);

        _bookTypeIdDouble = mock(PublicationTypeId.class);
        when(_bookTypeIdDouble.isBook()).thenReturn(true);
        _magazineTypeIdDouble = mock(PublicationTypeId.class);
        when(_magazineTypeIdDouble.isMagazine()).thenReturn(true);

        _bookIdentifierDouble = mock(ISBN.class);
        _magazineIdentifierDouble = mock(ISSN.class);
        _noIdentifierDouble = mock(NoIdentifier.class);

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
                _editionIdDouble,
                _bookTypeIdDouble,
                _bookIdentifierDouble,
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
    void testConstructorWithoutEditionId() {

        //SUT
        new Edition(
                _bookTypeIdDouble,
                _bookIdentifierDouble,
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
                _bookTypeIdDouble,
                _bookIdentifierDouble,
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
                _bookTypeIdDouble,
                _bookIdentifierDouble,
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
    void shouldThrowWhenEditionIdIsNull() {
        //Act
        //SUT
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () ->
                        new Edition(null,
                                _bookTypeIdDouble,
                                _bookIdentifierDouble,
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
        assertEquals(expectedMessageEditionId, exception.getMessage());

    }

    @Test
    void shouldThrowWhenTypeIdIsNull() {
        //Act
        //SUT
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () ->
                        new Edition(null,
                                _bookIdentifierDouble,
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
                        new Edition(_bookTypeIdDouble,
                                _bookIdentifierDouble,
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
                                _bookTypeIdDouble,
                                _bookIdentifierDouble,
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
                                _bookTypeIdDouble,
                                _bookIdentifierDouble,
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
                                _bookTypeIdDouble,
                                _bookIdentifierDouble,
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
                                _bookTypeIdDouble,
                                _magazineIdentifierDouble,
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
    void shouldThrowWhenBookYearIsAfter1970AndIdentifierIsNotIsbn() {

        //Act
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () ->
                        new Edition(
                                _bookTypeIdDouble,
                                _magazineIdentifierDouble,
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
    void shouldCreateBookEditionWhenYearIs1970AndIdentifierIsNoIdentifier() {
        new Edition(
                _bookTypeIdDouble,
                _noIdentifierDouble,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                Year.of(1970),  // isAfter(1970) → false, so NoIdentifier is valid
                _languageDouble
        );
    }

    @Test
    void shouldCreateBookEditionWhenYearIsBefore1970() {

        //SUT
        new Edition(
                _bookTypeIdDouble,
                _noIdentifierDouble,
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
    void shouldThrowWhenBookYearIsBefore1970AndIdentifierIsNull() {

        //SUT
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () ->
                        new Edition(
                                _bookTypeIdDouble,
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
                        )
                );

        //Assert
        assertEquals(expectedMessageIdentifier, exception.getMessage());

    }

    @Test
    void shouldCreateMagazineEditionWhenYearIsAfter1975AndIdentifierIsIssn() {
        //SUT
        new Edition(
                _magazineTypeIdDouble,
                _magazineIdentifierDouble,
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
                                _magazineTypeIdDouble,
                                _bookIdentifierDouble,
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
    void shouldCreateMagazineEditionWhenYearIsBefore1975() {
        //SUT
        new Edition(
                _magazineTypeIdDouble,
                _noIdentifierDouble,
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
    void shouldCreateMagazineEditionWhenYearIs1975AndIdentifierIsNoIdentifier() {
        //SUT
        new Edition(
                _magazineTypeIdDouble,
                _noIdentifierDouble,
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
                                _bookIdentifierDouble,
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
                _bookTypeIdDouble,
                _bookIdentifierDouble,
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
                        "\nType: " + _bookTypeIdDouble +
                        "\nIdentifier: " + _bookIdentifierDouble +
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
                _bookTypeIdDouble,
                _bookIdentifierDouble,
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
                        "\nType: " + _bookTypeIdDouble +
                        "\nIdentifier: " + _bookIdentifierDouble +
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
                _bookTypeIdDouble,
                _bookIdentifierDouble,
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
                _bookTypeIdDouble,
                _bookIdentifierDouble,
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
                _bookTypeIdDouble,
                _bookIdentifierDouble,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                _publishingYear,
                _languageDouble
        );

        Edition edition2 = new Edition(
                _bookTypeIdDouble,
                _bookIdentifierDouble,
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
                _bookTypeIdDouble,
                _bookIdentifierDouble,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                _publishingYear,
                _languageDouble
        );

        Edition edition2 = new Edition(
                _bookTypeIdDouble,
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
    void sameAsShouldReturnTrueWhenIdentifiersAreNoIdentifierAndBusinessFieldsAreEqual() {

        //Arrange
        //SUT
        Year oldYear = Year.of(1969);

        Edition edition1 = new Edition(
                _bookTypeIdDouble,
                _noIdentifierDouble,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                oldYear,
                _languageDouble
        );

        Edition edition2 = new Edition(
                _bookTypeIdDouble,
                _noIdentifierDouble,
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
    void sameAsShouldReturnFalseWhenIdentifiersAreNoIdentifierAndPublicationIsDifferent() {

        //Arrange
        //SUT
        Year oldYear = Year.of(1969);
        PublicationId otherPublicationId = mock(PublicationId.class);

        Edition edition1 = new Edition(
                _bookTypeIdDouble,
                _noIdentifierDouble,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                oldYear,
                _languageDouble
        );

        Edition edition2 = new Edition(
                _bookTypeIdDouble,
                _noIdentifierDouble,
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
        Year oldBookYear = Year.of(1969);
        Year oldMagazineYear = Year.of(1975);

        Edition edition1 = new Edition(
                _bookTypeIdDouble,
                _noIdentifierDouble,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                oldBookYear,
                _languageDouble
        );

        Edition edition2 = new Edition(
                _magazineTypeIdDouble,
                _noIdentifierDouble,
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
    void sameAsShouldReturnFalseWhenPublishingCompanyDiffers() {

        //Arrange
        Year oldYear = Year.of(1969);
        PublishingCompanyId otherCompany = mock(PublishingCompanyId.class);

        //SUT
        Edition edition1 = new Edition(
                _bookTypeIdDouble,
                _noIdentifierDouble,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                oldYear,
                _languageDouble
        );

        Edition edition2 = new Edition(
                _bookTypeIdDouble,
                _noIdentifierDouble,
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
                _bookTypeIdDouble,
                _noIdentifierDouble,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                oldYear,
                _languageDouble
        );

        Edition edition2 = new Edition(
                _bookTypeIdDouble,
                _noIdentifierDouble,
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
        Year oldYear = Year.of(1969);
        Language otherLanguage = mock(Language.class);

        //SUT
        Edition edition1 = new Edition(
                _bookTypeIdDouble,
                _noIdentifierDouble,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                oldYear,
                _languageDouble
        );

        Edition edition2 = new Edition(
                _bookTypeIdDouble,
                _noIdentifierDouble,
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
                _bookTypeIdDouble,
                _bookIdentifierDouble,
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
    void equalsShouldReturnTrueWhenEditionsHaveSameGeneratedId() {

        // Arrange
        EditionId sharedId = new EditionId("E-12345678");

        Edition edition1 = new Edition(
                sharedId,
                _bookTypeIdDouble,
                _bookIdentifierDouble,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                _publishingYear,
                _languageDouble,
                null, null, null, null, null
        );

        Edition edition2 = new Edition(
                sharedId,
                _bookTypeIdDouble,
                _bookIdentifierDouble,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                _publishingYear,
                _languageDouble,
                null, null, null, null, null
        );

        // Act
        boolean result = edition1.equals(edition2);

        // Assert
        assertTrue(result);
    }

    @Test
    void equalsShouldReturnFalseWhenObjectIsNull() {

        //Arrange
        //SUT
        Edition edition = new Edition(
                _bookTypeIdDouble,
                _bookIdentifierDouble,
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
                _bookTypeIdDouble,
                _bookIdentifierDouble,
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
                _bookTypeIdDouble,
                _bookIdentifierDouble,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                _publishingYear,
                _languageDouble
        );

        Edition edition2 = new Edition(
                _bookTypeIdDouble,
                _bookIdentifierDouble,
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
                _editionIdDouble,
                _bookTypeIdDouble,
                _bookIdentifierDouble,
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
        assertEquals(_editionIdDouble, edition.getEditionId());
        assertEquals(_bookTypeIdDouble, edition.getPublicationTypeId());
        assertEquals(_bookIdentifierDouble, edition.getIdentifier());
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
                _bookTypeIdDouble,
                _bookIdentifierDouble,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                _publishingYear,
                _languageDouble
        );

        Edition edition2 = new Edition(
                _bookTypeIdDouble,
                _bookIdentifierDouble,
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
        Edition e = new Edition(_bookTypeIdDouble, _bookIdentifierDouble, _publicationIdDouble, _publishingCompanyIdDouble, _publishingYear, _languageDouble);

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
        Edition e = new Edition(_bookTypeIdDouble, _bookIdentifierDouble, _publicationIdDouble, _publishingCompanyIdDouble, _publishingYear, _languageDouble);

        // Act
        boolean result = e.isByPublicationId(otherPublicationIdDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void isByPublicationIdNullReturnsFalse() {
        // SUT
        Edition e = new Edition(_bookTypeIdDouble, _bookIdentifierDouble, _publicationIdDouble, _publishingCompanyIdDouble, _publishingYear, _languageDouble);

        // Act
        boolean result = e.isByPublicationId(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void isByPublishingCompanyIdSamePublishingCompanyReturnsTrue() {
        // SUT
        Edition e = new Edition(_bookTypeIdDouble, _bookIdentifierDouble, _publicationIdDouble, _publishingCompanyIdDouble, _publishingYear, _languageDouble);

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
        Edition e = new Edition(_bookTypeIdDouble, _bookIdentifierDouble, _publicationIdDouble, _publishingCompanyIdDouble, _publishingYear, _languageDouble);

        // Act
        boolean result = e.isByPublishingCompanyId(otherPublishingCompanyIdDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void isByPublishingCompanyIdNullReturnsFalse() {
        //SUT
        Edition e = new Edition(_bookTypeIdDouble, _bookIdentifierDouble, _publicationIdDouble, _publishingCompanyIdDouble, _publishingYear, _languageDouble);

        //Act
        boolean result = e.isByPublishingCompanyId(null);

        //Assert
        assertFalse(result);
    }

    @Test
    void shouldThrowWhenBookYearIsBefore1970AndIdentifierIsIsbn() {
        //Act
        //SUT
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () ->
                        new Edition(
                                _bookTypeIdDouble,
                                _bookIdentifierDouble,
                                _publicationIdDouble,
                                _publishingCompanyIdDouble,
                                Year.of(1969),
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
    void shouldThrowWhenMagazineYearIsBeforeOrEqualTo1976AndIdentifierIsIssn() {
        //Act
        //SUT
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () ->
                        new Edition(
                                _magazineTypeIdDouble,
                                _magazineIdentifierDouble,
                                _publicationIdDouble,
                                _publishingCompanyIdDouble,
                                Year.of(1975),
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
}
