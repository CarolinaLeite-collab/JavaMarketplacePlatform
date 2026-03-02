package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ItemRepoTest {

    @Test
    void existsReturnsFalseWhenRepoIsEmpty() {
        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new PublishingCompany("Penguin"))
                .build();
        ItemRepo repo = new ItemRepo();

        assertFalse(repo.exists(pub));
    }

    @Test
    void existsReturnsTrueWhenItemWithPublicationExists() {
        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new PublishingCompany("Penguin"))
                .build();
        Condition condition = Condition.GOOD;

        ItemRepo repo = new ItemRepo();
        repo.createItem(pub, condition);

        assertTrue(repo.exists(pub));
    }

    @Test
    void existsReturnsFalseForNullPublication() {
        ItemRepo repo = new ItemRepo();

        assertFalse(repo.exists(null));
    }

    @Test
    void createItemCreatesAndStoresNewItem() {
        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new PublishingCompany("Penguin"))
                .build();
        Condition condition = Condition.GOOD;

        ItemRepo repo = new ItemRepo();
        Item item = repo.createItem(pub, condition);

        assertNotNull(item);
        assertEquals(condition, item.getCondition());
        assertTrue(repo.exists(pub));
    }

    @Test
    void createItemThrowsWhenPublicationAlreadyExists() {
        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new PublishingCompany("Penguin"))
                .build();
        Condition condition = Condition.GOOD;

        ItemRepo repo = new ItemRepo();
        repo.createItem(pub, condition);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> repo.createItem(pub, condition)
        );

        assertEquals("Item for this publication already exists!", ex.getMessage());
    }

    @Test
    void getAllReturnsUnmodifiableList() {
        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new PublishingCompany("Penguin"))
                .build();
        Condition condition = Condition.GOOD;

        ItemRepo repo = new ItemRepo();
        repo.createItem(pub, condition);

        List<Item> list = repo.getAll();

        assertEquals(1, list.size());
        assertThrows(UnsupportedOperationException.class, () -> list.add(null));
    }

    @Test
    void getAllReflectsNewItems() {
        Publication pub1 = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new PublishingCompany("Penguin"))
                .build();
        Publication pub2 = Publication.builder()
                .type(new PublicationType("MAGAZINE"))
                .identifier(new ISSN("1234-5678"))
                .year(Year.of(2022))
                .title(new Title("Science Weekly"))
                .publisher(new PublishingCompany("Nature"))
                .build();
        Condition condition = Condition.GOOD;

        ItemRepo repo = new ItemRepo();
        repo.createItem(pub1, condition);

        List<Item> list1 = repo.getAll();
        assertEquals(1, list1.size());

        repo.createItem(pub2, condition);

        List<Item> list2 = repo.getAll();
        assertEquals(2, list2.size());
    }
}
