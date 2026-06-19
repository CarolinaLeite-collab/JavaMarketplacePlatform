package MITELOVERS.mapper;


import MITELOVERS.domain.valueobject.LibrarySort;
import org.springframework.stereotype.Component;

@Component
public class LibrarySortRequestMapper {

    public LibrarySort toDomain(String sort) {

        return LibrarySort.from(sort);
    }
}
