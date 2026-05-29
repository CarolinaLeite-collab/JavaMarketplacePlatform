package MITELOVERS.mapper;

import MITELOVERS.controllers.rest.ItemRestController;
import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.dto.ItemResponseDTO;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Assembles Item domain objects into ItemResponseDTO instances.
 * Enriches the DTO with data from Edition, Publication, Author and Genre
 * so the client receives full context in a single response.
 */

@Component
public class ItemResponseDTOMapper {

    public ItemResponseDTO toResponseDTO(Item item,
                                         Edition edition,
                                         Publication publication,
                                         Author author,
                                         Genre genre) {

        ItemResponseDTO dto = new ItemResponseDTO(
                // Item fields
                item.identity().toString(),
                item.getCondition().toString(),
                item.getDescription().toString(),
                item.getSaleStatus().toString(),
                // Edition fields
                edition.getEditionId().toString(),
                edition.getIdentifier().toString(),
                edition.getEditionLanguage().toString(),
                edition.getPublishingYear().getValue(),
                edition.getPublicationTypeId().toString(),
                // Publication fields
                publication.getTitle().toString(),
                author.getName().toString(),
                publication.getReleaseYear().getValue(),
                genre.getGenre()
        );

        dto.add(
                linkTo(
                        methodOn(ItemRestController.class)
                                .getItemById(item.identity().toString())
                ).withSelfRel()
        );

        return dto;
    }
}
