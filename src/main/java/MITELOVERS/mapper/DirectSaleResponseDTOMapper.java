package MITELOVERS.mapper;

import MITELOVERS.controllers.rest.DirectSaleRestController;
import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.dto.response.DirectSaleResponseDTO;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DirectSaleResponseDTOMapper {

    public DirectSaleResponseDTO toResponseDTO(DirectSale directSale) {

        DirectSaleResponseDTO dto = new DirectSaleResponseDTO(
                directSale.identity().toString(),
                directSale.getItemsId().stream()
                        .map(ItemId::toString)
                        .toList(),
                directSale.getPrice().getValue(),
                directSale.getPrice().getCurrency().name(),
                directSale.getTimeLimit() != null ? directSale.getTimeLimit().getSeconds() : null,
                directSale.getCreationDate()
        );

        dto.add(
                linkTo(
                        methodOn(DirectSaleRestController.class)
                                .createDirectSale(null)
                ).withRel("createDirectSale")
        );

        dto.add(
                linkTo(
                        methodOn(DirectSaleRestController.class)
                                .getDirectSaleById(directSale.identity().toString())
                ).withSelfRel()
        );

        dto.add(
                linkTo(
                        methodOn(DirectSaleRestController.class)
                                .getAllDirectSales()
                ).withRel("allDirectSales")
        );

        return dto;
    }

}
