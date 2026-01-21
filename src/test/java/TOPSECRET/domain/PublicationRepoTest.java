package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PublicationRepoTest {
    //creates repo
    @Test
    public void sucessfulyReturnsPublicationAddedToRepo() {
        // arrange
        PublicationRepo repo = new PublicationRepo();
        Publication p = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new Publisher("Penguin"))
                .build();
        // arrange
        assertEquals(repo.add(p), p);
    }

    @Test
    public void sucessfulyReturnsPublicationAddedToRepoNotEmptyRepo() {
        // arrange
        PublicationRepo repo = new PublicationRepo();
        Publication p = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new Publisher("Penguin"))
                .build();
        Publication p1 = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9789723701241"))
                .year(Year.of(2013))
                .title(new Title("Photomaton & Vox "))
                .author(new Author("Herberto Helder"))
                .publisher(new Publisher("Assírio & Alvim"))
                .build();
        // arrange
        repo.add(p);
        assertEquals(repo.add(p1), p1);
    }
    @Test
    void doesntCreateInvalidPublication_emptyTitle() {
        assertThrows(IllegalArgumentException.class, () ->
                Publication.builder()
                        .type(new PublicationType("BOOK"))
                        .identifier(new ISBN("9780691181950"))
                        .year(Year.of(2019))
                        .title(new Title(""))   // invalid
                        .author(new Author("Seneca"))
                        .publisher(new Publisher("Penguin"))
                        .build()
        );
    }

    @Test
    void add_throws_whenNull() {
        PublicationRepo repo = new PublicationRepo();
        assertThrows(IllegalArgumentException.class, () -> repo.add(null));
    }

    @Test
    void doesntAddDuplicatePublication() {
        PublicationRepo repo = new PublicationRepo();

        Publication p = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to keep your cool"))
                .author(new Author("Seneca"))
                .publisher(new Publisher("Penguin"))
                .build();

        // first add succeeds
        assertSame(p, repo.add(p));

        // second add throws
        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> repo.add(p));

        assertEquals("Publication already exists in the repository", ex.getMessage());
    }

    @Test
    void returnsAllPublicationsWhenExistentListIsEmpty() {
    // Ensures that getDifferentOf returns all publications when no publications are provided

        // Arrange
        PublicationRepo repo = new PublicationRepo();

        Publication p1 = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new Publisher("Penguin"))
                .build();

        Publication p2 = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9789723701241"))
                .year(Year.of(2013))
                .title(new Title("Photomaton & Vox"))
                .author(new Author("Herberto Helder"))
                .publisher(new Publisher("Assírio & Alvim"))
                .build();

        repo.add(p1);
        repo.add(p2);

        // Act
        List<Publication> result = repo.getDifferentOf(List.of());

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(p1));
        assertTrue(result.contains(p2));
    }

    @Test
    void returnsOnlyPublicationsNotInExistentList() {
        // Ensures that getDifferentOf excludes publications already in the existent list

        // Arrange
        PublicationRepo repo = new PublicationRepo();

        Publication p1 = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new Publisher("Penguin"))
                .build();

        Publication p2 = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9789723701241"))
                .year(Year.of(2013))
                .title(new Title("Photomaton & Vox"))
                .author(new Author("Herberto Helder"))
                .publisher(new Publisher("Assírio & Alvim"))
                .build();

        repo.add(p1);
        repo.add(p2);

        // Act
        List<Publication> result = repo.getDifferentOf(List.of(p1));

        // Assert
        assertEquals(1, result.size());
        assertFalse(result.contains(p1));
        assertTrue(result.contains(p2));
    }

    @Test
    void returnsEmptyListWhenAllPublicationsExist() {
        // Ensures that getDifferentOf returns an empty list when all publications are already present

        // Arrange
        PublicationRepo repo = new PublicationRepo();

        Publication p = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new Publisher("Penguin"))
                .build();
        Publication p2 = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new Publisher("Penguin"))
                .build();
        Publication p3 = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new Publisher("Penguin"))
                .build();


        repo.add(p);
        repo.add(p2);
        repo.add(p3);

        // Act
        List<Publication> result = repo.getDifferentOf(List.of(p));

        // Assert
        assertTrue(result.isEmpty());
    }

}