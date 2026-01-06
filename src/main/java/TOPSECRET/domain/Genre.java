package TOPSECRET.domain;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Genre {
    ACTION("Action"),
    ART("Art"),
    AUTOBIOGRAPHY("Autobiography"),
    BIOGRAPHY("Biography"),
    BUSINESS("Business"),
    CHILDREN("Children"),
    COOKBOOK("Cookbook"),
    CRIME_FICTION("Crime Fiction"),
    DYSTOPIAN("Dystopian"),
    FANTASY("Fantasy"),
    FICTION("Fiction"),
    GRAPHIC_NOVEL("Graphic Novel"),
    HISTORICAL_FICTION("Historical Fiction"),
    HISTORY("History"),
    HORROR("Horror"),
    HUMOR("Humor"),
    MEDICAL("Medical"),
    MUSIC("Music"),
    MYSTERY("Mystery"),
    NONFICTION("Nonfiction"),
    PHILOSOPHY("Philosophy"),
    POETRY("Poetry"),
    PSYCHOLOGY("Psychology"),
    ROMANCE("Romance"),
    SCI_FI("Sci-Fi"),
    SCIENCE("Science"),
    SELF_HELP("Self-Help"),
    SPIRITUALITY("Spirituality"),
    SPORTS("Sports"),
    TECHNOLOGY("Technology"),
    THRILLER("Thriller"),
    TRAVEL("Travel"),
    YOUNG_ADULT("Young Adult");

    private final String _genre;

    Genre(String genre) {
        _genre = genre;
    }

    //Override to obtain a String of the mixed-cased genres (better for UI)
   @Override
   public String toString() {
       return _genre;
   }

   //The following method allows a user to write "sci-Fi" or "sci fi" instead of Sci-Fi, and it would still work:
   public static Genre fromString(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Genre cannot be null or empty");
        }
        String normalized = input.trim().toUpperCase().replaceAll("[\\s-]", "_");
        try {
            return Genre.valueOf(normalized);
        } catch (IllegalArgumentException e) {
            String validGenres = Arrays.stream(values()).map(Genre::toString).collect(Collectors.joining(", "));
            throw new IllegalArgumentException(
                    "\"" + input + "\" is not a valid genre. Valid genres: " + validGenres
            );
        }
   }
}
