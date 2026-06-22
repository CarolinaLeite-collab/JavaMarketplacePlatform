package MITELOVERS.mapper;

import MITELOVERS.applicationservices.ItemService.ItemRelated;
import MITELOVERS.controllers.rest.AuctionRestController;
import MITELOVERS.controllers.rest.DirectSaleRestController;
import MITELOVERS.controllers.rest.ItemRestController;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.valueobject.SaleStatus;
import MITELOVERS.dto.response.ItemResponseDTO;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Mapper responsible for assembling an {@link Item} domain object
 * into an {@link ItemResponseDTO}.
 *
 * <p>
 * Receives all related domain objects pre-resolved by the service layer.
 * Does not perform any data access.
 * </p>
 */

@Component
public class ItemResponseDTOMapper {

    public ItemResponseDTO toModel(Item item, ItemRelated related) {

        String pictureUrl = item.getPicture() != null
                ? item.getPicture().toString()
                : null;

        ItemResponseDTO dto = new ItemResponseDTO(
                item.identity().toString(),
                item.getCondition().toString(),
                item.getDescription().toString(),
                item.getSaleStatus().toString(),
                pictureUrl,
                related.edition().getEditionId().toString(),
                related.edition().getIdentifier().toString(),
                related.edition().getEditionLanguage().toString(),
                related.edition().getPublishingYear().getValue(),
                related.edition().getPublicationTypeId().toString(),
                related.publication().getTitle().toString(),
                related.author().getName().toString(),
                related.publication().getReleaseYear().getValue(),
                related.genre().getGenre().toString(),
                related.publisher().getPublishingCompanyName()
        );

        dto.add(linkTo(methodOn(ItemRestController.class)
                .getItemById(item.identity().toString()))
                .withSelfRel());

        if (item.getSaleStatus() == SaleStatus.NotOnSale) {
            dto.add(linkTo(methodOn(DirectSaleRestController.class).createDirectSale(null, null)).withRel("create-direct-sale"));
            dto.add(linkTo(AuctionRestController.class).withRel("create-auction"));
        }

        return dto;
    }
}
