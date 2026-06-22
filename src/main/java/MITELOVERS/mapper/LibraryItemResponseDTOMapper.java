package MITELOVERS.mapper;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.dto.response.LibraryItemResponseDTO;
import org.springframework.stereotype.Component;


/**
 * Maps library domain objects to {@link LibraryItemResponseDTO} instances.
 *
 * <p>
 * Resolves a library item's {@link Item} through its {@link Edition},
 * {@link Publication}, {@link Author} and {@link PublicationType} into the
 * single response shape used by both the library list and item detail
 * REST endpoints.
 * </p>
 *
 */

@Component
public class LibraryItemResponseDTOMapper {

    public LibraryItemResponseDTO toDTO(
            Item item,
            Publication publication,
            Edition edition,
            Author author,
            PublicationType publicationType
            ) {

        return new LibraryItemResponseDTO(
                item.identity().toString(),
                publication.getTitle().toString(),
                author.getName().toString(),
                publicationType.toString(),
                edition.getIdentifier().toString(),
                getPictureUrl(item)
        );
    }

    private String getPictureUrl(Item item) {
        return item.getPicture() != null ? item.getPicture().toString() : null;
    }
}
