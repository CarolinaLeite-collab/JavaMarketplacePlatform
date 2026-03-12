package TOPSECRET.domain;

import java.util.Objects;

public class Title {

    /**
     * Title of a publication from Publication. Cannot be null, empty, or whitespace‑only.
     */

    private final String _title;

    public Title(String title) {

        if (!isValidConstructorArgument(title)) {
            throw new IllegalArgumentException("Condition cannot be null!");
        }

        _title = title.trim();

    }

    public boolean isValidConstructorArgument(String title) {
        if (title == null || title.isBlank()) {
            return false;
        }
        return true;
    }

    public String getTitle() {
        return this._title; // Return original version, not lowercased version
    }

    // To standardize titles, as some entries may have varying capitalization
    public String getLowercaseTitle() {
        return this._title.toLowerCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Title)) return false;
        Title title = (Title) o;
        return _title.equalsIgnoreCase(title._title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_title.toLowerCase());
    }

}
