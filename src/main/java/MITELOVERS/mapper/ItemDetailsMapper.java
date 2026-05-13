package MITELOVERS.mapper;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.dto.ItemDetailsDTO;

/**
 * Mapper responsible for converting domain objects into {@link ItemDetailsDTO}.
 *
 * <p>
 * This class transforms core domain entities such as {@link Edition},
 * {@link Publication}, {@link PublicationType}, and {@link Author}
 * into a simplified DTO used for presentation or API responses.
 * </p>
 *
 * <p>
 * It also handles extraction of a human-readable identifier from the
 * edition, supporting both {@link MITELOVERS.domain.valueobject.ISBN}
 * and {@link MITELOVERS.domain.valueobject.ISSN}. If no valid identifier
 * is available, a default value is returned.
 * </p>
 */

public class ItemDetailsMapper {

    public static ItemDetailsDTO toDTO(
            Edition edition,
            Publication publication,
            PublicationType publicationType,
            Author author) {

        return new ItemDetailsDTO(
                publication.getTitle().toString(),
                author.getName().toString(),
                publicationType.toString(),
                edition.getIdentifier().toString()
        );
    }
}
