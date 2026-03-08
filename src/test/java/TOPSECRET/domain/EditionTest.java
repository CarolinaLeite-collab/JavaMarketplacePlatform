
package TOPSECRET.domain;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class EditionTest {
    @Test
    void validEditionWithIssn() {
        Edition edit = new Edition(
                new ISSN("1018-4783"),
                new NumberOfPages(30),
                3,
                LocalDate.of(2001, 4, 23),
                Binding.SADDLE_STITCH,
                new Description("Amazing Magazine"),
                new Dimension(21, 29.7, 1, DimensionUnit.CENTIMETERS),
                new Weight(224.7, Weight.WeightUnit.GRAMS),
                Language.of("pt", "Portuguese", "Português"));

        assertEquals(3, edit.getEditionNumber());
        assertEquals(new NumberOfPages(30), edit.getNumberOfPages());
        assertNotNull(edit);
        assertNotNull(edit.getIssn());
        assertNull(edit.getIsbn());
    }

    @Test
    void invalidEditionWithoutIssn() {
        assertThrows(IllegalArgumentException.class, () -> new Edition(
                (ISSN) null,    // null as object ISSN - ISSN not exist
                new NumberOfPages(30),
                3,
                LocalDate.of(2001, 4, 23),
                Binding.SADDLE_STITCH,
                new Description("Amazing Magazine"),
                new Dimension(21, 29.7, 1, DimensionUnit.CENTIMETERS),
                new Weight(224.7, Weight.WeightUnit.GRAMS),
                Language.of("pt", "Portuguese", "Português")));

    }

    @Test
    void validEditionWithIsbn() {
        Edition edit1 = new Edition (
                new ISBN("0306406152"),
                new NumberOfPages(250),
                1,
                LocalDate.of(1992, 5, 12),
                Binding.PUR,
                new Description("Amazing Book"),
                new Dimension(21, 29.7, 1, DimensionUnit.CENTIMETERS),
                new Weight(224.7, Weight.WeightUnit.GRAMS),
                Language.of("pt", "Portuguese", "Português"));

        assertNull(edit1.getIssn());
        assertNotNull(edit1.getIsbn());
        assertEquals(1, edit1.getEditionNumber());
    }

    @Test
    void invalidEditionWithoutIsbn() {
        assertThrows(IllegalArgumentException.class, () -> new Edition(
                (ISBN) null,    // null as object ISBN - ISBN not exist
                new NumberOfPages(250),
                1,
                LocalDate.of(1992, 5, 12),
                Binding.PUR,
                new Description("Amazing Book"),
                new Dimension(21, 29.7, 1, DimensionUnit.CENTIMETERS),
                new Weight(224.7, Weight.WeightUnit.GRAMS),
                Language.of("pt", "Portuguese", "Português")));
    }

    @Test
    void invalidEditionWithIsbnAndWithNullEditionNumber() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Edition(
                    new ISBN("0306406152"),
                    new NumberOfPages(300),
                    null,
                    LocalDate.of(1940, 2, 3),
                    Binding.SADDLE_STITCH,
                    new Description("Old book"),
                    new Dimension(21, 29.7, 1, DimensionUnit.CENTIMETERS),
                    new Weight(224.7, Weight.WeightUnit.GRAMS),
                    Language.of("pt", "Portuguese", "Português"));});
    }

    @Test
    void validEditionWithoutIssnAndIsbnAndWithNullEditionNumber(){
        Edition edit2 = new Edition(
                new NumberOfPages(30),
                null,
                LocalDate.of(1940, 2, 3),
                Binding.SADDLE_STITCH,
                new Description("Old book"),
                new Dimension(21, 29.7, 1, DimensionUnit.CENTIMETERS),
                new Weight(224.7, Weight.WeightUnit.GRAMS),
                Language.of("pt", "Portuguese", "Português"));

        assertNull(edit2.getIssn());
        assertNull(edit2.getIsbn());
        assertNull(edit2.getEditionNumber());
    }

    @Test
    void validEditionWithoutIssnAndIsbnWithEditionNumberOne() {
        Edition edit3 = new Edition(
                new NumberOfPages(30),
                1,   // boundary value
                LocalDate.of(2022, 10, 1),
                Binding.SADDLE_STITCH,
                new Description("Old magazine"),
                new Dimension(20, 30, 1, DimensionUnit.CENTIMETERS),
                new Weight(200, Weight.WeightUnit.GRAMS),
                Language.of("pt", "Portuguese", "Português")
        );

        assertEquals(1, edit3.getEditionNumber());
    }


    @Test
    void invalidEditionWithoutIssnAndIsbnWithNegativeEditionNumber(){
        assertThrows(IllegalArgumentException.class, () -> {
            new Edition(
                    new NumberOfPages(30),
                    -1,
                    LocalDate.of(1940, 11, 1),
                    Binding.SADDLE_STITCH,
                    new Description("Old Book"),
                    new Dimension(20, 30, 2, DimensionUnit.CENTIMETERS),
                    new Weight(300, Weight.WeightUnit.GRAMS),
                    Language.of("pt", "Portuguese", "Português"));});
    }

    @Test
    void invalidEditionWithNullEditionNumber() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Edition(
                    new ISBN("0306406152"),
                    new NumberOfPages(30),
                    null,
                    LocalDate.of(2001, 4, 23),
                    Binding.PUR,
                    new Description("Amazing Book"),
                    new Dimension(20, 30, 2, DimensionUnit.CENTIMETERS),
                    new Weight(300, Weight.WeightUnit.GRAMS),
                    Language.of("pt", "Portuguese", "Português"));});
    }

    @Test
    void validEditionWithEditionNumberOne() {
        Edition edit4 = new Edition(
                new ISSN ("1018-4783"),
                new NumberOfPages(30),
                1,
                LocalDate.of(2020, 1, 1),
                Binding.PUR,
                new Description("Amazing Magazine"),
                new Dimension(20, 30, 2, DimensionUnit.CENTIMETERS),
                new Weight(100, Weight.WeightUnit.GRAMS),
                Language.of("pt", "Portuguese", "Português"));

        assertEquals(1, edit4.getEditionNumber());
    }

    @Test
    void invalidEditionWithNegativeEditionNumber() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Edition(
                    new ISBN("0306406152"),
                    new NumberOfPages(30),
                    -3,
                    LocalDate.of(2001, 4, 23),
                    Binding.SADDLE_STITCH,
                    new Description("Amazing Book"),
                    new Dimension(21, 29.7, 1, DimensionUnit.CENTIMETERS),
                    new Weight(224.7, Weight.WeightUnit.GRAMS),
                    Language.of("pt", "Portuguese", "Português"));});
    }

    @Test
    void invalidEditionWithValidIsbnAndEditionNumberZero(){
        assertThrows(IllegalArgumentException.class, () -> {
            new Edition(
                    new ISBN("0306406152"),
                    new NumberOfPages(30),
                    0,
                    LocalDate.of(2001, 4, 23),
                    Binding.SADDLE_STITCH,
                    new Description("Amazing Magazine"),
                    new Dimension(21, 29.7, 1, DimensionUnit.CENTIMETERS),
                    new Weight(224.7, Weight.WeightUnit.GRAMS),
                    Language.of("pt", "Portuguese", "Português"));});
    }

    @Test
    void invalidEditionWithValidIssnAndEditionNumberZero(){
        assertThrows(IllegalArgumentException.class, () -> {
            new Edition(
                    new ISSN("1018-4783"),
                    new NumberOfPages(30),
                    0,
                    LocalDate.of(2020, 1, 1),
                    Binding.SADDLE_STITCH,
                    new Description("Pages validation"),
                    new Dimension(20, 30, 1, DimensionUnit.CENTIMETERS),
                    new Weight(200, Weight.WeightUnit.GRAMS),
                    Language.of("pt", "Portuguese", "Português"));});
    }

    @Test
    void invalidEditionWithNegativeNumberOfPages(){
        assertThrows(IllegalArgumentException.class, () -> {
            new Edition(
                    new ISBN("0306406152"),
                    new NumberOfPages(-30),
                    1,
                    LocalDate.of(2001, 4, 23),
                    Binding.SADDLE_STITCH,
                    new Description("Amazing Magazine"),
                    new Dimension(21, 29.7, 1, DimensionUnit.CENTIMETERS),
                    new Weight(224.7, Weight.WeightUnit.GRAMS),
                    Language.of("pt", "Portuguese", "Português"));});
    }

    @Test
    void invalidEditionWithIsbnButZeroNumberOfPages(){
        assertThrows(IllegalArgumentException.class, () -> {
            new Edition(
                    new ISBN("0306406152"),
                    new NumberOfPages(0),
                    1,
                    LocalDate.of(2001, 4, 23),
                    Binding.SADDLE_STITCH,
                    new Description("Amazing Magazine"),
                    new Dimension(21, 29.7, 1, DimensionUnit.CENTIMETERS),
                    new Weight(224.7, Weight.WeightUnit.GRAMS),
                    Language.of("pt", "Portuguese", "Português"));});

    }

    @Test
    void invalidEditionWithIssnButZeroNumberOfPages() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Edition(
                    new ISSN("1018-4783"),
                    new NumberOfPages(0),
                    1,
                    LocalDate.of(2020, 1, 1),
                    Binding.SADDLE_STITCH,
                    new Description("Pages validation"),
                    new Dimension(20, 30, 1, DimensionUnit.CENTIMETERS),
                    new Weight(200, Weight.WeightUnit.GRAMS),
                    Language.of("pt", "Portuguese", "Português"));});
    }

    @Test
    void invalidEditionWithoutIssnAndIsbnButZeroNumberOfPages() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Edition(
                    new NumberOfPages(0),
                    1,
                    LocalDate.of(2020, 1, 1),
                    Binding.SADDLE_STITCH,
                    new Description("Pages validation"),
                    new Dimension(20, 30, 1, DimensionUnit.CENTIMETERS),
                    new Weight(200, Weight.WeightUnit.GRAMS),
                    Language.of("pt", "Portuguese", "Português"));});
    }

    @Test
    void validEditionWithOneNumberOfPages(){
        Edition edit5 = new Edition(
                new ISSN ("1018-4783"),
                new NumberOfPages(1), // boundary valid value
                2,
                LocalDate.of(2020, 11, 1),
                Binding.PUR,
                new Description("One page edition"),
                new Dimension(20, 30, 0.3, DimensionUnit.CENTIMETERS),
                new Weight(10, Weight.WeightUnit.GRAMS),
                Language.of("pt", "Portuguese", "Português")
        );

        assertEquals(new NumberOfPages(1), edit5.getNumberOfPages());
    }

    @Test
    void gettersReturnCorrectValues(){
        LocalDate date = LocalDate.of(2001, 4, 23);
        Description descript = new Description("Amazing Magazine");
        Dimension dim = new Dimension(21, 29.7, 1, DimensionUnit.CENTIMETERS);
        Weight w = new Weight(224.7, Weight.WeightUnit.GRAMS);
        Language lang = Language.of ("pt", "Portuguese", "Português");
        NumberOfPages pages = new NumberOfPages(30);

        Edition edit6 = new Edition (
                new ISSN ("1018-4783"),
                pages,
                3,
                date,
                Binding.SADDLE_STITCH,
                descript,
                dim,
                w,
                lang);

        assertEquals(3, edit6.getEditionNumber());
        assertEquals(pages, edit6.getNumberOfPages());
        assertEquals(date, edit6.getPublicationDate());
        assertEquals(Binding.SADDLE_STITCH, edit6.getBinding());
        assertEquals(descript, edit6.getDescription());
        assertEquals(dim, edit6.getDimension());
        assertEquals(w, edit6.getWeight());
        assertEquals(lang, edit6.getLanguage());
    }
}


