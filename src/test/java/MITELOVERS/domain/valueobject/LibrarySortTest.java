package MITELOVERS.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertSame;

class LibrarySortTest {


        @Test
        void fromShouldConvertSupportedValues() {
            assertAll(
                    () -> assertSame(LibrarySort.TITLE,
                            LibrarySort.from("title")),
                    () -> assertSame(LibrarySort.AUTHOR,
                            LibrarySort.from("author")),
                    () -> assertSame(LibrarySort.PUBLICATION_TYPE,
                            LibrarySort.from("publicationtype")),
                    () -> assertSame(LibrarySort.IDENTIFIER,
                            LibrarySort.from("identifier"))
            );
        }

        @Test
        void fromShouldConvertAuthorAliases() {
            assertAll(
                    () -> assertSame(LibrarySort.AUTHOR,
                            LibrarySort.from("author")),
                    () -> assertSame(LibrarySort.AUTHOR,
                            LibrarySort.from("authorname"))
            );
        }

        @Test
        void fromShouldConvertPublicationTypeAliases() {
            assertAll(
                    () -> assertSame(LibrarySort.PUBLICATION_TYPE,
                            LibrarySort.from("publicationtype")),
                    () -> assertSame(LibrarySort.PUBLICATION_TYPE,
                            LibrarySort.from("publication_type")),
                    () -> assertSame(LibrarySort.PUBLICATION_TYPE,
                            LibrarySort.from("type"))
            );
        }

        @Test
        void fromShouldConvertIdentifierAliases() {
            assertAll(
                    () -> assertSame(LibrarySort.IDENTIFIER,
                            LibrarySort.from("identifier")),
                    () -> assertSame(LibrarySort.IDENTIFIER,
                            LibrarySort.from("isbn")),
                    () -> assertSame(LibrarySort.IDENTIFIER,
                            LibrarySort.from("issn")),
                    () -> assertSame(LibrarySort.IDENTIFIER,
                            LibrarySort.from("isbn-issn"))
            );
        }

        @Test
        void fromShouldBeCaseInsensitive() {
            assertAll(
                    () -> assertSame(LibrarySort.TITLE,
                            LibrarySort.from("TITLE")),
                    () -> assertSame(LibrarySort.AUTHOR,
                            LibrarySort.from("AuThOr")),
                    () -> assertSame(LibrarySort.PUBLICATION_TYPE,
                            LibrarySort.from("TYPE")),
                    () -> assertSame(LibrarySort.IDENTIFIER,
                            LibrarySort.from("ISBN"))
            );
        }

        @Test
        void fromShouldReturnNoneForNullOrBlankValue() {
            assertAll(
                    () -> assertSame(LibrarySort.NONE,
                            LibrarySort.from(null)),
                    () -> assertSame(LibrarySort.NONE,
                            LibrarySort.from("")),
                    () -> assertSame(LibrarySort.NONE,
                            LibrarySort.from("   "))
            );
        }

        @Test
        void fromShouldReturnNoneForUnsupportedValue() {
            assertSame(LibrarySort.NONE, LibrarySort.from("unknown"));
        }
    }