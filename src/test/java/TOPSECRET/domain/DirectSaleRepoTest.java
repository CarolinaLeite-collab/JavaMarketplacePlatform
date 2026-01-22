package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DirectSaleRepoTest {

    private User _user;
    private DirectSaleRepo _directSaleRepo;
    private Publication _publication;
    private Item _item;
    private Author _author;
    private DirectSale _directSale1;
    private DirectSale _directSale2;


    @BeforeEach
    void setUp() {

        _user = new User(
                new Name("Zé Isep"),
                new Email("ze@isep.pt")
        );

        _author = new Author("Seneca");

        _publication = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new Publisher("Penguin"))
                .build();

        _item = new Item(_publication, Condition.GOOD);

        _directSale1 = new DirectSale(_item, new Price(20.0,Currency.EUR), null);
        _directSale2 = new DirectSale(_item, new Price(25.0,Currency.EUR), null);

        _directSaleRepo = new DirectSaleRepo();

    }

    @Test
    void testADirectSaleRepoConstructor() {

        new DirectSaleRepo();

    }

    @Test
    void testGetDirectSaleItemsByAuthorNoDirectSalesShouldReturnEmptyList() {

        //act
        List<Item> emptyList = _directSaleRepo.getDirectSaleItemsByAuthor(_author);

        //assert
        assertNotNull(emptyList);
        assertTrue(emptyList.isEmpty());

    }

    @Test
    void testGetDirectSaleItemsByAuthorWithDirectSalesShouldReturnNonEmptyList() {

        //arrange
        _directSaleRepo.createDirectSale(_item, new Price(20.0,Currency.EUR), null);
        _directSaleRepo.createDirectSale(_item, new Price(25.0,Currency.EUR), null);

        //act
        List<Item> list = _directSaleRepo.getDirectSaleItemsByAuthor(_author);

        //assert
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }

}