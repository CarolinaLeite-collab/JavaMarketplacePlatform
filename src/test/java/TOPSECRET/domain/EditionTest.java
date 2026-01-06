package TOPSECRET.domain;

import net.bytebuddy.implementation.bytecode.Throw;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class EditionTest {
    @Test
    void validEditionWithIssn() {
        Edition edit = new Edition(new ISSN("1018-4783"),
                null,
                30,
                3,
                LocalDate.of(2001, 4, 23),
                Binding.SADDLE_STITCH,
                new Description("Amazing Magazine"),
                new Dimension(21, 29.7, 1, DimensionUnit.CENTIMETERS),
                new Weight(224.7, Weight.WeightUnit.GRAMS),
                Language.of("pt", "Portuguese", "Português"));

        assertEquals(3, edit.getEditionNumber());
        assertEquals(30, edit.getNumberOfPages());
        assertNotNull(edit);
        assertNotNull(edit.getIssn());
        assertNull(edit.getIsbn());
    }

    @Test
    void validEditionWithIsbn() {
        Edition edit1 = new Edition (
                null,
                new ISBN(9789720048758L),
                250,
                1,
                LocalDate.of(1992, 5, 12),
                Binding.PUR,
                new Description("Amazing Book"),
                new Dimension(21, 29.7, 1, DimensionUnit.CENTIMETERS),
                new Weight(224.7, Weight.WeightUnit.GRAMS),
                Language.of("pt", "Portuguese", "Português"));

        assertNull(edit1.getIssn());
        assertNotNull(edit1.getIsbn());
    }

    @Test
    void editionWithIssnAndIsbn() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Edition(new ISSN("1018-4783"),
                    new ISBN(9789720048758L),
                    30,
                    3,
                    LocalDate.of(2001, 4, 23),
                    Binding.SADDLE_STITCH,
                    new Description("Amazing Magazine"),
                    new Dimension(21, 29.7, 1, DimensionUnit.CENTIMETERS),
                    new Weight(224.7, Weight.WeightUnit.GRAMS),
                    Language.of("pt", "Portuguese", "Português"));});
    }

    @Test
    void editionWithoutIssnAndIsbn(){
        Edition edit2 = new Edition(null,
                null,
                30,
                3,
                LocalDate.of(2001, 4, 23),
                Binding.SADDLE_STITCH,
                new Description("Amazing Magazine"),
                new Dimension(21, 29.7, 1, DimensionUnit.CENTIMETERS),
                new Weight(224.7, Weight.WeightUnit.GRAMS),
                Language.of("pt", "Portuguese", "Português"));

        assertNull(edit2.getIssn());
        assertNull(edit2.getIsbn());
    }

    @Test
    void negativeEditionNumber() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Edition(null,
                    new ISBN(9789720048758L),
                    30,
                    -3,
                    LocalDate.of(2001, 4, 23),
                    Binding.SADDLE_STITCH,
                    new Description("Amazing Magazine"),
                    new Dimension(21, 29.7, 1, DimensionUnit.CENTIMETERS),
                    new Weight(224.7, Weight.WeightUnit.GRAMS),
                    Language.of("pt", "Portuguese", "Português"));});
    }

    @Test
    void zeroEditionNumber(){
        assertThrows(IllegalArgumentException.class, () -> {
            new Edition(null,
                    new ISBN(9789720048758L),
                    30,
                    0,
                    LocalDate.of(2001, 4, 23),
                    Binding.SADDLE_STITCH,
                    new Description("Amazing Magazine"),
                    new Dimension(21, 29.7, 1, DimensionUnit.CENTIMETERS),
                    new Weight(224.7, Weight.WeightUnit.GRAMS),
                    Language.of("pt", "Portuguese", "Português"));});
    }

    @Test
    void negativeNumberOfPages(){
        assertThrows(IllegalArgumentException.class, () -> {
            new Edition(null,
                    new ISBN(9789720048758L),
                    -30,
                    0,
                    LocalDate.of(2001, 4, 23),
                    Binding.SADDLE_STITCH,
                    new Description("Amazing Magazine"),
                    new Dimension(21, 29.7, 1, DimensionUnit.CENTIMETERS),
                    new Weight(224.7, Weight.WeightUnit.GRAMS),
                    Language.of("pt", "Portuguese", "Português"));});
    }

    @Test
    void zeroNumberOfPages(){
        assertThrows(IllegalArgumentException.class, () -> {
            new Edition(null,
                    new ISBN(9789720048758L),
                    0,
                    0,
                    LocalDate.of(2001, 4, 23),
                    Binding.SADDLE_STITCH,
                    new Description("Amazing Magazine"),
                    new Dimension(21, 29.7, 1, DimensionUnit.CENTIMETERS),
                    new Weight(224.7, Weight.WeightUnit.GRAMS),
                    Language.of("pt", "Portuguese", "Português"));});

    }

}

