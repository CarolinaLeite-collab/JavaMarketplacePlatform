package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.ValueObject;
import java.util.Objects;

public class Title implements ValueObject {

    /**
     * Title of a publication from Publication. Cannot be null, empty, or whitespace‑only.
     */

    private final String _title;

    public Title(String title) {

        if (!isValidConstructorArgument(title)) {
            throw new IllegalArgumentException("Title cannot be null, empty, or blank");
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
        return this._title;
    }

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
