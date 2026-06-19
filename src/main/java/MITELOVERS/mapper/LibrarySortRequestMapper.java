package MITELOVERS.mapper;


import MITELOVERS.domain.valueobject.LibrarySort;
import org.springframework.stereotype.Component;

/**
 * Maps the library sorting parameter received through the API
 * to a {@link LibrarySort} domain value.
 *
 * <p>
 * Missing or unsupported values are converted according to the rules
 * defined by {@link LibrarySort#from(String)}.
 * </p>
 */

@Component
public class LibrarySortRequestMapper {

    public LibrarySort toDomain(String sort) {

        return LibrarySort.from(sort);
    }
}
