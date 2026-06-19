package MITELOVERS.mapper;

import MITELOVERS.domain.valueobject.LibrarySort;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

class LibrarySortRequestMapperTest {

    @Test
    void toDomainShouldConvertSortValue() {
        LibrarySortRequestMapper mapper =
                new LibrarySortRequestMapper();

        LibrarySort result = mapper.toDomain("author");

        assertSame(LibrarySort.AUTHOR, result);
    }

    @Test
    void toDomainShouldReturnNoneForMissingValue() {
        LibrarySortRequestMapper mapper =
                new LibrarySortRequestMapper();

        LibrarySort result = mapper.toDomain(null);

        assertSame(LibrarySort.NONE, result);
    }
}