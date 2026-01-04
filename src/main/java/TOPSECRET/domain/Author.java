package TOPSECRET.domain;

/**
 * Author is the person who originates, creates, and writes a literary work.
 * The same name can mean different authors.
 */

public class Author {
    private String name;

    public Author (String name) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Author name cannot be null or empty");

        this.name = name.trim();
    }

    public String getName() { return this.name; }

    public String getLowerCaseName() { return name.toLowerCase(); }

}
