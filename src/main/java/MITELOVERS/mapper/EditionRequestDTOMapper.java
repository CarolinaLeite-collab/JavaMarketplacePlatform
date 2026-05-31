package MITELOVERS.mapper;

import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.request.EditionRequestDTO;
import org.springframework.stereotype.Component;

import java.time.Year;

/**
 * Assembles EditionRequestDTO instances into Edition domain objects.
 */

@Component
public class EditionRequestDTOMapper {

    public PublicationTypeId toPublicationTypeId(EditionRequestDTO dto) {

        return new PublicationTypeId(dto.getPublicationTypeId());

    }

    public Identifier toIdentifier(EditionRequestDTO dto) {
        PublicationTypeId typeId = toPublicationTypeId(dto);
        Year year = Year.of(dto.getPublishingYear());

        if (dto.getIdentifier() == null || dto.getIdentifier().isBlank()) {
            return new NoIdentifier();
        }

        if (typeId.isBook() && year.isAfter(Year.of(1970))) {
            return new ISBN(dto.getIdentifier());
        }

        if (typeId.isMagazine() && year.isAfter(Year.of(1976))) {
            return new ISSN(dto.getIdentifier());
        }

        return new NoIdentifier();

    }

    public Dimension toDimension(EditionRequestDTO dto) {
        if (dto.getDimension() == null) return null;

        return new Dimension(
                dto.getDimension().getWidth(),
                dto.getDimension().getHeight(),
                dto.getDimension().getThickness(),
                DimensionUnit.fromString(dto.getDimension().getUnit())
        );

    }

    public Weight toWeight(EditionRequestDTO dto) {
        if (dto.getWeight() == null) return null;

        return new Weight(
                dto.getWeight().getValue(),
                Weight.WeightUnit.fromAbbreviation(dto.getWeight().getUnit())
        );

    }

    public NumberOfPages toNumberOfPages(EditionRequestDTO dto) {
        if (dto.getNumberOfPages() == null) return null;

        return new NumberOfPages(dto.getNumberOfPages());

    }

    public EditionNumber toEditionNumber(EditionRequestDTO dto) {
        if (dto.getEditionNumber() == null) return null;

        return new EditionNumber(dto.getEditionNumber());

    }

    public Binding toBinding(EditionRequestDTO dto) {
        if (dto.getBinding() == null) return null;

        return Binding.valueOf(dto.getBinding().toUpperCase().replace(" ", "_"));

    }

}
