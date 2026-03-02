package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.Test;

import java.time.Period;
import java.util.List;

import static TOPSECRET.domain.Currency.EUR;
import static org.junit.jupiter.api.Assertions.*;

class GetDirectSaleItemsByGenreControllerTest {

    @Test
    void shouldReturnItemsByGenreThroughController() {

        Genre fantasy = new Genre("Fantasy");

        Publication book1 = Publication.builder()
                .type(new PublicationType("Book"))
                .identifier(new ISBN("9780691181950"))
                .year(java.time.Year.of(2023))
                .title(new Title("Controller Book"))
                .author(new Author("Author D"))
                .publisher(new PublishingCompany("Publisher D"))
                .genre(fantasy)
                .build();

        Item item = new Item(book1, Condition.GOOD);
        DirectSaleRepo directSaleRepo = new DirectSaleRepo();
        directSaleRepo.createDirectSale(item, new Price(30, EUR), Period.ofDays(14));
        GetDirectSaleItemsByGenreController controller =
                new GetDirectSaleItemsByGenreController(directSaleRepo);

        List<Item> result = controller.getDirectSaleItemsByGenre(fantasy);

        assertEquals(1, result.size());
        assertEquals(item, result.get(0));
    }
}
