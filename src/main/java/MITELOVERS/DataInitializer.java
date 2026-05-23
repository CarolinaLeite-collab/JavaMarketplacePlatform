package MITELOVERS;

import MITELOVERS.domain.appraisalentity.AppraisalEntity;
import MITELOVERS.domain.appraisalentity.AppraisalEntityFactory;
import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.auction.AuctionFactory;
import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.author.AuthorFactory;
import MITELOVERS.domain.city.City;
import MITELOVERS.domain.city.CityFactory;
import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.directsale.DirectSaleFactory;
import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.genre.GenreFactory;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.item.ItemFactory;
import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.listofitems.ListOfItemsFactory;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.publication.PublicationFactory;
import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.publicationtype.PublicationTypeFactory;
import MITELOVERS.domain.repository.*;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.user.UserFactory;
import MITELOVERS.domain.valueobject.*;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Year;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Generated
@Component
@Profile("bootstrap")
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    public CommandLineRunner initData(
            IGenreRepo genreRepo,
            IAuthorRepo authorRepo,
            IPublicationRepo publicationRepo,
            IPublicationTypeRepo publicationTypeRepo,
            ICityRepo cityRepo,
            IAppraisalEntityRepo appraisalEntityRepo,
            IListOfItemsRepo listOfItemsRepo,
            GenreFactory genreFactory,
            AuthorFactory authorFactory,
            PublicationFactory publicationFactory,
            PublicationTypeFactory publicationTypeFactory,
            CityFactory cityFactory,
            AppraisalEntityFactory appraisalEntityFactory,
            ListOfItemsFactory listOfItemsFactory,
            IUserRepo userRepo,
            UserFactory userFactory,
            IDirectSaleRepo directSaleRepo,
            DirectSaleFactory directSaleFactory,
            IAuctionRepo auctionRepo,
            AuctionFactory auctionFactory,
            IItemRepo itemRepo,
            ItemFactory itemFactory) {

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

            // -------------------------------------------------------
            // User
            Name userName = new Name("Pedro Silva");
            Email userEmail = new Email("pedro@aeiou.com");
            PhonePrefix phonePrefix = new PhonePrefix("+351");
            Phone phone = new Phone(phonePrefix, "912345678");

            User user = userFactory.createUser(userName, null, userEmail, phone);
            userRepo.save(user);

            Name userName2 = new Name("Ana Costa");
            Email userEmail2 = new Email("ana@aeiou.com");
            PhonePrefix phonePrefix2 = new PhonePrefix("+351");
            Phone phone2 = new Phone(phonePrefix2, "923456789");
            User user2 = userFactory.createUser(userName2, null, userEmail2, phone2);
            userRepo.save(user2);

            Name userName3 = new Name("Ângelo Martins");
            Email userEmail3 = new Email("angelo@aeiou.com");
            PhonePrefix phonePrefix3 = new PhonePrefix("+351");
            Phone phone3 = new Phone(phonePrefix3, "934567890");
            User user3 = userFactory.createUser(userName3, null, userEmail3, phone3);
            userRepo.save(user3);

            log.info("Users found with findAll():");
            log.info("Users saved: Pedro Silva, Ana Costa, Ângelo Martins");
            log.info("-------------------------------");
            for (User u : userRepo.findAll()) {
                log.info(u.toString());
            }
            // -------------------------------------------------------
            // Publication Types
            PublicationType book = publicationTypeFactory.createPublicationType("Book");
            PublicationType magazine = publicationTypeFactory.createPublicationType("Magazine");
            publicationTypeRepo.save(book);
            publicationTypeRepo.save(magazine);
            log.info("Publication types saved: Book, Magazine");

            // -------------------------------------------------------
            // Authors
            Author orwell = authorFactory.createAuthor(new Name("George Orwell"));
            Author asimov = authorFactory.createAuthor(new Name("Isaac Asimov"));
            Author yuval = authorFactory.createAuthor(new Name("Yuval Noah Harari"));
            authorRepo.save(orwell);
            authorRepo.save(asimov);
            authorRepo.save(yuval);
            log.info("Authors saved: George Orwell, Isaac Asimov, Yuval Noah Harari");

            // -------------------------------------------------------
            // Publications
            Publication nineteenEightyFour = publicationFactory.createPublication(
                    new Title("1984"),
                    orwell.identity(),
                    Year.of(1949),
                    genre1.identity()  // Fiction
            );
            Publication foundationSeries = publicationFactory.createPublication(
                    new Title("Foundation"),
                    asimov.identity(),
                    Year.of(1951),
                    genre3.identity()  // Science Fiction
            );
            Publication sapiens = publicationFactory.createPublication(
                    new Title("Sapiens"),
                    yuval.identity(),
                    Year.of(2011),
                    genre2.identity()  // Non-Fiction
            );
            publicationRepo.save(nineteenEightyFour);
            publicationRepo.save(foundationSeries);
            publicationRepo.save(sapiens);
            log.info("Publications saved: 1984, Foundation, Sapiens");

            // -------------------------------------------------------
            // Cities
            City porto = cityFactory.createCity("Porto", new CountryId("PT"));
            City lisbon = cityFactory.createCity("Lisbon", new CountryId("PT"));
            City london = cityFactory.createCity("London", new CountryId("GB"));
            cityRepo.save(porto);
            cityRepo.save(lisbon);
            cityRepo.save(london);
            log.info("Cities saved: Porto, Lisbon, London");

            // -------------------------------------------------------
            // Appraisal Entities
            AppraisalEntity booker = appraisalEntityFactory.createAppraisalEntity(
                    new Name("Booker Prize"),
                    List.of(book.identity()),
                    List.of(genre1.identity())  // Fiction
            );
            AppraisalEntity hugo = appraisalEntityFactory.createAppraisalEntity(
                    new Name("Hugo Awards"),
                    List.of(book.identity(), magazine.identity()),
                    List.of(genre3.identity())  // Science Fiction
            );
            appraisalEntityRepo.save(booker);
            appraisalEntityRepo.save(hugo);
            log.info("Appraisal entities saved: Booker Prize, Hugo Awards");

            log.info("DataInitializer completed successfully.");

            // -------------------------------------------------------
            // Lists of Items
            ListOfItems list1 = listOfItemsFactory.createListOfItems(
                    user.identity(),
                    new Name("Pedro Favourites"),
                    genre1.identity()
            );
            listOfItemsRepo.save(list1);

            ListOfItems list2 = listOfItemsFactory.createPublicListOfItems(
                    user2.identity(),
                    new Name("Ana Sci-Fi List"),
                    genre3.identity(),
                    new SharedDuration(30)
            );
            listOfItemsRepo.save(list2);

            ListOfItems list3 = listOfItemsFactory.createListOfItems(
                    user3.identity(),
                    new Name("Angelo Non-Fiction"),
                    genre2.identity()
            );
            listOfItemsRepo.save(list3);

            // -------------------------------------------------------
            // Items
            ItemId itemId1 = new ItemId("3C5D126F8B");
            Item item1 = itemFactory.createItem(
                    itemId1,
                    new EditionId("edition-123"),
                    Condition.GOOD,
                    new Description("Used copy in good condition"),
                    SaleStatus.OnDirectSale,
                    new Name("Book for sale")
            );
            itemRepo.save(item1);

            ItemId itemId2 = new ItemId("3F9F4BFAB2");
            Item item2 = itemFactory.createItem(
                    itemId2,
                    new EditionId("edition-456"),
                    Condition.FAIR,
                    new Description("Old copy"),
                    SaleStatus.OnDirectSale,
                    new Name("Rare Book for sale")
            );
            itemRepo.save(item2);


            // -------------------------------------------------------
            // Direct Sales
            ItemId item3 = new ItemId();

            DirectSale directSale1 = directSaleFactory.createDirectSale(
                    List.of(itemId1, itemId2),
                    new Price(9.99, Currency.EUR),
                    Duration.ofDays(30)
            );
            directSaleRepo.save(directSale1);

            DirectSale directSale2 = directSaleFactory.createDirectSale(
                    List.of(item3),
                    new Price(14.99, Currency.EUR),
                    Duration.ofDays(7)
            );
            directSaleRepo.save(directSale2);

            DirectSale directSale3 = directSaleFactory.createDirectSale(
                    List.of(new ItemId()),
                    new Price(4.99, Currency.EUR),
                    null  // unlimited duration
            );
            directSaleRepo.save(directSale3);

            log.info("Direct sales saved: 3 direct sales");

            // -------------------------------------------------------
            // Auctions
            Auction auction1 = auctionFactory.createAuction(
                    List.of(new ItemId()),
                    new Price(5.00, Currency.EUR),
                    new Price(10.00, Currency.EUR),
                    ZonedDateTime.now(),
                    ZonedDateTime.now().plusDays(7)
            );
            auctionRepo.save(auction1);

            Auction auction2 = auctionFactory.createAuction(
                    List.of(new ItemId()),
                    new Price(8.00, Currency.EUR),
                    new Price(15.00, Currency.EUR),
                    ZonedDateTime.now(),
                    ZonedDateTime.now().plusDays(3)
            );
            auctionRepo.save(auction2);

            Auction auction3 = auctionFactory.createAuction(
                    List.of(new ItemId()),
                    new Price(2.00, Currency.EUR),
                    new Price(6.00, Currency.EUR),
                    ZonedDateTime.now(),
                    ZonedDateTime.now().plusDays(14)
            );
            auctionRepo.save(auction3);

            log.info("Auctions saved: 3 auctions");

            // Auction with bids and winner
            Auction auctionWithBids = auctionFactory.createAuction(
                    List.of(new ItemId()),
                    new Price(5.00, Currency.EUR),
                    new Price(10.00, Currency.EUR),
                    ZonedDateTime.now().minusDays(7),   // started in the past
                    ZonedDateTime.now().plusMinutes(1)  // ends in 1 minute — still active
            );

            // Place bids while active
            auctionWithBids.placeBid(user.identity(), new Price(11.00, Currency.EUR));
            auctionWithBids.placeBid(user2.identity(), new Price(13.00, Currency.EUR));
            auctionWithBids.placeBid(user3.identity(), new Price(15.00, Currency.EUR));

            // Finalize manually
            auctionWithBids.finalizeAuction();

            auctionRepo.save(auctionWithBids);
            log.info("Auction with bids saved — winner: Ângelo Martins at 15.00 EUR");
        };
    }

}