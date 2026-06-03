package MITELOVERS.mapper;

import MITELOVERS.domain.publishingcompany.PublishingCompany;
import MITELOVERS.dto.response.PublishingCompanyResponseDTO;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class PublishingCompanyResponseDTOMapper implements RepresentationModelAssembler<PublishingCompany, PublishingCompanyResponseDTO> {

    public PublishingCompanyResponseDTO toModel(PublishingCompany publishingCompany) {

        PublishingCompanyResponseDTO dto = new PublishingCompanyResponseDTO(
                publishingCompany.identity().toString(),
                publishingCompany.getPublishingCompanyName()
                );

        return dto;

    }

}
