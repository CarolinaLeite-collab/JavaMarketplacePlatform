import TOPSECRET.domain.edition.EditionMagazine;
import TOPSECRET.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class EditionMagazineTest {

    private MagazineId _magazineId;
    private PublicationId _publicationId;
    private PublishingCompanyId _companyId;
    private Year _publishingYear;
    private Language _language;
    private IssueNumber _issueNumber;
    private Periodicity _periodicity;
    private Dimension _dimension;
    private Weight _weight;

    @BeforeEach
    void setUp() {
        _magazineId = mock(MagazineId.class);
        _publicationId = mock(PublicationId.class);
        _companyId = mock(PublishingCompanyId.class);
        _publishingYear = Year.of(2020);
        _language = mock(Language.class);
        _issueNumber = mock(IssueNumber.class);
        _periodicity = mock(Periodicity.class);
        _dimension = mock(Dimension.class);
        _weight = mock(Weight.class);
    }

    @Test
    void shouldBuildEditionMagazineSuccessfully() {
        //Act
        //SUT
        EditionMagazine magazine = new EditionMagazine.Builder(
                _magazineId,
                _publicationId,
                _companyId,
                _publishingYear,
                _language,
                _issueNumber,
                _periodicity
        ).build();

        //Assert
        assertNotNull(magazine);
        assertSame(_magazineId, magazine.getId());
        assertSame(_publicationId, magazine.getPublication());
        assertSame(_companyId, magazine.getPublishingCompany());
        assertSame(_publishingYear, magazine.getPublishingYear());
        assertSame(_language, magazine.getEditionLanguage());
        assertSame(_issueNumber, magazine.getIssueNumber());
        assertSame(_periodicity, magazine.getPeriodicity());
    }

    @Test
    void shouldBuildEditionMagazineWithOptionalFields() {
        //Act
        //SUT
        EditionMagazine magazine = new EditionMagazine.Builder(
                _magazineId,
                _publicationId,
                _companyId,
                _publishingYear,
                _language,
                _issueNumber,
                _periodicity
        )
                .withDimension(_dimension)
                .withWeight(_weight)
                .build();

        //Assert
        assertSame(_dimension, magazine.getDimension());
        assertSame(_weight, magazine.getWeight());
    }

    @Test
    void shouldThrowWhenMagazineIdIsNull() {
        //Act & Assert
        //SUT
        assertThrows(IllegalArgumentException.class, () ->
                new EditionMagazine.Builder(
                        null,
                        _publicationId,
                        _companyId,
                        _publishingYear,
                        _language,
                        _issueNumber,
                        _periodicity
                ).build()
        );
    }

    @Test
    void shouldThrowWhenPublicationIdIsNull() {
        //Act & Assert
        //SUT
        assertThrows(IllegalArgumentException.class, () ->
                new EditionMagazine.Builder(
                        _magazineId,
                        null,
                        _companyId,
                        _publishingYear,
                        _language,
                        _issueNumber,
                        _periodicity
                ).build()
        );
    }

    @Test
    void shouldThrowWhenPublishingCompanyIsNull() {
        //Act & Assert
        //SUT
        assertThrows(IllegalArgumentException.class, () ->
                new EditionMagazine.Builder(
                        _magazineId,
                        _publicationId,
                        null,
                        _publishingYear,
                        _language,
                        _issueNumber,
                        _periodicity
                ).build()
        );
    }

    @Test
    void shouldThrowWhenPublishingYearIsNull() {
        //Act & Assert
        //SUT
        assertThrows(IllegalArgumentException.class, () ->
                new EditionMagazine.Builder(
                        _magazineId,
                        _publicationId,
                        _companyId,
                        null,
                        _language,
                        _issueNumber,
                        _periodicity
                ).build()
        );
    }

    @Test
    void shouldThrowWhenLanguageIsNull() {
        //Act & Assert
        //SUT
        assertThrows(IllegalArgumentException.class, () ->
                new EditionMagazine.Builder(
                        _magazineId,
                        _publicationId,
                        _companyId,
                        _publishingYear,
                        null,
                        _issueNumber,
                        _periodicity
                ).build()
        );
    }

    @Test
    void shouldThrowWhenIssueNumberIsNull() {
        //Act & Assert
        //SUT
        assertThrows(IllegalArgumentException.class, () ->
                new EditionMagazine.Builder(
                        _magazineId,
                        _publicationId,
                        _companyId,
                        _publishingYear,
                        _language,
                        null,
                        _periodicity
                ).build()
        );
    }

    @Test
    void shouldThrowWhenPeriodicityIsNull() {
        //Act & Assert
        //SUT
        assertThrows(IllegalArgumentException.class, () ->
                new EditionMagazine.Builder(
                        _magazineId,
                        _publicationId,
                        _companyId,
                        _publishingYear,
                        _language,
                        _issueNumber,
                        null
                ).build()
        );
    }
}