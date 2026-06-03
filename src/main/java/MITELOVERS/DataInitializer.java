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
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.edition.EditionFactory;
import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.genre.GenreFactory;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.item.ItemFactory;
import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.library.LibraryFactory;
import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.listofitems.ListOfItemsFactory;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.publication.PublicationFactory;
import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.publicationtype.PublicationTypeFactory;
import MITELOVERS.domain.publishingcompany.PublishingCompany;
import MITELOVERS.domain.publishingcompany.PublishingCompanyFactory;
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
            ItemFactory itemFactory,
            ILibraryRepo libraryRepo,
            LibraryFactory libraryFactory,
            IPublishingCompanyRepo publishingCompanyRepo,
            PublishingCompanyFactory publishingCompanyFactory,
            IEditionRepo editionRepo,
            EditionFactory editionFactory
    ) {

        return args -> {

            // -------------------------------------------------------
            // Genre
            // Create and save a few Genres
            Genre genre1 = genreFactory.createGenre("Arts");
            genreRepo.save(genre1);

            Genre genre2 = genreFactory.createGenre("Biography");
            genreRepo.save(genre2);

            Genre genre3 = genreFactory.createGenre("Education");
            genreRepo.save(genre3);

            Genre genre4 = genreFactory.createGenre("Fiction");
            genreRepo.save(genre4);

            Genre genre5 = genreFactory.createGenre("History");
            genreRepo.save(genre5);

            Genre genre6 = genreFactory.createGenre("Literature");
            genreRepo.save(genre6);

            Genre genre7 = genreFactory.createGenre("Non-Fiction");
            genreRepo.save(genre7);

            Genre genre8 = genreFactory.createGenre("Other");
            genreRepo.save(genre8);

            Genre genre9 = genreFactory.createGenre("Science");
            genreRepo.save(genre9);

            Genre genre10 = genreFactory.createGenre("Science-Fiction");
            genreRepo.save(genre10);

            Genre genre11 = genreFactory.createGenre("Technology");
            genreRepo.save(genre11);



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
            Author orwell = authorFactory.createAuthor(new AuthorId("Orwell G.-F43DD6"), new Name("George Orwell"));
            Author asimov = authorFactory.createAuthor(new AuthorId("Asimov I.-D60AD1"),new Name("Isaac Asimov"));
            Author yuval = authorFactory.createAuthor(new AuthorId("Harari Y.N.-54369C"),new Name("Yuval Noah Harari"));
            Author helder = authorFactory.createAuthor(new AuthorId("Helder H.-27DB3C"),new Name("Helberto Helder"));
            Author koolhaas = authorFactory.createAuthor(new AuthorId("Koolhaas R.-23B3C"),new Name("Rem Koolhaas"));
            Author alexander = authorFactory.createAuthor(new AuthorId("Alexander C.-13B3C"),new Name("Cristopher Alexander"));
            Author seneca = authorFactory.createAuthor(
                    new AuthorId("Seneca L.A.-018812"),
                    new Name("Lucius Annaeus Seneca")
            );
            authorRepo.save(seneca);
            authorRepo.save(orwell);
            authorRepo.save(asimov);
            authorRepo.save(yuval);
            authorRepo.save(helder);
            authorRepo.save(koolhaas);
            authorRepo.save(alexander);
            authorRepo.save(seneca);

            log.info("Authors saved: George Orwell, Isaac Asimov, Yuval Noah Harari, Helberto Helder, Rem Koolhaas,Cristopher Alexander, Seneca");

            // -------------------------------------------------------
            // Publications
            Publication novaYorkDelirante = publicationFactory.createPublication(
                    new Title("Nova York Delirante"),
                    koolhaas.identity(),
                    Year.of(1978),
                    genre1.identity()  // Arts
            );

            Publication nineteenEightyFour = publicationFactory.createPublication(
                    new Title("1984"),
                    orwell.identity(),
                    Year.of(1949),
                    genre4.identity()  // Fiction
            );
            Publication foundationSeries = publicationFactory.createPublication(
                    new Title("Foundation"),
                    asimov.identity(),
                    Year.of(1951),
                    genre10.identity()  // Science Fiction
            );
            Publication sapiens = publicationFactory.createPublication(
                    new Title("Sapiens"),
                    yuval.identity(),
                    Year.of(2011),
                    genre7.identity()  // Non-Fiction
            );

            Publication pattern = publicationFactory.createPublication(
                    new Title("A Pattern Language"),
                    alexander.identity(),
                    Year.of(1977),
                    genre11.identity()  // Non-Fiction
            );

            Publication shortnessOfLife = publicationFactory.createPublication(
                    new Title("On the Shortness of Life"),
                    seneca.identity(),
                    Year.of(49),
                    genre7.identity() // Non-Fiction
            );

            publicationRepo.save(shortnessOfLife);
            publicationRepo.save(novaYorkDelirante);
            publicationRepo.save(nineteenEightyFour);
            publicationRepo.save(foundationSeries);
            publicationRepo.save(sapiens);
            publicationRepo.save(pattern);
            publicationRepo.save(shortnessOfLife);

            log.info("Publications saved: 1984, Foundation, Sapiens, novaYorkDelirante, patternLanguage");

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
                    List.of(genre4.identity())  // Fiction
            );
            AppraisalEntity hugo = appraisalEntityFactory.createAppraisalEntity(
                    new Name("Hugo Awards"),
                    List.of(book.identity(), magazine.identity()),
                    List.of(genre10.identity())  // Science Fiction
            );
            appraisalEntityRepo.save(booker);
            appraisalEntityRepo.save(hugo);
            log.info("Appraisal entities saved: Booker Prize, Hugo Awards");


            // -------------------------------------------------------
            // Publishing Companies
            PublishingCompany seckerWarburg = publishingCompanyFactory.createPublishingCompany("Secker and Warburg");
            publishingCompanyRepo.save(seckerWarburg);

            PublishingCompany gnomePress = publishingCompanyFactory.createPublishingCompany("Gnome Press");
            publishingCompanyRepo.save(gnomePress);

            PublishingCompany gg = publishingCompanyFactory.createPublishingCompany("GG");
            publishingCompanyRepo.save(gg);

            PublishingCompany oxfordUP = publishingCompanyFactory.createPublishingCompany("Oxford University Press");
            publishingCompanyRepo.save(gg);
            PublishingCompany penguinBooks = publishingCompanyFactory.createPublishingCompany("Penguin Books");
            publishingCompanyRepo.save(penguinBooks);

            log.info("Publishing companies saved: Secker & Warburg, Gnome Press, GG, Oxford University Press, Penguin Books");

            // -------------------------------------------------------
            // Editions
            Edition editionNovaYorkDelirante = editionFactory.createEdition(
                    book.identity(),
                    new ISBN("9788425222481"),
                    novaYorkDelirante.identity(),
                    gg.identity(),
                    Year.of(2008),
                    Language.PORTUGUESE_BR,
                    new Dimension(17.0, 24.0, 2.0, DimensionUnit.CENTIMETERS),
                    null,
                    new NumberOfPages(365),
                    new EditionNumber(1),
                    Binding.PUR
            );
            editionRepo.save(editionNovaYorkDelirante);

            Edition edition1984 = editionFactory.createEdition(
                    book.identity(),
                    new NoIdentifier(),
                    nineteenEightyFour.identity(),
                    seckerWarburg.identity(),
                    Year.of(1949),
                    Language.ENGLISH,
                    null,
                    null,
                    null,
                    null, Binding.PUR
            );
            editionRepo.save(edition1984);

            Edition edition1984Modern = editionFactory.createEdition(
                    book.identity(),
                    new ISBN("9780451524935"),
                    nineteenEightyFour.identity(),
                    seckerWarburg.identity(),
                    Year.of(2003),
                    Language.ENGLISH,
                    null, null, null, null, null
            );
            editionRepo.save(edition1984Modern);

            Edition edition1977PatternLanguage = editionFactory.createEdition(
                    book.identity(),
                    new ISBN("0195019199"),
                    pattern.identity(),
                    oxfordUP.identity(),
                    Year.of(1977),
                    Language.ENGLISH,
                    null,
                    null,
                    new NumberOfPages(1171),
                    new EditionNumber(1),
                    Binding.HARDCOVER
            );
            editionRepo.save(edition1984Modern);

            Edition editionShortnessOfLife = editionFactory.createEdition(
                    book.identity(),
                    new ISBN("9780141018812"),
                    shortnessOfLife.identity(),
                    penguinBooks.identity(),
                    Year.of(2004),
                    Language.ENGLISH,
                    new Dimension(10.9, 18.0, 0.8, DimensionUnit.CENTIMETERS),
                    null,
                    new NumberOfPages(112),
                    new EditionNumber(1),
                    Binding.PUR
            );
            editionRepo.save(editionShortnessOfLife);

            log.info("Editions saved: 1984, Foundation, Nova York Delirante,");


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
                    edition1984.identity(),
                    Condition.GOOD,
                    new Description("Used copy in good condition"),
                    SaleStatus.OnDirectSale,
                    new Picture("https://upload.wikimedia.org/wikipedia/commons/5/51/1984_first_edition_cover.jpg")

            );
            itemRepo.save(item1);


            ItemId itemId2 = new ItemId("3F9F4BFAB2");
            Item item2 = itemFactory.createItem(
                    itemId2,
                    edition1984Modern.identity(),
                    Condition.FAIR,
                    new Description("Modern edition"),
                    SaleStatus.OnDirectSale
            );
            itemRepo.save(item2);

            ItemId itemId3 = new ItemId("3F9F4BFAB5");
            Item item3 = itemFactory.createItem(
                    itemId3,
                    editionNovaYorkDelirante.identity(),
                    Condition.GOOD,
                    new Description("Portuguese Edition of Delirious New York"),
                    SaleStatus.OnDirectSale,
                    new Picture("/images/deliriousNewYork.png")
            );
            itemRepo.save(item3);

            ItemId itemId4 = new ItemId("3B9F4BFAB5");
            Item item4 = itemFactory.createItem(
                    itemId4,
                    edition1977PatternLanguage.identity(),
                    Condition.FAIR,
                    new Description("First Edition"),
                    SaleStatus.NotOnSale,
                    new Picture("/images/patternLanguage.png")
            );
            itemRepo.save(item4);

            ItemId shortnessOfLifeItemId = new ItemId("0141018812");
            Item shortnessOfLifeItem = itemFactory.createItem(
                    shortnessOfLifeItemId,
                    editionShortnessOfLife.identity(),
                    Condition.GOOD,
                    new Description("Penguin Great Ideas edition of On the Shortness of Life"),
                    SaleStatus.OnDirectSale
            );
            itemRepo.save(shortnessOfLifeItem);


            // -------------------------------------------------------
            // Direct Sales
            ItemId item5 = new ItemId();

            DirectSale directSale1 = directSaleFactory.createDirectSale(
                    List.of(itemId1, itemId2),
                    new Price(9.99, Currency.EUR),
                    Duration.ofDays(30)
            );
            directSaleRepo.save(directSale1);

            DirectSale directSale2 = directSaleFactory.createDirectSale(
                    List.of(item5),
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
            // Additional Items for missing genres
            // -------------------------------------------------------

            // Edition for Sapiens (Non-Fiction)
            Edition editionSapiens = editionFactory.createEdition(
                    book.identity(),                     // PublicationType: Book
                    new ISBN("9780099590088"),           // Valid Sapiens ISBN
                    sapiens.identity(),                  // Publication Sapiens
                    gg.identity(),                       // Publishing Company GG
                    Year.of(2014),                       // Year of edition
                    Language.ENGLISH,                    // Language
                    null,                                // Dimension
                    null,                                // Weight
                    new NumberOfPages(498),              // Pages
                    new EditionNumber(1),                // Edition number
                    Binding.PUR                          // Binding
            );
            editionRepo.save(editionSapiens);

            // Edition for Foundation (Science-Fiction)
            Edition editionFoundationSeries = editionFactory.createEdition(
                    book.identity(),                     // PublicationType: Book
                    new ISBN("9780553293357"),           // Valid Foundation ISBN
                    foundationSeries.identity(),         // Publication Foundation
                    gg.identity(),                       // Publishing Company GG
                    Year.of(1991),                       // Year of edition
                    Language.ENGLISH,                    // Language
                    null,                                // Dimension
                    null,                                // Weight
                    new NumberOfPages(255),              // Pages
                    new EditionNumber(1),                // Edition number
                    Binding.PUR                          // Binding
            );
            editionRepo.save(editionFoundationSeries);

            log.info("Two additional editions saved for DirectSales");

            // Non-Fiction item
            ItemId nfItemId = new ItemId("5E4D3C2B1A");
            Item nfItem = itemFactory.createItem(
                    nfItemId,
                    editionSapiens.identity(), // Publication Sapiens → Non-Fiction
                    Condition.GOOD,
                    new Description("Non-Fiction test item"),
                    SaleStatus.OnDirectSale
            );
            itemRepo.save(nfItem);

            // Science-Fiction item
            ItemId sfItemId = new ItemId("F1A2B3C4D5");
            Item sfItem = itemFactory.createItem(
                    sfItemId,
                    editionFoundationSeries.identity(), // Publication Foundation → Sci-Fi
                    Condition.GOOD,
                    new Description("Sci-Fi test item"),
                    SaleStatus.OnDirectSale
            );
            itemRepo.save(sfItem);

            log.info("Two additional items saved for DirectSales");

            // -------------------------------------------------------
            // DirectSales for ALL genres
            // -------------------------------------------------------

            // Fiction — DirectSale #1 (existing items)
            DirectSale fictionSale1 = directSaleFactory.createDirectSale(
                    List.of(itemId1, itemId2),
                    new Price(7.99, Currency.EUR),
                    Duration.ofDays(15)
            );
            directSaleRepo.save(fictionSale1);

            // Fiction — DirectSale #2 (new item)
            ItemId fictionExtraId = new ItemId("0A1B2C3D4E");
            Item fictionExtraItem = itemFactory.createItem(
                    fictionExtraId,
                    edition1984.identity(),
                    Condition.FAIR,
                    new Description("Extra Fiction item"),
                    SaleStatus.OnDirectSale
            );
            itemRepo.save(fictionExtraItem);

            DirectSale fictionSale2 = directSaleFactory.createDirectSale(
                    List.of(fictionExtraId),
                    new Price(5.99, Currency.EUR),
                    Duration.ofDays(10)
            );
            directSaleRepo.save(fictionSale2);

            // Non-Fiction
            DirectSale nonFictionSale = directSaleFactory.createDirectSale(
                    List.of(nfItemId),
                    new Price(8.49, Currency.EUR),
                    Duration.ofDays(20)
            );
            directSaleRepo.save(nonFictionSale);

            // Science-Fiction
            DirectSale sciFiSale = directSaleFactory.createDirectSale(
                    List.of(sfItemId),
                    new Price(6.49, Currency.EUR),
                    Duration.ofDays(25)
            );
            directSaleRepo.save(sciFiSale);

           // Architecture (existing item3)
            DirectSale architectureSale = directSaleFactory.createDirectSale(
                    List.of(itemId3),
                    new Price(12.99, Currency.EUR),
                    Duration.ofDays(30)
            );
            directSaleRepo.save(architectureSale);

            log.info("DirectSales of all genres");

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

            // Libraries with items
            Library libraryPedro = libraryFactory.createLibrary(user.identity());
            libraryPedro.addItemIdToLibrary(itemId1);
            libraryRepo.save(libraryPedro);

            Library libraryAna = libraryFactory.createLibrary(user2.identity());
            libraryAna.addItemIdToLibrary(itemId2);
            libraryRepo.save(libraryAna);

            Library libraryAngelo = libraryFactory.createLibrary(user3.identity());
            libraryRepo.save(libraryAngelo);  // biblioteca vazia

            log.info("Libraries saved: Pedro, Ana, Angelo");

            log.info("DataInitializer completed successfully.");

        };

    }

}