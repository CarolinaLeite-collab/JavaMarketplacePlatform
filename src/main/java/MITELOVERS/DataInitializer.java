package MITELOVERS;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.directsale.DirectSaleFactory;
import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.genre.GenreFactory;
import MITELOVERS.domain.repository.IDirectSaleRepo;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.valueobject.Currency;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.Price;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;



@Component
@Profile("bootstrap")
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    public CommandLineRunner initData(
            IGenreRepo genreRepo,
            GenreFactory genreFactory,
            DirectSaleFactory directSaleFactory,
            IDirectSaleRepo directSaleRepo) {

        return args -> {

            //Create and save a few objects
            // -------------------------------------------------------
            // Genre
            Genre genre1 = genreFactory.createGenre("Fiction");
            genreRepo.save(genre1);
            Genre genre2 = genreFactory.createGenre("Non-Fiction");
            genreRepo.save(genre2);
            Genre genre3 = genreFactory.createGenre("Science Fiction");
            genreRepo.save(genre3);

            //DirectSale
            ItemId itemId = new ItemId("ABCDEF1234");
            List<ItemId> itemsId = new ArrayList<>();
            itemsId.add(itemId);
            Price price = new Price(10, Currency.EUR);
            Duration timeLimit = Duration.ofDays(7);

            DirectSale directSale1 = directSaleFactory.createDirectSale(itemsId, price, timeLimit);
            directSaleRepo.save(directSale1);

        };
    }


}