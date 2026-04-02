package TOPSECRET.domain.Author;


/**
 * Author is the person who originates, creates, and writes a literary work.
 * The same name can mean different authors.
 */

public class Author {

    private String _name;

    Author (String name) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Author name cannot be null or empty");

        _name = name.trim();
    }

    public String getName() { return _name; }

    public String getLowerCaseName() { return _name.toLowerCase(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return _name.equalsIgnoreCase(author._name);
    }

    @Override
    public int hashCode() {
        return _name.toLowerCase().hashCode();
    }

}
