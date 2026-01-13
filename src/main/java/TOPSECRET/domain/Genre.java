package TOPSECRET.domain;

import java.util.*;
import java.util.stream.Collectors;

/** A genre is a category used to classify publications based
 * on shared characteristics like style, form, or content.
 */

public class Genre {
    public static final Genre ACTION             = new Genre("Action");
    public static final Genre ART                = new Genre("Art");
    public static final Genre AUTOBIOGRAPHY      = new Genre("Autobiography");
    public static final Genre BIOGRAPHY          = new Genre("Biography");
    public static final Genre BUSINESS           = new Genre("Business");
    public static final Genre CHILDREN           = new Genre("Children");
    public static final Genre COOKBOOK           = new Genre("Cookbook");
    public static final Genre CRIME_FICTION      = new Genre("Crime Fiction");
    public static final Genre DYSTOPIAN          = new Genre("Dystopian");
    public static final Genre FANTASY            = new Genre("Fantasy");
    public static final Genre FICTION            = new Genre("Fiction");
    public static final Genre GRAPHIC_NOVEL      = new Genre("Graphic Novel");
    public static final Genre HISTORICAL_FICTION = new Genre("Historical Fiction");
    public static final Genre HISTORY            = new Genre("History");
    public static final Genre HORROR             = new Genre("Horror");
    public static final Genre HUMOR              = new Genre("Humor");
    public static final Genre MEDICAL            = new Genre("Medical");
    public static final Genre MUSIC              = new Genre("Music");
    public static final Genre MYSTERY            = new Genre("Mystery");
    public static final Genre NONFICTION         = new Genre("Nonfiction");
    public static final Genre PHILOSOPHY         = new Genre("Philosophy");
    public static final Genre POETRY             = new Genre("Poetry");
    public static final Genre PSYCHOLOGY         = new Genre("Psychology");
    public static final Genre ROMANCE            = new Genre("Romance");
    public static final Genre SCI_FI             = new Genre("Sci-Fi");
    public static final Genre SCIENCE            = new Genre("Science");
    public static final Genre SELF_HELP          = new Genre("Self-Help");
    public static final Genre SPIRITUALITY       = new Genre("Spirituality");
    public static final Genre SPORTS             = new Genre("Sports");
    public static final Genre TECHNOLOGY         = new Genre("Technology");
    public static final Genre THRILLER           = new Genre("Thriller");
    public static final Genre TRAVEL             = new Genre("Travel");
    public static final Genre YOUNG_ADULT        = new Genre("Young Adult");


    // Map for quick lookups and for fromString to work:
    private static final Map<String, Genre> GENRE_BY_KEY = Map.ofEntries(
            Map.entry("ACTION", ACTION), Map.entry("ART", ART),
            Map.entry("AUTOBIOGRAPHY", AUTOBIOGRAPHY),
            Map.entry("BIOGRAPHY", BIOGRAPHY), Map.entry("BUSINESS", BUSINESS),
            Map.entry("CHILDREN", CHILDREN), Map.entry("COOKBOOK", COOKBOOK),
            Map.entry("CRIME_FICTION", CRIME_FICTION), Map.entry("DYSTOPIAN", DYSTOPIAN),
            Map.entry("FANTASY", FANTASY), Map.entry("FICTION", FICTION),
            Map.entry("GRAPHIC_NOVEL", GRAPHIC_NOVEL),
            Map.entry("HISTORICAL_FICTION", HISTORICAL_FICTION),
            Map.entry("HISTORY", HISTORY), Map.entry("HORROR", HORROR),
            Map.entry("HUMOR", HUMOR), Map.entry("MEDICAL", MEDICAL),
            Map.entry("MUSIC", MUSIC), Map.entry("MYSTERY", MYSTERY),
            Map.entry("NONFICTION", NONFICTION), Map.entry("PHILOSOPHY", PHILOSOPHY),
            Map.entry("POETRY", POETRY), Map.entry("PSYCHOLOGY", PSYCHOLOGY),
            Map.entry("ROMANCE", ROMANCE), Map.entry("SCI_FI", SCI_FI),
            Map.entry("SCIENCE", SCIENCE), Map.entry("SELF_HELP", SELF_HELP),
            Map.entry("SPIRITUALITY", SPIRITUALITY), Map.entry("SPORTS", SPORTS),
            Map.entry("TECHNOLOGY", TECHNOLOGY), Map.entry("THRILLER", THRILLER),
            Map.entry("TRAVEL", TRAVEL), Map.entry("YOUNG_ADULT", YOUNG_ADULT)
    );

    private static final Genre[] ALL_GENRES = {
            ACTION, ART, AUTOBIOGRAPHY, BIOGRAPHY, BUSINESS, CHILDREN, COOKBOOK,
            CRIME_FICTION, DYSTOPIAN, FANTASY, FICTION, GRAPHIC_NOVEL,
            HISTORICAL_FICTION, HISTORY, HORROR, HUMOR, MEDICAL, MUSIC,
            MYSTERY, NONFICTION, PHILOSOPHY, POETRY, PSYCHOLOGY, ROMANCE,
            SCI_FI, SCIENCE, SELF_HELP, SPIRITUALITY, SPORTS, TECHNOLOGY,
            THRILLER, TRAVEL, YOUNG_ADULT
    };

    private final String _genre;

    private Genre(String genre) {
        _genre = genre;
    }

    //Override to obtain a String of the mixed-cased genres (better for UI)
   @Override
   public String toString() {
       return _genre;
   }

    public static List<Genre> getAllGenres() {
        return List.copyOf(List.of(ALL_GENRES));
    }

   //The following method allows a user to write "sci-Fi" or "sci fi" instead of Sci-Fi, and it would still work:
   public static Genre fromString(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Genre cannot be null or empty");
        }
        String normalized = input.trim().toUpperCase().replaceAll("[\\s-]", "_");
        Genre result = GENRE_BY_KEY.get(normalized);
        if (result == null) {
            String validGenres = String.join(", ", Arrays.stream(ALL_GENRES).map(Genre::toString).toArray(String[]::new));
            throw new IllegalArgumentException(
                    "\"" + input + "\" is not a valid genre. Valid genres: " + validGenres
            );
        }
        return result;
   }
}
