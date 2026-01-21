package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PublicationInfoTest {

    @Test
    void shouldCreatePublicationInfoForBook() {
        PublicationInfo publicationInfoBook = new PublicationInfo(
                new Title("title"),
                new Genre("action"),
                new Author("Eça de Queirós"),
                new Edition(
                        new ISBN("9780306406157"),
                        30,
                        3,
                        LocalDate.of(2001, 4, 23),
                        Binding.SADDLE_STITCH,
                        new Description("Amazing Magazine"),
                        new Dimension(21, 29.7, 1, DimensionUnit.CENTIMETERS),
                        new Weight(224.7, Weight.WeightUnit.GRAMS),
                        Language.of("pt", "Portuguese", "Português")),
                new Publisher("My Publisher")
        );
        assertNotNull(publicationInfoBook);
    }
    @Test
    void shouldCreatePublicationInfoForMagazine() {

        PublicationInfo publicationInfoMagazine = new PublicationInfo(
                new Title ("title"),
                new Genre("action"),
                new Author ("Eça de Queirós"),
                new Edition(
                        new ISSN("1018-4783"),
                        30,
                        3,
                        LocalDate.of(2001, 4, 23),
                        Binding.SADDLE_STITCH,
                        new Description("Amazing Magazine"),
                        new Dimension(21, 29.7, 1, DimensionUnit.CENTIMETERS),
                        new Weight(224.7, Weight.WeightUnit.GRAMS),
                        Language.of("pt", "Portuguese", "Português")),
                new Publisher("My Publisher")
        );
        assertNotNull(publicationInfoMagazine);
    }
}