package TOPSECRET.mapper;

import TOPSECRET.domain.author.Author;
import TOPSECRET.domain.edition.Edition;
import TOPSECRET.domain.publication.Publication;
import TOPSECRET.domain.publicationtype.PublicationType;
import TOPSECRET.domain.valueobject.ISBN;
import TOPSECRET.domain.valueobject.ISSN;
import TOPSECRET.dto.ItemDetailsDTO;

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
 * edition, supporting both {@link TOPSECRET.domain.valueobject.ISBN}
 * and {@link TOPSECRET.domain.valueobject.ISSN}. If no valid identifier
 * is available, a default value is returned.
 * </p>
 */

public class ItemDetailsMapper {

    public static ItemDetailsDTO toDTO(
            Edition edition,
            Publication publication,
            PublicationType publicationType,
            Author author) {

        String identifier = "no identifier";

        if (edition.getIdentifier() instanceof ISBN || edition.getIdentifier() instanceof ISSN) {

            identifier = edition.getIdentifier().toString();

        }

        return new ItemDetailsDTO(
                publication.getTitle().toString(),
                author.getName(),
                publicationType.toString(),
                identifier
        );
    }
}
