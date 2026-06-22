package MITELOVERS.mapper;


import MITELOVERS.domain.valueobject.LibrarySort;
import org.springframework.stereotype.Component;

/**
 * Maps the library sorting parameter received through the API
 * to a {@link LibrarySort} domain value.
 */

@Component
public class LibrarySortRequestMapper {

    public LibrarySort toDomain(String sort) {
        if (sort == null || sort.isBlank()) {
            return LibrarySort.NONE;
        }

        return switch (sort.toLowerCase()) {
            case "title" -> LibrarySort.TITLE;
            case "author", "authorname" -> LibrarySort.AUTHOR;
            case "publicationtype", "type", "publication_type" ->
                    LibrarySort.PUBLICATION_TYPE;
            case "identifier", "isbn", "issn", "isbn-issn" ->
                    LibrarySort.IDENTIFIER;
            default -> LibrarySort.NONE;
        };
    }
}
