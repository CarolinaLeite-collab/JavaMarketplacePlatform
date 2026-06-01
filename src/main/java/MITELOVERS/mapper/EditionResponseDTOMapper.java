package MITELOVERS.mapper;

import MITELOVERS.domain.edition.Edition;
import MITELOVERS.dto.DimensionDTO;
import MITELOVERS.dto.response.EditionResponseDTO;
import MITELOVERS.dto.WeightDTO;
import org.springframework.stereotype.Component;

/**
 * Assembles Edition domain objects into EditionResponseDTO instances.
 */

@Component
public class EditionResponseDTOMapper {

    public EditionResponseDTO toModel(Edition edition) {

        EditionResponseDTO dto = EditionResponseDTO.builder()

                .editionId(edition.getEditionId().toString())
                .publicationTypeId(edition.getPublicationTypeId().toString())
                .identifier(edition.getIdentifier().toString())
                .publicationId(edition.getPublicationId().toString())
                .publishingCompanyId(edition.getPublishingCompanyId().toString())
                .publishingYear(edition.getPublishingYear().getValue())
                .language(edition.getEditionLanguage().toString())

                // optional fields
                .dimension(edition.getDimension() != null ? new DimensionDTO(
                        edition.getDimension().getWidth(),
                        edition.getDimension().getHeight(),
                        edition.getDimension().getThickness(),
                        edition.getDimension().getUnit().toString()
                ) : null)

                .weight(edition.getWeight() != null ? new WeightDTO(
                        edition.getWeight().getValue(),
                        edition.getWeight().getWeightUnit().toString()
                ) : null)

                .numberOfPages(edition.getNumberOfPages() != null ? edition.getNumberOfPages().getNumberOfPages() : null)
                .editionNumber(edition.getEditionNumber() != null ? edition.getEditionNumber().getValue() : null)
                .binding(edition.getBinding() != null ? edition.getBinding().toString() : null)
                .build();

        return dto;

    }

}

