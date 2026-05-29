package MITELOVERS.mapper;

import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.DimensionDTO;
import MITELOVERS.dto.EditionRequestDTO;
import MITELOVERS.dto.WeightDTO;
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
        EditionRequestDTOMapper requestDTOMapper = new EditionRequestDTOMapper();

        // Act
        PublicationTypeId result = requestDTOMapper.toPublicationTypeId(dto);

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
        EditionRequestDTOMapper requestDTOMapper = new EditionRequestDTOMapper();

        // Act
        Identifier result = requestDTOMapper.toIdentifier(dto);

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
        EditionRequestDTOMapper requestDTOMapper = new EditionRequestDTOMapper();

        // Act
        Identifier result = requestDTOMapper.toIdentifier(dto);

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
        EditionRequestDTOMapper requestDTOMapper = new EditionRequestDTOMapper();

        // Act
        Identifier result = requestDTOMapper.toIdentifier(dto);

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
        EditionRequestDTOMapper requestDTOMapper = new EditionRequestDTOMapper();

        // Act
        Identifier result = requestDTOMapper.toIdentifier(dto);

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
        EditionRequestDTOMapper requestDTOMapper = new EditionRequestDTOMapper();

        // Act
        Identifier result = requestDTOMapper.toIdentifier(dto);

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
        EditionRequestDTOMapper requestDTOMapper = new EditionRequestDTOMapper();

        // Act
        Identifier result = requestDTOMapper.toIdentifier(dto);

        // Assert
        assertInstanceOf(NoIdentifier.class, result);

    }


    @Test
    void toDimensionNullReturnsNull() {

        // Arrange
        EditionRequestDTO dto = EditionRequestDTO.builder()
                .publicationTypeId("BOOK")
                .publishingYear(2000)
                .dimension(null)
                .build();

        // SUT
        EditionRequestDTOMapper requestDTOMapper = new EditionRequestDTOMapper();

        // Act
        Dimension result = requestDTOMapper.toDimension(dto);

        // Assert
        assertNull(result);

    }

    @Test
    void toDimensionReturnsCorrectVO() {

        // Arrange
        EditionRequestDTO dto = EditionRequestDTO.builder()
                .publicationTypeId("BOOK")
                .publishingYear(2000)
                .identifier("9780747532743")
                .dimension(new DimensionDTO(20.0,40.0,5.0,"cm"))
                .build();

        // SUT
        EditionRequestDTOMapper requestDTOMapper = new EditionRequestDTOMapper();

        // Act
        Dimension result = requestDTOMapper.toDimension(dto);

        //Assert
        assertAll(
                () -> assertEquals(20.0, result.getWidth()),
                () -> assertEquals(40.0, result.getHeight()),
                () -> assertEquals(5.0, result.getThickness()),
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
        EditionRequestDTOMapper requestDTOMapper = new EditionRequestDTOMapper();

        // Act
        Weight result = requestDTOMapper.toWeight(dto);

        // Assert
        assertNull(result);

    }

    @Test
    void toWeightReturnsWeight() {

        // Arrange
        EditionRequestDTO dto = EditionRequestDTO.builder()
                .publicationTypeId("BOOK")
                .publishingYear(2000)
                .weight(new WeightDTO(5.0,"kg"))
                .build();

        // SUT
        EditionRequestDTOMapper requestDTOMapper = new EditionRequestDTOMapper();

        // Act
        Weight result = requestDTOMapper.toWeight(dto);

        // Assert
        assertEquals(5.0, result.getValue());
        assertEquals(Weight.WeightUnit.KILOGRAMS, result.getWeightUnit());

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
        EditionRequestDTOMapper requestDTOMapper = new EditionRequestDTOMapper();

        // Act
        NumberOfPages result = requestDTOMapper.toNumberOfPages(dto);

        // Assert
        assertEquals(null, result);

    }

    @Test
    void toNumberOfPagesReturnsNumberOfPages() {

        // Arrange
        EditionRequestDTO dto = EditionRequestDTO.builder()
                .publicationTypeId("BOOK")
                .publishingYear(2000)
                .numberOfPages(20)
                .build();

        // SUT
        EditionRequestDTOMapper requestDTOMapper = new EditionRequestDTOMapper();

        // Act
        NumberOfPages result = requestDTOMapper.toNumberOfPages(dto);

        // Assert
        assertEquals(20, result.getNumberOfPages());

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
        EditionRequestDTOMapper requestDTOMapper = new EditionRequestDTOMapper();

        // Act
        EditionNumber result = requestDTOMapper.toEditionNumber(dto);

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
        EditionRequestDTOMapper requestDTOMapper = new EditionRequestDTOMapper();

        // Act
        EditionNumber result = requestDTOMapper.toEditionNumber(dto);

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
        EditionRequestDTOMapper requestDTOMapper = new EditionRequestDTOMapper();

        // Act
        Binding result = requestDTOMapper.toBinding(dto);

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
        EditionRequestDTOMapper requestDTOMapper = new EditionRequestDTOMapper();

        // Act
        Binding result = requestDTOMapper.toBinding(dto);

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
        EditionRequestDTOMapper requestDTOMapper = new EditionRequestDTOMapper();

        // Act
        Binding result = requestDTOMapper.toBinding(dto);

        // Assert
        assertEquals(Binding.HARDCOVER, result);

    }

}