package MITELOVERS.mapper;

import MITELOVERS.controllers.rest.LibraryRestController;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.dto.LibraryItemSummaryDTO;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class LibraryItemSummaryMapper {

    public LibraryItemSummaryDTO toDTO(Item item, Publication publication) {

        String pictureUrl = null;

        if (item.getPicture() != null) {
            pictureUrl = item.getPicture().toString();
        }

        LibraryItemSummaryDTO dto = new LibraryItemSummaryDTO(
                item.identity().toString(),
                publication.getTitle().toString(),
                pictureUrl
        );

        return dto;
    }
}
