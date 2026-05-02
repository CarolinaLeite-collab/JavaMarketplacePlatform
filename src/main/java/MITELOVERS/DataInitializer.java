package MITELOVERS;

import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.genre.GenreFactory;
import MITELOVERS.domain.repository.IGenreRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Optional;
import MITELOVERS.domain.valueobject.GenreId;

@Component
@Profile("bootstrap")
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    public CommandLineRunner initData(
            IGenreRepo genreRepo,
            GenreFactory genreFactory) {

        return args -> {

            // -------------------------------------------------------
            // Genre
            // Create and save a few Genres
            Genre genre1 = genreFactory.createGenre("Fiction");
            genreRepo.save(genre1);

            Genre genre2 = genreFactory.createGenre("Non-Fiction");
            genreRepo.save(genre2);

            Genre genre3 = genreFactory.createGenre("Science Fiction");
            genreRepo.save(genre3);

            // Fetch and log all genres
            log.info("Genres found with findAll():");
            log.info("-------------------------------");
            for (Genre genre : genreRepo.findAll()) {
                log.info(genre.toString());
            }
            log.info("");

            // Fetch an individual genre by ID
            GenreId targetGenreId = genre1.identity();
            Optional<Genre> opGenre = genreRepo.ofIdentity(targetGenreId);
            if (opGenre.isPresent()) {
                log.info("Genre found with ofIdentity(genre1.identity()):");
                log.info("--------------------------------");
                log.info(opGenre.get().toString());
                log.info("");
            }

            // Log all Genre keys
            log.info("All Genre IDs:");
            log.info("-------------------------------");
            for (GenreId id : genreRepo.findAllKeys()) {
                log.info(id.toString());
            }
        };
    }

}