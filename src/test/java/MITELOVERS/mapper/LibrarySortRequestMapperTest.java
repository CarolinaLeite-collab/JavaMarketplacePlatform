package MITELOVERS.mapper;

import MITELOVERS.domain.valueobject.LibrarySort;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertSame;

class LibrarySortRequestMapperTest {

    @Test
    void toDomainShouldConvertSupportedValues() {

        LibrarySortRequestMapper mapper = new LibrarySortRequestMapper();

        assertAll(
                () -> assertSame(LibrarySort.TITLE,
                        mapper.toDomain("title")),
                () -> assertSame(LibrarySort.AUTHOR,
                        mapper.toDomain("author")),
                () -> assertSame(LibrarySort.PUBLICATION_TYPE,
                        mapper.toDomain("publicationtype")),
                () -> assertSame(LibrarySort.IDENTIFIER,
                        mapper.toDomain("identifier"))
        );
    }

    @Test
    void toDomainShouldConvertAuthorAliases() {

        LibrarySortRequestMapper mapper = new LibrarySortRequestMapper();
        assertAll(
                () -> assertSame(LibrarySort.AUTHOR,
                        mapper.toDomain("author")),
                () -> assertSame(LibrarySort.AUTHOR,
                        mapper.toDomain("authorname"))
        );
    }

    @Test
    void toDomainShouldConvertPublicationTypeAliases() {

        LibrarySortRequestMapper mapper = new LibrarySortRequestMapper();

        assertAll(
                () -> assertSame(LibrarySort.PUBLICATION_TYPE,
                        mapper.toDomain("publicationtype")),
                () -> assertSame(LibrarySort.PUBLICATION_TYPE,
                        mapper.toDomain("publication_type")),
                () -> assertSame(LibrarySort.PUBLICATION_TYPE,
                        mapper.toDomain("type"))
        );
    }

    @Test
    void toDomainShouldConvertIdentifierAliases() {
        LibrarySortRequestMapper mapper = new LibrarySortRequestMapper();

        assertAll(
                () -> assertSame(LibrarySort.IDENTIFIER,
                        mapper.toDomain("identifier")),
                () -> assertSame(LibrarySort.IDENTIFIER,
                        mapper.toDomain("isbn")),
                () -> assertSame(LibrarySort.IDENTIFIER,
                        mapper.toDomain("issn")),
                () -> assertSame(LibrarySort.IDENTIFIER,
                        mapper.toDomain("isbn-issn"))
        );
    }

    @Test
    void toDomainShouldBeCaseInsensitive() {

        LibrarySortRequestMapper mapper = new LibrarySortRequestMapper();

        assertAll(
                () -> assertSame(LibrarySort.TITLE,
                        mapper.toDomain("title")),
                () -> assertSame(LibrarySort.AUTHOR,
                        mapper.toDomain("AuThOr")),
                () -> assertSame(LibrarySort.PUBLICATION_TYPE,
                        mapper.toDomain("TYPE")),
                () -> assertSame(LibrarySort.IDENTIFIER,
                        mapper.toDomain("ISBN"))
        );
    }

    @Test
    void toDomainShouldReturnNoneForNullOrBlankValue() {

        LibrarySortRequestMapper mapper = new LibrarySortRequestMapper();

        assertAll(
                () -> assertSame(LibrarySort.NONE,
                        mapper.toDomain(null)),
                () -> assertSame(LibrarySort.NONE,
                        mapper.toDomain("")),
                () -> assertSame(LibrarySort.NONE,
                        mapper.toDomain("   "))
        );
    }

    @Test
    void toDomainShouldReturnNoneForUnsupportedValue() {

        LibrarySortRequestMapper mapper = new LibrarySortRequestMapper();

        assertSame(LibrarySort.NONE, mapper.toDomain("unknown"));
    }
}
