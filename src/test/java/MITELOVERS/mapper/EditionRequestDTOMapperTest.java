package MITELOVERS.mapper;

import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.DimensionDTO;
import MITELOVERS.dto.WeightDTO;
import MITELOVERS.dto.request.EditionRequestDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EditionRequestDTOMapperTest {


    @Test
    void toPublicationTypeIdReturnsCorrectVO() {

        // Arrange
        EditionRequestDTO dto = EditionRequestDTO.builder()
                .publicationTypeId("BOOK")
                .publishingYear(2000)
                .build();

        // SUT
        EditionRequestDTOMapper mapper = new EditionRequestDTOMapper();

        // Act
        PublicationTypeId result = mapper.toPublicationTypeId(dto);

        // Assert
        assertEquals("BOOK", result.toString());

    }

    @Test
    void toIdentifierNullReturnsNoIdentifier() {

        // Arrange
        EditionRequestDTO dto = EditionRequestDTO.builder()
                .publicationTypeId("BOOK")
                .publishingYear(2000)
                .identifier(null)
                .build();

        // SUT
        EditionRequestDTOMapper mapper = new EditionRequestDTOMapper();

        // Act
        Identifier result = mapper.toIdentifier(dto);

        // Assert
        assertInstanceOf(NoIdentifier.class, result);

    }

    @Test
    void toIdentifierBlankReturnsNoIdentifier() {

        // Arrange
        EditionRequestDTO dto = EditionRequestDTO.builder()
                .publicationTypeId("BOOK")
                .publishingYear(2000)
                .identifier("  ")
                .build();

        // SUT
        EditionRequestDTOMapper mapper = new EditionRequestDTOMapper();

        // Act
        Identifier result = mapper.toIdentifier(dto);

        // Assert
        assertInstanceOf(NoIdentifier.class, result);

    }

    @Test
    void toIdentifierBookAfter1970ReturnsISBN() {

        // Arrange
        EditionRequestDTO dto = EditionRequestDTO.builder()
                .publicationTypeId("BOOK")
                .publishingYear(2000)
                .identifier("9780747532743")
                .build();

        // SUT
        EditionRequestDTOMapper mapper = new EditionRequestDTOMapper();

        // Act
        Identifier result = mapper.toIdentifier(dto);

        // Assert
        assertInstanceOf(ISBN.class, result);

    }

    @Test
    void toIdentifierBookBefore1970ReturnsNoIdentifier() {

        // Arrange
        EditionRequestDTO dto = EditionRequestDTO.builder()
                .publicationTypeId("BOOK")
                .publishingYear(1960)
                .identifier("9780747532743")
                .build();

        // SUT
        EditionRequestDTOMapper mapper = new EditionRequestDTOMapper();

        // Act
        Identifier result = mapper.toIdentifier(dto);

        // Assert
        assertInstanceOf(NoIdentifier.class, result);

    }

    @Test
    void toIdentifierMagazineAfter1976ReturnsISSN() {

        // Arrange
        EditionRequestDTO dto = EditionRequestDTO.builder()
                .publicationTypeId("MAGAZINE")
                .publishingYear(2000)
                .identifier("2156-5570")
                .build();

        // SUT
        EditionRequestDTOMapper mapper = new EditionRequestDTOMapper();

        // Act
        Identifier result = mapper.toIdentifier(dto);

        // Assert
        assertInstanceOf(ISSN.class, result);

    }

    @Test
    void toIdentifierMagazineBefore1976ReturnsNoIdentifier() {

        // Arrange
        EditionRequestDTO dto = EditionRequestDTO.builder()
                .publicationTypeId("MAGAZINE")
                .publishingYear(1970)
                .identifier("2156-5570")
                .build();

        // SUT
        EditionRequestDTOMapper mapper = new EditionRequestDTOMapper();

        // Act
        Identifier result = mapper.toIdentifier(dto);

        // Assert
        assertInstanceOf(NoIdentifier.class, result);

    }

    // ── toDimension ─────────────────────────────────────────────────────────

    @Test
    void toDimensionNullReturnsNull() {

        // Arrange
        EditionRequestDTO dto = EditionRequestDTO.builder()
                .publicationTypeId("BOOK")
                .publishingYear(2000)
                .dimension(null)
                .build();

        // SUT
        EditionRequestDTOMapper mapper = new EditionRequestDTOMapper();

        // Act
        Dimension result = mapper.toDimension(dto);

        // Assert
        assertNull(result);

    }

    @Test
    void toDimensionReturnsDimension() {

        // Arrange
        EditionRequestDTO dto = EditionRequestDTO.builder()
                .publicationTypeId("BOOK")
                .publishingYear(2000)
                .dimension(new DimensionDTO(20.0, 25.0, 3.0, "cm"))
                .build();

        // SUT
        EditionRequestDTOMapper mapper = new EditionRequestDTOMapper();

        // Act
        Dimension result = mapper.toDimension(dto);

        // Assert
        assertNotNull(result);
        assertAll(
                () -> assertEquals(20.0, result.getWidth()),
                () -> assertEquals(25.0, result.getHeight()),
                () -> assertEquals(3.0, result.getThickness()),
                () -> assertEquals(DimensionUnit.CENTIMETERS, result.getUnit())
        );

    }


    @Test
    void toWeightNullReturnsNull() {

        // Arrange
        EditionRequestDTO dto = EditionRequestDTO.builder()
                .publicationTypeId("BOOK")
                .publishingYear(2000)
                .weight(null)
                .build();

        // SUT
        EditionRequestDTOMapper mapper = new EditionRequestDTOMapper();

        // Act
        Weight result = mapper.toWeight(dto);

        // Assert
        assertNull(result);

    }

    @Test
    void toWeightReturnsWeight() {

        // Arrange
        EditionRequestDTO dto = EditionRequestDTO.builder()
                .publicationTypeId("BOOK")
                .publishingYear(2000)
                .weight(new WeightDTO(500.0, "g"))
                .build();

        // SUT
        EditionRequestDTOMapper mapper = new EditionRequestDTOMapper();

        // Act
        Weight result = mapper.toWeight(dto);

        // Assert
        assertNotNull(result);
        assertAll(
                () -> assertEquals(500.0, result.getValue()),
                () -> assertEquals(Weight.WeightUnit.GRAMS, result.getWeightUnit())
        );

    }


    @Test
    void toNumberOfPagesNullReturnsNull() {

        // Arrange
        EditionRequestDTO dto = EditionRequestDTO.builder()
                .publicationTypeId("BOOK")
                .publishingYear(2000)
                .numberOfPages(null)
                .build();

        // SUT
        EditionRequestDTOMapper mapper = new EditionRequestDTOMapper();

        // Act
        NumberOfPages result = mapper.toNumberOfPages(dto);

        // Assert
        assertNull(result);

    }

    @Test
    void toNumberOfPagesReturnsNumberOfPages() {

        // Arrange
        EditionRequestDTO dto = EditionRequestDTO.builder()
                .publicationTypeId("BOOK")
                .publishingYear(2000)
                .numberOfPages(300)
                .build();

        // SUT
        EditionRequestDTOMapper mapper = new EditionRequestDTOMapper();

        // Act
        NumberOfPages result = mapper.toNumberOfPages(dto);

        // Assert
        assertNotNull(result);
        assertEquals(300, result.getNumberOfPages());

    }


    @Test
    void toEditionNumberNullReturnsNull() {

        // Arrange
        EditionRequestDTO dto = EditionRequestDTO.builder()
                .publicationTypeId("BOOK")
                .publishingYear(2000)
                .editionNumber(null)
                .build();

        // SUT
        EditionRequestDTOMapper mapper = new EditionRequestDTOMapper();

        // Act
        EditionNumber result = mapper.toEditionNumber(dto);

        // Assert
        assertNull(result);

    }

    @Test
    void toEditionNumberReturnsEditionNumber() {

        // Arrange
        EditionRequestDTO dto = EditionRequestDTO.builder()
                .publicationTypeId("BOOK")
                .publishingYear(2000)
                .editionNumber(1)
                .build();

        // SUT
        EditionRequestDTOMapper mapper = new EditionRequestDTOMapper();

        // Act
        EditionNumber result = mapper.toEditionNumber(dto);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getValue());

    }


    @Test
    void toBindingNullReturnsNull() {

        // Arrange
        EditionRequestDTO dto = EditionRequestDTO.builder()
                .publicationTypeId("BOOK")
                .publishingYear(2000)
                .binding(null)
                .build();

        // SUT
        EditionRequestDTOMapper mapper = new EditionRequestDTOMapper();

        // Act
        Binding result = mapper.toBinding(dto);

        // Assert
        assertNull(result);

    }

    @Test
    void toBindingReturnsBinding() {

        // Arrange
        EditionRequestDTO dto = EditionRequestDTO.builder()
                .publicationTypeId("BOOK")
                .publishingYear(2000)
                .binding("HARDCOVER")
                .build();

        // SUT
        EditionRequestDTOMapper mapper = new EditionRequestDTOMapper();

        // Act
        Binding result = mapper.toBinding(dto);

        // Assert
        assertEquals(Binding.HARDCOVER, result);

    }

    @Test
    void toBindingLowercaseReturnsBinding() {

        // Arrange
        EditionRequestDTO dto = EditionRequestDTO.builder()
                .publicationTypeId("BOOK")
                .publishingYear(2000)
                .binding("hardcover")
                .build();

        // SUT
        EditionRequestDTOMapper mapper = new EditionRequestDTOMapper();

        // Act
        Binding result = mapper.toBinding(dto);

        // Assert
        assertEquals(Binding.HARDCOVER, result);

    }

}