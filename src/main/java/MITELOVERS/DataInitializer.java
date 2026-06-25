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
import MITELOVERS.domain.shoppingcart.ShoppingCart;
import MITELOVERS.domain.shoppingcart.ShoppingCartFactory;
import MITELOVERS.domain.shoppingcart.ShoppingCartLineFactory;
import MITELOVERS.domain.shoppingcart.ShoppingCartLine;
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
            EditionFactory editionFactory,
            IShoppingCartRepo shoppingCartRepo,
            ShoppingCartFactory shoppingCartFactory,
            ShoppingCartLineFactory shoppingCartLineFactory
    ) {

        return args -> {

            if (genreRepo.findAllKeys().iterator().hasNext()) {
                log.info("Database already seeded — skipping DataInitializer.");
                return;
            }

            // -------------------------------------------------------
            // Genre
            // Create and save a few Genres
            Genre genreArts = genreFactory.createGenre("Arts");
            genreRepo.save(genreArts);

            Genre genreBiography = genreFactory.createGenre("Biography");
            genreRepo.save(genreBiography);

            Genre genreEducation = genreFactory.createGenre("Education");
            genreRepo.save(genreEducation);

            Genre genreFiction = genreFactory.createGenre("Fiction");
            genreRepo.save(genreFiction);

            Genre genreHistory = genreFactory.createGenre("History");
            genreRepo.save(genreHistory);

            Genre genreLiterature = genreFactory.createGenre("Literature");
            genreRepo.save(genreLiterature);

            Genre genreNonFiction = genreFactory.createGenre("Non-Fiction");
            genreRepo.save(genreNonFiction);

            Genre genreOther = genreFactory.createGenre("Other");
            genreRepo.save(genreOther);

            Genre genreScience = genreFactory.createGenre("Science");
            genreRepo.save(genreScience);

            Genre genreSF = genreFactory.createGenre("Science-Fiction");
            genreRepo.save(genreSF);

            Genre genreTechnology = genreFactory.createGenre("Technology");
            genreRepo.save(genreTechnology);

            Genre genreChildren = genreFactory.createGenre("Children's Literature");
            genreRepo.save(genreChildren);


            // Fetch and log all genres
            log.info("Genres found with findAll():");
            log.info("-------------------------------");
            for (Genre genre : genreRepo.findAll()) {
                log.info(genre.toString());
            }
            log.info("");

            // Fetch an individual genre by ID
            GenreId targetGenreId = genreArts.identity();
            Optional<Genre> opGenre = genreRepo.ofIdentity(targetGenreId);
            if (opGenre.isPresent()) {
                log.info("Genre found with ofIdentity(genreArts.identity()):");
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

            Name userName4 = new Name("Guest");
            Email userEmail4 = new Email("guest@aeiou.com");
            User user4 = userFactory.createUser(userName4, null, userEmail4, null);
            user4.removeRole(Role.USER);
            user4.addRole(Role.NONREGISTRED);
            userRepo.save(user4);

            Name userNameTono = new Name("Manuel António");
            Email userEmailTono = new Email("tono@aeiou.com");
            PhonePrefix phonePrefixTono = new PhonePrefix("+351");
            Phone phoneTono = new Phone(phonePrefixTono, "911567890");
            User userTono = userFactory.createUser(userNameTono, null, userEmailTono, phoneTono);
            userRepo.save(userTono);

            log.info("Users found with findAll():");
            log.info("Users saved: Pedro Silva, Ana Costa, Ângelo Martins, Guest");
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
            Author alexander = authorFactory.createAuthor(new AuthorId("Alexander C.-13B3C"), new Name("Cristopher Alexander"));
            Author asimov = authorFactory.createAuthor(new AuthorId("Asimov I.-D60AD1"), new Name("Isaac Asimov"));
            Author eg = authorFactory.createAuthor(new AuthorId("Gray E.A.-219812"), new Name("Eileen Gray"));
            Author yuval = authorFactory.createAuthor(new AuthorId("Harari Y.N.-54369C"), new Name("Yuval Noah Harari"));
            Author helder = authorFactory.createAuthor(new AuthorId("Helder H.-27DB3C"), new Name("Helberto Helder"));
            Author koolhaas = authorFactory.createAuthor(new AuthorId("Koolhaas R.-23B3C"), new Name("Rem Koolhaas"));
            Author orwell = authorFactory.createAuthor(new AuthorId("Orwell G.-F43DD6"), new Name("George Orwell"));
            Author scharmen = authorFactory.createAuthor(new AuthorId("Scharmen F.-10B3C"), new Name("Fred Scharmen"));
            Author seneca = authorFactory.createAuthor(new AuthorId("Seneca L.A.-018812"), new Name("Lucius Annaeus Seneca"));
            Author ruiTavares = authorFactory.createAuthor(new AuthorId("Tavares R.-483561"), new Name("Rui Tavares"));
            Author saramago = authorFactory.createAuthor(new AuthorId("Saramago J.-A1B2C3"), new Name("José Saramago"));
            Author delahaye = authorFactory.createAuthor(new AuthorId("Delahaye G.-C1D2E3"), new Name("Gilbert Delahaye"));
            Author berghauserPont = authorFactory.createAuthor(new AuthorId("BerghauserPont M.-A7F2D1"), new Name("Meta Berghauser Pont"));
            Author varoufakis = authorFactory.createAuthor(new AuthorId("Varoufakis Y.-A7D2D1"), new Name ("Yanis Varoufakis"));
            Author christie = authorFactory.createAuthor(new AuthorId("Christie A.-D9D2D1"), new Name ("Agatha Christie"));
            Author celas = authorFactory.createAuthor(new AuthorId("Celas C.-D9D0D0"), new Name ("Carolina Celas"));
            Author solaMolares = authorFactory.createAuthor(new AuthorId("SolaMorales I.-D9D0D0"), new Name ("Ignasi de Solà-Morales"));
            Author goethe = authorFactory.createAuthor(new AuthorId("Goethe J.-J2D0D0"), new Name ("Johann Wolfgang von Goethe"));

            authorRepo.save(ruiTavares);
            authorRepo.save(alexander);
            authorRepo.save(eg);
            authorRepo.save(yuval);
            authorRepo.save(helder);
            authorRepo.save(koolhaas);
            authorRepo.save(orwell);
            authorRepo.save(scharmen);
            authorRepo.save(seneca);
            authorRepo.save(asimov);
            authorRepo.save(saramago);
            authorRepo.save(delahaye);
            authorRepo.save(berghauserPont);
            authorRepo.save(varoufakis);
            authorRepo.save(christie);
            authorRepo.save(celas);
            authorRepo.save(solaMolares);
            authorRepo.save(goethe);

            log.info("Authors saved: Cristopher Alexander, Isaac Asimov, Eileen Gray, Yuval Noah Harari, Helberto Helder, Rem Koolhaas, Rui Tavares, George Orwell, Fred Scharmen, Lucius Annaeus Seneca, Meta Berghauser Pont");

            // -------------------------------------------------------
            // Synopsis

            String novaYorkDeliranteSynopsis =
                    "An exploration of New York City's urban evolution, presenting Manhattan as a laboratory of architecture, culture, and imagination where density and chaos shaped the modern metropolis.";

            String nineteenEightyFourSynopsis =
                    "In a totalitarian regime where the State controls truth and constantly watches its citizens, Winston Smith struggles to preserve freedom of thought and his own identity.";

            String foundationSeriesSynopsis =
                    "As a galactic empire collapses, scientists use psychohistory to preserve knowledge and reduce centuries of chaos by establishing the Foundation.";

            String sapiensSynopsis =
                    "A journey through the history of humankind, from early hunter-gatherers to modern civilization, exploring how ideas, cultures, and revolutions shaped Homo sapiens.";

            String patternSynopsis =
                    "An exploration of the relationship between architecture, nature, and human behavior, showing how recurring patterns can inspire more vibrant and functional spaces.";

            String shortnessOfLifeSynopsis =
                    "Seneca reflects on the value of time and existence, arguing that life is long enough when lived with purpose, wisdom, and virtue.";

            String e1027Synopsis =
                    "A study of the iconic E-1027 house, highlighting Eileen Gray's innovative vision and the building's lasting importance in the history of modern architecture.";

            String spaceSettlementsSynopsis =
                    "A visionary proposal for human colonies in space, exploring scientific and technological solutions for living beyond Earth.";

            String hipocritoesEOlhigarcasSynopsis =
                    "A critique of political and economic elites, examining the structures of power, inequality, and influence that shape contemporary societies.";

            String intermitenciasSynopsis =
                    "When nobody dies in an entire country, society faces unexpected consequences while Death herself questions her role and the meaning of human existence.";

            String anitaVasComprasSynopsis =
                    "Anita joins her family on a shopping trip, discovering products, people, and everyday lessons through a simple and educational adventure.";

            String spaceMatrixSynopsis =
                    "A reflection on architecture and urbanism through the relationship between space, culture, and technology, proposing new ways of understanding the built environment.";

            String italienischeReiseSynopsis =
                    "A travel journal recounting Goethe’s journey through Italy, combining observations on art, culture, and nature with a personal quest for inspiration and self-discovery.";

            String diferenciasSynopsis =
                    "A reflection on contemporary architecture, highlighting the diversity of ideas, styles, and cultural influences that define the built environment today.";

            String horizonteSynopsis =
                    "A poetic illustrated book that explores the idea of the horizon through gentle storytelling and evocative artwork.";

            String technoFSynopsis =
                    "A critique of the rise of technofeudalism and the growing influence of digital platforms on the economy, politics, and democracy.";

            String agathaCristieSynopsis =
                    "A light-hearted memoir recounting Agatha Christie’s experiences on archaeological journeys in the Middle East.";


            log.info("Authors saved: Cristopher Alexander, Isaac Asimov, Eileen Gray, Yuval Noah Harari, Helberto Helder, Rem Koolhaas, George Orwell, Fred Scharmen, Lucius Annaeus Seneca, Rui Tavares, José Saramago, Gilbert Delahaye, Meta Berghauser Pont, Agatha Christie, Carolina Celas, Ignasi de Solà-Morales, Johann Wolfgang von Goethe");
            // -------------------------------------------------------
            // Publications
            Publication novaYorkDelirante = publicationFactory.createPublication(
                    new Title("Nova York Delirante"),
                    koolhaas.identity(),
                    Year.of(1978),
                    genreArts.identity(),  // Arts
                    novaYorkDeliranteSynopsis

            );

            Publication nineteenEightyFour = publicationFactory.createPublication(
                    new Title("1984"),
                    orwell.identity(),
                    Year.of(1949),
                    genreFiction.identity(),  // Fiction
                    nineteenEightyFourSynopsis


            );
            Publication foundationSeries = publicationFactory.createPublication(
                    new Title("Foundation"),
                    asimov.identity(),
                    Year.of(1951),
                    genreSF.identity(),  // Science Fiction
                    foundationSeriesSynopsis
            );
            Publication sapiens = publicationFactory.createPublication(
                    new Title("Sapiens"),
                    yuval.identity(),
                    Year.of(2011),
                    genreNonFiction.identity(),  // Non-Fiction
                    sapiensSynopsis

            );

            Publication pattern = publicationFactory.createPublication(
                    new Title("A Pattern Language"),
                    alexander.identity(),
                    Year.of(1977),
                    genreTechnology.identity(),  // Non-Fiction
                    patternSynopsis
            );

            Publication shortnessOfLife = publicationFactory.createPublication(
                    new Title("On the Shortness of Life"),
                    seneca.identity(),
                    Year.of(49),
                    genreNonFiction.identity(), // Non-Fiction
                    shortnessOfLifeSynopsis
            );

            Publication e1027 = publicationFactory.createPublication(
                    new Title("E.1027. Maison en Bord de Mer"),
                    eg.identity(),
                    Year.of(1929),
                    genreArts.identity(), // Arts / Architecture
                    e1027Synopsis
            );

            Publication spaceSettlements = publicationFactory.createPublication(
                    new Title("Space Settlements"),
                    scharmen.identity(),
                    Year.of(2019),
                    genreArts.identity(), // Arts / Architecture
                    spaceSettlementsSynopsis
            );

            Publication hipocritoesEOlhigarcas = publicationFactory.createPublication(
                    new Title("Hipocritões e Oligarcas"),
                    ruiTavares.identity(),
                    Year.of(2025),
                    genreNonFiction.identity(), // Non-Fiction
                    hipocritoesEOlhigarcasSynopsis
            );

            Publication intermitencias = publicationFactory.createPublication(
                    new Title("As Intermitências da Morte"),
                    saramago.identity(),
                    Year.of(2005),
                    genreFiction.identity(),  // Fiction
                    intermitenciasSynopsis

            );

            Publication anitaVasCompras = publicationFactory.createPublication(
                    new Title("Anita vai às Compras"),
                    delahaye.identity(),
                    Year.of(1954),
                    genreChildren.identity(),  // Other
                    anitaVasComprasSynopsis
            );

            Publication spaceMatrix = publicationFactory.createPublication(
                    new Title("Spacematrix: Space, Density and Urban Form"),
                    berghauserPont.identity(),
                    Year.of(2005),
                    genreArts.identity(),
                    spaceMatrixSynopsis

            );

            Publication italienischeReise = publicationFactory.createPublication(
                    new Title("Italienische Reise"),
                    goethe.identity(),
                    Year.of(1981),
                    genreLiterature.identity(),
                    italienischeReiseSynopsis
            );

            Publication diferenciasPub = publicationFactory.createPublication(
                    new Title("Differencias"),
                    solaMolares.identity(),
                    Year.of(1995),
                    genreArts.identity(),
                    diferenciasSynopsis
            );

            Publication horizontePub = publicationFactory.createPublication(
                    new Title("Horizonte"),
                    celas.identity(),
                    Year.of(2018),
                    genreChildren.identity(),
                    horizonteSynopsis
            );

            Publication technoFPub = publicationFactory.createPublication(
                    new Title("Techno Feudalismo"),
                    varoufakis.identity(),
                    Year.of(2023),
                    genreNonFiction.identity(),
                    technoFSynopsis
            );

            Publication agathaCristiePub = publicationFactory.createPublication(
                    new Title("Agatha Christie na Síria"),
                    christie.identity(),
                    Year.of(1946),
                    genreNonFiction.identity(),
                    agathaCristieSynopsis
            );

            publicationRepo.save(shortnessOfLife);
            publicationRepo.save(novaYorkDelirante);
            publicationRepo.save(nineteenEightyFour);
            publicationRepo.save(foundationSeries);
            publicationRepo.save(sapiens);
            publicationRepo.save(pattern);
            publicationRepo.save(shortnessOfLife);
            publicationRepo.save(e1027);
            publicationRepo.save(spaceSettlements);
            publicationRepo.save(hipocritoesEOlhigarcas);
            publicationRepo.save(intermitencias);
            publicationRepo.save(anitaVasCompras);
            publicationRepo.save(spaceMatrix);
            publicationRepo.save(italienischeReise);
            publicationRepo.save(diferenciasPub);
            publicationRepo.save(horizontePub);
            publicationRepo.save(technoFPub);
            publicationRepo.save(agathaCristiePub);

            log.info("Publications saved: On the Shortness of Life, Nova York Delirante, 1984, Foundation, Sapiens, A Pattern Language, E.1027. Maison en Bord de Mer, Space Settlements, Hipocritões e Oligarcas, As Intermitências da Morte, Anita vai às Compras, Spacematrix: Space, Density and Urban Form, Italienische Reise, Differencias, Horizonte, Techno Feudalismo");
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
                    List.of(genreFiction.identity())  // Fiction
            );
            AppraisalEntity hugo = appraisalEntityFactory.createAppraisalEntity(
                    new Name("Hugo Awards"),
                    List.of(book.identity(), magazine.identity()),
                    List.of(genreSF.identity())  // Science Fiction
            );
            appraisalEntityRepo.save(booker);
            appraisalEntityRepo.save(hugo);
            log.info("Appraisal entities saved: Booker Prize, Hugo Awards");


            // -------------------------------------------------------
            // Publishing Companies
            PublishingCompany tintaDaChinaBrasil =
                    publishingCompanyFactory.createPublishingCompany("Tinta da China");
            publishingCompanyRepo.save(tintaDaChinaBrasil);

            PublishingCompany seckerWarburg = publishingCompanyFactory.createPublishingCompany("Secker and Warburg");
            publishingCompanyRepo.save(seckerWarburg);

            PublishingCompany gnomePress = publishingCompanyFactory.createPublishingCompany("Gnome Press");
            publishingCompanyRepo.save(gnomePress);

            PublishingCompany gg = publishingCompanyFactory.createPublishingCompany("GG");
            publishingCompanyRepo.save(gg);

            PublishingCompany oxfordUP = publishingCompanyFactory.createPublishingCompany("Oxford University Press");
            publishingCompanyRepo.save(oxfordUP);
            PublishingCompany penguinBooks = publishingCompanyFactory.createPublishingCompany("Penguin Books");
            publishingCompanyRepo.save(penguinBooks);

            PublishingCompany editionsAlbertMorance =
                    publishingCompanyFactory.createPublishingCompany("Éditions Albert Morancé");
            publishingCompanyRepo.save(editionsAlbertMorance);
            PublishingCompany columbia = publishingCompanyFactory.createPublishingCompany("Columbia Books");
            publishingCompanyRepo.save(columbia);

            PublishingCompany camiloAzurara = publishingCompanyFactory.createPublishingCompany("Caminho");
            publishingCompanyRepo.save(camiloAzurara);

            PublishingCompany hachette = publishingCompanyFactory.createPublishingCompany("Hachette");
            publishingCompanyRepo.save(hachette);

            PublishingCompany nai010Publishers = publishingCompanyFactory.createPublishingCompany("naiOIO Publishers");
            publishingCompanyRepo.save(nai010Publishers);

            PublishingCompany cHBeckVerlag = publishingCompanyFactory.createPublishingCompany("CH Beck");
            publishingCompanyRepo.save(cHBeckVerlag);

            PublishingCompany orfeuPublisher = publishingCompanyFactory.createPublishingCompany("Orfeu Negro");
            publishingCompanyRepo.save(orfeuPublisher);

            PublishingCompany bhPublisher = publishingCompanyFactory.createPublishingCompany("The Bodley Head");
            publishingCompanyRepo.save(bhPublisher);

            log.info("Publishing companies saved: Columbia Books, Éditions Albert Morancé, GG, Gnome Press, Orfeu Negro, Oxford University Press, Penguin Books, Secker and Warburg, Tinta da China, naiOIO Publishers");

            // -------------------------------------------------------
            // Editions

            Edition editionHipocritoesEOlhigarcas = editionFactory.createEdition(
                    book.identity(),
                    new ISBN("9786584835610"),
                    hipocritoesEOlhigarcas.identity(),
                    tintaDaChinaBrasil.identity(),
                    Year.of(2025),
                    Language.PORTUGUESE,
                    null,
                    null,
                    new NumberOfPages(240),
                    new EditionNumber(1),
                    Binding.PAPERBACK
            );
            editionRepo.save(editionHipocritoesEOlhigarcas);

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
                    Binding.PAPERBACK
            );
            editionRepo.save(editionNovaYorkDelirante);

            Edition spaceSetlements = editionFactory.createEdition(
                    book.identity(),
                    new ISBN("9781941332498"),
                    spaceSettlements.identity(),
                    columbia.identity(),
                    Year.of(2019),
                    Language.ENGLISH_US,
                    null,
                    null,
                    new NumberOfPages(208),
                    new EditionNumber(1),
                    Binding.PAPERBACK
            );
            editionRepo.save(spaceSetlements);

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
                    null,
                    Binding.PAPERBACK
            );
            editionRepo.save(edition1984);

            Edition edition1984Modern = editionFactory.createEdition(
                    book.identity(),
                    new ISBN("9780451524935"),
                    nineteenEightyFour.identity(),
                    seckerWarburg.identity(),
                    Year.of(2003),
                    Language.ENGLISH,
                    null, null, null, null,
                    Binding.PAPERBACK
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
            editionRepo.save(edition1977PatternLanguage);

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
                    Binding.PAPERBACK
            );
            editionRepo.save(editionShortnessOfLife);

            Edition editionE1027Original = editionFactory.createEdition(
                    magazine.identity(),
                    new NoIdentifier(), // sem ISBN na edição original de 1929
                    e1027.identity(),
                    editionsAlbertMorance.identity(),
                    Year.of(1929),
                    Language.FRENCH,
                    null,
                    null,
                    null,
                    new EditionNumber(1),
                    Binding.PAPERBACK
            );
            editionRepo.save(editionE1027Original);

            Edition editionIntermitencias = editionFactory.createEdition(
                    book.identity(),
                    new ISBN("9789722116404"),
                    intermitencias.identity(),
                    camiloAzurara.identity(),
                    Year.of(2005),
                    Language.PORTUGUESE,
                    null, null,
                    new NumberOfPages(191),
                    new EditionNumber(1),
                    Binding.PAPERBACK
            );
            editionRepo.save(editionIntermitencias);

            Edition editionAnitaVasCompras = editionFactory.createEdition(
                    book.identity(),
                    new NoIdentifier(),
                    anitaVasCompras.identity(),
                    hachette.identity(),
                    Year.of(1954),
                    Language.PORTUGUESE,
                    null, null,
                    new NumberOfPages(32),
                    new EditionNumber(1),
                    Binding.HARDCOVER
            );
            editionRepo.save(editionAnitaVasCompras);

            log.info("Editions saved: 1984, Foundation, Nova York Delirante, E.1027");

            Edition editionSpaceMatrix = editionFactory.createEdition(
                    book.identity(),
                    new ISBN("9780471989677"),
                    spaceMatrix.identity(),
                    nai010Publishers.identity(),
                    Year.of(2005),
                    Language.ENGLISH,
                    new Dimension(17.0, 24.0, 2.5, DimensionUnit.CENTIMETERS),
                    new Weight(794, Weight.WeightUnit.GRAMS),
                    new NumberOfPages(320),
                    new EditionNumber(1),
                    Binding.PAPERBACK

            );

            editionRepo.save(editionSpaceMatrix);

            Edition editionItalienishReise = editionFactory.createEdition(
                    book.identity(),
                    new ISBN("9783406611391"),
                    italienischeReise.identity(),
                    cHBeckVerlag.identity(),
                    Year.of(2002),
                    Language.GERMAN,
                    new Dimension(13.0, 18.6, 4.2, DimensionUnit.CENTIMETERS),
                    new Weight(600, Weight.WeightUnit.GRAMS),
                    new NumberOfPages(748),
                    new EditionNumber(1),
                    Binding.HARDCOVER
            );

            editionRepo.save(editionItalienishReise);

            Edition editiondifferencias = editionFactory.createEdition(
                    book.identity(),
                    new ISBN("8425219124"),
                    diferenciasPub.identity(),
                    gg.identity(),
                    Year.of(2003),
                    Language.SPANISH,
                    new Dimension(17.0, 21.0, 1.0, DimensionUnit.CENTIMETERS),
                    new Weight(540, Weight.WeightUnit.GRAMS),
                    new NumberOfPages(168),
                    new EditionNumber(1),
                    Binding.HARDCOVER
            );

            editionRepo.save(editiondifferencias);

            Edition editionHorizonte = editionFactory.createEdition(
                    book.identity(),
                    new ISBN("9789898868190"),
                    horizontePub.identity(),
                    orfeuPublisher.identity(),
                    Year.of(2023),
                    Language.PORTUGUESE,
                    new Dimension(19.5, 28.5, 1.0, DimensionUnit.CENTIMETERS),
                    new Weight(395, Weight.WeightUnit.GRAMS),
                    new NumberOfPages(40),
                    new EditionNumber(2),
                    Binding.HARDCOVER
            );

            editionRepo.save(editionHorizonte);

            Edition editionTechnoF = editionFactory.createEdition(
                    book.identity(),
                    new ISBN("9781847927279"),
                    technoFPub.identity(),
                    bhPublisher.identity(),
                    Year.of(2023),
                    Language.ENGLISH,
                    new Dimension(16.3, 24.2, 2.9, DimensionUnit.CENTIMETERS),
                    new Weight(503, Weight.WeightUnit.GRAMS),
                    new NumberOfPages(304),
                    new EditionNumber(1),
                    Binding.PAPERBACK
            );

            editionRepo.save(editionTechnoF);

            Edition editionAgatha = editionFactory.createEdition(
                    book.identity(),
                    new ISBN("9789896710453"),
                    agathaCristiePub.identity(),
                    tintaDaChinaBrasil.identity(),
                    Year.of(2010),
                    Language.PORTUGUESE,
                    new Dimension(16.3, 24.2, 2.9, DimensionUnit.CENTIMETERS),
                    new Weight(503, Weight.WeightUnit.GRAMS),
                    new NumberOfPages(285),
                    new EditionNumber(1),
                    Binding.PAPERBACK
            );

            editionRepo.save(editionAgatha);

            // -------------------------------------------------------
            // Items

            ItemId hipocritoesItemId = new ItemId("6584835610");

            Item hipocritoesItem = itemFactory.createItem(
                    hipocritoesItemId,
                    editionHipocritoesEOlhigarcas.identity(),
                    Condition.GOOD,
                    new Description("Paperback edition of Hipocritões e Oligarcas"),
                    SaleStatus.OnDirectSale,
                    new Picture("https://raw.githubusercontent.com/CarolinaLeite1251987/imagens/main/images/hipocritoes.png")

            );
            itemRepo.save(hipocritoesItem);

            ItemId spaceSetlementsItemId = new ItemId("1F9F4BFAD2");
            Item spaceSetlementsItem = itemFactory.createItem(
                    spaceSetlementsItemId,
                    spaceSetlements.identity(),
                    Condition.FAIR,
                    new Description("Great book"),
                    SaleStatus.Sold,
                    new Picture("https://raw.githubusercontent.com/CarolinaLeite1251987/imagens/main/images/spaceSettlements.png")
            );
            itemRepo.save(spaceSetlementsItem);

            ItemId itemId1984 = new ItemId("3C5D126F8B");
            Item item1 = itemFactory.createItem(
                    itemId1984,
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
                    new Picture("https://raw.githubusercontent.com/CarolinaLeite1251987/imagens/main/images/deliriousNewYork.png")
            );
            itemRepo.save(item3);

            ItemId itemId4 = new ItemId("3B9F4BFAB5");
            Item item4 = itemFactory.createItem(
                    itemId4,
                    edition1977PatternLanguage.identity(),
                    Condition.FAIR,
                    new Description("First Edition"),
                    SaleStatus.OnAuction,
                    new Picture("https://raw.githubusercontent.com/CarolinaLeite1251987/imagens/main/images/patternLanguage.png")

            );
            itemRepo.save(item4);

            ItemId shortnessOfLifeItemId = new ItemId("0141018812");
            Item shortnessOfLifeItem = itemFactory.createItem(
                    shortnessOfLifeItemId,
                    editionShortnessOfLife.identity(),
                    Condition.GOOD,
                    new Description("Penguin Great Ideas edition of On the Shortness of Life"),
                    SaleStatus.OnDirectSale,
                    new Picture("https://raw.githubusercontent.com/CarolinaLeite1251987/imagens/main/images/seneca.png")
            );
            itemRepo.save(shortnessOfLifeItem);

            ItemId itemIdE1027 = new ItemId("3C5D126F9B");
            Item itemE1027 = itemFactory.createItem(
                    itemIdE1027,
                    editionE1027Original.identity(),
                    Condition.FAIR,
                    new Description("Magazine rare First Edition"),
                    SaleStatus.OnDirectSale,
                    new Picture("https://raw.githubusercontent.com/CarolinaLeite1251987/imagens/main/images/E1027.png")

            );
            itemRepo.save(itemE1027);


            ItemId itemId5 = new ItemId("A1B2C3D4E5");
            Item item5 = itemFactory.createItem(
                    itemId5,
                    editionIntermitencias.identity(),
                    Condition.GOOD,
                    new Description("As Intermitências da Morte - first copy"),
                    SaleStatus.NotOnSale,
                    new Picture("https://www.worldofbooks.com/cdn/shop/files/8535907254.jpg?v=1750732116&width=493")
            );
            itemRepo.save(item5);

            ItemId itemId6 = new ItemId("B2C3D4E5F6");
            Item item6 = itemFactory.createItem(
                    itemId6,
                    editionAnitaVasCompras.identity(),
                    Condition.FAIR,
                    new Description("Anita vai às Compras - second copy"),
                    SaleStatus.NotOnSale,
                    new Picture("https://www.bokay.pt/wp-content/uploads/2024/04/Anita-vai-as-aulas-de-Gilbert-Delahaye-e-Marcel-Marlier-Pi.png")
            );
            itemRepo.save(item6);

            ItemId spaceMatrixItemId = new ItemId("5FA7B1C2D3");
            Item spaceMatrixItem = itemFactory.createItem(
                    spaceMatrixItemId,
                    editionSpaceMatrix.identity(),
                    Condition.GOOD,
                    new Description("Paperback in good condition. Minimal shelf wear, pages clean and unmarked. Spine intact. Essential reading on urban density and spatial form. Ships from Porto within 2 business days."),
                    SaleStatus.NotOnSale,
                    new Picture("https://books.open.tudelft.nl/public/presses/1/submission_38_40_coverImage_en_US_t.jpg")
            );
            itemRepo.save(spaceMatrixItem);

            ItemId italienishReiseItemId = new ItemId("5FA8B1C9D3");
            Item italienishReiseItem = itemFactory.createItem(
                    italienishReiseItemId,
                    editionItalienishReise.identity(),
                    Condition.GOOD,
                    new Description("Hardcover in good condition, with clean pages and light cover wear"),
                    SaleStatus.OnDirectSale,
                    new Picture("https://raw.githubusercontent.com/CarolinaLeite1251987/imagens/main/images/italienishReise.png")
            );
            itemRepo.save(italienishReiseItem);

            ItemId differenciasItemId = new ItemId("4DA8B1C9D3");
            Item differenciasItem = itemFactory.createItem(
                    differenciasItemId,
                    editiondifferencias.identity(),
                    Condition.MINT,
                    new Description("A well-preserved copy of this influential work on contemporary architectural theory. Light wear to the cover."),
                    SaleStatus.OnDirectSale,
                    new Picture("https://raw.githubusercontent.com/CarolinaLeite1251987/imagens/main/images/diferencias.png")
            );
            itemRepo.save(differenciasItem);

            ItemId horizonteItemId = new ItemId("5AA1B1C1D3");
            Item horizonteItem = itemFactory.createItem(
                    horizonteItemId,
                    editionHorizonte.identity(),
                    Condition.POOR,
                    new Description("A worn copy with visible signs of use. Pages remain readable, making it a practical reading copy for children."),
                    SaleStatus.OnDirectSale,
                    new Picture("https://raw.githubusercontent.com/CarolinaLeite1251987/imagens/main/images/horizonte.png")
            );
            itemRepo.save(horizonteItem);

            ItemId technoItemId = new ItemId("5BB1A1A1D3");
            Item technoItem = itemFactory.createItem(
                    technoItemId,
                    editionTechnoF.identity(),
                    Condition.FAIR,
                    new Description("Used but in good condition. Some signs of handling are present, but the book remains fully readable and complete."),
                    SaleStatus.OnDirectSale,
                    new Picture("https://raw.githubusercontent.com/CarolinaLeite1251987/imagens/main/images/technoFeudalism.png")
            );
            itemRepo.save(technoItem);

            ItemId agathaItemId = new ItemId("5AB1A1A1D3");
            Item agathaItem = itemFactory.createItem(
                    agathaItemId,
                    editionAgatha.identity(),
                    Condition.FAIR,
                    new Description("Pre-owned copy with light wear from previous use. Great fun to read."),
                    SaleStatus.OnDirectSale,
                    new Picture("https://raw.githubusercontent.com/CarolinaLeite1251987/imagens/main/images/siria.png")
            );
            itemRepo.save(agathaItem);

            log.info("Items saved: Hipocritões e Oligarcas, Space Settlements, 1984 first edition, 1984 modern edition, Nova York Delirante, A Pattern Language, On the Shortness of Life, E.1027. Maison en Bord de Mer, As Intermitências da Morte, Anita vai às Compras, Spacematrix, Italienische Reise, Differencias, Horizonte, Techno Feudalismo, Sapiens, Foundation");

            // -------------------------------------------------------
            // Lists of Items

            ListOfItems list1 = listOfItemsFactory.createListOfItems(
                    user.identity(),
                    new Name("Pedro Favourites"),
                    genreEducation.identity()
            );
            listOfItemsRepo.save(list1);

            ListOfItems list2 = listOfItemsFactory.createPublicListOfItems(
                    user2.identity(),
                    new Name("Ana Sci-Fi List"),
                    genreEducation.identity(),
                    new SharedDuration(30)
            );
            listOfItemsRepo.save(list2);

            ListOfItems list3 = listOfItemsFactory.createPublicListOfItems(
                    user3.identity(),
                    new Name("Angelo Non-Fiction"),
                    genreBiography.identity(),
                    new SharedDuration(300)
            );
            listOfItemsRepo.save(list3);

            ListOfItems list4 = listOfItemsFactory.createPublicListOfItems(
                    user.identity(),
                    new Name("Architecture Picks"),
                    genreArts.identity(),
                    new SharedDuration(15)
            );
            listOfItemsRepo.save(list4);

            ListOfItems list5 = listOfItemsFactory.createPublicListOfItems(
                    user2.identity(),
                    new Name("Ana Classics Collection"),
                    genreFiction.identity(),  // Fiction
                    new SharedDuration(20)
            );
            listOfItemsRepo.save(list5);

            // -------------------------------------------------------
            // Direct Sales

            DirectSale hipocritoesSale = directSaleFactory.createDirectSale(
                    List.of(hipocritoesItemId),
                    user.identity(),
                    new Price(14.99, Currency.EUR),
                    Duration.ofDays(30)
            );
            directSaleRepo.save(hipocritoesSale);

            DirectSale directSale1 = directSaleFactory.createDirectSale(
                    List.of(itemId1984),
                    user.identity(),
                    new Price(9.99, Currency.EUR),
                    Duration.ofDays(30)
            );
            directSaleRepo.save(directSale1);

            DirectSale directSale2 = directSaleFactory.createDirectSale(
                    List.of(shortnessOfLifeItemId),
                    user2.identity(),
                    new Price(14.99, Currency.EUR),
                    Duration.ofDays(7)
            );
            directSaleRepo.save(directSale2);

            DirectSale directSale3 = directSaleFactory.createDirectSale(
                    List.of(differenciasItemId),
                    userTono.identity(),
                    new Price(75.60, Currency.EUR),
                    Duration.ofDays(30)
            );

            directSaleRepo.save(directSale3);

            DirectSale directSaleSiria = directSaleFactory.createDirectSale(
                    List.of(agathaItemId),
                    userTono.identity(),
                    new Price(5, Currency.EUR),
                    null  // unlimited duration
            );
            directSaleRepo.save(directSaleSiria);

            log.info("Direct sales saved: Hipocritões e Oligarcas, 1984, On the Shortness of Life, Differencias, Techno Feudalismo, Sapiens, Foundation, Nova York Delirante, Nova York Delirante completed sale");


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
                    Binding.PUR                         // Binding

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

            // Edition for Spacematrix
            Edition editionSpacematrix = editionFactory.createEdition(
                    book.identity(),
                    new ISBN("9780553293357"),
                    foundationSeries.identity(),
                    gg.identity(),
                    Year.of(1991),
                    Language.ENGLISH,
                    new Dimension(17,24,3, DimensionUnit.CENTIMETERS) ,                                // Dimension
                    new Weight(794, Weight.WeightUnit.GRAMS),
                    new NumberOfPages(255),
                    new EditionNumber(1),
                    Binding.PUR
            );
            editionRepo.save(editionSpacematrix);

            log.info("Two additional editions saved for DirectSales");

            // Non-Fiction item
            ItemId nfItemId = new ItemId("5E4D3C2B1A");
            Item nfItem = itemFactory.createItem(
                    nfItemId,
                    editionSapiens.identity(), // Publication Sapiens → Non-Fiction
                    Condition.GOOD,
                    new Description("Non-Fiction test item"),
                    SaleStatus.OnDirectSale,
                    new Picture("https://raw.githubusercontent.com/CarolinaLeite1251987/imagens/main/images/sapiens.png")
            );
            itemRepo.save(nfItem);

            // Science-Fiction item
            ItemId sfItemId = new ItemId("F1A2B3C4D5");
            Item sfItem = itemFactory.createItem(
                    sfItemId,
                    editionFoundationSeries.identity(), // Publication Foundation → Sci-Fi
                    Condition.GOOD,
                    new Description("Sci-Fi test item"),
                    SaleStatus.OnDirectSale,
                    new Picture("https://raw.githubusercontent.com/CarolinaLeite1251987/imagens/main/images/fundacion.png")

            );
            itemRepo.save(sfItem);

            log.info("Two additional items saved for DirectSales");

            // -------------------------------------------------------
            // DirectSales for ALL genres
            // -------------------------------------------------------
            // Non-Fiction
            DirectSale nonFictionSale = directSaleFactory.createDirectSale(
                    List.of(nfItemId),
                    user2.identity(),
                    new Price(8.49, Currency.EUR),
                    Duration.ofDays(20)
            );
            directSaleRepo.save(nonFictionSale);

            // Science-Fiction
            DirectSale sciFiSale = directSaleFactory.createDirectSale(
                    List.of(sfItemId),
                    user2.identity(),
                    new Price(6.49, Currency.EUR),
                    Duration.ofDays(25)
            );
            directSaleRepo.save(sciFiSale);

            // Architecture (existing item3)
            DirectSale architectureSale = directSaleFactory.createDirectSale(
                    List.of(itemId3),
                    user3.identity(),
                    new Price(12.99, Currency.EUR),
                    Duration.ofDays(30)
            );
            directSaleRepo.save(architectureSale);

            log.info("DirectSales of all genres");

            // -------------------------------------------------------
            // Auctions

            Auction auction2 = auctionFactory.createAuction(
                    List.of(italienishReiseItemId),
                    new Price(10.00, Currency.EUR),
                    new Price(55.00, Currency.EUR),
                    ZonedDateTime.now(),
                    ZonedDateTime.now().plusDays(10),
                    userTono.identity()
            );
            auctionRepo.save(auction2);

            Auction auction3 = auctionFactory.createAuction(
                    List.of(horizonteItemId),
                    new Price(3.50, Currency.EUR),
                    new Price(20.00, Currency.EUR),
                    ZonedDateTime.now(),
                    ZonedDateTime.now().plusDays(31),
                    userTono.identity()
            );
            auctionRepo.save(auction3);

            Auction spaceMatrixAuction = auctionFactory.createAuction(
                    List.of(spaceMatrixItemId),
                    new Price(20.00, Currency.EUR),
                    new Price(30.00, Currency.EUR),
                    new Price(50.00, Currency.EUR),
                    ZonedDateTime.now(),
                    ZonedDateTime.now().plusDays(7),
                    user3.identity()
            );

            auctionRepo.save(spaceMatrixAuction);

            log.info("Auctions saved: 5 auctions");

            // Auction with bids and winner
            Auction auctionWithBids = auctionFactory.createAuction(
                    List.of(itemId4),
                    new Price(5.00, Currency.EUR),
                    new Price(10.00, Currency.EUR),
                    ZonedDateTime.now().minusDays(7),   // started in the past
                    ZonedDateTime.now().plusMinutes(1),  // ends in 1 minute — still active
                    user.identity()
            );

            // Place bids while active
            auctionWithBids.placeBid(userTono.identity(), new Price(11.00, Currency.EUR));
            auctionWithBids.placeBid(user2.identity(), new Price(13.00, Currency.EUR));
            auctionWithBids.placeBid(user3.identity(), new Price(15.00, Currency.EUR));

            // Finalize manually
            auctionWithBids.finalizeAuction();

            auctionRepo.save(auctionWithBids);
            log.info("Auction with bids saved — winner: Ângelo Martins at 15.00 EUR");

            // Libraries with items
            Library libraryPedro = libraryFactory.createLibrary(user.identity());
            libraryPedro.addItemIdToLibrary(itemId1984);
            libraryPedro.addItemIdToLibrary(itemId4);
            libraryPedro.addItemIdToLibrary(itemIdE1027);
            libraryPedro.addItemIdToLibrary(shortnessOfLifeItemId);
            libraryPedro.addItemIdToLibrary(spaceSetlementsItemId);
            libraryPedro.addItemIdToLibrary(itemId5);
            libraryPedro.addItemIdToLibrary(itemId6);

            libraryRepo.save(libraryPedro);

            Library libraryAna = libraryFactory.createLibrary(user2.identity());
            libraryAna.addItemIdToLibrary(itemId2);
            libraryRepo.save(libraryAna);

            Library libraryAngelo = libraryFactory.createLibrary(user3.identity());
            libraryRepo.save(libraryAngelo);  // biblioteca vazia

            log.info("Libraries saved: Pedro, Ana, Angelo");

            // -------------------------------------------------------
            // List of Items
            // Pedro Favourites
            list1.addItem(itemId4);                 // A Pattern Language
            list1.addItem(itemIdE1027);
            list1.addItem(spaceSetlementsItemId);

            // Ana Sci-Fi List
            list2.addItem(sfItemId);                // Foundation
            list1.addItem(itemId1984);                 // 1984

            // Angelo Non-Fiction
            list3.addItem(nfItemId);                // Sapiens
            list3.addItem(shortnessOfLifeItemId);   // On the Shortness of Life
            list3.addItem(hipocritoesItemId);

            // Pedro's architecture
            list4.addItem(itemId3);                // Nova York Delirante
            list4.addItem(itemIdE1027);            // E.1027
            list4.addItem(spaceSetlementsItemId);  // Space Settlements

            list5.addItem(itemId1984);     // 1984
            list5.addItem(itemId2);     // 1984 Modern Edition
            list5.addItem(nfItemId);

            listOfItemsRepo.save(list1);
            listOfItemsRepo.save(list2);
            listOfItemsRepo.save(list3);
            listOfItemsRepo.save(list4);
            listOfItemsRepo.save(list5);

            // -------------------------------------------------------
            // Shopping Cart - Empty for All Users
            ShoppingCart pedroCart = shoppingCartFactory.createShoppingCart(user.identity());

            shoppingCartRepo.save(pedroCart);

            ShoppingCart anaCart =
                    shoppingCartFactory.createShoppingCart(user2.identity());

            shoppingCartRepo.save(anaCart);

            ShoppingCart angeloCart =
                    shoppingCartFactory.createShoppingCart(user3.identity());

            shoppingCartRepo.save(angeloCart);

            log.info("Shopping cart saved for Pedro with 2 lines");

            log.info("DataInitializer completed successfully.");

            ShoppingCart tonoCart =
                    shoppingCartFactory.createShoppingCart(userTono.identity());
            shoppingCartRepo.save(tonoCart);


        };

    }

}
